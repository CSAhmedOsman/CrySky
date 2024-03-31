package app.harbi.crysky.model.data

sealed class ResponseStatus<T> {
    class Success<T>(var data: T) : ResponseStatus<T>()
    class Failure<T>(var msg: String) : ResponseStatus<T>()
    class Loading<T> : ResponseStatus<T>()
}