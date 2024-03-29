package app.harbi.crysky.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import app.harbi.crysky.model.WeatherResponseEntity

@Database(entities = [WeatherResponseEntity::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherResponseDao(): WeatherResponseDao

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
