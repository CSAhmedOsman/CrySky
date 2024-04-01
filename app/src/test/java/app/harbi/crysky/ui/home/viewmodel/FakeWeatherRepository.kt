package app.harbi.crysky.ui.home.viewmodel

import app.harbi.crysky.model.data.CityResponse
import app.harbi.crysky.model.data.WeatherResponse
import app.harbi.crysky.model.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeWeatherRepository(
    val fakeWeatherList: MutableList<WeatherResponse>,
    val fakeCityList: MutableList<CityResponse>,
) : WeatherRepository {

    override fun getRemoteWeather(
        city: CityResponse, units: String, language: String,
    ): Flow<WeatherResponse> {
        // Return a flow emitting fake weather data
        return flow {
            emit(fakeWeatherList.first()) // Emitting the first fake weather response
        }
    }

    override fun getRemoteCities(name: String): Flow<CityResponse> {
        return flow {
            emit(fakeCityList.first()) // Emitting the first fake city response
        }
    }

    override fun getLocalWeather(): Flow<List<WeatherResponse>> {
        // Return a flow emitting fake weather list
        return flow {
            emit(fakeWeatherList) // Emitting the fake weather list
        }
    }

    override suspend fun insertWeather(weatherResponse: WeatherResponse) {
        // No implementation needed for fake repository
    }

    override suspend fun deleteWeather() {
        // No implementation needed for fake repository
    }

    override fun getFavCityResponse(): Flow<List<CityResponse>> {
        // Return a flow emitting fake favorite city list
        return flow {
            emit(fakeCityList) // Emitting the fake favorite city list
        }
    }

    override fun getAlertCityResponse(): Flow<List<CityResponse>> {
        // Return a flow emitting fake alert city list
        return flow {
            emit(fakeCityList) // Emitting the fake alert city list
        }
    }

    override suspend fun insertCityResponse(cityResponse: CityResponse) {
        // No implementation needed for fake repository
    }

    override suspend fun deleteCityResponse(cityResponse: CityResponse) {
        // No implementation needed for fake repository
    }
}
