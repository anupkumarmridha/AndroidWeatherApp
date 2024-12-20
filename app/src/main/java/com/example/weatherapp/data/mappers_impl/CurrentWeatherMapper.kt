package com.example.weatherapp.data.mappers_impl

import com.example.weatherapp.data.mappers.ApiMapper
import com.example.weatherapp.data.remote.models.ApiCurrentWeather
import com.example.weatherapp.domain.model.CurrentWeather
import com.example.weatherapp.utils.Util
import com.example.weatherapp.utils.WeatherInfoItem

class CurrentWeatherMapper : ApiMapper<CurrentWeather, ApiCurrentWeather> {
    override fun mapToDomain(apiEntity: ApiCurrentWeather): CurrentWeather {
        return CurrentWeather(
            temperature = apiEntity.temperature2m,
            time = parseTime(apiEntity.time),
            weatherStatus = parseWeatherStatus(apiEntity.weatherCode),
            windDirection = parseWindDirection(apiEntity.windDirection10m),
            windSpeed = apiEntity.windSpeed10m,
            isDay = apiEntity.isDay == 1
        )
    }

    private fun parseTime(time: Long): String {
        return Util.formatUnixDate("MMM,d", time)
    }

    private fun parseWeatherStatus(code: Int): WeatherInfoItem {
        return Util.getWeatherInfo(code)
    }

    private fun parseWindDirection(windDirection: Double): String {
        return Util.getWindDirection(windDirection)
    }
}