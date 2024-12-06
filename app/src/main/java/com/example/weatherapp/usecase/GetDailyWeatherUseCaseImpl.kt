package com.example.weatherapp.usecase

import android.util.Log
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.domain.model.DailyWeatherModel
import javax.inject.Inject

class GetDailyWeatherUseCaseImpl @Inject constructor(
    private val repository: WeatherRepository
) : GetDailyWeatherUseCase {
    override suspend fun invoke(latitude: Double, longitude: Double): DailyWeatherModel {
        Log.d("GetDailyWeatherUseCaseImpl", "Invoking use case for daily weather with latitude: $latitude, longitude: $longitude")
        val apiWeather = repository.getCurrentWeather(latitude, longitude)
        val dailyWeather = DailyWeatherModel(
            maxTemperature = apiWeather.daily.temperature2mMax,
            minTemperature = apiWeather.daily.temperature2mMin,
            sunriseTimes = apiWeather.daily.sunrise,
            sunsetTimes = apiWeather.daily.sunset
        )
        Log.d("GetDailyWeatherUseCaseImpl", "Daily weather data: $dailyWeather")
        return dailyWeather
    }
}