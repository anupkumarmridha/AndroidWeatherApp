package com.example.weatherapp.redux

import com.example.weatherapp.domain.model.Daily
import com.example.weatherapp.domain.model.Weather

sealed class WeatherAction {
    object Loading : WeatherAction()
    data class Success(val weather: Weather?) : WeatherAction()
    data class Error(val message: String?) : WeatherAction()
    data class SetDailyWeatherInfo(val dailyWeatherInfo: Daily.WeatherInfo?) : WeatherAction()
}
