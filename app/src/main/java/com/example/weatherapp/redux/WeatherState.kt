package com.example.weatherapp.redux

import com.example.weatherapp.domain.model.Daily
import com.example.weatherapp.domain.model.Weather

data class WeatherState(
    val weather: Weather? = null,
    val error: String? = null,
    val isLoading: Boolean = false,
    val dailyWeatherInfo: Daily.WeatherInfo? = null
)
