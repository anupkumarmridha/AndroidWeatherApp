package com.example.weatherapp.di

import com.example.weatherapp.data.mappers.ApiMapper
import com.example.weatherapp.data.mappers_impl.ApiDailyMapper
import com.example.weatherapp.data.mappers_impl.ApiHourlyMapper
import com.example.weatherapp.data.mappers_impl.ApiWeatherMapper
import com.example.weatherapp.data.mappers_impl.CurrentWeatherMapper
import com.example.weatherapp.data.remote.models.ApiCurrentWeather
import com.example.weatherapp.data.remote.models.ApiDailyWeather
import com.example.weatherapp.data.remote.models.ApiHourlyWeather
import com.example.weatherapp.data.remote.models.ApiWeather
import com.example.weatherapp.domain.model.CurrentWeather
import com.example.weatherapp.domain.model.Daily
import com.example.weatherapp.domain.model.Hourly
import com.example.weatherapp.domain.model.Weather
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {
    @ApiDailyMapperAnnotation
    @Provides
    fun provideDailyApiMapper(): ApiMapper<Daily, ApiDailyWeather> {
        return ApiDailyMapper()
    }

    @ApiCurrentWeatherMapperAnnotation
    @Provides
    fun provideCurrentWeatherMapper(): ApiMapper<CurrentWeather, ApiCurrentWeather> {
        return CurrentWeatherMapper()
    }

    @ApiHourlyWeatherMapperAnnotation
    @Provides
    fun provideHourlyMapper(): ApiMapper<Hourly, ApiHourlyWeather> {
        return ApiHourlyMapper()
    }

    @ApiWeatherMapperAnnotation
    @Provides
    fun provideApiWeatherMapper(
        apiDailyMapper: ApiMapper<Daily, ApiDailyWeather>,
        apiCurrentWeatherMapper: ApiMapper<CurrentWeather, ApiCurrentWeather>,
        apiHourlyMapper: ApiMapper<Hourly, ApiHourlyWeather>,
    ): ApiMapper<Weather, ApiWeather> {
        return ApiWeatherMapper(
            apiDailyMapper,
            apiCurrentWeatherMapper,
            apiHourlyMapper,
        )
    }


}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApiDailyMapperAnnotation


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApiWeatherMapperAnnotation


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApiCurrentWeatherMapperAnnotation


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApiHourlyWeatherMapperAnnotation
