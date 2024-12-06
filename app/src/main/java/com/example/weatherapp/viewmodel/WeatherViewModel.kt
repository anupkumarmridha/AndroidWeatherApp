package com.example.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.domain.model.CurrentWeatherModel
import com.example.weatherapp.domain.model.DailyWeatherModel
import com.example.weatherapp.domain.model.HourlyWeatherModel
import com.example.weatherapp.usecase.GetCurrentWeatherUseCase
import com.example.weatherapp.usecase.GetDailyWeatherUseCase
import com.example.weatherapp.usecase.GetHourlyWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getDailyWeatherUseCase: GetDailyWeatherUseCase,
    private val getHourlyWeatherUseCase: GetHourlyWeatherUseCase
) : ViewModel() {

    private val _currentWeather = MutableLiveData<CurrentWeatherModel>()
    val currentWeather: LiveData<CurrentWeatherModel> get() = _currentWeather

    private val _dailyWeather = MutableLiveData<DailyWeatherModel>()
    val dailyWeather: LiveData<DailyWeatherModel> get() = _dailyWeather

    private val _hourlyWeather = MutableLiveData<HourlyWeatherModel>()
    val hourlyWeather: LiveData<HourlyWeatherModel> get() = _hourlyWeather

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                Log.d("WeatherViewModel", "Attempting to fetch weather data for lat: $latitude, lon: $longitude")
                val current = getCurrentWeatherUseCase.invoke(latitude, longitude)
                _currentWeather.value = current
                Log.d("WeatherViewModel", "Current Weather Data: $current")

                val daily = getDailyWeatherUseCase.invoke(latitude, longitude)
                _dailyWeather.value = daily
                Log.d("WeatherViewModel", "Daily Weather Data: $daily")

                val hourly = getHourlyWeatherUseCase.invoke(latitude, longitude)
                _hourlyWeather.value = hourly
                Log.d("WeatherViewModel", "Hourly Weather Data: $hourly")
            } catch (e: Exception) {
                _error.value = "Failed to fetch weather data: ${e.message}"
                Log.e("WeatherViewModel", "Error: ${e.message}", e)
            }
        }
    }
}
