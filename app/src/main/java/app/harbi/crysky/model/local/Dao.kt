package app.harbi.crysky.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.harbi.crysky.model.Constants
import app.harbi.crysky.model.data.CityResponse
import app.harbi.crysky.model.data.WeatherResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherResponseDao {

    @Query("SELECT * FROM weather_response")
    fun getWeatherResponse(): Flow<List<WeatherResponse>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherResponse(weatherResponse: WeatherResponse)

    @Query("DELETE FROM weather_response")
    suspend fun deleteWeatherResponse()

    @Query("SELECT * FROM city_response WHERE type = '${Constants.FAV}'")
    fun getFavCityResponse(): Flow<List<CityResponse>>

    @Query("SELECT * FROM city_response WHERE type = '${Constants.ALERT}'")
    fun getAlertCityResponse(): Flow<List<CityResponse>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCityResponse(cityResponse: CityResponse)

    @Delete
    suspend fun deleteCityResponse(cityResponse: CityResponse)

}
