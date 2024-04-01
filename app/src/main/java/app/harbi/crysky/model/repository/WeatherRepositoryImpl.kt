package app.harbi.crysky.model.repository

import app.harbi.crysky.model.data.CityResponse
import app.harbi.crysky.model.data.WeatherResponse
import app.harbi.crysky.model.local.WeatherLocalDataSource
import app.harbi.crysky.model.remote.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow

class WeatherRepositoryImpl(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val localDataSource: WeatherLocalDataSource,
) : WeatherRepository {
    override fun getRemoteWeather(
        city: CityResponse, units: String, language: String,
    ): Flow<WeatherResponse> {
        return remoteDataSource.getWeather(city, units, language)
    }

    override fun getLocalWeather(): Flow<List<WeatherResponse>> {
        return localDataSource.getWeatherResponse()
    }

    override fun getRemoteCities(name: String): Flow<CityResponse> {
        return remoteDataSource.getCities(name)
    }

    override suspend fun insertWeather(weatherResponse: WeatherResponse) {
        localDataSource.insertWeatherResponse(weatherResponse)
    }

    override suspend fun deleteWeather() {
        localDataSource.deleteWeatherResponse()
    }

    override fun getFavCityResponse(): Flow<List<CityResponse>> {
        return localDataSource.getFavCityResponse()
    }

    override fun getAlertCityResponse(): Flow<List<CityResponse>> {
        return localDataSource.getAlertCityResponse()
    }

    override suspend fun insertCityResponse(cityResponse: CityResponse) {
        localDataSource.insertCityResponse(cityResponse)
    }

    override suspend fun deleteCityResponse(cityResponse: CityResponse) {
        localDataSource.deleteCityResponse(cityResponse)
    }
}