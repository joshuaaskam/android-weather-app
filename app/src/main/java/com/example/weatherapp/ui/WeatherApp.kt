@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.weatherapp.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.R
import com.example.weatherapp.ui.screens.ForecastScreen
import com.example.weatherapp.ui.screens.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherApp() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val weatherViewModel: WeatherViewModel =
        viewModel(factory = WeatherViewModel.Factory)
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { WeatherTopAppBar(scrollBehavior = scrollBehavior,
            locationName = weatherViewModel.locationName,
            onLocationChange = { newLocation ->
                weatherViewModel.locationName = newLocation
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
                retryAction = weatherViewModel::getForecast,
                contentPadding = it
            )
        }
    }
}

@Composable
fun WeatherTopAppBar(scrollBehavior: TopAppBarScrollBehavior,
                     locationName: String,
                     onLocationChange: (String) -> Unit,
                     modifier: Modifier = Modifier) {
    var editedLocation by remember { mutableStateOf(locationName) }

    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Row{
                TextField(
                    value = editedLocation,
                    label = { Text(stringResource(R.string.location)) },
                    onValueChange = {
                        editedLocation = it
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        // Update location
                        onLocationChange(editedLocation)
                    })
                )
            }
        },
        modifier = modifier
    )
}
