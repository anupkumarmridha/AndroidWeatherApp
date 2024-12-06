package com.example.weatherapp.usecase

import com.example.weatherapp.domain.model.HourlyWeatherModel

interface GetHourlyWeatherUseCase {
    suspend fun invoke(latitude: Double, longitude: Double): HourlyWeatherModel
}