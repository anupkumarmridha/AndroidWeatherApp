package com.example.weatherapp.domain.model

data class CurrentWeatherModel(
    val temperature: Double,
    val humidity: Int,
    val isDay: Int,
    val windSpeed: Double
)