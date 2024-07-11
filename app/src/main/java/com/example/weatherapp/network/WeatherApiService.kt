package com.example.weatherapp.network

import com.example.weatherapp.model.WeeklyWeatherForecast
import com.example.weatherapp.model.HourlyWeatherForecast
import retrofit2.http.GET
interface WeatherApiService {
    @GET("v1/forecast")
    suspend fun getHourlyWeatherForecast(): HourlyWeatherForecast
}