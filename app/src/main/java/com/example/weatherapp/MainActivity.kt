package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherapp.data.api.ApiServiceBuilder
import com.example.weatherapp.data.repository.WeatherRepositoryImpl
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.usecase.GetCurrentWeatherUseCaseImpl
import com.example.weatherapp.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WeatherScreen(weatherViewModel = weatherViewModel, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun WeatherScreen(weatherViewModel: WeatherViewModel, modifier: Modifier = Modifier) {
    val currentWeather = weatherViewModel.currentWeather.observeAsState()
    val error = weatherViewModel.error.observeAsState()
    println(currentWeather.value)
    when {
        currentWeather.value != null -> {
            Text(
                text = "Temperature: ${currentWeather.value?.temperature}°C\n" +
                        "Humidity: ${currentWeather.value?.humidity}%\n" +
                        "Wind Speed: ${currentWeather.value?.windSpeed} km/h",
                modifier = modifier
            )
        }
        error.value != null -> {
            Text(
                text = "Error: ${error.value}",
                modifier = modifier
            )
        }
        else -> {
            Text(
                text = "Loading...",
                modifier = modifier
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherScreenPreview() {
    WeatherAppTheme {
        WeatherScreen(weatherViewModel = WeatherViewModel(GetCurrentWeatherUseCaseImpl(
            WeatherRepositoryImpl(ApiServiceBuilder.create())
        )))
    }
}
