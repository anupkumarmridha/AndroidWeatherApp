package com.example.weatherapp.domain.repository

import com.example.weatherapp.domain.model.Weather
import com.example.weatherapp.utils.Response
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeatherData(latitude: Float, longitude: Float): Flow<Response<Weather>>
}
