package com.example.weatherapp.domain.repository

import com.example.weatherapp.domain.model.Weather
import com.example.weatherapp.utils.Response
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeatherData(): Flow<Response<Weather>>
}