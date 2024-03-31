package app.harbi.crysky.model.local

import app.harbi.crysky.model.data.CityResponse
import app.harbi.crysky.model.data.WeatherResponseEntity
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {
    fun getWeatherResponse(): Flow<WeatherResponseEntity>
    suspend fun insertWeatherResponse(weatherResponse: WeatherResponseEntity)
    suspend fun deleteWeatherResponse()

    fun getCityResponse(): Flow<CityResponse>
    suspend fun insertCityResponse(cityResponse: CityResponse)
    suspend fun deleteCityResponse(cityResponse: CityResponse)

}