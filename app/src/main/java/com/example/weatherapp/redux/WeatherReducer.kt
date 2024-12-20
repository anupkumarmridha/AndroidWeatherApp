package com.example.weatherapp.redux


val appReducer: (AppState, Action) -> AppState = { state, action ->
    when (action) {
        is Action.FetchWeather -> state.copy(isLoading = true, error = null)
        is Action.WeatherFetchSuccess -> state.copy(
            isLoading = false,
            weather = action.weather,
            error = null
        )
        is Action.WeatherFetchError -> state.copy(
            isLoading = false,
            error = action.message
        )
        is Action.Search -> state.copy(searchLocation = action.query)
        is Action.SetDailyWeatherInfo -> state.copy(dailyWeatherInfo = action.info)
        Action.Loading -> state.copy(isLoading = true)
        is Action.SetLocation -> state.copy(latitude = action.latitude, longitude = action.longitude)
    }
}

