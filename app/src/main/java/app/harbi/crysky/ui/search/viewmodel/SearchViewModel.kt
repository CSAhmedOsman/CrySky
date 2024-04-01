package app.harbi.crysky.ui.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.harbi.crysky.model.data.CityResponse
import app.harbi.crysky.model.data.ResponseStatus
import app.harbi.crysky.model.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _cities: MutableStateFlow<ResponseStatus<List<CityResponse>>> =
        MutableStateFlow(ResponseStatus.Loading())
    val cities: StateFlow<ResponseStatus<List<CityResponse>>> = _cities.asStateFlow()

    fun searchCities(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val cities = repository.getRemoteCities(name).toList()
                _cities.value = ResponseStatus.Success(cities)
            } catch (e: Exception) {
                _cities.value = ResponseStatus.Failure(e.message ?: "Unknown error")
            }
        }
    }

    fun addCity(cityResponse: CityResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertCityResponse(cityResponse)
        }
    }
}