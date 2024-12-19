package com.example.weatherapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.R
import com.example.weatherapp.domain.model.CurrentWeather
import com.example.weatherapp.domain.model.Daily
import com.example.weatherapp.domain.model.Hourly
import com.example.weatherapp.utils.Util
import com.example.weatherapp.utils.WeatherInfoItem
import com.example.weatherapp.viewmodel.HomeViewModel
import java.util.Date

const val degreeTxt = "°"

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
//    val homeState = homeViewModel.homeState
    val homeState by WeatherStore.stateFlow.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when (homeState.isLoading) {
            true -> {
                // Show a loading indicator
                CircularProgressIndicator()
            }

            else -> {
                // Show the weather data
                homeState.weather?.let {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CurrentWeatherItem(
                            currentWeather = it.currentWeather,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Chennai", // Location text below the weather item
                            style = MaterialTheme.typography.headlineMedium,
                        )
                    }

                    HourlyWeatherItem(
                        hourly = it.hourly,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }

                homeState.dailyWeatherInfo?.let {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SunSetWeatherItem(weatherInfo = it)
                        UvIndexWeatherItem(weatherInfo = it)
                    }
                }


            }

        }

    }
}


@Composable
fun CurrentWeatherItem(
    currentWeather: CurrentWeather,
    modifier: Modifier = Modifier
) {
    // Display the current weather

    Column(
        modifier=modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
        ){
        Icon(
            painter = painterResource(id = currentWeather.weatherStatus.icon),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier=Modifier.height(8.dp))
        Text(
            text = "${currentWeather.temperature}$degreeTxt",
            style = MaterialTheme.typography.displayMedium,
        )
        Spacer(modifier=Modifier.height(4.dp))
        Text(
            text = currentWeather.weatherStatus.info,
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier=Modifier.height(4.dp))
        Text(
            text = "Wind: ${currentWeather.windSpeed} km/h",
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier=Modifier.height(4.dp))
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
    Card (modifier=modifier.fillMaxWidth()){
        Row(
            modifier=Modifier
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
        LazyRow(modifier=Modifier.padding(16.dp)) {
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
        modifier=modifier.padding(8.dp),
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
