package app.harbi.crysky.model.local

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import app.harbi.crysky.model.Constants
import app.harbi.crysky.model.data.CityResponse
import app.harbi.crysky.model.data.WeatherResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
class DAOTest {

    private lateinit var dataBase: WeatherDatabase
    private lateinit var dao: WeatherResponseDao

    private var weatherResponse: WeatherResponse = WeatherResponse()
    private var fakeFavCityResponse = CityResponse(type = Constants.FAV)
    private var fakeAlertCityResponse = CityResponse(type = Constants.ALERT)

    @get: Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dataBase = Room.inMemoryDatabaseBuilder(
            context, WeatherDatabase::class.java
        ).build()
        dao = dataBase.weatherResponseDao()
    }

    @After
    fun tearDown() {
        dataBase.close()
    }

    @Test
    fun insertCity_getFavCity_returnTrue() = runBlocking {
        //Given
        dao.insertCityResponse(fakeFavCityResponse)

        //When
        val result = dao.getFavCityResponse().first()

        //Then

        assertThat(result, not(nullValue()))
        assertThat(result.contains(fakeFavCityResponse), `is`(true))
    }

    @Test
    fun insertCity_getAlertCity_returnTrue() = runBlocking {
        //Given
        dao.insertCityResponse(fakeAlertCityResponse)

        //When
        val result = dao.getAlertCityResponse().first()

        //Then

        assertThat(result, not(nullValue()))
        assertThat(result.contains(fakeAlertCityResponse), `is`(true))
    }

    @Test
    fun insertCity_deleteIt_returnEmptyList() = runBlocking {
        //Given
        dao.insertCityResponse(fakeFavCityResponse)

        //When
        dao.deleteCityResponse(fakeFavCityResponse)

        //Then
        val result = dao.getFavCityResponse().first()

        assertThat(result, `is`(emptyList()))

    }

    @Test
    fun insertWeatherResponse_getWeatherResponse() = runBlocking {
        //Given
        dao.insertWeatherResponse(weatherResponse)

        //When
        val result = dao.getWeatherResponse().first().first()

        //Then
        assert(result == weatherResponse)

    }

    @Test
    fun insertWeatherResponse_deleteIt_ReturnNull() = runBlocking {
        //Given
        dao.insertWeatherResponse(weatherResponse)

        //When
        dao.deleteWeatherResponse()

        //Then
        val result = dao.getWeatherResponse().first().first()
        assertThat(result, `is`(nullValue()))
    }
}