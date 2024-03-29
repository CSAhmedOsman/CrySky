package app.harbi.crysky.model.local

import app.harbi.crysky.model.WeatherResponseEntity
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {
    fun getWeatherResponse(): Flow<WeatherResponseEntity>

    suspend fun insertWeatherResponse(weatherResponse: WeatherResponseEntity)

    suspend fun deleteWeatherResponse(weatherResponse: WeatherResponseEntity)

}