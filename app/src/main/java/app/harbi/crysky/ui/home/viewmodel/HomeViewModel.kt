package app.harbi.crysky.ui.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.harbi.crysky.model.convertResponseToEntity
import app.harbi.crysky.model.data.ResponseStatus
import app.harbi.crysky.model.data.WeatherResponse
import app.harbi.crysky.model.repository.WeatherRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val weather: MutableStateFlow<ResponseStatus<WeatherResponse>> =
        MutableStateFlow(ResponseStatus.Loading())
    val weatherData: StateFlow<ResponseStatus<WeatherResponse>> = weather.asStateFlow()

    fun getWeather(latLng: LatLng, units: String, language: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getRemoteWeather(latLng, units, language).catch {
                weather.value = ResponseStatus.Failure(it.message.toString())
                Log.i("TAG", "getWeather: ${it.message}")
            }.collect {
                weather.value = ResponseStatus.Success(it)
                Log.i("TAG", "getWeather: $it")
                repository.deleteWeather()
                repository.insertWeather(convertResponseToEntity(it))
            }
        }
    }

}