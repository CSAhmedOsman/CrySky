package app.harbi.crysky.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import app.harbi.crysky.model.data.CityResponse
import app.harbi.crysky.model.data.WeatherResponseEntity

@Database(entities = [WeatherResponseEntity::class, CityResponse::class], version = 2)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherResponseDao(): WeatherResponseDao
    abstract fun cityResponseDao(): CityResponseDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherDatabase? = null
        fun getInstance(ctx: Context): WeatherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(ctx, WeatherDatabase::class.java, "weather_db").build()
                INSTANCE = instance
                instance
            }
        }
    }
}
