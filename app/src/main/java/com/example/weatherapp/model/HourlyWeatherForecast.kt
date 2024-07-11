package com.example.weatherapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HourlyWeatherForecast(
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
    @SerialName(value = "hourly_units")
    val hourlyUnits: HourlyUnits,
    val hourly: Hourly
)

@Serializable
data class HourlyUnits(
    val time: String,
    @SerialName(value = "temperature_2m")
    val temperature2m: String
)

@Serializable
data class Hourly(
    val time: List<String>,
    @SerialName(value = "temperature_2m")
    val temperature2m: List<Float>
)

