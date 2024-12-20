package com.example.weatherapp.ui.home

import WeatherSearchBar
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.R
import com.example.weatherapp.domain.model.CurrentWeather
import com.example.weatherapp.domain.model.Daily
import com.example.weatherapp.domain.model.Hourly
import com.example.weatherapp.ui.components.RequestLocationPermissions
import com.example.weatherapp.utils.Util
import com.example.weatherapp.utils.WeatherInfoItem
import com.example.weatherapp.viewmodel.WeatherViewModel
import java.util.Date

const val degreeTxt = "°"

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    weatherViewModel: WeatherViewModel = hiltViewModel() // Inject WeatherViewModel
) {
    // Convert the StateFlow into a State for Compose to observe
    val homeState by weatherViewModel.state.collectAsState()

    // Request location permissions
    RequestLocationPermissions(
        onPermissionsGranted = {
            Log.d("HomeScreen", "Permissions granted")
            weatherViewModel.fetchLocationDetails() // Fetch location details when permission is granted
        },
        onPermissionsDenied = {
            Log.e("HomeScreen", "Permissions denied") // Log error for denied permission
        }
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween // Distribute elements with space between
    ) {
        // Spacer for additional margin at the top
        Spacer(modifier = Modifier.height(16.dp))

        // Add the SearchBar
        WeatherSearchBar(
            onSearch = { query ->
                weatherViewModel.searchLocation(query) // Trigger search in ViewModel
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Display Weather Content
        homeState.weather?.let { weather ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Current Weather
                CurrentWeatherItem(currentWeather = weather.currentWeather)
                Spacer(modifier = Modifier.height(8.dp))

                // Location Text
                Text(
                    text = homeState.searchLocation ?: "Unknown Location",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )

                // Daily Weather Info (Sunset, UV Index)
                homeState.dailyWeatherInfo?.let { dailyWeather ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SunSetWeatherItem(weatherInfo = dailyWeather)
                        UvIndexWeatherItem(weatherInfo = dailyWeather)
                    }
                }
            }
        } ?: run {
            // Show Loading or Error if Weather Data is Not Available
            if (homeState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                Text(
                    text = "Unable to load weather data",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        // Hourly Weather
        homeState.weather?.hourly?.let { hourlyWeather ->
            HourlyWeatherItem(
                hourly = hourlyWeather,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp)) // Spacer for Bottom Margin
    }
}



@Composable
fun CurrentWeatherItem(
    currentWeather: CurrentWeather,
    modifier: Modifier = Modifier
) {
    // Display the current weather

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = currentWeather.weatherStatus.icon),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "${currentWeather.temperature}$degreeTxt",
            style = MaterialTheme.typography.displayMedium,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = currentWeather.weatherStatus.info,
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Wind: ${currentWeather.windSpeed} km/h",
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = currentWeather.time,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}


@Composable
fun HourlyWeatherItem(
    modifier: Modifier = Modifier,
    hourly: Hourly,
) {
    // Display the hourly weather
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Today",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = Util.formatNormalDate("MMMM,dd", Date().time),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        HorizontalDivider(
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        // Display the hourly weather data
        LazyRow(modifier = Modifier.padding(16.dp)) {
            items(hourly.weatherInfo) { infoItem ->
                HourlyWeatherInfoItem(infoItem = infoItem)
            }
        }
    }
}

@Composable
fun HourlyWeatherInfoItem(
    infoItem: Hourly.HourlyInfoItem,
    modifier: Modifier = Modifier
) {
    // Display the hourly weather info
    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "${infoItem.temperature} $degreeTxt",
            style = MaterialTheme.typography.bodySmall,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Icon(
            painter = painterResource(id = infoItem.weatherStatus.icon),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = infoItem.time,
            style = MaterialTheme.typography.bodySmall,
        )

    }
}


@Composable
fun SunSetWeatherItem(modifier: Modifier = Modifier, weatherInfo: Daily.WeatherInfo) {
    Card(modifier = Modifier.padding(horizontal = 8.dp)) {
        Column(
            Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Sunrise",
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = weatherInfo.sunrise,
                style = MaterialTheme.typography.displayMedium,
            )
            Text(
                text = "Sunset ${weatherInfo.sunset}",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
fun UvIndexWeatherItem(modifier: Modifier = Modifier, weatherInfo: Daily.WeatherInfo) {
    Card(modifier = Modifier.padding(horizontal = 8.dp)) {
        Column(
            Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "UV INDEX",
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = weatherInfo.uvIndex.toString(),
                style = MaterialTheme.typography.displayMedium,
            )
            Text(
                text = "Status ${weatherInfo.weatherStatus.info}",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
//    HomeScreen()


    // Dummy WeatherInfoItem
    val dummyWeatherInfoItem = WeatherInfoItem(
        info = "Clear Sky",
        icon = R.drawable.clear_sky // Replace with an actual drawable resource ID
    )

// Dummy CurrentWeather
    val dummyCurrentWeather = CurrentWeather(
        temperature = 25.0, // 25 degrees Celsius
        time = "12:00 PM",
        weatherStatus = dummyWeatherInfoItem,
        windDirection = "NE",
        windSpeed = 15.0, // 15 km/h
        isDay = true
    )
    CurrentWeatherItem(currentWeather = dummyCurrentWeather)
}


@Preview(showBackground = true)
@Composable
fun HourlyWeatherItemPreview() {
    // Dummy Hourly
    // Sample WeatherInfoItem
    val sampleWeatherInfoItemSunny = WeatherInfoItem(
        info = "Sunny",
        icon = R.drawable.clear_sky
    )
    val sampleWeatherInfoItemCloudy = WeatherInfoItem(
        info = "Cloudy",
        icon = R.drawable.cloudy
    )

// Sample Hourly Data
    val sampleHourly = Hourly(
        temperature = listOf(22.0, 24.0, 26.0, 28.0), // Temperatures in degrees Celsius
        time = listOf("08:00 AM", "10:00 AM", "12:00 PM", "02:00 PM"), // Corresponding times
        weatherStatus = listOf(
            sampleWeatherInfoItemSunny,
            sampleWeatherInfoItemSunny,
            sampleWeatherInfoItemCloudy,
            sampleWeatherInfoItemCloudy
        ) // Weather conditions
    )

// Access the weatherInfo for preview
    val sampleHourlyInfoList = sampleHourly

    HourlyWeatherItem(hourly = sampleHourlyInfoList)
}


@Preview(showBackground = true)
@Composable
fun HourlyWeatherInfoItemPreview() {
    // Sample WeatherInfoItem
    val sampleWeatherInfoItem = WeatherInfoItem(
        info = "Sunny",
        icon = R.drawable.clear_sky
    )
    // Sample HourlyInfoItem
    val sampleHourlyInfoItem = Hourly.HourlyInfoItem(
        temperature = 25.0,
        time = "12:00 PM",
        weatherStatus = sampleWeatherInfoItem
    )
    HourlyWeatherInfoItem(infoItem = sampleHourlyInfoItem)
}


//@Preview(showBackground = true)
//@Composable
//fun SunSetWeatherItemPreview() {
//    // Sample WeatherInfo

//    SunSetWeatherItem(weatherInfo = sampleWeatherInfo)
//}
