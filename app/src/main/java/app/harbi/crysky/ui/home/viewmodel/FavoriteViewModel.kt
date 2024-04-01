package app.harbi.crysky.ui.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.harbi.crysky.model.data.CityResponse
import app.harbi.crysky.model.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _cities: MutableSharedFlow<List<CityResponse>> = MutableSharedFlow()
    val cities: SharedFlow<List<CityResponse>> = _cities

    fun getAlertLocalCities() {
        Log.i("TAG", "getLocalCities: call")
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("TAG", "getLocalCities: before try")
            repository.getAlertCityResponse().collect { _cities.emit(it) }
            Log.i("TAG", "getLocalCities: Success")
        }
    }

    fun getFavLocalCities() {
        Log.i("TAG", "getLocalCities: call")
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("TAG", "getLocalCities: before try")
            repository.getFavCityResponse().collect { _cities.emit(it) }
            Log.i("TAG", "getLocalCities: Success")
        }
    }

    fun deleteCity(cityResponse: CityResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCityResponse(cityResponse)
        }
    }
}