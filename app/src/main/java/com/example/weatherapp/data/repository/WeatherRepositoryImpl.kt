package com.example.weatherapp.data.repository

import com.example.weatherapp.data.mappers_impl.ApiWeatherMapper
import com.example.weatherapp.data.remote.WeatherApi
import com.example.weatherapp.domain.model.Weather
import com.example.weatherapp.domain.repository.WeatherRepository
import com.example.weatherapp.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi,
    private val apiWeatherMapper: ApiWeatherMapper
) : WeatherRepository {
    override fun getWeatherData(latitude: Float, longitude: Float): Flow<Response<Weather>> = flow {
        emit(Response.Loading())
        val apiWeather = weatherApi.getWeatherData(latitude, longitude)
        val weather = apiWeatherMapper.mapToDomain(apiWeather)
        emit(Response.Success(weather))
    }.catch { e ->
        e.printStackTrace()
        emit(Response.Error(e.message.orEmpty()))
    }
}


