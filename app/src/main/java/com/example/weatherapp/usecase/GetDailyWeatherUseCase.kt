package com.example.weatherapp.usecase
import com.example.weatherapp.domain.model.DailyWeatherModel

interface GetDailyWeatherUseCase {
    suspend fun invoke(latitude: Double, longitude: Double): DailyWeatherModel
}