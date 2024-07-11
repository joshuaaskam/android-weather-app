package com.example.weatherapp

import android.app.Application
import com.example.weatherapp.data.AppContainer
import com.example.weatherapp.data.DefaultAppContainer

// This will be called onCreate() when the app starts and creates dependencies.
class WeatherForecastApplication : Application() {
    // AppContainer instance used by the rest of classes to obtain dependencies
    private lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}