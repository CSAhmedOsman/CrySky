package app.harbi.crysky.model.local

import app.harbi.crysky.model.WeatherResponseEntity
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSourceImpl(private val dao: WeatherResponseDao) : WeatherLocalDataSource {

    override fun getWeatherResponse(): Flow<WeatherResponseEntity> {
        return dao.getWeatherResponse()
    }

    override suspend fun insertWeatherResponse(weatherResponse: WeatherResponseEntity) {
        dao.insertWeatherResponse(weatherResponse)
    }

    override suspend fun deleteWeatherResponse(weatherResponse: WeatherResponseEntity) {
        dao.deleteWeatherResponse(weatherResponse)
    }
}