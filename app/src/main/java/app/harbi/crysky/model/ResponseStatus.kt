package app.harbi.crysky.model

sealed class ResponseStatus {
    class Success(var data: WeatherResponse) : ResponseStatus()
    class Failure(var msg: String) : ResponseStatus()
    data object Loading : ResponseStatus()
}