package app.harbi.crysky.model.repository

import app.harbi.crysky.model.Constants
import app.harbi.crysky.model.data.CityResponse
import app.harbi.crysky.model.data.WeatherResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class WeatherRepositoryTest {

    private lateinit var repository: WeatherRepositoryImpl

    private val fakeWeatherResponse = WeatherResponse()
    private val fakeCityResponse = CityResponse()

    private val fakeFavCityResponse = CityResponse(type = Constants.FAV)
    private val fakeAlertCityResponse = CityResponse(type = Constants.ALERT)

    private val fakeRemoteDataSource =
        FakeWeatherRemoteDataSource(fakeWeatherResponse, fakeCityResponse)
    private val fakeLocalDataSource = FakeWeatherLocalDataSource(
        mutableListOf(fakeWeatherResponse),
        mutableListOf(fakeFavCityResponse),
        mutableListOf(fakeAlertCityResponse)
    )

    @Before
    fun setUp() {
        repository = WeatherRepositoryImpl(fakeRemoteDataSource, fakeLocalDataSource)
    }

    @Test
    fun testGetRemoteWeather() = runTest {
        // When:
        val weather = repository.getRemoteWeather(fakeCityResponse, "metric", "en").first()

        // Then
        assertThat(weather, `is`(fakeWeatherResponse))
    }

    @Test
    fun testGetLocalWeather() = runTest {
        // Call the function under test
        val weatherList = repository.getLocalWeather().first().first()

        // Assert the result
        assertThat(weatherList, `is`(fakeWeatherResponse))
    }


    @Test
    fun testGetRemoteCities() = runTest {
        // Call the function under test
        val cities = repository.getRemoteCities("London").first()

        // Assert the result
        assertThat(cities, `is`(fakeCityResponse))
    }

    @Test
    fun testInsertWeather() = runTest {
        // Call the function under test
        repository.insertWeather(fakeWeatherResponse)

        // Fetch the inserted weather
        val insertedWeather = repository.getLocalWeather().first().first()

        // Assert the result
        assertThat(insertedWeather, `is`(fakeWeatherResponse))
    }

    @Test
    fun testDeleteWeather() = runTest {
        // Call the function under test
        repository.deleteWeather()

        // Fetch the weather list after deletion
        val weatherList = repository.getLocalWeather().first()

        // Assert the result
        assertThat(weatherList.size, `is`(0))
    }

    @Test
    fun testGetFavCityResponse() = runTest {
        // Call the function under test
        val favCities = repository.getFavCityResponse().first()

        // Assert the result
        assertThat(favCities, `is`(listOf(fakeFavCityResponse)))
    }

    @Test
    fun testGetAlertCityResponse() = runTest {
        // Call the function under test
        val alertCities = repository.getAlertCityResponse().first()

        // Assert the result
        assertThat(alertCities, `is`(listOf(fakeAlertCityResponse)))
    }

    @Test
    fun testInsertCityResponse() = runTest {
        // Call the function under test
        repository.insertCityResponse(fakeCityResponse)

        // Fetch the inserted city response
        val insertedCityResponse = repository.getFavCityResponse().first().last()

        // Assert the result
        assertThat(insertedCityResponse, `is`(fakeCityResponse))
    }

    @Test
    fun testDeleteCityResponse() = runTest {
        // Call the function under test
        repository.deleteCityResponse(fakeFavCityResponse)

        // Fetch the favorite cities after deletion
        val favCities = repository.getFavCityResponse().first()

        // Assert the result
        assertThat(favCities.size, `is`(0))
    }

}
