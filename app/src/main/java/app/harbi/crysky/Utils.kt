package app.harbi.crysky

import android.widget.ImageView
import app.harbi.crysky.model.City
import app.harbi.crysky.model.Clouds
import app.harbi.crysky.model.Coord
import app.harbi.crysky.model.Main
import app.harbi.crysky.model.RainInfo
import app.harbi.crysky.model.Snow
import app.harbi.crysky.model.Sys
import app.harbi.crysky.model.Weather
import app.harbi.crysky.model.WeatherData
import app.harbi.crysky.model.WeatherResponse
import app.harbi.crysky.model.WeatherResponseEntity
import app.harbi.crysky.model.Wind

fun loadImage(view: ImageView, uri: String?, myError: Int) {
    var resources = myError
    if (!uri.isNullOrEmpty()) {
        when (uri) {
            "01d" -> resources = R.drawable.d01
            "02d" -> resources = R.drawable.d02
            "03d" -> resources = R.drawable.d03
            "04d" -> resources = R.drawable.d04
            "09d" -> resources = R.drawable.d09
            "10d" -> resources = R.drawable.d10
            "11d" -> resources = R.drawable.d11
            "13d" -> resources = R.drawable.d13
            "50d" -> resources = R.drawable.d50
            "01n" -> resources = R.drawable.n01
            "02n" -> resources = R.drawable.n02
            "03n" -> resources = R.drawable.n03
            "04n" -> resources = R.drawable.n04
            "09n" -> resources = R.drawable.n09
            "10n" -> resources = R.drawable.n10
            "11n" -> resources = R.drawable.n11
            "13n" -> resources = R.drawable.n13
            "50n" -> resources = R.drawable.n50
        }
    }
    view.setImageResource(resources)
}

/*fun loadImage(view: ImageView, uri: String?, myError: Int) {
    if (!uri.isNullOrEmpty()) {
        val url = "https://openweathermap.org/img/wn/$uri.png"
        Log.i("TAG", "loadImage: $url")
        Glide.with(view.context)
            .load(url)
            .apply(RequestOptions().override(48, 48))
            .error(myError)
            .into(view)
    } else {
        view.setImageResource(myError)
    }
    *//*
    if (!uri.isNullOrEl")
        Picasso.get()
        .load(url)
        .error(myError)
        .into(view)
    }*//*
}*/

sealed class Constants(val value: String) {
    object API_KEY : Constants("7baa921546c01f29ed41d4976a7edfb0")
    object X_API_KEY : Constants("3wOLIQcmIyIscmugzgRSIw==CB1xnWodAvXqzQWu")
}

fun convertResponseToEntity(response: WeatherResponse): WeatherResponseEntity {
    val firstWeatherData = response.list.firstOrNull()
    return WeatherResponseEntity(
        cod = response.cod,
        message = response.message,
        cnt = response.cnt,
        dt = firstWeatherData?.dt ?: 0,
        temp = firstWeatherData?.main?.temp ?: 0.0,
        feels_like = firstWeatherData?.main?.feels_like ?: 0.0,
        temp_min = firstWeatherData?.main?.temp_min ?: 0.0,
        temp_max = firstWeatherData?.main?.temp_max ?: 0.0,
        pressure = firstWeatherData?.main?.pressure ?: 0,
        sea_level = firstWeatherData?.main?.sea_level ?: 0,
        grnd_level = firstWeatherData?.main?.grnd_level ?: 0,
        humidity = firstWeatherData?.main?.humidity ?: 0,
        temp_kf = firstWeatherData?.main?.temp_kf ?: 0.0,
        main = firstWeatherData?.weather?.get(0)?.main ?: "",
        description = firstWeatherData?.weather?.get(0)?.description ?: "",
        icon = firstWeatherData?.weather?.get(0)?.icon ?: "",
        all = firstWeatherData?.clouds?.all ?: 0,
        speed = firstWeatherData?.wind?.speed ?: 0.0,
        deg = firstWeatherData?.wind?.deg ?: 0,
        gust = firstWeatherData?.wind?.gust ?: 0.0,
        visibility = firstWeatherData?.visibility ?: 0,
        pop = firstWeatherData?.pop ?: 0.0,
        RainInfo3h = firstWeatherData?.rain?.`3h` ?: 0.0,
        Snow3h = firstWeatherData?.snow?.`3h` ?: 0.0,
        pod = firstWeatherData?.sys?.pod ?: "",
        dt_txt = firstWeatherData?.dt_txt ?: "",
        name = response.city.name,
        lat = response.city.coord.lat,
        lon = response.city.coord.lon,
        country = response.city.country,
        population = response.city.population,
        timezone = response.city.timezone,
        sunrise = response.city.sunrise,
        sunset = response.city.sunset,
    )
}

fun convertEntityToResponse(entity: WeatherResponseEntity): WeatherResponse {
    return WeatherResponse(
        cod = entity.cod,
        message = entity.message,
        cnt = entity.cnt,
        list = listOf(
            WeatherData(
                dt = entity.dt,
                main = Main(
                    temp = entity.temp,
                    feels_like = entity.feels_like,
                    temp_min = entity.temp_min,
                    temp_max = entity.temp_max,
                    pressure = entity.pressure,
                    sea_level = entity.sea_level,
                    grnd_level = entity.grnd_level,
                    humidity = entity.humidity,
                    temp_kf = entity.temp_kf,
                ),
                weather = listOf(
                    Weather(
                        main = entity.main,
                        description = entity.description,
                        icon = entity.icon,
                    )
                ),
                clouds = Clouds(all = entity.all),
                wind = Wind(speed = entity.speed, deg = entity.deg, gust = entity.gust),
                visibility = entity.visibility,
                pop = entity.pop,
                rain = RainInfo(`3h` = entity.RainInfo3h),
                snow = Snow(`3h` = entity.Snow3h),
                sys = Sys(pod = entity.pod),
                dt_txt = entity.dt_txt,
            )
        ),
        city = City(
            name = entity.name,
            coord = Coord(lat = entity.lat, lon = entity.lon),
            country = entity.country,
            population = entity.population,
            timezone = entity.timezone,
            sunrise = entity.sunrise,
            sunset = entity.sunset,
        )
    )
}
