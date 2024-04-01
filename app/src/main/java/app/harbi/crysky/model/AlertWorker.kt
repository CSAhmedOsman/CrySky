package app.harbi.crysky.model

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import app.harbi.crysky.R
import app.harbi.crysky.model.data.CityResponse
import app.harbi.crysky.model.data.WeatherResponse
import app.harbi.crysky.model.local.WeatherDatabase
import app.harbi.crysky.model.local.WeatherLocalDataSourceImpl
import app.harbi.crysky.model.remote.CityService
import app.harbi.crysky.model.remote.WeatherRemoteDataSourceImpl
import app.harbi.crysky.model.remote.WeatherService
import app.harbi.crysky.model.repository.WeatherRepositoryImpl
import app.harbi.crysky.ui.home.view.HomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AlertWorker(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    private var repository: WeatherRepositoryImpl = WeatherRepositoryImpl(
        WeatherRemoteDataSourceImpl(
            Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(WeatherService::class.java),
            Retrofit.Builder().baseUrl("https://api.api-ninjas.com")
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(CityService::class.java)
        ), WeatherLocalDataSourceImpl(WeatherDatabase.getInstance(context).weatherResponseDao())
    )

    companion object {
        const val KEY_LATITUDE = "latitude"
        const val KEY_LONGITUDE = "longitude"
        const val KEY_UNIT = "unit"
        const val KEY_LANG = "language"
        const val KEY_DATE_TIME_MILLIS = "dateTimeMillis"
        const val CHANNEL_ID = "YourChannelId"
        const val NOTIFICATION_ID = 123
        const val KEY_CITY_NAME = "cityName"
    }

    override suspend fun doWork(): Result {
        try {
            val latitude = inputData.getDouble(KEY_LATITUDE, 0.0)
            val longitude = inputData.getDouble(KEY_LONGITUDE, 0.0)
            val unit = inputData.getString(KEY_UNIT)
            val lang = inputData.getString(KEY_LANG)
            val dateTimeMillis = inputData.getLong(KEY_DATE_TIME_MILLIS, 0L)
            val cityName = inputData.getString(KEY_CITY_NAME)
            val city = CityResponse(cityName!!, longitude, latitude, type = Constants.ALERT)


            val weatherResponse = getWeatherDetails(latitude, longitude, unit, lang)

            weatherResponse?.let {
                showNotification(
                    context.getString(
                        R.string.weatherDisc,
                        cityName
                    ) + " : " + it.list[0].weather[0].description
                )
                deleteCityFromRoom(city)
            }

            return Result.success()

        } catch (e: Exception) {

            return Result.failure()
        }
    }

    private suspend fun deleteCityFromRoom(city: CityResponse) {
        repository.deleteCityResponse(city)
    }


    private suspend fun getWeatherDetails(
        latitude: Double,
        longitude: Double,
        unit: String?,
        lang: String?,
    ): WeatherResponse? {
        return withContext(Dispatchers.IO) {
            try {
                repository.getRemoteWeather(
                    CityResponse(
                        latitude = latitude,
                        longitude = longitude
                    ), unit ?: "", lang ?: ""
                ).toList().first()
            } catch (e: Exception) {
                null
            }
        }
    }

    private fun showNotification(message: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Make alerts",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, HomeActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_MUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Weather Alert")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_cloud_fill)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
