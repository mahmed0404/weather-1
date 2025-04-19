package com.example.weather.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import com.example.weather.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.weather.data.WeatherModel
import com.example.weather.ui.theme.BlueCardBg
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

@Composable
fun MainCard(currentDay: MutableState<WeatherModel>) {
    Column(
        modifier = Modifier.padding(5.dp),
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = BlueCardBg
            ),
            elevation = CardDefaults.cardElevation(0.dp),
            shape = RoundedCornerShape(10.dp),

            ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = currentDay.value.time,
                        modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                        style = TextStyle(fontSize = 15.sp),
                        color = Color.White
                    )
                    AsyncImage(
                        model = "https:${currentDay.value.icon}",
                        contentDescription = "im2",
                        modifier = Modifier
                            .size(35.dp)
                            .padding(top = 3.dp, end = 8.dp)

                    )
                }
                Text(
                    text = currentDay.value.city,
                    style = TextStyle(fontSize = 24.sp),
                    color = Color.White
                )
                Text(
                    text = if (currentDay.value.currentTemp.isNotEmpty())
                        currentDay.value.currentTemp.toFloat().toInt().toString() + "°C"
                    else "${currentDay.value.maxTemp.toFloat().toInt()}°/" + currentDay.value.minTemp.toFloat().toInt().toString() + "°"
                        ,
                    style = TextStyle(fontSize = 65.sp),
                    color = Color.White
                )
                Text(
                    text = currentDay.value.condition,
                    style = TextStyle(fontSize = 16.sp),
                    color = Color.White
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                        }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = "im3",
                            modifier = Modifier.size(50.dp),

                            )
                    }
                    Text(
                        text = currentDay.value.maxTemp.toFloat().toInt().toString() +
                                "°/" +
                                currentDay.value.minTemp.toFloat().toInt().toString() +
                                "°",
                        style = TextStyle(fontSize = 16.sp),
                        color = Color.White
                    )
                    IconButton(
                        onClick = {
                        }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_sync),
                            contentDescription = "im4",
                            modifier = Modifier.size(50.dp),
                        )
                    }
                }
            }

        }

    }
}

@Composable
fun TabLayout(daysList: MutableState<List<WeatherModel>>, currentDay: MutableState<WeatherModel>) {
    val tabList = listOf("HOURS", "DAYS")
    val pagerState = rememberPagerState { tabList.size }
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()
    val tabPosition = pagerState.currentPage
    Column(
        modifier = Modifier
            .padding(
                5.dp
            )
            .clip(RoundedCornerShape(9.dp))
    ) {
        TabRow(
            selectedTabIndex = tabIndex,
            indicator = { pos ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(currentTabPosition = pos[tabPosition]),
                    color = Color.White
                )
            },
            containerColor = BlueCardBg,
            contentColor = Color.White
        ) {
            tabList.forEachIndexed { index, text ->
                Tab(
                    selected = false,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(text = text)
                    }
                )
            }
        }
        HorizontalPager(
            beyondViewportPageCount = tabList.size,
            state = pagerState,
            modifier = Modifier.weight(1.0f)
        ) { index ->
            val list = when(index){
                0 -> getWeatherByHours(currentDay.value.hours)
                1 -> daysList.value
                else -> daysList.value
            }
            MainList(list, currentDay)
        }
    }

}

private fun getWeatherByHours(hours: String): List<WeatherModel>{
    if (hours.isEmpty()) return listOf()
    val hoursArray = JSONArray(hours)
    val list = ArrayList<WeatherModel>()
    for (i in 0 until hoursArray.length()){
        val item = hoursArray[i] as JSONObject
        list.add(
            WeatherModel(
                "",
                item.getString("time"),
                item.getString("temp_c") + "°C",
                item.getJSONObject("condition").getString("text"),
                item.getJSONObject("condition").getString("icon"),
                "",
                "",
                ""
            )
        )

    }
    return list
}

