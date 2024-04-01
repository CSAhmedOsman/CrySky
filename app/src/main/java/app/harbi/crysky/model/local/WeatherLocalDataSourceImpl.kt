package app.harbi.crysky.model.local

import app.harbi.crysky.model.data.CityResponse
import app.harbi.crysky.model.data.WeatherResponse
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSourceImpl(private val dao: WeatherResponseDao) : WeatherLocalDataSource {

    override fun getWeatherResponse(): Flow<List<WeatherResponse>> {
        return dao.getWeatherResponse()
    }

    override suspend fun insertWeatherResponse(weatherResponse: WeatherResponse) {
        dao.insertWeatherResponse(weatherResponse)
    }

    override suspend fun deleteWeatherResponse() {
        dao.deleteWeatherResponse()
    }

    override fun getFavCityResponse(): Flow<List<CityResponse>> {
        return dao.getFavCityResponse()
    }

    override fun getAlertCityResponse(): Flow<List<CityResponse>> {
        return dao.getAlertCityResponse()
    }

    override suspend fun insertCityResponse(cityResponse: CityResponse) {
        dao.insertCityResponse(cityResponse)
    }

    override suspend fun deleteCityResponse(cityResponse: CityResponse) {
        dao.deleteCityResponse(cityResponse)
    }

}