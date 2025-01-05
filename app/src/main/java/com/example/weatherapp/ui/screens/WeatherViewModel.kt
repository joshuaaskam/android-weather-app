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
import com.example.weatherapp.data.GeocodeResponse
import com.example.weatherapp.data.GeocoderRepository
import com.example.weatherapp.data.NetworkWeatherForecastRepository
import com.example.weatherapp.data.WeatherForecastRepository
import com.example.weatherapp.model.HourlyWeatherForecast
import com.example.weatherapp.model.WeeklyWeatherForecast
import kotlinx.coroutines.coroutineScope
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

class WeatherViewModel(
    private val weatherForecastRepository: WeatherForecastRepository,
    private val geocoderRepository: GeocoderRepository
) : ViewModel() {
    // The mutable State that stores the status of the most recent request
    var weatherUiState: WeatherUiState by mutableStateOf(WeatherUiState.Loading)
        private set

    private var _locationName: String by mutableStateOf("")

    // These Latitude and longitude values will update the private states and update getForecast
    // when changed.
    var locationName: String
        get() = _locationName
        set(value) {
            _locationName = value
            getForecast()
        }

    // Gets hourly weather forecast from the weather API Retrofit service and updates the
    // hourly forecast.
    fun getForecast() {
        viewModelScope.launch {
            weatherUiState = WeatherUiState.Loading
            val geocodeResponse = geocoderRepository.getCoordinatesFromCityName(locationName)
            when(geocodeResponse){
                is GeocodeResponse.Error -> {
                    weatherUiState = WeatherUiState.Error(geocodeResponse.errorMessage)
                }
                is GeocodeResponse.Success -> {
                    weatherUiState = try {
                        WeatherUiState.Success(
                            hourlyForecast = weatherForecastRepository.getHourlyForecast(
                                latitude = geocodeResponse.coordinates.first.toFloat(),
                                longitude = geocodeResponse.coordinates.second.toFloat()
                            ),
                            weeklyForecast = weatherForecastRepository.getWeeklyForecast(
                                latitude = geocodeResponse.coordinates.first.toFloat(),
                                longitude = geocodeResponse.coordinates.second.toFloat()
                            )
                        )
                    } catch (e: IOException) {
                        WeatherUiState.Error(e.toString())
                    } catch (e: HttpException) {
                        WeatherUiState.Error(e.toString())
                    } catch (e: Exception) {
                        WeatherUiState.Error(e.toString())
                    }
                }
            }
        }
    }

    // Factory for WeatherViewModel that takes WeatherForecastRepository as a dependency
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as WeatherForecastApplication)
                val weatherForecastRepository = application.container.weatherForecastRepository
                val geocoderRepository = application.container.geocoderRepository
                WeatherViewModel(weatherForecastRepository = weatherForecastRepository, geocoderRepository = geocoderRepository)
            }
        }
    }
}
