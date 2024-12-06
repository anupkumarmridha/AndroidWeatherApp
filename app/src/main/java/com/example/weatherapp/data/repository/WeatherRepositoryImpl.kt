package com.example.weatherapp.data.repository

import android.util.Log
import com.example.weatherapp.data.api.WeatherApi
import com.example.weatherapp.data.models.ApiWeather
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi
) : WeatherRepository {
    override suspend fun getCurrentWeather(latitude: Double, longitude: Double): ApiWeather {
        return try {
            Log.d("WeatherRepositoryImpl", "Fetching weather data for latitude: $latitude, longitude: $longitude")
            val response = weatherApi.getWeatherData(
                latitude = latitude,
                longitude = longitude,
                current = "temperature_2m,relative_humidity_2m,is_day,rain,weather_code,wind_speed_10m,wind_direction_10m",
                hourly = "temperature_2m,weather_code",
                daily = "weather_code,temperature_2m_max,temperature_2m_min,sunrise,sunset,uv_index_max,wind_speed_10m_max,wind_direction_10m_dominant"
            )
            Log.d("WeatherRepositoryImpl", "Weather data fetched successfully: $response")
            response
        } catch (e: Exception) {
            Log.e("WeatherRepositoryImpl", "Failed to fetch weather data: ${e.message}", e)
            throw Exception("Failed to fetch weather data: ${e.message}", e)
        }
    }
}

