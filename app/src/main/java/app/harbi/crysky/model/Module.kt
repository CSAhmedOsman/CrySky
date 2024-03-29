package app.harbi.crysky.model

import app.harbi.crysky.model.local.WeatherDatabase
import app.harbi.crysky.model.local.WeatherLocalDataSource
import app.harbi.crysky.model.local.WeatherLocalDataSourceImpl
import app.harbi.crysky.model.local.WeatherResponseDao
import app.harbi.crysky.model.remote.WeatherRemoteDataSource
import app.harbi.crysky.model.remote.WeatherRemoteDataSourceImpl
import app.harbi.crysky.model.remote.WeatherService
import app.harbi.crysky.ui.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val modules = module {
    single<WeatherResponseDao> {
        WeatherDatabase.getInstance(androidContext()).weatherResponseDao()
    }

    single<WeatherService> {
        Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(WeatherService::class.java)
    }

    factory<WeatherLocalDataSource> { WeatherLocalDataSourceImpl(get()) }
    factory<WeatherRemoteDataSource> { WeatherRemoteDataSourceImpl(get()) }

    single<WeatherRepository> { WeatherRepositoryImpl(get(), get()) }
}

val viewModels = module {
    viewModel { HomeViewModel(get()) }
}