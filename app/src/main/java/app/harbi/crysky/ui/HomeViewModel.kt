package app.harbi.crysky.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.harbi.crysky.convertResponseToEntity
import app.harbi.crysky.model.ResponseStatus
import app.harbi.crysky.model.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val weather: MutableStateFlow<ResponseStatus> = MutableStateFlow(ResponseStatus.Loading)

    val weatherData: StateFlow<ResponseStatus> = weather.asStateFlow()

    fun getWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getRemoteWeather().catch {
                weather.value = ResponseStatus.Failure(it.message.toString())
                Log.i("TAG", "getWeather: ${it.message}")
            }.collect {
                weather.value = ResponseStatus.Success(it)
                Log.i("TAG", "getWeather: $it")
                repository.insertWeather(convertResponseToEntity(it))
            }
        }
    }
}