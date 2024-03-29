package app.harbi.crysky.ui

import android.app.Application
import app.harbi.crysky.model.modules
import app.harbi.crysky.model.viewModels
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(modules = arrayOf(modules, viewModels))
        }
    }
}
