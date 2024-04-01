package app.harbi.crysky.ui.home.view.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import app.harbi.crysky.R
import app.harbi.crysky.databinding.FragmentHomeBinding
import app.harbi.crysky.model.Constants
import app.harbi.crysky.model.combineMaxMinWeatherData
import app.harbi.crysky.model.data.CityResponse
import app.harbi.crysky.model.data.ResponseStatus
import app.harbi.crysky.model.data.WeatherResponse
import app.harbi.crysky.model.getFlagEmoji
import app.harbi.crysky.model.getSavedPreferences
import app.harbi.crysky.model.loadImage
import app.harbi.crysky.ui.home.viewmodel.HomeViewModel
import app.harbi.crysky.ui.search.view.SearchActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val LOCATION_PERMISSION_ID: Int = 5005
    private lateinit var fusedClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val viewModel: HomeViewModel by viewModel()
    private val hourAdapter: HourAdapter by lazy { HourAdapter { resId -> getString(resId) } }
    private val dayAdapter: DayAdapter by lazy { DayAdapter { resId -> getString(resId) } }
    private lateinit var units: String
    private lateinit var language: String
    private lateinit var locationPreferences: SharedPreferences
    private lateinit var city: CityResponse

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        locationPreferences =
            requireActivity().getSharedPreferences(Constants.LOCATION, Context.MODE_PRIVATE)

        city = CityResponse(
            latitude = getSavedPreferences(locationPreferences, Constants.LATITUDE, 0.0),
            longitude = getSavedPreferences(locationPreferences, Constants.LONGITUDE, 0.0)
        )

        val settingsPreferences = requireActivity().getSharedPreferences(
            Constants.SETTINGS_PREFERENCES, Context.MODE_PRIVATE
        )

        units = getSavedPreferences(settingsPreferences, Constants.UNITS, Constants.METRIC)
        language = getSavedPreferences(settingsPreferences, Constants.LANGUAGE, Constants.ENGLISH)

        binding.rvHourly.adapter = hourAdapter

        binding.rvDaily.adapter = dayAdapter

        lifecycleScope.launch {
            viewModel.weatherData.collect {
                when (it) {
                    is ResponseStatus.Success -> {
                        updateUi(it.data)
                    }

                    is ResponseStatus.Failure -> {
                        binding.gifWait.visibility = View.GONE
                        binding.wifiOffCard.visibility = View.VISIBLE
                        binding.page.visibility = View.GONE

                        Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
                    }

                    is ResponseStatus.Loading -> {
                        binding.gifWait.visibility = View.VISIBLE
                        binding.wifiOffCard.visibility = View.GONE
                        binding.page.visibility = View.GONE

                        Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.btnSearch.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            intent.setAction(Intent.ACTION_VIEW)
            startActivity(intent)
        }

        binding.btnLocation.setOnClickListener {
            if (checkPermission()) {
                updateLocation()
            } else {
                requestPermissions()
            }
        }

        viewModel.getWeather(city, units, language)
    }

    override fun onStart() {
        super.onStart()

        val city = CityResponse(
            latitude = getSavedPreferences(locationPreferences, Constants.LATITUDE, 0.0),
            longitude = getSavedPreferences(locationPreferences, Constants.LONGITUDE, 0.0)
        )

        if (city != this.city) {
            this.city = city
            viewModel.getWeather(city, units, language)
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val newCity = CityResponse(
                    latitude = getSavedPreferences(locationPreferences, Constants.LATITUDE, 0.0),
                    longitude = getSavedPreferences(locationPreferences, Constants.LONGITUDE, 0.0)
                )

                if (newCity != this@HomeFragment.city) {
                    this@HomeFragment.city = newCity

                    saveSetting(
                        locationPreferences, Constants.LATITUDE, newCity.latitude.toString()
                    )
                    saveSetting(
                        locationPreferences, Constants.LONGITUDE, newCity.longitude.toString()
                    )

                    viewModel.getWeather(newCity, units, language)
                }
            }
        }
    }

    private fun updateUi(data: WeatherResponse) {
        binding.gifWait.visibility = View.GONE
        binding.wifiOffCard.visibility = View.GONE
        binding.page.visibility = View.VISIBLE

        binding.tvCityName.text =
            String.format("${data.city.name}, ${data.city.country} ${getFlagEmoji(data.city.country)}")
        binding.tvDate.text = data.list[0].dt_txt.split(" ")[0]
        loadImage(binding.imgCurrent, data.list[0].weather[0].icon, R.drawable.d02)

        binding.tvCurrentTemp.text = String.format(
            "%.2f %s", data.list[0].main.temp, when (units) {
                Constants.STANDARD -> getString(R.string.kelvin)
                Constants.IMPERIAL -> getString(R.string.fahrenheit)
                else -> getString(R.string.celsius)
            }
        )

        binding.tvWeatherDescription.text = data.list[0].weather[0].description
        binding.tvDynamicPressure.text = String.format("%d hPa", data.list[0].main.pressure)
        binding.tvDynamicHumidity.text = String.format("%d %%", data.list[0].main.humidity)

        binding.tvDynamicWind.text = String.format(
            "%.2f %s", data.list[0].wind.speed, when (units) {
                Constants.IMPERIAL -> getString(R.string.miles_per_hour)
                else -> getString(R.string.meter_per_sec)
            }
        )

        binding.tvDynamicCloud.text = String.format("%d %%", data.list[0].clouds.all)
        binding.tvDynamicVisibility.text =
            String.format("%d %s", data.list[0].visibility, getString(R.string.meter))

        lifecycleScope.launch {
            hourAdapter.apply {
                units = this@HomeFragment.units
                submitList(data.list)
            }

            val list = data.list.asFlow().filter { ((it.dt - 14400) % 28800).toInt() == 0 }
                .zip(data.list.asFlow().filter { ((it.dt) % 28800).toInt() == 0 }) { max, min ->
                    combineMaxMinWeatherData(max, min)
                }.toList()
            dayAdapter.apply {
                units = this@HomeFragment.units
                submitList(list)
            }
        }
    }

    private fun checkPermission() = ActivityCompat.checkSelfPermission(
        requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
        requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermissions() {
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            ), LOCATION_PERMISSION_ID
        )
        updateLocation()
    }

    private fun updateLocation() {
        if (checkPermission()) {
            fusedClient.requestLocationUpdates(
                LocationRequest.Builder(0).apply {
                    setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                }.build(), locationCallback, Looper.myLooper()
            )
        }
    }

    private fun saveSetting(preferences: SharedPreferences, key: String, value: String) {
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_ID) if (grantResults[0] == PackageManager.PERMISSION_GRANTED) updateLocation()
    }

}