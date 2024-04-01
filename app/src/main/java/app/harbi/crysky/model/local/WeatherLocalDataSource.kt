package app.harbi.crysky.model.local

import app.harbi.crysky.model.data.CityResponse
import app.harbi.crysky.model.data.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {
    fun getWeatherResponse(): Flow<List<WeatherResponse>>
    suspend fun insertWeatherResponse(weatherResponse: WeatherResponse)
    suspend fun deleteWeatherResponse()

    fun getFavCityResponse(): Flow<List<CityResponse>>

    fun getAlertCityResponse(): Flow<List<CityResponse>>

    suspend fun insertCityResponse(cityResponse: CityResponse)

    suspend fun deleteCityResponse(cityResponse: CityResponse)

}