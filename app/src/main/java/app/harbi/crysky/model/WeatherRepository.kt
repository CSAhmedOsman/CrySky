package app.harbi.crysky.model

import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getRemoteWeather(): Flow<WeatherResponse>

    fun getLocalWeather(): Flow<WeatherResponseEntity>

    suspend fun insertWeather(weatherResponse: WeatherResponseEntity)

    suspend fun deleteWeather(weatherResponse: WeatherResponseEntity)
}
