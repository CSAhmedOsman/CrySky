package app.harbi.crysky.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.harbi.crysky.model.data.CityResponse
import app.harbi.crysky.model.data.WeatherResponse

@Database(entities = [WeatherResponse::class, CityResponse::class], version = 4)
@TypeConverters(Converters::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherResponseDao(): WeatherResponseDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherDatabase? = null
        fun getInstance(ctx: Context): WeatherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(
                        ctx.applicationContext,
                        WeatherDatabase::class.java,
                        "weather_db"
                    ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
