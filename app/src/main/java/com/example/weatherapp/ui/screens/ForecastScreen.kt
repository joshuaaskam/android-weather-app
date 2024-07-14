package com.example.weatherapp.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ForecastScreen(
    weatherUiState: WeatherUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    when (weatherUiState) {
        is WeatherUiState.Loading -> Text(text = "Loading")
        is WeatherUiState.Success -> Text(text = weatherUiState.hourlyForecast.toString())
        is WeatherUiState.Error -> Text(text = weatherUiState.errorMessage) // Should add retry here
    }
}