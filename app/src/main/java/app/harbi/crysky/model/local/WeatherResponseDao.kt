package app.harbi.crysky.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.harbi.crysky.model.WeatherResponseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherResponseDao {

    @Query("SELECT * FROM weather_response")
    fun getWeatherResponse(): Flow<WeatherResponseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherResponse(weatherResponse: WeatherResponseEntity)

    @Delete
    suspend fun deleteWeatherResponse(weatherResponse: WeatherResponseEntity)

}