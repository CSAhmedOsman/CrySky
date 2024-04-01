package app.harbi.crysky.ui.search.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import app.harbi.crysky.R
import app.harbi.crysky.databinding.ActivitySearchBinding
import app.harbi.crysky.model.AlertWorker
import app.harbi.crysky.model.Constants
import app.harbi.crysky.model.data.CityResponse
import app.harbi.crysky.model.data.ResponseStatus
import app.harbi.crysky.model.getSavedPreferences
import app.harbi.crysky.ui.search.viewmodel.SearchViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class SearchActivity : AppCompatActivity() {

    private lateinit var settingsPreferences: SharedPreferences
    private lateinit var locationPreferences: SharedPreferences
    private lateinit var binding: ActivitySearchBinding
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mGoogleMap: GoogleMap
    private var marker: Marker? = null

    private var selectedDate: String = ""
    private var selectedTime: String = ""

    private val viewModel: SearchViewModel by viewModel()
    private val cityAdapter: CityAdapter by lazy {
        CityAdapter(R.drawable.ic_favorite, { changeLocation(it) }, {
            it.type = Constants.FAV
            viewModel.addCity(it)
            Toast.makeText(this@SearchActivity, "Added", Toast.LENGTH_SHORT).show()
        }, {
            saveSetting(it)
            finish()
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationPreferences =
            this@SearchActivity.getSharedPreferences(Constants.LOCATION, Context.MODE_PRIVATE)

        settingsPreferences = this@SearchActivity
            .getSharedPreferences(Constants.SETTINGS_PREFERENCES, Context.MODE_PRIVATE)

        val city = CityResponse(
            latitude = getSavedPreferences(locationPreferences, Constants.LATITUDE, 0.0),
            longitude = getSavedPreferences(locationPreferences, Constants.LONGITUDE, 0.0)
        )

        mapFragment =
            supportFragmentManager.findFragmentById(binding.mapView.id) as SupportMapFragment

        mapFragment.getMapAsync {
            mGoogleMap = it
        }

        binding.etSearchResult.addTextChangedListener {
            viewModel.searchCities(it.toString().trim())
        }

        binding.rvResult.adapter = cityAdapter

        lifecycleScope.launch {
            viewModel.cities.collect {
                when (it) {
                    is ResponseStatus.Success -> {
                        binding.gifWait.visibility = View.GONE
                        binding.wifiOffCard.visibility = View.GONE
                        binding.rvResult.visibility = View.VISIBLE

                        if (it.data.isEmpty()) binding.rvResult.visibility = View.GONE

                        cityAdapter.submitList(it.data)
                    }

                    is ResponseStatus.Failure -> {
                        binding.gifWait.visibility = View.GONE
                        binding.wifiOffCard.visibility = View.VISIBLE
                        binding.rvResult.visibility = View.GONE

                        Toast.makeText(this@SearchActivity, "Get City Failure", Toast.LENGTH_SHORT)
                            .show()
                    }

                    is ResponseStatus.Loading -> {
                        binding.gifWait.visibility = View.VISIBLE
                        binding.wifiOffCard.visibility = View.GONE
                        binding.rvResult.visibility = View.GONE

                        Toast.makeText(this@SearchActivity, "Loading", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        changeLocation(city)
    }

    private fun changeLocation(city: CityResponse) {

        mapFragment.getMapAsync {
            mGoogleMap.setOnMapClickListener {
                val cityResponse = CityResponse(latitude = it.latitude, longitude = it.longitude)
                changeLocation(cityResponse)
                saveSetting(cityResponse)
                showDateDialog(cityResponse)
            }

            mGoogleMap.clear()
            mGoogleMap.addMarker(
                MarkerOptions().position(LatLng(city.latitude, city.longitude))
                    .title(if (city.name.trim().isNotEmpty()) city.name else "City")
            )
            mGoogleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(LatLng(city.latitude, city.longitude), 12f)
            )
        }
    }

    private fun saveSetting(city: CityResponse) {
        val editor = locationPreferences.edit()
        editor.putString(Constants.LATITUDE, city.latitude.toString())
        editor.putString(Constants.LONGITUDE, city.longitude.toString())
        editor.apply()
    }

    private fun showDateDialog(city: CityResponse) {
        val calender = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this@SearchActivity,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                selectedDate =
                    String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth)
                showTimeDialog(city)
            },
            calender.get(Calendar.YEAR),
            calender.get(Calendar.MONTH),
            calender.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = calender.timeInMillis
        datePickerDialog.show()
    }


    private fun showTimeDialog(city: CityResponse) {
        val uniqueKey = city.name + city.type

        val timePickerDialog = TimePickerDialog(
            this@SearchActivity, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
                val dateTime = "$selectedDate $selectedTime"

                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                val selectedMillis = sdf.parse(dateTime)?.time ?: return@OnTimeSetListener

                try {
                    val latitude = city.latitude
                    val longitude = city.longitude
                    val cityName = city.name

                    val language = getSavedPreferences(
                        settingsPreferences,
                        Constants.LANGUAGE,
                        Constants.ENGLISH
                    )

                    val unit = getSavedPreferences(
                        settingsPreferences,
                        Constants.UNITS,
                        Constants.METRIC
                    )

                    // Pass the parameters to the WorkManager
                    val inputData = workDataOf(
                        AlertWorker.KEY_LONGITUDE to longitude,
                        AlertWorker.KEY_LATITUDE to latitude,
                        AlertWorker.KEY_LANG to language,
                        AlertWorker.KEY_UNIT to unit,
                        AlertWorker.KEY_CITY_NAME to cityName
                    )

                    val alertWorkRequest =
                        OneTimeWorkRequestBuilder<AlertWorker>().setInputData(inputData)
                            .setInitialDelay(
                                selectedMillis - System.currentTimeMillis(), TimeUnit.MILLISECONDS
                            ).addTag(uniqueKey).build()

                    // Enqueue the WorkManager task
                    WorkManager.getInstance(this).enqueue(alertWorkRequest)

                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                Toast.makeText(this, "Added to alert", Toast.LENGTH_SHORT).show()
                city.type = Constants.ALERT
                viewModel.addCity(city)
            }, 0, 0, false
        )
        timePickerDialog.show()
    }
}