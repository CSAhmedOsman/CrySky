package app.harbi.crysky.model.local

import app.harbi.crysky.model.data.CityResponse
import app.harbi.crysky.model.data.WeatherResponseEntity
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSourceImpl(
    private val weatherResponseDao: WeatherResponseDao,
    private val cityResponseDao: CityResponseDao,
) : WeatherLocalDataSource {

    override fun getWeatherResponse(): Flow<WeatherResponseEntity> {
        return weatherResponseDao.getWeatherResponse()
    }

    override suspend fun insertWeatherResponse(weatherResponse: WeatherResponseEntity) {
        weatherResponseDao.insertWeatherResponse(weatherResponse)
    }

    override suspend fun deleteWeatherResponse() {
        weatherResponseDao.deleteWeatherResponse()
    }

    override fun getCityResponse(): Flow<CityResponse> {
        return cityResponseDao.getCityResponse()
    }

    override suspend fun insertCityResponse(cityResponse: CityResponse) {
        cityResponseDao.insertCityResponse(cityResponse)
    }

    override suspend fun deleteCityResponse(cityResponse: CityResponse) {
        cityResponseDao.deleteCityResponse(cityResponse)
    }

}