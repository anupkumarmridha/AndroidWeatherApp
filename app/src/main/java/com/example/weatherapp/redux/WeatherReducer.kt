package com.example.weatherapp.redux

import org.reduxkotlin.Reducer
val weatherReducer: Reducer<WeatherState> = { state, action ->
    when (action) {
        is WeatherAction.Loading -> state.copy(isLoading = true)
        is WeatherAction.Success -> state.copy(isLoading = false, weather = action.weather, error = null)
        is WeatherAction.Error -> state.copy(isLoading = false, error = action.message)
        is WeatherAction.SetDailyWeatherInfo -> state.copy(dailyWeatherInfo = action.dailyWeatherInfo)
        else -> state
    }
}
