package app.harbi.crysky.model

data class WeatherPojo(
    val current: Current? = null,
    val timezone: String? = null,
    val timezoneOffset: Int? = null,
    val daily: List<DailyItem?>? = null,
    val lon: Double? = null,
    val hourly: List<HourlyItem?>? = null,
    val lat: Double? = null,
)

data class WeatherItem(
    val icon: String? = null,
    val description: String? = null,
    val main: String? = null,
    val id: Int? = null,
)

data class HourlyItem(
    val temp: Double? = null,
    val visibility: Int? = null,
    val uvi: Any? = null,
    val pressure: Int? = null,
    val clouds: Int? = null,
    val feelsLike: Any? = null,
    val windGust: Any? = null,
    val dt: Int? = null,
    val pop: Double? = null,
    val windDeg: Int? = null,
    val dewPoint: Any? = null,
    val weather: List<WeatherItem?>? = null,
    val humidity: Int? = null,
    val windSpeed: Any? = null,
)

data class Current(
    val sunrise: Int? = null,
    val temp: Double? = null,
    val visibility: Int? = null,
    val uvi: Any? = null,
    val pressure: Int? = null,
    val clouds: Int? = null,
    val feelsLike: Any? = null,
    val windGust: Any? = null,
    val dt: Int? = null,
    val windDeg: Int? = null,
    val dewPoint: Any? = null,
    val sunset: Int? = null,
    val weather: List<WeatherItem?>? = null,
    val humidity: Int? = null,
    val windSpeed: Double? = null,
)

data class Temp(
    val min: Any? = null,
    val max: Any? = null,
    val eve: Any? = null,
    val night: Any? = null,
    val day: Double? = null,
    val morn: Any? = null,
)

data class FeelsLike(
    val eve: Any? = null,
    val night: Any? = null,
    val day: Any? = null,
    val morn: Any? = null,
)

data class DailyItem(
    val moonset: Int? = null,
    val sunrise: Int? = null,
    val temp: Temp? = null,
    val moonPhase: Any? = null,
    val uvi: Any? = null,
    val moonrise: Int? = null,
    val pressure: Int? = null,
    val clouds: Int? = null,
    val feelsLike: FeelsLike? = null,
    val windGust: Any? = null,
    val dt: Int? = null,
    val pop: Double? = null,
    val windDeg: Int? = null,
    val dewPoint: Any? = null,
    val sunset: Int? = null,
    val weather: List<WeatherItem?>? = null,
    val humidity: Int? = null,
    val windSpeed: Double? = null,
)