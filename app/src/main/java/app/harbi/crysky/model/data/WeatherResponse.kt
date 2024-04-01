package app.harbi.crysky.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "weather_response", primaryKeys = ["lat", "lon"])
data class WeatherResponseEntity(
    val cod: String = "",
    val message: Double = 0.0,
    val cnt: Int = 0,

//    val list: List<WeatherData> = emptyList(),
    val dt: Long = 0,
//    val main: Main = Main(),
    val temp: Double = 0.0,
    val feels_like: Double = 0.0,
    val temp_min: Double = 0.0,
    val temp_max: Double = 0.0,
    val pressure: Int = 0,
    val sea_level: Int = 0,
    val grnd_level: Int = 0,
    val humidity: Int = 0,
    val temp_kf: Double = 0.0,
//    val weather: List<Weather> = emptyList(),
    val main: String = "",
    val description: String = "",
    val icon: String = "",
//    val clouds: Clouds = Clouds(),
    val all: Int = 0,
//    val wind: Wind = Wind(),
    val speed: Double = 0.0,
    val deg: Int = 0,
    val gust: Double = 0.0,
    val visibility: Int = 0,
    val pop: Double = 0.0,
//    val rain: RainInfo? = null,
    val RainInfo3h: Double = 0.0,
//    val snow: Snow? = null,
    val Snow3h: Double = 0.0,
//    val sys: Sys = Sys(),
    val pod: String = "",
    val dt_txt: String = "",

//    val city: City = City(),
    val name: String = "",
//    val coord: Coord = Coord(),
    //@SerializedName("latitude")
    val lat: Double = 0.0,
    //@SerializedName("longitude")
    val lon: Double = 0.0,
    val country: String = "",
    val population: Int = 0,
    val timezone: Int = 0,
    val sunrise: Long = 0,
    val sunset: Long = 0,
    @SerializedName("is_capital") val isCapital: Boolean = false,
)

@Entity(tableName = "city_response", primaryKeys = ["latitude", "longitude", "type"])
data class CityResponse(
    val name: String = " ",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val country: String = "",

    var time: String = "",
    var type: String = "",
)

@Entity(tableName = "weather_response")
data class WeatherResponse(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cod: String = "",
    val message: Double = 0.0,
    val cnt: Int = 0,
    val list: List<WeatherData> = emptyList(),
    val city: City = City(),
)

data class CurrentWeatherResponse(
    val coord: Coord,
    val weather: List<Weather>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val timezone: Int,
    val id: Long,
    val name: String,
    val cod: Int,
)

data class WeatherData(
    val dt: Long = 0,
    val main: Main = Main(),
    val weather: List<Weather> = emptyList(),
    val clouds: Clouds = Clouds(),
    val wind: Wind = Wind(),
    val visibility: Int = 0,
    val pop: Double = 0.0,
    val rain: RainInfo? = null,
    val snow: Snow? = null,
    val sys: Sys = Sys(),
    val dt_txt: String = "",
)

data class Main(
    val temp: Double = 0.0,
    val feels_like: Double = 0.0,
    val temp_min: Double = 0.0,
    val temp_max: Double = 0.0,
    val pressure: Int = 0,
    val sea_level: Int = 0,
    val grnd_level: Int = 0,
    val humidity: Int = 0,
    val temp_kf: Double = 0.0,
)

data class Weather(
    val id: Long = 0,
    val main: String = "",
    val description: String = "",
    val icon: String = "",
)

data class Clouds(
    val all: Int = 0,
)

data class Wind(
    val speed: Double = 0.0,
    val deg: Int = 0,
    val gust: Double = 0.0,
)

data class RainInfo(
    val `3h`: Double = 0.0,
)

data class Snow(
    val `3h`: Double = 0.0,
)

data class Sys(
    val pod: String = "",
    val type: Int = 0,
    val id: Int = 0,
    val country: String = "",
    val sunrise: Long = 0,
    val sunset: Long = 0,
)

data class City(
    val id: Int = 0,
    val name: String = "",
    val coord: Coord = Coord(),
    val country: String = "",
    val population: Int = 0,
    val timezone: Int = 0,
    val sunrise: Long = 0,
    val sunset: Long = 0,
)

data class Coord(
    val lat: Double = 0.0,
    val lon: Double = 0.0,
)
