package com.example.weatherapp.usecase

import android.util.Log
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.domain.model.HourlyWeatherModel
import javax.inject.Inject

class GetHourlyWeatherUseCaseImpl @Inject constructor(
    private val repository: WeatherRepository
) : GetHourlyWeatherUseCase {
    override suspend fun invoke(latitude: Double, longitude: Double): HourlyWeatherModel {
        Log.d("GetHourlyWeatherUseCaseImpl", "Invoking use case for hourly weather with latitude: $latitude, longitude: $longitude")
        val apiWeather = repository.getCurrentWeather(latitude, longitude)
        val hourlyWeather = HourlyWeatherModel(
            temperatures = apiWeather.hourly.temperature2m,
            weatherCodes = apiWeather.hourly.weatherCode
        )
        Log.d("GetHourlyWeatherUseCaseImpl", "Hourly weather data: $hourlyWeather")
        return hourlyWeather
    }
}