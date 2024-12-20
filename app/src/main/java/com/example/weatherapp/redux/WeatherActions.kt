package com.example.weatherapp.redux

import com.example.weatherapp.domain.model.Daily
import com.example.weatherapp.domain.model.Weather

sealed class Action {
    data object FetchWeather : Action()
    data class WeatherFetchSuccess(val weather: Weather?) : Action()
    data class WeatherFetchError(val message: String) : Action()
    data class Search(val query: String) : Action()
    data class SetDailyWeatherInfo(val info: Daily.WeatherInfo?) : Action()
    data object Loading : Action()
    data class SetLocation(val latitude: Double, val longitude: Double) : Action()
}

