package com.example.weatherapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import com.example.weatherapp.model.Daily
import com.example.weatherapp.model.Hourly
import java.time.LocalDate
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
        is WeatherUiState.Loading -> Text(
            text = stringResource(R.string.loading),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
        )
        is WeatherUiState.Success -> {
            Column(
                verticalArrangement = Arrangement.Top
            ) {
                HourlyForecast(
                    hourlyForecasts = weatherUiState.hourlyForecast.hourly,
                    contentPadding = contentPadding
                )
                WeeklyForecast(
                    dailyForecasts = weatherUiState.weeklyForecast.daily,
                    contentPadding = contentPadding
                )
            }
        }
        is WeatherUiState.Error -> {
            Column (
                modifier = modifier,
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = weatherUiState.errorMessage,
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = stringResource(R.string.loading_failed),
                    modifier = Modifier.padding(8.dp)
                )
                Button(onClick = retryAction) {
                    Text(stringResource(R.string.retry))
                }
            }
        }
    }
}

@Composable
fun HourlyForecast(
    hourlyForecasts: Hourly,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val tempHourPairs = hourlyForecasts.time.zip(hourlyForecasts.temperature2m)

    Card(
        modifier = Modifier
            .wrapContentHeight()
            .padding(8.dp),
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

@Composable
fun HourlyForecastCard(tempHourPair: Pair<String, Float>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = formatTimeStringToHour(tempHourPair.first))
        Text(text = tempHourPair.second.toString() + "\u2109")
    }
}

@Composable
fun WeeklyForecast(
    dailyForecasts: Daily,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val dayMaxMinPair = dailyForecasts.time.zip(dailyForecasts.temperatureMax.zip(dailyForecasts.temperatureMin))

    Card(
        modifier = Modifier
            .wrapContentHeight()
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        LazyColumn {
            items(dayMaxMinPair) {
                DailyForecastCard(
                    dayMaxMinPair = it,
                    modifier = modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun DailyForecastCard(dayMaxMinPair: Pair<String, Pair<Float, Float>>, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Text(text = formatDateToDay(dayMaxMinPair.first) + " ")
        Text(text = dayMaxMinPair.second.first.toString() + "\u2109" + "-")
        Text(text = dayMaxMinPair.second.second.toString() + "\u2109")
    }
}

fun formatTimeStringToHour(input: String): String {
    val dateTime = LocalDateTime.parse(input)

    val outputFormatter = DateTimeFormatter.ofPattern("ha")

    return dateTime.format(outputFormatter)
}

fun formatDateToDay(dateString: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(dateString, formatter)
    return date.dayOfWeek.toString()
}
