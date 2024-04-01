package app.harbi.crysky.model

import android.content.SharedPreferences
import android.widget.ImageView
import app.harbi.crysky.R
import app.harbi.crysky.model.data.City
import app.harbi.crysky.model.data.Clouds
import app.harbi.crysky.model.data.Coord
import app.harbi.crysky.model.data.Main
import app.harbi.crysky.model.data.RainInfo
import app.harbi.crysky.model.data.Snow
import app.harbi.crysky.model.data.Sys
import app.harbi.crysky.model.data.Weather
import app.harbi.crysky.model.data.WeatherData
import app.harbi.crysky.model.data.WeatherResponse
import app.harbi.crysky.model.data.WeatherResponseEntity
import app.harbi.crysky.model.data.Wind
import java.util.Locale

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

object Constants {
    //Keys
    const val WEATHER_API_KEY = "7baa921546c01f29ed41d4976a7edfb0"
    const val X_API_KEY = "3wOLIQcmIyIscmugzgRSIw==CB1xnWodAvXqzQWu"

    const val SETTINGS_PREFERENCES = "MySettings"

    //Location
    const val LOCATION = "location"
    const val GPS = "GPS"
    const val MAP = "Map"
    const val LATITUDE = "latitude"
    const val LONGITUDE = "longitude"

    //Units
    const val UNITS = "units"
    const val STANDARD = "standard"    //Kelvin - meters per second
    const val METRIC = "metric"    //Celsius - meters per second
    const val IMPERIAL = "imperial"    //Fahrenheit - miles per hour

    //Language
    const val LANGUAGE = "language"
    const val ENGLISH = "en"
    const val ARABIC = "ar"

    //City
    const val FAV = "fav"
    const val ALERT = "alert"

}

fun <T> getSavedPreferences(
    preferences: SharedPreferences, key: String, defaultValue: T,
): T {
    val result = preferences.getString(key, defaultValue.toString()) ?: defaultValue.toString()
    return when (defaultValue) {
        is Int -> result.toInt() as T
        is Double -> result.toDouble() as T
        else -> result as T
    }
}

fun combineMaxMinWeatherData(max: WeatherData, min: WeatherData) =
    WeatherData(
        max.dt,
        Main(
            (max.main.temp + min.main.temp) / 2,
            (max.main.feels_like + min.main.feels_like) / 2,
            min.main.temp_min,
            max.main.temp_max,
            (max.main.pressure + min.main.pressure) / 2,
            (max.main.sea_level + min.main.sea_level) / 2,
            (max.main.grnd_level + min.main.grnd_level) / 2,
            (max.main.humidity + min.main.humidity) / 2,
            (max.main.temp_kf + min.main.temp_kf) / 2
        ),
        max.weather,
        Clouds((max.clouds.all + min.clouds.all) / 2),
        Wind(
            (max.wind.speed + min.wind.speed) / 2,
            (max.wind.deg + min.wind.deg) / 2,
            (max.wind.gust + min.wind.gust) / 2
        ),
        (max.visibility + min.visibility) / 2,
        (max.pop + min.pop) / 2,
        null, null,
        max.sys,
        max.dt_txt
    )

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

