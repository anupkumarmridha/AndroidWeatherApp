package com.example.weatherapp.di

import android.util.Log
import com.example.weatherapp.data.api.ApiServiceBuilder
import com.example.weatherapp.data.api.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideWeatherApi(): WeatherApi {
        Log.d("NetworkModule", "Providing WeatherApi instance")
        return ApiServiceBuilder.create()
    }
}
