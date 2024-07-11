package com.example.weatherapp.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.ui.screens.WeatherUiState
import com.example.weatherapp.ui.screens.WeatherViewModel

@Composable
fun WeatherApp() {
    val weatherViewModel: WeatherViewModel =
        viewModel(factory = WeatherViewModel.Factory)
    val weatherUiState: WeatherUiState = weatherViewModel.weatherUiState
    when (weatherUiState) {
        is WeatherUiState.Loading -> Text(text = "Loading")
        is WeatherUiState.Success -> Text(text = weatherUiState.hourlyForecast.toString())
        is WeatherUiState.Error -> Text(text = weatherUiState.errorMessage)
    }
}