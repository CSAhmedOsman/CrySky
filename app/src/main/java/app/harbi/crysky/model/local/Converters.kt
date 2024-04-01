package app.harbi.crysky.model.local

import androidx.room.TypeConverter
import app.harbi.crysky.model.data.City
import app.harbi.crysky.model.data.CityResponse
import app.harbi.crysky.model.data.Clouds
import app.harbi.crysky.model.data.Coord
import app.harbi.crysky.model.data.CurrentWeatherResponse
import app.harbi.crysky.model.data.Main
import app.harbi.crysky.model.data.RainInfo
import app.harbi.crysky.model.data.Snow
import app.harbi.crysky.model.data.Sys
import app.harbi.crysky.model.data.Weather
import app.harbi.crysky.model.data.WeatherData
import app.harbi.crysky.model.data.WeatherResponse
import app.harbi.crysky.model.data.Wind
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Converters {
    @TypeConverter
    fun fromCoord(coord: Coord): String {
        return Gson().toJson(coord)
    }

    @TypeConverter
    fun toCoord(json: String): Coord {
        return Gson().fromJson(json, Coord::class.java)
    }

    @TypeConverter
    fun fromCity(city: City): String {
        return Gson().toJson(city)
    }

    @TypeConverter
    fun toCity(json: String): City {
        return Gson().fromJson(json, City::class.java)
    }

    @TypeConverter
    fun fromMain(main: Main): String {
        return Gson().toJson(main)
    }

    @TypeConverter
    fun toMain(json: String): Main {
        return Gson().fromJson(json, Main::class.java)
    }

    @TypeConverter
    fun fromWeather(weather: List<Weather>): String {
        return Gson().toJson(weather)
    }

    @TypeConverter
    fun toWeather(json: String): List<Weather> {
        val type = object : TypeToken<List<Weather>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromClouds(clouds: Clouds): String {
        return Gson().toJson(clouds)
    }

    @TypeConverter
    fun toClouds(json: String): Clouds {
        return Gson().fromJson(json, Clouds::class.java)
    }

    @TypeConverter
    fun fromWind(wind: Wind): String {
        return Gson().toJson(wind)
    }

    @TypeConverter
    fun toWind(json: String): Wind {
        return Gson().fromJson(json, Wind::class.java)
    }

    @TypeConverter
    fun fromRainInfo(rainInfo: RainInfo?): String {
        return Gson().toJson(rainInfo)
    }

    @TypeConverter
    fun toRainInfo(json: String): RainInfo? {
        return Gson().fromJson(json, RainInfo::class.java)
    }

    @TypeConverter
    fun fromSnow(snow: Snow?): String {
        return Gson().toJson(snow)
    }

    @TypeConverter
    fun toSnow(json: String): Snow? {
        return Gson().fromJson(json, Snow::class.java)
    }

    @TypeConverter
    fun fromSys(sys: Sys): String {
        return Gson().toJson(sys)
    }

    @TypeConverter
    fun toSys(json: String): Sys {
        return Gson().fromJson(json, Sys::class.java)
    }

    @TypeConverter
    fun fromWeatherDataList(weatherDataList: List<WeatherData>): String {
        return Gson().toJson(weatherDataList)
    }

    @TypeConverter
    fun toWeatherDataList(json: String): List<WeatherData> {
        val type = object : TypeToken<List<WeatherData>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromWeatherResponse(weatherResponse: WeatherResponse): String {
        return Gson().toJson(weatherResponse)
    }

    @TypeConverter
    fun toWeatherResponse(json: String): WeatherResponse {
        return Gson().fromJson(json, WeatherResponse::class.java)
    }

    @TypeConverter
    fun fromCurrentWeatherResponse(currentWeatherResponse: CurrentWeatherResponse): String {
        return Gson().toJson(currentWeatherResponse)
    }

    @TypeConverter
    fun toCurrentWeatherResponse(json: String): CurrentWeatherResponse {
        return Gson().fromJson(json, CurrentWeatherResponse::class.java)
    }

    @TypeConverter
    fun fromCityResponse(cityResponse: CityResponse): String {
        return Gson().toJson(cityResponse)
    }

    @TypeConverter
    fun toCityResponse(json: String): CityResponse {
        return Gson().fromJson(json, CityResponse::class.java)
    }
}
