package app.harbi.crysky.model.repository

import app.harbi.crysky.model.data.CityResponse
import app.harbi.crysky.model.data.WeatherResponse
import app.harbi.crysky.model.data.WeatherResponseEntity
import app.harbi.crysky.model.local.WeatherLocalDataSource
import app.harbi.crysky.model.remote.WeatherRemoteDataSource
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

class WeatherRepositoryImpl(
    val remoteDataSource: WeatherRemoteDataSource,
    val localDataSource: WeatherLocalDataSource,
) : WeatherRepository {
    override fun getRemoteWeather(
        latLng: LatLng, units: String, language: String,
    ): Flow<WeatherResponse> {
        return remoteDataSource.getWeather(latLng, units, language)
    }

    override fun getLocalWeather(): Flow<WeatherResponseEntity> {
        return localDataSource.getWeatherResponse()
    }

    override fun getRemoteCities(name: String): Flow<CityResponse> {
        return remoteDataSource.getCities(name)
    }

    override fun getLocalCities(): Flow<CityResponse> {
        return localDataSource.getCityResponse()
    }

    override suspend fun insertCity(cityResponse: CityResponse) {
        localDataSource.insertCityResponse(cityResponse)
    }

    override suspend fun deleteCity(cityResponse: CityResponse) {
        localDataSource.deleteCityResponse(cityResponse)
    }

    override suspend fun insertWeather(weatherResponse: WeatherResponseEntity) {
        localDataSource.insertWeatherResponse(weatherResponse)
    }

    override suspend fun deleteWeather() {
        localDataSource.deleteWeatherResponse()
    }

}