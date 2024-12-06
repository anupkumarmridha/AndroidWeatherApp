package com.example.weatherapp.data.models


import com.google.gson.annotations.SerializedName

data class CurrentUnits(
    @SerializedName("interval")
    val interval: String,
    @SerializedName("is_day")
    val isDay: String,
    @SerializedName("rain")
    val rain: String,
    @SerializedName("relative_humidity_2m")
    val relativeHumidity2m: String,
    @SerializedName("temperature_2m")
    val temperature2m: String,
    @SerializedName("time")
    val time: String,
    @SerializedName("weather_code")
    val weatherCode: String,
    @SerializedName("wind_direction_10m")
    val windDirection10m: String,
    @SerializedName("wind_speed_10m")
    val windSpeed10m: String
)