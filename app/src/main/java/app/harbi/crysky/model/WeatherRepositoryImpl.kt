package app.harbi.crysky.model

import app.harbi.crysky.model.local.WeatherLocalDataSource
import app.harbi.crysky.model.remote.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow

class WeatherRepositoryImpl(
    val remoteDataSource: WeatherRemoteDataSource,
    val localDataSource: WeatherLocalDataSource,
) : WeatherRepository {
    override fun getRemoteWeather(): Flow<WeatherResponse> {
        return remoteDataSource.getWeather()
    }

    override fun getLocalWeather(): Flow<WeatherResponseEntity> {
        return localDataSource.getWeatherResponse()
    }

    override suspend fun insertWeather(weatherResponse: WeatherResponseEntity) {
        localDataSource.insertWeatherResponse(weatherResponse)
    }

    override suspend fun deleteWeather(weatherResponse: WeatherResponseEntity) {
        localDataSource.deleteWeatherResponse(weatherResponse)
    }


}