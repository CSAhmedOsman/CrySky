package app.harbi.crysky.ui.home.view.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import app.harbi.crysky.R
import app.harbi.crysky.databinding.FragmentHomeBinding
import app.harbi.crysky.model.Constants
import app.harbi.crysky.model.combineMaxMinWeatherData
import app.harbi.crysky.model.data.ResponseStatus
import app.harbi.crysky.model.data.WeatherResponse
import app.harbi.crysky.model.loadImage
import app.harbi.crysky.ui.home.viewmodel.HomeViewModel
import app.harbi.crysky.ui.search.view.SearchActivity
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModel()
    private val hourAdapter: HourAdapter by lazy { HourAdapter { resId -> getString(resId) } }
    private val dayAdapter: DayAdapter by lazy { DayAdapter { resId -> getString(resId) } }
    private lateinit var units: String
    private lateinit var language: String

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

        val locationPreferences =
            requireActivity().getSharedPreferences(Constants.LOCATION, Context.MODE_PRIVATE)

        val latLng = LatLng(
            locationPreferences.getString(Constants.LATITUDE, "0.0")?.toDouble() ?: 0.0,
            locationPreferences.getString(Constants.LONGITUDE, "0.0")?.toDouble() ?: 0.0
        )

        val settingsPreferences = requireActivity().getSharedPreferences(
            Constants.SETTINGS_PREFERENCES, Context.MODE_PRIVATE
        )

        units = settingsPreferences.getString(Constants.UNITS, Constants.METRIC) ?: Constants.METRIC
        language = settingsPreferences.getString(Constants.LANGUAGE, Constants.ENGLISH)
            ?: Constants.ENGLISH

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

        binding.btnLocation.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            intent.setAction(Intent.ACTION_VIEW)
            startActivity(intent)
        }

        viewModel.getWeather(latLng, units, language)
    }

    private fun updateUi(data: WeatherResponse) {
        binding.gifWait.visibility = View.GONE
        binding.wifiOffCard.visibility = View.GONE
        binding.page.visibility = View.VISIBLE

        binding.tvCityName.text = data.city.name
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
}