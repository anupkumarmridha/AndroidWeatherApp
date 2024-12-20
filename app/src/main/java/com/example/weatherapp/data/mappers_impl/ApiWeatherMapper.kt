package com.example.weatherapp.data.mappers_impl

import com.example.weatherapp.data.mappers.ApiMapper
import com.example.weatherapp.data.remote.models.ApiCurrentWeather
import com.example.weatherapp.data.remote.models.ApiDailyWeather
import com.example.weatherapp.data.remote.models.ApiHourlyWeather
import com.example.weatherapp.data.remote.models.ApiWeather
import com.example.weatherapp.di.ApiCurrentWeatherMapperAnnotation
import com.example.weatherapp.di.ApiDailyMapperAnnotation
import com.example.weatherapp.di.ApiHourlyWeatherMapperAnnotation
import com.example.weatherapp.domain.model.CurrentWeather
import com.example.weatherapp.domain.model.Daily
import com.example.weatherapp.domain.model.Hourly
import com.example.weatherapp.domain.model.Weather
import javax.inject.Inject

class ApiWeatherMapper @Inject constructor(
    @ApiDailyMapperAnnotation private val apiDailyMapper: ApiMapper<Daily, ApiDailyWeather>,
    @ApiCurrentWeatherMapperAnnotation private val apiCurrentWeatherMapper: ApiMapper<CurrentWeather, ApiCurrentWeather>,
    @ApiHourlyWeatherMapperAnnotation private val apiHourlyMapper: ApiMapper<Hourly, ApiHourlyWeather>,
) : ApiMapper<Weather, ApiWeather> {
    override fun mapToDomain(apiEntity: ApiWeather): Weather {
        return Weather(
            currentWeather = apiCurrentWeatherMapper.mapToDomain(apiEntity.current),
            daily = apiDailyMapper.mapToDomain(apiEntity.daily),
            hourly = apiHourlyMapper.mapToDomain(apiEntity.hourly)
        )
    }
}