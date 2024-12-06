package com.example.weatherapp.domain.model

data class DailyWeatherModel(
    val maxTemperature: List<Double>,
    val minTemperature: List<Double>,
    val sunriseTimes: List<String>,
    val sunsetTimes: List<String>
)