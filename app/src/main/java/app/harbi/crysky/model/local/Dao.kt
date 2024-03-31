package app.harbi.crysky.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.harbi.crysky.model.data.CityResponse
import app.harbi.crysky.model.data.WeatherResponseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherResponseDao {

    @Query("SELECT * FROM weather_response")
    fun getWeatherResponse(): Flow<WeatherResponseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherResponse(weatherResponse: WeatherResponseEntity)

    @Query("DELETE FROM weather_response")
    suspend fun deleteWeatherResponse()

}

@Dao
interface CityResponseDao {

    @Query("SELECT * FROM city_response")
    fun getCityResponse(): Flow<CityResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCityResponse(cityResponse: CityResponse)

    @Delete
    suspend fun deleteCityResponse(cityResponse: CityResponse)

}