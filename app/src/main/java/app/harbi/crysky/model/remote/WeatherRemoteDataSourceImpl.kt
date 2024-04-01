package app.harbi.crysky.model.remote

import app.harbi.crysky.model.data.CityResponse
import app.harbi.crysky.model.data.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class WeatherRemoteDataSourceImpl(
    private val weatherService: WeatherService,
    private val cityService: CityService,
) : WeatherRemoteDataSource {

    override fun getWeather(
        city: CityResponse, units: String, language: String,
    ): Flow<WeatherResponse> = flow {
        val response = weatherService.getWeather(city.latitude, city.longitude, units, language)
        if (response.isSuccessful) {
            val weather = response.body() ?: WeatherResponse()
            emit(weather)
        } else {
            emit(WeatherResponse())
        }
    }

    override fun getCities(name: String): Flow<CityResponse> = flow {
        val response = cityService.getCities(name)
        if (response.isSuccessful) {
            val cities = response.body() ?: emptyList()
            emitAll(cities.asFlow())
        } else {
            emitAll(emptyList<CityResponse>().asFlow())
        }
    }
}