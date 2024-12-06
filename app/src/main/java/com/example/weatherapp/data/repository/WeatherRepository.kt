package com.example.weatherapp.data.repository

import com.example.weatherapp.data.models.ApiWeather

interface WeatherRepository {
    suspend fun getCurrentWeather(latitude: Double, longitude: Double): ApiWeather
}