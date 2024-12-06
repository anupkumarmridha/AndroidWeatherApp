package com.example.weatherapp.usecase

import com.example.weatherapp.domain.model.CurrentWeatherModel

interface GetCurrentWeatherUseCase {
    suspend fun invoke(latitude: Double, longitude: Double): CurrentWeatherModel
}