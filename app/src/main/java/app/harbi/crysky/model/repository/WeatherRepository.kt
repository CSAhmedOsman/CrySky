package app.harbi.crysky.model.repository

import app.harbi.crysky.model.data.CityResponse
import app.harbi.crysky.model.data.WeatherResponse
import app.harbi.crysky.model.data.WeatherResponseEntity
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getRemoteWeather(latLng: LatLng, units: String, language: String): Flow<WeatherResponse>
    fun getLocalWeather(): Flow<WeatherResponseEntity>

    fun getRemoteCities(name: String): Flow<CityResponse>
    fun getLocalCities(): Flow<CityResponse>

    suspend fun insertCity(cityResponse: CityResponse)
    suspend fun deleteCity(cityResponse: CityResponse)

    suspend fun insertWeather(weatherResponse: WeatherResponseEntity)
    suspend fun deleteWeather()
}
