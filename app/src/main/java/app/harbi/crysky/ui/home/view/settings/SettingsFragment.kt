package app.harbi.crysky.ui.home.view.settings

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import app.harbi.crysky.databinding.FragmentSettingsBinding
import app.harbi.crysky.model.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.util.Locale

class SettingsFragment : Fragment() {

    private val LOCATION_PERMISSION_ID: Int = 5005
    private lateinit var fusedClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var locationPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val settingsPreferences = requireActivity()
            .getSharedPreferences(Constants.SETTINGS_PREFERENCES, Context.MODE_PRIVATE)

        locationPreferences = requireActivity()
            .getSharedPreferences(Constants.LOCATION, Context.MODE_PRIVATE)

        // Load previously saved settings if available
        loadSavedSettings(settingsPreferences)

        // Set listeners for radio button selections
        binding.radioGroupLocation.setOnCheckedChangeListener { _, checkedId ->
            // Handle location selection
            when (checkedId) {
                binding.radioButtonGPS.id -> {
                    saveSetting(settingsPreferences, Constants.LOCATION, Constants.GPS)
                    if (!checkPermission())
                        requestPermissions()
                }

                binding.radioButtonMap.id -> {
                    saveSetting(settingsPreferences, Constants.LOCATION, Constants.MAP)
                }
            }
        }

        binding.radioGroupUnits.setOnCheckedChangeListener { _, checkedId ->
            // Handle temperature unit selection
            when (checkedId) {
                binding.radioButtonStandard.id -> {
                    saveSetting(settingsPreferences, Constants.UNITS, Constants.STANDARD)
                }

                binding.radioButtonMetric.id -> {
                    saveSetting(settingsPreferences, Constants.UNITS, Constants.METRIC)
                }

                binding.radioButtonImperial.id -> {
                    saveSetting(settingsPreferences, Constants.UNITS, Constants.IMPERIAL)
                }
            }
        }

        binding.radioGroupLanguage.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.radioButtonArabic.id -> {
                    saveSetting(settingsPreferences, Constants.LANGUAGE, Constants.ARABIC)
                    setLocale(Constants.ARABIC)
                }

                binding.radioButtonEnglish.id -> {
                    saveSetting(settingsPreferences, Constants.LANGUAGE, Constants.ENGLISH)
                    setLocale(Constants.ENGLISH)
                }
            }
        }
    }

    private fun saveSetting(preferences: SharedPreferences, key: String, value: String) {
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    private fun loadSavedSettings(settingsPreferences: SharedPreferences) {
        val location = settingsPreferences.getString(Constants.LOCATION, Constants.MAP)
        val units = settingsPreferences.getString(Constants.UNITS, Constants.METRIC)
        val language = settingsPreferences.getString(Constants.LANGUAGE, Constants.ENGLISH)

        // Edit radio button selections
        binding.radioButtonGPS.isChecked = location == Constants.GPS
        binding.radioButtonMap.isChecked = location == Constants.MAP

        binding.radioButtonStandard.isChecked = units == Constants.STANDARD
        binding.radioButtonMetric.isChecked = units == Constants.METRIC
        binding.radioButtonImperial.isChecked = units == Constants.IMPERIAL

        binding.radioButtonArabic.isChecked = language == Constants.ARABIC
        binding.radioButtonEnglish.isChecked = language == Constants.ENGLISH
    }

    private fun checkPermission() =
        ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermissions() {
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
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

    override fun onStart() {
        super.onStart()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val latitude = locationResult.lastLocation?.latitude ?: 0.0
                saveSetting(locationPreferences, Constants.LATITUDE, latitude.toString())
                val longitude = locationResult.lastLocation?.longitude ?: 0.0
                saveSetting(locationPreferences, Constants.LONGITUDE, longitude.toString())
            }
        }
        if (checkPermission()) {
            updateLocation()
        } else {
            requestPermissions()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_ID)
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                updateLocation()
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)

        requireActivity().recreate()
    }
}
