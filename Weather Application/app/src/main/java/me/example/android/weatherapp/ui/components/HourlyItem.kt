package me.example.android.weatherapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import me.example.android.weatherapp.BuildConfig.OPEN_WEATHER_ICON_2X
import me.example.android.weatherapp.BuildConfig.OPEN_WEATHER_ICON_URL
import me.example.android.weatherapp.extensions.toHumidity
import me.example.android.weatherapp.extensions.toTemperature
import me.example.android.weatherapp.extensions.toTime
import me.example.android.weatherapp.ui.theme.Blue
import me.example.android.weatherapp.ui.theme.dimensions
import weatherapp.domain.models.Hourly

@Composable
fun HourlyItem(
    hourly: Hourly,
    timeUnit: String,
    temperatureUnit: String
) {

    Box(
        modifier = Modifier
            .size(105.dp)
            .background(Blue)
            .padding(vertical = 16.dp, horizontal = 4.dp)
            .clip(CircleShape)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = hourly.dt!!.toTime(timeUnit),
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "${hourly.weather?.get(0)?.description}", style = TextStyle(
                    color = Color.White,
                    fontSize = 9.sp
                )
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = hourly.humidity!!.toHumidity(),
                style = TextStyle(
                    color = Color.White,
                    fontSize = 9.sp
                )
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = hourly.temp!!.toTemperature(temperatureUnit),
                style = TextStyle(
                    color = Color.White,
                    fontSize = 13.sp
                )
            )

        }
    }
}