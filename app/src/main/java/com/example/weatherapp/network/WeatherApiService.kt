package com.example.weatherapp.network

import com.example.weatherapp.model.WeeklyWeatherForecast
import com.example.weatherapp.model.HourlyWeatherForecast
import retrofit2.http.GET
import retrofit2.http.Query
interface WeatherApiService {
    @GET("v1/forecast")
    suspend fun getHourlyWeatherForecast(
        @Query("latitude") latitude: Float,
        @Query("longitude") longitude: Float,
        @Query("hourly") hourly: String = "temperature_2m",
        @Query("temperature_unit") temperatureUnit: String = "fahrenheit",
        @Query("timezone") timeZone: String = "auto",
        @Query("forecast_days") forecastDays: Int = 1
    ): HourlyWeatherForecast
}