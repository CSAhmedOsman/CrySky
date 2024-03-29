package app.harbi.crysky.model.remote

import app.harbi.crysky.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRemoteDataSourceImpl(private val service: WeatherService) : WeatherRemoteDataSource {

    override fun getWeather(): Flow<WeatherResponse> = flow {
        val responseResponse = service.getWeather()
        if (responseResponse.isSuccessful) {
            val weather = responseResponse.body() ?: WeatherResponse()
            emit(weather)
        } else {
            emit(WeatherResponse())
        }
    }
}