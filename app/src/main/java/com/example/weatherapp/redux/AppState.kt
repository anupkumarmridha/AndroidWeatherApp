package com.example.weatherapp.redux

import com.example.weatherapp.domain.model.Daily
import com.example.weatherapp.domain.model.Weather

data class AppState(
    val weather: Weather? = null,
    val error: String? = null,
    val isLoading: Boolean = false,
    val dailyWeatherInfo: Daily.WeatherInfo? = null,
    val searchLocation: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)
