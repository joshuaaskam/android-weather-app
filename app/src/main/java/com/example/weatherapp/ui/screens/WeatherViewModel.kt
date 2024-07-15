package com.example.weatherapp.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.weatherapp.WeatherForecastApplication
import com.example.weatherapp.data.NetworkWeatherForecastRepository
import com.example.weatherapp.data.WeatherForecastRepository
import com.example.weatherapp.model.HourlyWeatherForecast
import com.example.weatherapp.model.WeeklyWeatherForecast
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// Represents different states of the API request
sealed interface WeatherUiState {
    data class Success(
        val hourlyForecast: HourlyWeatherForecast,
        val weeklyForecast: WeeklyWeatherForecast
    ) : WeatherUiState
    data class Error(val errorMessage: String) : WeatherUiState
    data object Loading : WeatherUiState
}

class WeatherViewModel(private val weatherForecastRepository: WeatherForecastRepository) : ViewModel() {
    // The mutable State that stores the status of the most recent request
    var weatherUiState: WeatherUiState by mutableStateOf(WeatherUiState.Loading)
        private set

    //Call on init so we can display forecast immediately.
    init {
        getForecast(latitude = 33.86f, longitude = -118.08f)
    }

    // Gets hourly weather forecast from the weather API Retrofit service and updates the
    // hourly forecast.
    fun getForecast(latitude: Float, longitude: Float) {
        viewModelScope.launch {
            weatherUiState = WeatherUiState.Loading
            weatherUiState = try {
                WeatherUiState.Success(
                    hourlyForecast = weatherForecastRepository.getHourlyForecast(latitude = latitude, longitude= longitude),
                    weeklyForecast = weatherForecastRepository.getWeeklyForecast(latitude = latitude, longitude = longitude)
                )
            } catch (e: IOException) {
                WeatherUiState.Error(e.toString())
            } catch (e: HttpException) {
                WeatherUiState.Error(e.toString())
            }
        }
    }

    // Factory for WeatherViewModel that takes WeatherForecastRepository as a dependency
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as WeatherForecastApplication)
                val weatherForecastRepository = application.container.weatherForecastRepository
                WeatherViewModel(weatherForecastRepository = weatherForecastRepository)
            }
        }
    }
}
