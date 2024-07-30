package com.example.weatherapp.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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

    private var _latitude: Float by mutableFloatStateOf(0f)
    private var _longitude: Float by mutableFloatStateOf(0f)

    // These Latitude and longitude values will update the private states and update getForecast
    // when changed.
    var latitude: Float
        get() = _latitude
        set(value) {
            _latitude = value
            getForecast()
        }
    var longitude: Float
        get() = _longitude
        set(value) {
            _longitude = value
            getForecast()
        }

    //Call on init so we can display forecast immediately.
    init {
        getForecast()
    }

    // Gets hourly weather forecast from the weather API Retrofit service and updates the
    // hourly forecast.
    fun getForecast() {
        viewModelScope.launch {
            weatherUiState = WeatherUiState.Loading
            weatherUiState = try {
                WeatherUiState.Success(
                    hourlyForecast = weatherForecastRepository.getHourlyForecast(latitude = _latitude, longitude= _longitude),
                    weeklyForecast = weatherForecastRepository.getWeeklyForecast(latitude = _latitude, longitude = _longitude)
                )
            } catch (e: IOException) {
                WeatherUiState.Error(e.toString())
            } catch (e: HttpException) {
                WeatherUiState.Error(e.toString())
            }
            catch (e: Exception) {
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
