package app.harbi.crysky.ui.search.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import app.harbi.crysky.databinding.ActivitySearchBinding
import app.harbi.crysky.model.Constants
import app.harbi.crysky.model.data.CityResponse
import app.harbi.crysky.model.data.ResponseStatus
import app.harbi.crysky.ui.search.viewmodel.SearchViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mGoogleMap: GoogleMap
    private var marker: Marker? = null

    private val viewModel: SearchViewModel by viewModel()
    private val cityAdapter: CityAdapter by lazy {
        CityAdapter({ city -> changeLocation(city) }, { city -> viewModel.addFavCity(city) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapFragment =
            supportFragmentManager.findFragmentById(binding.mapView.id) as SupportMapFragment

        mapFragment.getMapAsync { mGoogleMap = it }

        binding.etSearchResult.addTextChangedListener {
            viewModel.searchCities(it.toString().trim())
        }

        binding.rvSearchResult.adapter = cityAdapter

        lifecycleScope.launch {
            viewModel.cities.collect {
                when (it) {
                    is ResponseStatus.Success -> {
                        binding.gifWait.visibility = View.GONE
                        binding.wifiOffCard.visibility = View.GONE
                        binding.rvSearchResult.visibility = View.VISIBLE

                        if (it.data.isEmpty())
                            binding.rvSearchResult.visibility = View.GONE

                        cityAdapter.submitList(it.data)
                    }

                    is ResponseStatus.Failure -> {
                        binding.gifWait.visibility = View.GONE
                        binding.wifiOffCard.visibility = View.VISIBLE
                        binding.rvSearchResult.visibility = View.GONE

                        Toast.makeText(this@SearchActivity, "Get City Failure", Toast.LENGTH_SHORT)
                            .show()
                    }

                    is ResponseStatus.Loading -> {
                        binding.gifWait.visibility = View.VISIBLE
                        binding.wifiOffCard.visibility = View.GONE
                        binding.rvSearchResult.visibility = View.GONE

                        Toast.makeText(this@SearchActivity, "Loading", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        val locationPreferences =
            this@SearchActivity.getSharedPreferences(Constants.LOCATION, Context.MODE_PRIVATE)

        val city = CityResponse(
            latitude = locationPreferences.getString(Constants.LATITUDE, "0.0")?.toDouble() ?: 0.0,
            longitude = locationPreferences.getString(Constants.LONGITUDE, "0.0")?.toDouble() ?: 0.0
        )

        changeLocation(city)
    }

    private fun changeLocation(city: CityResponse) {
        mapFragment.getMapAsync {
            marker?.remove()
            val location = LatLng(city.latitude, city.longitude)
            marker = it.addMarker(
                MarkerOptions().position(location)
                    .title(if (city.name.trim().isNotEmpty()) city.name else "City")
            )
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
        }
    }
}