fun getFlagEmoji(countryCode: String): String {

    val flagMap = mapOf(
        "AF" to "\uD83C\uDDE6\uD83C\uDDEB", // Afghanistan
        "AL" to "\uD83C\uDDE6\uD83C\uDDF1", // Albania
        "DZ" to "\uD83C\uDDE9\uD83C\uDDFF", // Algeria
        "AD" to "\uD83C\uDDE6\uD83C\uDDEE", // Andorra
        "AO" to "\uD83C\uDDE6\uD83C\uDDF4", // Angola
        "AG" to "\uD83C\uDDE6\uD83C\uDDEE", // Antigua and Barbuda
        "AR" to "\uD83C\uDDE6\uD83C\uDDF7", // Argentina
        "AM" to "\uD83C\uDDE6\uD83C\uDDF2", // Armenia
        "AU" to "\uD83C\uDDE6\uD83C\uDDFA", // Australia
        "AT" to "\uD83C\uDDE6\uD83C\uDDF9", // Austria
        "AZ" to "\uD83C\uDDE6\uD83C\uDDFF", // Azerbaijan
        "BS" to "\uD83C\uDDE7\uD83C\uDDED", // Bahamas
        "BH" to "\uD83C\uDDE7\uD83C\uDDED", // Bahrain
        "BD" to "\uD83C\uDDE7\uD83C\uDDE9", // Bangladesh
        "BB" to "\uD83C\uDDE7\uD83C\uDDE7", // Barbados
        "BY" to "\uD83C\uDDE7\uD83C\uDDFE", // Belarus
        "BE" to "\uD83C\uDDE7\uD83C\uDDEA", // Belgium
        "BZ" to "\uD83C\uDDE7\uD83C\uDDFF", // Belize
        "BJ" to "\uD83C\uDDE7\uD83C\uDDEF", // Benin
        "BT" to "\uD83C\uDDE7\uD83C\uDDF9", // Bhutan
        "BO" to "\uD83C\uDDE7\uD83C\uDDF4", // Bolivia
        "BA" to "\uD83C\uDDE7\uD83C\uDDE6", // Bosnia and Herzegovina
        "BW" to "\uD83C\uDDE7\uD83C\uDDFC", // Botswana
        "BR" to "\uD83C\uDDE7\uD83C\uDDF7", // Brazil
        "BN" to "\uD83C\uDDE7\uD83C\uDDFE", // Brunei
        "BG" to "\uD83C\uDDE7\uD83C\uDDEC", // Bulgaria
        "BF" to "\uD83C\uDDE7\uD83C\uDDEB", // Burkina Faso
        "BI" to "\uD83C\uDDE7\uD83C\uDDEC", // Burundi
        "CV" to "\uD83C\uDDE8\uD83C\uDDFB", // Cabo Verde
        "KH" to "\uD83C\uDDE8\uD83C\uDDF2", // Cambodia
        "CM" to "\uD83C\uDDE8\uD83C\uDDF2", // Cameroon
        "CA" to "\uD83C\uDDE8\uD83C\uDDE6", // Canada
        "CF" to "\uD83C\uDDE8\uD83C\uDDEB", // Central African Republic
        "TD" to "\uD83C\uDDE8\uD83C\uDDED", // Chad
        "CL" to "\uD83C\uDDE8\uD83C\uDDF1", // Chile
        "CN" to "\uD83C\uDDE8\uD83C\uDDF3", // China
        "CO" to "\uD83C\uDDE8\uD83C\uDDF4", // Colombia
        "KM" to "\uD83C\uDDE8\uD83C\uDDF2", // Comoros
        "CD" to "\uD83C\uDDE8\uD83C\uDDEC", // Congo, Democratic Republic of the
        "CG" to "\uD83C\uDDE8\uD83C\uDDEE", // Congo, Republic of the
        "CR" to "\uD83C\uDDE8\uD83C\uDDF7", // Costa Rica
        "HR" to "\uD83C\uDDE8\uD83C\uDDFA", // Croatia
        "CU" to "\uD83C\uDDE8\uD83C\uDDFA", // Cuba
        "CY" to "\uD83C\uDDE8\uD83C\uDDFE", // Cyprus
        "CZ" to "\uD83C\uDDE8\uD83C\uDDFF", // Czech Republic
        "DK" to "\uD83C\uDDE9\uD83C\uDDF0", // Denmark
        "DJ" to "\uD83C\uDDE9\uD83C\uDDEF", // Djibouti
        "DM" to "\uD83C\uDDE9\uD83C\uDDF2", // Dominica
        "DO" to "\uD83C\uDDE9\uD83C\uDDF4", // Dominican Republic
        "TL" to "\uD83C\uDDEA\uD83C\uDDF9", // East Timor (Timor-Leste)
        "EC" to "\uD83C\uDDEA\uD83C\uDDE8", // Ecuador
        "EG" to "\uD83C\uDDEA\uD83C\uDDEC", // Egypt
        "SV" to "\uD83C\uDDEA\uD83C\uDDFB", // El Salvador
        "GQ" to "\uD83C\uDDEC\uD83C\uDDF6", // Equatorial Guinea
        "ER" to "\uD83C\uDDEA\uD83C\uDDF7", // Eritrea
        "EE" to "\uD83C\uDDEA\uD83C\uDDEA", // Estonia
        "SZ" to "\uD83C\uDDF8\uD83C\uDDFF", // Eswatini
        "ET" to "\uD83C\uDDEA\uD83C\uDDF9", // Ethiopia
        "FJ" to "\uD83C\uDDEB\uD83C\uDDEF", // Fiji
        "FI" to "\uD83C\uDDEB\uD83C\uDDEE", // Finland
        "FR" to "\uD83C\uDDEB\uD83C\uDDF7", // France
        "GA" to "\uD83C\uDDEC\uD83C\uDDE6", // Gabon
        "GM" to "\uD83C\uDDEC\uD83C\uDDF2", // Gambia
        "GE" to "\uD83C\uDDEC\uD83C\uDDEA", // Georgia
        "DE" to "\uD83C\uDDE9\uD83C\uDDEA", // Germany
        "GH" to "\uD83C\uDDEC\uD83C\uDDED", // Ghana
        "GR" to "\uD83C\uDDEC\uD83C\uDDF7", // Greece
        "GD" to "\uD83C\uDDEC\uD83C\uDDE9", // Grenada
        "GT" to "\uD83C\uDDEC\uD83C\uDDF9", // Guatemala
        "GN" to "\uD83C\uDDEC\uD83C\uDDF3", // Guinea
        "GW" to "\uD83C\uDDEC\uD83C\uDDFC", // Guinea-Bissau
        "GY" to "\uD83C\uDDEC\uD83C\uDDFE", // Guyana
        "HT" to "\uD83C\uDDED\uD83C\uDDF9", // Haiti
        "HN" to "\uD83C\uDDED\uD83C\uDDF3", // Honduras
        "HU" to "\uD83C\uDDED\uD83C\uDDFA", // Hungary
        "IS" to "\uD83C\uDDEE\uD83C\uDDF8", // Iceland
        "IN" to "\uD83C\uDDEE\uD83C\uDDF3", // India
        "ID" to "\uD83C\uDDEE\uD83C\uDDE9", // Indonesia
        "IR" to "\uD83C\uDDEE\uD83C\uDDF7", // Iran
        "IQ" to "\uD83C\uDDEE\uD83C\uDDF6", // Iraq
        "IE" to "\uD83C\uDDEE\uD83C\uDDEA", // Ireland
        "IL" to "\uD83C\uDDEE\uD83C\uDDF1", // Israel
        "IT" to "\uD83C\uDDEE\uD83C\uDDF9", // Italy
        "CI" to "\uD83C\uDDEE\uD83C\uDDEE", // Ivory Coast
        "JM" to "\uD83C\uDDEF\uD83C\uDDF2", // Jamaica
        "JP" to "\uD83C\uDDEF\uD83C\uDDF5", // Japan
        "JO" to "\uD83C\uDDEF\uD83C\uDDF4", // Jordan
        "KZ" to "\uD83C\uDDF0\uD83C\uDDFF", // Kazakhstan
        "KE" to "\uD83C\uDDF0\uD83C\uDDEA", // Kenya
        "KI" to "\uD83C\uDDF0\uD83C\uDDEE", // Kiribati
        "KP" to "\uD83C\uDDF0\uD83C\uDDF5", // Korea, North
        "KR" to "\uD83C\uDDF0\uD83C\uDDF7", // Korea, South
        "XK" to "\uD83C\uDDFD\uD83C\uDDF0", // Kosovo
        "KW" to "\uD83C\uDDF0\uD83C\uDDFC", // Kuwait
        "KG" to "\uD83C\uDDF0\uD83C\uDDEC", // Kyrgyzstan
        "LA" to "\uD83C\uDDF1\uD83C\uDDE6", // Laos
        "LV" to "\uD83C\uDDF1\uD83C\uDDFB", // Latvia
        "LB" to "\uD83C\uDDF1\uD83C\uDDE7", // Lebanon
        "LS" to "\uD83C\uDDF1\uD83C\uDDF8", // Lesotho
        "LR" to "\uD83C\uDDF1\uD83C\uDDF7", // Liberia
        "LY" to "\uD83C\uDDF1\uD83C\uDDFE", // Libya
        "LI" to "\uD83C\uDDF1\uD83C\uDDEE", // Liechtenstein
        "LT" to "\uD83C\uDDF1\uD83C\uDDF9", // Lithuania
        "LU" to "\uD83C\uDDF1\uD83C\uDDFA", // Luxembourg
        "MG" to "\uD83C\uDDF2\uD83C\uDDEC", // Madagascar
        "MW" to "\uD83C\uDDF2\uD83C\uDDFC", // Malawi
        "MY" to "\uD83C\uDDF2\uD83C\uDDFE", // Malaysia
        "MV" to "\uD83C\uDDF2\uD83C\uDDFB", // Maldives
        "ML" to "\uD83C\uDDF2\uD83C\uDDF1", // Mali
        "MT" to "\uD83C\uDDF2\uD83C\uDDF9", // Malta
        "MH" to "\uD83C\uDDF2\uD83C\uDDED", // Marshall Islands
        "MR" to "\uD83C\uDDF2\uD83C\uDDF7", // Mauritania
        "MU" to "\uD83C\uDDF2\uD83C\uDDFA", // Mauritius
        "MX" to "\uD83C\uDDF2\uD83C\uDDFD", // Mexico
        "FM" to "\uD83C\uDDEB\uD83C\uDDEE", // Micronesia
        "MD" to "\uD83C\uDDF2\uD83C\uDDE9", // Moldova
        "MC" to "\uD83C\uDDF2\uD83C\uDDE8", // Monaco
        "MN" to "\uD83C\uDDF2\uD83C\uDDF3", // Mongolia
        "ME" to "\uD83C\uDDF2\uD83C\uDDEA", // Montenegro
        "MA" to "\uD83C\uDDF2\uD83C\uDDE6", // Morocco
        "MZ" to "\uD83C\uDDF2\uD83C\uDDFF", // Mozambique
        "MM" to "\uD83C\uDDF2\uD83C\uDDF2", // Myanmar
        "NA" to "\uD83C\uDDF3\uD83C\uDDE6", // Namibia
        "NR" to "\uD83C\uDDF3\uD83C\uDDF7", // Nauru
        "NP" to "\uD83C\uDDF3\uD83C\uDDF5", // Nepal
        "NL" to "\uD83C\uDDF3\uD83C\uDDF1", // Netherlands
        "NZ" to "\uD83C\uDDF3\uD83C\uDDFF", // New Zealand
        "NI" to "\uD83C\uDDE7\uD83C\uDDEE", // Nicaragua
        "NE" to "\uD83C\uDDF3\uD83C\uDDEA", // Niger
        "NG" to "\uD83C\uDDEC\uD83C\uDDEC", // Nigeria
        "MK" to "\uD83C\uDDF2\uD83C\uDDF0", // North Macedonia
        "NO" to "\uD83C\uDDF3\uD83C\uDDF4", // Norway
        "OM" to "\uD83C\uDDF4\uD83C\uDDF2", // Oman
        "PK" to "\uD83C\uDDF5\uD83C\uDDF0", // Pakistan
        "PW" to "\uD83C\uDDF5\uD83C\uDDFC", // Palau
        "PS" to "\uD83C\uDDF5\uD83C\uDDF8", // Palestine
        "PA" to "\uD83C\uDDF5\uD83C\uDDE6", // Panama
        "PG" to "\uD83C\uDDF5\uD83C\uDDEC", // Papua New Guinea
        "PY" to "\uD83C\uDDF5\uD83C\uDDFE", // Paraguay
        "PE" to "\uD83C\uDDF5\uD83C\uDDEA", // Peru
        "PH" to "\uD83C\uDDF5\uD83C\uDDED", // Philippines
        "PL" to "\uD83C\uDDF5\uD83C\uDDF1", // Poland
        "PT" to "\uD83C\uDDF5\uD83C\uDDF9", // Portugal
        "QA" to "\uD83C\uDDF6\uD83C\uDDE6", // Qatar
        "RO" to "\uD83C\uDDF7\uD83C\uDDF4", // Romania
        "RU" to "\uD83C\uDDF7\uD83C\uDDFA", // Russia
        "RW" to "\uD83C\uDDF7\uD83C\uDDFC", // Rwanda
        "KN" to "\uD83C\uDDF0\uD83C\uDDF3", // Saint Kitts and Nevis
        "LC" to "\uD83C\uDDF1\uD83C\uDDE8",  // Saint Lucia
        "VC" to "\uD83C\uDDFB\uD83C\uDDE8", // Saint Vincent and the Grenadines
        "WS" to "\uD83C\uDDFC\uD83C\uDDF8", // Samoa
        "SM" to "\uD83C\uDDF8\uD83C\uDDF2", // San Marino
        "ST" to "\uD83C\uDDF8\uD83C\uDDF9", // Sao Tome and Principe
        "SA" to "\uD83C\uDDF8\uD83C\uDDE6", // Saudi Arabia
        "SN" to "\uD83C\uDDF8\uD83C\uDDF3", // Senegal
        "RS" to "\uD83C\uDDF7\uD83C\uDDF8", // Serbia
        "SC" to "\uD83C\uDDF8\uD83C\uDDE8", // Seychelles
        "SL" to "\uD83C\uDDF8\uD83C\uDDF1", // Sierra Leone
        "SG" to "\uD83C\uDDF8\uD83C\uDDEC", // Singapore
        "SK" to "\uD83C\uDDF8\uD83C\uDDF0", // Slovakia
        "SI" to "\uD83C\uDDF8\uD83C\uDDEE", // Slovenia
        "SB" to "\uD83C\uDDF8\uD83C\uDDE7", // Solomon Islands
        "SO" to "\uD83C\uDDF8\uD83C\uDDF4", // Somalia
        "ZA" to "\uD83C\uDDFF\uD83C\uDDE6", // South Africa
        "SS" to "\uD83C\uDDF8\uD83C\uDDF8", // South Sudan
        "ES" to "\uD83C\uDDEA\uD83C\uDDF8", // Spain
        "LK" to "\uD83C\uDDF1\uD83C\uDDF0", // Sri Lanka
        "SD" to "\uD83C\uDDF8\uD83C\uDDE9", // Sudan
        "SR" to "\uD83C\uDDF8\uD83C\uDDF7", // Suriname
        "SE" to "\uD83C\uDDF8\uD83C\uDDEA", // Sweden
        "CH" to "\uD83C\uDDE8\uD83C\uDDED", // Switzerland
        "SY" to "\uD83C\uDDF8\uD83C\uDDFE", // Syria
        "TW" to "\uD83C\uDDF9\uD83C\uDDFC", // Taiwan
        "TJ" to "\uD83C\uDDF9\uD83C\uDDEF", // Tajikistan
        "TZ" to "\uD83C\uDDF9\uD83C\uDDFF", // Tanzania
        "TH" to "\uD83C\uDDF9\uD83C\uDDED", // Thailand
        "TG" to "\uD83C\uDDF9\uD83C\uDDEC", // Togo
        "TO" to "\uD83C\uDDF9\uD83C\uDDF4", // Tonga
        "TT" to "\uD83C\uDDF9\uD83C\uDDF9", // Trinidad and Tobago
        "TN" to "\uD83C\uDDF9\uD83C\uDDF3", // Tunisia
        "TR" to "\uD83C\uDDF9\uD83C\uDDF7", // Turkey
        "TM" to "\uD83C\uDDF9\uD83C\uDDFE", // Turkmenistan
        "TV" to "\uD83C\uDDF9\uD83C\uDDFB", // Tuvalu
        "UG" to "\uD83C\uDDFA\uD83C\uDDEC", // Uganda
        "UA" to "\uD83C\uDDFA\uD83C\uDDE6", // Ukraine
        "AE" to "\uD83C\uDDE6\uD83C\uDDEA", // United Arab Emirates
        "GB" to "\uD83C\uDDEC\uD83C\uDDE7", // United Kingdom
        "US" to "\uD83C\uDDFA\uD83C\uDDF8", // United States
        "UY" to "\uD83C\uDDFA\uD83C\uDDFE", // Uruguay
        "UZ" to "\uD83C\uDDFA\uD83C\uDDFF", // Uzbekistan
        "VU" to "\uD83C\uDDFB\uD83C\uDDFA", // Vanuatu
        "VA" to "\uD83C\uDDFB\uD83C\uDDE6", // Vatican City
        "VE" to "\uD83C\uDDFB\uD83C\uDDEA", // Venezuela
        "VN" to "\uD83C\uDDFB\uD83C\uDDF3", // Vietnam
        "YE" to "\uD83C\uDDFE\uD83C\uDDEA", // Yemen
        "ZM" to "\uD83C\uDDFF\uD83C\uDDF2"  // Zambia
    )

    return flagMap[countryCode.uppercase(Locale.getDefault())]
        ?: "\uD83C\uDDF5\uD83C\uDDF8" // Default flag emoji
}
