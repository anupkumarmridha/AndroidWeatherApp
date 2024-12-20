package com.example.weatherapp.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.domain.repository.WeatherRepository
import com.example.weatherapp.redux.Action
import com.example.weatherapp.redux.AppState
import com.example.weatherapp.ui.components.LocationPermissionHandler
import com.example.weatherapp.utils.Response
import com.example.weatherapp.utils.Util
import com.google.android.gms.location.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.reduxkotlin.Store
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    @ApplicationContext private val context: Context,
    private val store: Store<AppState>
) : ViewModel() {

    private val _state = MutableStateFlow(AppState()) // Default state
    val state: StateFlow<AppState> = _state

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10 * 1000).build()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->
                Log.d("WeatherViewModel", "Location received: (${location.latitude}, ${location.longitude})")
                fetchWeatherData(location.latitude, location.longitude)
                fusedLocationClient.removeLocationUpdates(this)
            } ?: run {
                Log.e("WeatherViewModel", "Failed to get location")
                store.dispatch(Action.WeatherFetchError("Unable to get location"))
            }
        }
    }

    init {
        Log.d("WeatherViewModel", "ViewModel initialized")
        store.subscribe { _state.value = store.state }
    }

    @SuppressLint("MissingPermission")
    fun fetchLocationDetails() {
        val permissionHandler = LocationPermissionHandler(context)

        if (!permissionHandler.hasLocationPermissions()) {
            Log.e("WeatherViewModel", "Permissions not granted")
            store.dispatch(Action.WeatherFetchError("Location permissions are not granted"))
            return
        }

        Log.d("WeatherViewModel", "Requesting location updates")
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,  // Ensure this is initialized
            Looper.getMainLooper()
        )
    }

    private fun fetchWeatherData(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                Log.d("WeatherViewModel", "Fetching weather data for ($latitude, $longitude)")
                val cityName = getLocationName(context, latitude, longitude) // Get location name
                store.dispatch(Action.Search(cityName)) // Store the city name in the state

                repository.getWeatherData(latitude.toFloat(), longitude.toFloat()).collect { response ->
                    when (response) {
                        is Response.Loading -> store.dispatch(Action.Loading)
                        is Response.Success -> {
                            store.dispatch(Action.WeatherFetchSuccess(response.data))
                            val todayWeather = response.data?.daily?.weatherInfo?.find {
                                Util.isTodayDate(it.time)
                            }
                            store.dispatch(Action.SetDailyWeatherInfo(todayWeather))
                        }
                        is Response.Error -> store.dispatch(Action.WeatherFetchError(response.message ?: "Unknown error"))
                    }
                }
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error fetching weather data: ${e.message}")
                store.dispatch(Action.WeatherFetchError(e.message ?: "Unknown error"))
            }
        }
    }
    fun searchLocation(query: String) {
        store.dispatch(Action.Search(query))
        val (lat, lon) = state.value.run { latitude to longitude }
        if (lat != null && lon != null) {
            fetchWeatherData(lat, lon)
        }
    }

    suspend fun getLocationName(context: Context, latitude: Double, longitude: Double): String {
        return withContext(Dispatchers.IO) { // Perform Geocoder operations on a background thread
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(latitude, longitude, 1) // Fetch the address
                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    address.locality ?: "Unknown Location" // Return the locality (e.g., city name)
                } else {
                    "Unknown Location"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                "Unknown Location"
            }
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun fetchLocationFromCity(cityName: String) {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses = withContext(Dispatchers.IO) {
                geocoder.getFromLocationName(cityName, 1)
            } ?: emptyList()

            if (addresses.isNotEmpty()) {
                val address = addresses[0]
                val latitude = address.latitude
                val longitude = address.longitude
                store.dispatch(Action.SetLocation(latitude, longitude))
                fetchWeatherData(latitude, longitude)
            } else {
                store.dispatch(Action.WeatherFetchError("City not found"))
            }
        } catch (e: Exception) {
            store.dispatch(Action.WeatherFetchError("Error fetching city details: ${e.message}"))
        }
    }
}
