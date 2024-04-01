package app.harbi.crysky.model.repository

import app.harbi.crysky.model.data.CityResponse
import app.harbi.crysky.model.data.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getRemoteWeather(
        city: CityResponse, units: String, language: String,
    ): Flow<WeatherResponse>

    fun getRemoteCities(name: String): Flow<CityResponse>

    fun getLocalWeather(): Flow<List<WeatherResponse>>
    suspend fun insertWeather(weatherResponse: WeatherResponse)
    suspend fun deleteWeather()

    fun getFavCityResponse(): Flow<List<CityResponse>>
    fun getAlertCityResponse(): Flow<List<CityResponse>>
    suspend fun insertCityResponse(cityResponse: CityResponse)
    suspend fun deleteCityResponse(cityResponse: CityResponse)

}
