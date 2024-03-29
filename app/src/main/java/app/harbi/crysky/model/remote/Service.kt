package app.harbi.crysky.model.remote

import app.harbi.crysky.Constants
import app.harbi.crysky.model.CityResponse
import app.harbi.crysky.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface WeatherService {
    @GET("forecast")
    suspend fun getWeather(
        @Query("lat") latitude: Double = 29.95,
        @Query("lon") longitude: Double = 31.54,
        @Query("appid") apiKey: String = Constants.API_KEY.value,
        @Query("units") units: String = "metric",
        @Query("lang") language: String = "ar",
    ): Response<WeatherResponse>

    @GET("weather") // Endpoint for current weather
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double = 29.95,
        @Query("lon") longitude: Double = 31.54,
        @Query("appid") apiKey: String = Constants.API_KEY.value,
        @Query("units") units: String = "metric",
        @Query("lang") language: String = "ar",
    ): Response<WeatherResponse>
}

interface CityService {
    @Headers("X-Api-Key: 3wOLIQcmIyIscmugzgRSIw==CB1xnWodAvXqzQWu")
    @GET("/v1/city")
    suspend fun getCities(
        @Query("name") cityName: String = " ",
        @Query("country") country: String = "",
        @Query("min_population") minPopulation: Int = 0,
        @Query("max_population") maxPopulation: Int = 0,
        @Query("min_lat") minLat: Double = 0.0,
        @Query("max_lat") maxLat: Double = 0.0,
        @Query("min_lon") minLon: Double = 0.0,
        @Query("max_lon") maxLon: Double = 0.0,
        @Query("limit") limit: Int = 10,
    ): Response<List<CityResponse>>
}