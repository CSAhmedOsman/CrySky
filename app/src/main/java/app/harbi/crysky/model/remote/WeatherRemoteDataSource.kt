package app.harbi.crysky.model.remote

import app.harbi.crysky.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRemoteDataSource {
    fun getWeather(): Flow<WeatherResponse>
}