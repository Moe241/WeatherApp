package me.example.android.weatherapp.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import me.example.android.weatherapp.extensions.toDay
import me.example.android.weatherapp.extensions.toHumidity
import me.example.android.weatherapp.extensions.toTemperature
import me.example.android.weatherapp.ui.theme.Blue
import weatherapp.domain.models.Daily
import weatherapp.domain.models.PreferenceUnits

@ExperimentalAnimationApi
@Composable
fun ForecastItem(
    daily: Daily,
    preferenceUnits: PreferenceUnits,
) {

    Box(
        modifier = Modifier
            .size(105.dp)
            .background(Blue)
            .padding(vertical = 16.dp, horizontal = 6.dp)
            .clip(CircleShape)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = daily.dt!!.toDay(),
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "${daily.weather?.get(0)?.description}", style = TextStyle(
                    color = Color.White,
                    fontSize = 9.sp
                )
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = daily.humidity!!.toHumidity(),
                style = TextStyle(
                    color = Color.White,
                    fontSize = 9.sp
                ),
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = daily.temp?.max!!.toTemperature(preferenceUnits.temperature),
                style = TextStyle(
                    color = Color.White,
                    fontSize = 13.sp
                ),
            )

        }
    }
}