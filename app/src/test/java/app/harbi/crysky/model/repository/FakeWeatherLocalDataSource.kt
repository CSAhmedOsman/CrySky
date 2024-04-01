package app.harbi.crysky.model.repository

import app.harbi.crysky.model.data.CityResponse
import app.harbi.crysky.model.data.WeatherResponse
import app.harbi.crysky.model.local.WeatherLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeWeatherLocalDataSource(
    private val weatherResponses: MutableList<WeatherResponse> = mutableListOf(),
    private val favCities: MutableList<CityResponse> = mutableListOf(),
    private val alertCities: MutableList<CityResponse> = mutableListOf(),
) : WeatherLocalDataSource {

    override fun getWeatherResponse(): Flow<List<WeatherResponse>> {
        return flow {
            emit(weatherResponses)
        }
    }

    override suspend fun insertWeatherResponse(weatherResponse: WeatherResponse) {
        weatherResponses.add(weatherResponse)
    }

    override suspend fun deleteWeatherResponse() {
        weatherResponses.clear()
    }

    override fun getFavCityResponse(): Flow<List<CityResponse>> {
        return flow {
            emit(favCities)
        }
    }

    override fun getAlertCityResponse(): Flow<List<CityResponse>> {
        return flow {
            emit(alertCities)
        }
    }

    override suspend fun insertCityResponse(cityResponse: CityResponse) {
        if (!favCities.contains(cityResponse)) {
            favCities.add(cityResponse)
        }
    }

    override suspend fun deleteCityResponse(cityResponse: CityResponse) {
        favCities.remove(cityResponse)
    }
}
