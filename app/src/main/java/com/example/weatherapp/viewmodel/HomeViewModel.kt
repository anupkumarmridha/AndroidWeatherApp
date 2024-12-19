package com.example.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.domain.repository.WeatherRepository
import com.example.weatherapp.redux.WeatherAction
import com.example.weatherapp.utils.Response
import com.example.weatherapp.utils.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    init {
        fetchWeatherData()
    }

    private fun fetchWeatherData() {
        viewModelScope.launch {
            try {
                // Dispatch loading action initially
                WeatherStore.dispatch(WeatherAction.Loading)
                repository.getWeatherData().collect { response ->
                    when (response) {
                        is Response.Loading -> {
                            // Dispatch loading action when Response.Loading is emitted
                            WeatherStore.dispatch(WeatherAction.Loading)
                        }
                        is Response.Success -> {
                            // Dispatch success action with data
                            WeatherStore.dispatch(WeatherAction.Success(response.data))
                            val todayDailyWeatherInfo = response.data?.daily?.weatherInfo?.find {
                                Util.isTodayDate(it.time)
                            }
                            WeatherStore.dispatch(WeatherAction.SetDailyWeatherInfo(todayDailyWeatherInfo))
                        }
                        is Response.Error -> {
                            // Dispatch error action with message
                            WeatherStore.dispatch(WeatherAction.Error(response.message))
                        }
                    }
                }
            } catch (e: Exception) {
                // Dispatch error action for exceptions
                WeatherStore.dispatch(WeatherAction.Error(e.message ?: "Unknown error"))
            }
        }
    }
}
