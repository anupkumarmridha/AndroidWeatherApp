package com.example.weatherapp.usecase

import android.util.Log
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.domain.model.CurrentWeatherModel
import javax.inject.Inject

class GetCurrentWeatherUseCaseImpl @Inject constructor(
    private val repository: WeatherRepository
) : GetCurrentWeatherUseCase {
    override suspend fun invoke(latitude: Double, longitude: Double): CurrentWeatherModel {
        Log.d("GetCurrentWeatherUseCaseImpl", "Invoking use case for latitude: $latitude, longitude: $longitude")
        val apiWeather = repository.getCurrentWeather(latitude, longitude)
        val currentWeather = CurrentWeatherModel(
            temperature = apiWeather.current.temperature2m,
            humidity = apiWeather.current.relativeHumidity2m,
            isDay = apiWeather.current.isDay,
            windSpeed = apiWeather.current.windSpeed10m
        )
        Log.d("GetCurrentWeatherUseCaseImpl", "Current weather data: $currentWeather")
        return currentWeather
    }
}