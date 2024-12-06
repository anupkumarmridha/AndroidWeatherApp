package com.example.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.domain.model.CurrentWeatherModel
import com.example.weatherapp.usecase.GetCurrentWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase
) : ViewModel() {

    private val _currentWeather = MutableLiveData<CurrentWeatherModel>()
    val currentWeather: LiveData<CurrentWeatherModel> get() = _currentWeather

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                Log.d("WeatherViewModel", "Attempting to fetch weather data for lat: $latitude, lon: $longitude")
                val weather = getCurrentWeatherUseCase.invoke(latitude, longitude)
                _currentWeather.value = weather
                Log.d("WeatherViewModel", "Weather Data: $weather")
                println("Weather Data: $weather")
            } catch (e: Exception) {

                _error.value = "Failed to fetch weather data: ${e.message}"
                Log.e("WeatherViewModel", "Error: ${e.message}")
                println("Error: ${e.message}")
            }
        }
    }
}