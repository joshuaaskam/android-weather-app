@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.weatherapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.ui.screens.ForecastScreen
import com.example.weatherapp.ui.screens.WeatherUiState
import com.example.weatherapp.ui.screens.WeatherViewModel
import com.example.weatherapp.ui.theme.WeatherAppTheme

@Composable
fun WeatherApp() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { WeatherTopAppBar(scrollBehavior = scrollBehavior) }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            val weatherViewModel: WeatherViewModel =
                viewModel(factory = WeatherViewModel.Factory)
            ForecastScreen(
                weatherUiState = weatherViewModel.weatherUiState,
                retryAction = { /* TODO */ },
                contentPadding = it
            )
        }
    }
}

@Composable
fun WeatherTopAppBar(scrollBehavior: TopAppBarScrollBehavior, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = "Current Location",
                style = MaterialTheme.typography.headlineSmall,
            )
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun WeatherTopAppBarPreview() {
    WeatherAppTheme {
        WeatherTopAppBar(
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        )
    }
}