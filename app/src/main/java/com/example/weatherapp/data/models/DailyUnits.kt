package com.example.weatherapp.data.models


import com.google.gson.annotations.SerializedName

data class DailyUnits(
    @SerializedName("sunrise")
    val sunrise: String,
    @SerializedName("sunset")
    val sunset: String,
    @SerializedName("temperature_2m_max")
    val temperature2mMax: String,
    @SerializedName("temperature_2m_min")
    val temperature2mMin: String,
    @SerializedName("time")
    val time: String,
    @SerializedName("uv_index_max")
    val uvIndexMax: String,
    @SerializedName("weather_code")
    val weatherCode: String,
    @SerializedName("wind_direction_10m_dominant")
    val windDirection10mDominant: String,
    @SerializedName("wind_speed_10m_max")
    val windSpeed10mMax: String
)