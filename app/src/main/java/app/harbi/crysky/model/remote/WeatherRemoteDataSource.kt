package app.harbi.crysky.model.remote

import app.harbi.crysky.model.data.CityResponse
import app.harbi.crysky.model.data.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRemoteDataSource {
    fun getWeather(city: CityResponse,units: String, language: String): Flow<WeatherResponse>
    fun getCities(name: String): Flow<CityResponse>
}