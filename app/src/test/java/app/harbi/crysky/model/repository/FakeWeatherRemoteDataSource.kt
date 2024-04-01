package app.harbi.crysky.model.repository

import app.harbi.crysky.model.data.CityResponse
import app.harbi.crysky.model.data.WeatherResponse
import app.harbi.crysky.model.local.WeatherLocalDataSource
import app.harbi.crysky.model.remote.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeWeatherRemoteDataSource(
    val fakeWeatherResponse: WeatherResponse,
    val fakeCityResponse: CityResponse,
) : WeatherRemoteDataSource {
    override fun getWeather(
        city: CityResponse,
        units: String,
        language: String,
    ): Flow<WeatherResponse> {
        return flow {
            emit(fakeWeatherResponse)
        }
    }

    override fun getCities(name: String): Flow<CityResponse> {
        return flow {
            emit(fakeCityResponse)
        }
    }
}
