package com.example.weatherapp.data.models


import com.google.gson.annotations.SerializedName

data class Current(
    @SerializedName("interval")
    val interval: Int,
    @SerializedName("is_day")
    val isDay: Int,
    @SerializedName("rain")
    val rain: Double,
    @SerializedName("relative_humidity_2m")
    val relativeHumidity2m: Int,
    @SerializedName("temperature_2m")
    val temperature2m: Double,
    @SerializedName("time")
    val time: String,
    @SerializedName("weather_code")
    val weatherCode: Int,
    @SerializedName("wind_direction_10m")
    val windDirection10m: Int,
    @SerializedName("wind_speed_10m")
    val windSpeed10m: Double
)