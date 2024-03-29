package app.harbi.crysky.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import app.harbi.crysky.R
import app.harbi.crysky.databinding.FragmentHomeBinding
import app.harbi.crysky.loadImage
import app.harbi.crysky.model.ResponseStatus
import app.harbi.crysky.model.WeatherResponse
import app.harbi.crysky.ui.HomeViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModel()
    private lateinit var hourAdapter: HourAdapter

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

        hourAdapter = HourAdapter()
        binding.recyclerView.apply {
            adapter = hourAdapter
        }

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

                        Toast.makeText(requireContext(), it.msg, Toast.LENGTH_LONG).show()
                    }

                    is ResponseStatus.Loading -> {
                        binding.gifWait.visibility = View.VISIBLE
                        binding.wifiOffCard.visibility = View.GONE
                        binding.page.visibility = View.GONE

                        Toast.makeText(requireContext(), "Loading", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        viewModel.getWeather()
    }

    private fun updateUi(data: WeatherResponse) {
        binding.gifWait.visibility = View.GONE
        binding.wifiOffCard.visibility = View.GONE
        binding.page.visibility = View.VISIBLE

        binding.tvCityName.text = data.city.name
        binding.tvDate.text = data.list[0].dt_txt.split(" ")[0]
        loadImage(binding.imgCurrent, data.list[0].weather[0].icon, R.drawable.d02)
        binding.tvCurrentTemp.text = String.format("%.2f °c", data.list[0].main.temp)
        binding.tvWeatherDescription.text = data.list[0].weather[0].description
        binding.tvDynamicPressure.text = String.format("%d hpa", data.list[0].main.pressure)
        binding.tvDynamicHumidity.text = String.format("%d %%", data.list[0].main.humidity)
        binding.tvDynamicWind.text = String.format("%.2f m/s", data.list[0].wind.speed)
        binding.tvDynamicCloud.text = String.format("%d %%", data.list[0].clouds.all)
        binding.tvDynamicVisibility.text = String.format("%d m", data.list[0].visibility)

        hourAdapter.submitList(data.list)
    }
}