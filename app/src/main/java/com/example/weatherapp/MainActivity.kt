package com.example.weatherapp

import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherapp.ui.daily.DailyScreen
import com.example.weatherapp.ui.home.HomeScreen
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.utils.Tabs

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                WeatherApp()
            }
        }
    }

    @Composable
    private fun WeatherApp() {
        var selectedTabIndex by rememberSaveable {
            mutableIntStateOf(0)
        }

        Scaffold (
            modifier = Modifier.fillMaxSize(),
            bottomBar={
                WeatherAppBottomNavigationBar(
                    tabs = Tabs.entries,
                    selectedIndex = selectedTabIndex
                ){
                    selectedTabIndex = it
                }
            }
        ){ innerPadding ->
            when(selectedTabIndex){
                0 -> {
                    HomeScreen(modifier = Modifier.padding(innerPadding))
                }
                1 -> {
                    DailyScreen(modifier = Modifier.padding(innerPadding))
                }

            }

        }
    }
    @Composable
    fun WeatherAppBottomNavigationBar(
        modifier: Modifier = Modifier,
        tabs: List<Tabs>,
        selectedIndex: Int,
        onSelectedChange: (Int) -> Unit
    ){
        NavigationBar(modifier = modifier) {
            tabs.forEachIndexed { index, tab ->
            NavigationBarItem(
                selected = index == selectedIndex,
                onClick = { onSelectedChange(index) },
                icon = {
                    Icon(imageVector = tab.icon, contentDescription = tab.title)
                },
                label = {
                    Text(text = tab.title)
                }
            )
            }
        }
    }

}




