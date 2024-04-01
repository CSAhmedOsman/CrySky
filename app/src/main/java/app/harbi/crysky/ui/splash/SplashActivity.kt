package app.harbi.crysky.ui.splash

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import app.harbi.crysky.databinding.ActivitySplashBinding
import app.harbi.crysky.model.Constants
import app.harbi.crysky.model.getSavedPreferences
import app.harbi.crysky.ui.home.view.HomeActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val LOCATION_PERMISSION_ID: Int = 5005
    private lateinit var fusedClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationPreferences: SharedPreferences

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedClient = LocationServices.getFusedLocationProviderClient(this@SplashActivity)

        locationPreferences = getSharedPreferences(Constants.LOCATION, Context.MODE_PRIVATE)

    }

    override fun onStart() {
        super.onStart()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val latitude = locationResult.lastLocation?.latitude.toString()
                saveSetting(locationPreferences, Constants.LATITUDE, latitude)
                val longitude = locationResult.lastLocation?.longitude.toString()
                saveSetting(locationPreferences, Constants.LONGITUDE, longitude)
            }
        }

        lifecycleScope.launch {

            val lat = getSavedPreferences(locationPreferences, Constants.LATITUDE, "")
            val lon = getSavedPreferences(locationPreferences, Constants.LONGITUDE, "")

            if (lat.isEmpty() && lon.isEmpty()) {
                if (checkPermission()) {
                    updateLocation()
                } else {
                    requestPermissions()
                }
            }

            delay(3000)
            val intent = Intent(this@SplashActivity, HomeActivity::class.java)
            intent.setAction(Intent.ACTION_VIEW)
            startActivity(intent)
            finish()
        }
    }

    private fun checkPermission() = ActivityCompat.checkSelfPermission(
        this@SplashActivity, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
        this@SplashActivity, Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun updateLocation() {
        if (checkPermission()) {
            fusedClient.requestLocationUpdates(
                LocationRequest.Builder(0).apply {
                    setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                }.build(), locationCallback, Looper.myLooper()
            )
        }
    }

    private fun requestPermissions() {
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            ), LOCATION_PERMISSION_ID
        )
        updateLocation()
    }

    private fun saveSetting(preferences: SharedPreferences, key: String, value: String) {
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_ID)
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                updateLocation()
    }
}
