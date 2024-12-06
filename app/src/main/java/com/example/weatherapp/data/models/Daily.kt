package com.example.weatherapp.data.models


import com.google.gson.annotations.SerializedName

data class Daily(
    @SerializedName("sunrise")
    val sunrise: List<String>,
    @SerializedName("sunset")
    val sunset: List<String>,
    @SerializedName("temperature_2m_max")
    val temperature2mMax: List<Double>,
    @SerializedName("temperature_2m_min")
    val temperature2mMin: List<Double>,
    @SerializedName("time")
    val time: List<String>,
    @SerializedName("uv_index_max")
    val uvIndexMax: List<Double>,
    @SerializedName("weather_code")
    val weatherCode: List<Int>,
    @SerializedName("wind_direction_10m_dominant")
    val windDirection10mDominant: List<Int>,
    @SerializedName("wind_speed_10m_max")
    val windSpeed10mMax: List<Double>
)