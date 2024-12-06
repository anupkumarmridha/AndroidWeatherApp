package com.example.weatherapp.domain.model

data class HourlyWeatherModel(
    val temperatures: List<Double>,
    val weatherCodes: List<Int>
)