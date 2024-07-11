package com.example.weatherapp.data

import com.example.weatherapp.model.HourlyWeatherForecast
import com.example.weatherapp.model.WeeklyWeatherForecast
import com.example.weatherapp.network.WeatherApiService

interface WeatherForecastRepository {
    suspend fun getHourlyForecast(): HourlyWeatherForecast

    suspend fun getWeeklyForecast(): WeeklyWeatherForecast
}

class NetworkWeatherForecastRepository(
    private val weatherApiService: WeatherApiService
) : WeatherForecastRepository {
    /** Fetches list of MarsPhoto from marsApi*/
    override suspend fun getHourlyForecast(): HourlyWeatherForecast {
        TODO("Not yet implemented")
    }

    override suspend fun getWeeklyForecast(): WeeklyWeatherForecast {
        TODO("Not yet implemented")
    }
}
