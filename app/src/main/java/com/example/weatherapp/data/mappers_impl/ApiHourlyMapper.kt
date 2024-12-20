package com.example.weatherapp.data.mappers_impl

import com.example.weatherapp.data.mappers.ApiMapper
import com.example.weatherapp.data.remote.models.ApiHourlyWeather
import com.example.weatherapp.domain.model.Hourly
import com.example.weatherapp.utils.Util
import com.example.weatherapp.utils.WeatherInfoItem

class ApiHourlyMapper : ApiMapper<Hourly, ApiHourlyWeather> {
    override fun mapToDomain(apiEntity: ApiHourlyWeather): Hourly {
        return Hourly(
            temperature = apiEntity.temperature2m,
            time = parseTime(apiEntity.time),
            weatherStatus = parseWeatherStatus(apiEntity.weatherCode)
        )
    }

    private fun parseTime(time: List<Long>): List<String> {
        return time.map {
            Util.formatUnixDate("HH:mm", it)
        }
    }

    private fun parseWeatherStatus(code: List<Int>): List<WeatherInfoItem> {
        return code.map {
            Util.getWeatherInfo(it)
        }
    }
}

