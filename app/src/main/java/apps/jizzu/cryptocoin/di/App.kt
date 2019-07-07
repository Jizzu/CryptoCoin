package apps.jizzu.cryptocoin.di

import android.app.Application
import android.content.Context
import apps.jizzu.cryptocoin.di.component.AppComponent
import apps.jizzu.cryptocoin.di.component.DaggerAppComponent
import apps.jizzu.cryptocoin.di.module.ContextModule
import apps.jizzu.cryptocoin.di.module.NetworkModule

class App : Application() {
    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
                .contextModule(ContextModule(applicationContext))
                .networkModule(NetworkModule())
                .build()
    }

    fun getAppComponent() = appComponent

    companion object {
        fun getApp(context: Context) = context.applicationContext as App
    }
}