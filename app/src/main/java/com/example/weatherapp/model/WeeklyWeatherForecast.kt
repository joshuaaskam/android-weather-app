package com.example.weatherapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeeklyWeatherForecast(
    val latitude: Float,
    val longitude: Float,
    @SerialName(value = "generationtime_ms")
    val generationTimeMs: Float,
    @SerialName(value = "utc_offset_seconds")
    val utcOffsetSeconds: Int,
    @SerialName(value = "timezone")
    val timeZone: String,
    @SerialName(value = "timezone_abbreviation")
    val timeZoneAbbreviation: String,
    val elevation: Float,
    @SerialName("daily_units")
    val dailyUnits: DailyUnits,
    @SerialName("daily")
    val daily: Daily
)

@Serializable

data class DailyUnits(
    val time: String,
    @SerialName("temperature_2m_max")
    val temperatureMaxUnit: String,
    @SerialName("temperature_2m_min")
    val temperatureMinUnit: String
)

@Serializable

data class Daily(
    val time: List<String>,
    @SerialName("temperature_2m_max")
    val temperatureMax: List<Float>,
    @SerialName("temperature_2m_min")
    val temperatureMin: List<Float>
)
