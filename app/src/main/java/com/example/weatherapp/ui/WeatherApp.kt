@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.weatherapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.R
import com.example.weatherapp.ui.screens.ForecastScreen
import com.example.weatherapp.ui.screens.WeatherUiState
import com.example.weatherapp.ui.screens.WeatherViewModel
import com.example.weatherapp.ui.theme.WeatherAppTheme

@Composable
fun WeatherApp() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val weatherViewModel: WeatherViewModel =
        viewModel(factory = WeatherViewModel.Factory)
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { WeatherTopAppBar(scrollBehavior = scrollBehavior,
            latitude = weatherViewModel.latitude,
            longitude = weatherViewModel.longitude,
            onLatitudeChange = { newLatitude ->
                weatherViewModel.latitude = newLatitude
            },
            onLongitudeChange = { newLongitude ->
                weatherViewModel.longitude = newLongitude
            },
        ) }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            ForecastScreen(
                weatherUiState = weatherViewModel.weatherUiState,
                retryAction = { /* TODO */ },
                contentPadding = it
            )
        }
    }
}

@Composable
fun WeatherTopAppBar(scrollBehavior: TopAppBarScrollBehavior,
                     latitude: Float,
                     longitude: Float,
                     onLatitudeChange: (Float) -> Unit,
                     onLongitudeChange: (Float) -> Unit,
                     modifier: Modifier = Modifier) {
    var editedLatitude by remember { mutableStateOf(latitude.toString()) }
    var editedLongitude by remember { mutableStateOf(longitude.toString()) }

    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Row(
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TextField(
                    value = editedLatitude,
                    label = { Text(stringResource(R.string.latitude)) },
                    onValueChange = {
                        editedLatitude = it
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        // Update latitude
                        // TODO: Add input validation for a latitude in valid range.
                        // Ex: Currently, an invalid latitude such as 500.0 will go through.
                        // Will only update value if it is a valid float.
                        editedLatitude.toFloatOrNull()?.let { newLatitude ->
                            onLatitudeChange(newLatitude)
                        }
                    })
                )
                TextField(
                    value = editedLongitude,
                    label = { Text(stringResource(R.string.longitude)) },
                    onValueChange = {
                        editedLongitude = it
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        // Update longitude
                        // TODO: Add input validation for a longitude in valid range.
                        editedLongitude.toFloatOrNull()?.let { newLongitude ->
                            onLongitudeChange(newLongitude)
                        }
                    })
                )
            }
        },
        modifier = modifier
    )
}
