package com.example.weatherapp.di

import android.util.Log
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.usecase.GetCurrentWeatherUseCase
import com.example.weatherapp.usecase.GetCurrentWeatherUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    @ViewModelScoped
    fun provideGetCurrentWeatherUseCase(repository: WeatherRepository): GetCurrentWeatherUseCase {
        Log.d("ViewModelModule", "Providing GetCurrentWeatherUseCase instance")
        return GetCurrentWeatherUseCaseImpl(repository)
    }
}