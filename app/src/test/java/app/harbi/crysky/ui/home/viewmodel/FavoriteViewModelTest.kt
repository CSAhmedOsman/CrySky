package app.harbi.crysky.ui.home.viewmodel

import app.harbi.crysky.model.Constants
import app.harbi.crysky.model.data.CityResponse
import app.harbi.crysky.model.data.WeatherResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class FavoriteViewModelTests {

    private lateinit var viewModel: FavoriteViewModel

    private val fakeWeatherResponse = WeatherResponse()

    private val fakeFavCityResponse = CityResponse(type = Constants.FAV)
    private val fakeAlertCityResponse = CityResponse(type = Constants.ALERT)


    private val fakeRepository =
        FakeWeatherRepository(
            mutableListOf(fakeWeatherResponse),
            mutableListOf(fakeAlertCityResponse, fakeFavCityResponse)
        )


    @Before
    fun setUp() {
        viewModel = FavoriteViewModel(fakeRepository)
    }

    @Test
    fun getAlertLocalCity() = runTest {
        // When:
        viewModel.getAlertLocalCities()

        val weather = viewModel.cities.first().first()
        // Then
        assertThat(weather, `is`(fakeAlertCityResponse))
    }


}