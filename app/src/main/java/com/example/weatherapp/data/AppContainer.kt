package com.example.weatherapp.data

import com.example.weatherapp.network.WeatherApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val weatherForecastRepository: WeatherForecastRepository
}

class DefaultAppContainer : AppContainer {
    private val baseUrl = "https://api.open-meteo.com/"

    // Retrofit builder will build a retrofit object using a kotlinx.serialization converter
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    // Retrofit service object for creating api calls.
    // Retrofit automatically creates implementation of a given interface.
    private val retrofitService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }

    // Dependency injection of repository
    override val weatherForecastRepository: WeatherForecastRepository by lazy {
        NetworkWeatherForecastRepository(retrofitService)
    }
}