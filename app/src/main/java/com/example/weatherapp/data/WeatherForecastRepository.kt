package com.example.weatherapp.data

import com.example.weatherapp.model.HourlyWeatherForecast
import com.example.weatherapp.model.WeeklyWeatherForecast
import com.example.weatherapp.network.WeatherApiService

interface WeatherForecastRepository {
    suspend fun getHourlyForecast(latitude: Float, longitude: Float): HourlyWeatherForecast

    suspend fun getWeeklyForecast(latitude: Float, longitude: Float): WeeklyWeatherForecast
}

class NetworkWeatherForecastRepository(
    private val weatherApiService: WeatherApiService
) : WeatherForecastRepository {
    override suspend fun getHourlyForecast(latitude: Float, longitude: Float): HourlyWeatherForecast {
        return weatherApiService.getHourlyWeatherForecast(latitude = latitude, longitude = longitude)
    }

    override suspend fun getWeeklyForecast(latitude: Float, longitude: Float): WeeklyWeatherForecast {
        return weatherApiService.getWeeklyWeatherForecast(latitude = latitude, longitude = longitude)
    }
}
