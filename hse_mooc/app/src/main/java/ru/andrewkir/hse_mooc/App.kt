package com.example.digitalstudentassistant

import android.app.Application
import com.example.digitalstudentassistant.di.AppComponent
import com.example.digitalstudentassistant.di.modules.NetworkModule.Companion.BASE_URL

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appContext(this)
            .apiUrl(BuildConfig.BASE_URL)
            .build()
        appComponent.inject(this)
    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}