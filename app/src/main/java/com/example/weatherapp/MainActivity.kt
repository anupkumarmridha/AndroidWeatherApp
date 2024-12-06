package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable

import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import com.example.weatherapp.ui.theme.WeatherAppTheme
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
    val dailyWeather = weatherViewModel.dailyWeather.observeAsState()
    val hourlyWeather = weatherViewModel.hourlyWeather.observeAsState()
    val error = weatherViewModel.error.observeAsState()
// Define latitude and longitude at the top level
    val latitude = remember { mutableStateOf("52.52") }
    val longitude = remember { mutableStateOf("13.41") }
    Column(modifier = modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Enter Latitude:")

            TextField(
                value = latitude.value,
                onValueChange = { latitude.value = it },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Enter Longitude:")

            TextField(
                value = longitude.value,
                onValueChange = { longitude.value = it },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val lat = latitude.value.toDoubleOrNull()
            val lon = longitude.value.toDoubleOrNull()
            if (lat != null && lon != null) {
                weatherViewModel.fetchWeather(lat, lon)
            }
        }) {
            Text(text = "Get Weather")
        }
        Spacer(modifier = Modifier.height(16.dp))

        when {
            currentWeather.value != null && dailyWeather.value != null && hourlyWeather.value != null -> {
                Text(
                    text = "Current Weather:\n" +
                            "Temperature: ${currentWeather.value?.temperature}°C\n" +
                            "Humidity: ${currentWeather.value?.humidity}%\n" +
                            "Wind Speed: ${currentWeather.value?.windSpeed} km/h\n\n" +
                            "Daily Weather:\n" +
                            "Max Temperature: ${dailyWeather.value?.maxTemperature?.joinToString()}°C\n" +
                            "Min Temperature: ${dailyWeather.value?.minTemperature?.joinToString()}°C\n" +
                            "Sunrise Times: ${dailyWeather.value?.sunriseTimes?.joinToString()}\n" +
                            "Sunset Times: ${dailyWeather.value?.sunsetTimes?.joinToString()}\n\n" +
                            "Hourly Weather:\n" +
                            "Temperatures: ${hourlyWeather.value?.temperatures?.joinToString()}°C\n" +
                            "Weather Codes: ${hourlyWeather.value?.weatherCodes?.joinToString()}"
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
}
