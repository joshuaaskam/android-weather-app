package com.example.weatherapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherapp.model.Hourly
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ForecastScreen(
    weatherUiState: WeatherUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    when (weatherUiState) {
        is WeatherUiState.Loading -> Text(text = "Loading")
        is WeatherUiState.Success -> HourlyForecastScreen(
            hourlyForecasts = weatherUiState.hourlyForecast.hourly, contentPadding = contentPadding
        )
        is WeatherUiState.Error -> Text(text = weatherUiState.errorMessage) // Should add retry here
    }
}

@Composable
fun HourlyForecastScreen(
    hourlyForecasts: Hourly,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val tempHourPairs = hourlyForecasts.time.zip(hourlyForecasts.temperature2m)

    Box (
        modifier = Modifier.fillMaxSize().padding(8.dp),
        contentAlignment = Alignment.TopCenter
    ){
        Card(
            modifier = Modifier.wrapContentHeight(),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            LazyRow {
                items(tempHourPairs) {
                    HourlyForecastCard(
                        tempHourPair = it,
                        modifier = modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun HourlyForecastCard(tempHourPair: Pair<String, Float>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = formatTimeString(tempHourPair.first))
        Text(text = tempHourPair.second.toString() + "\u2109")
    }
}

fun formatTimeString(input: String): String {
    val dateTime = LocalDateTime.parse(input)

    val outputFormatter = DateTimeFormatter.ofPattern("ha")

    return dateTime.format(outputFormatter)
}