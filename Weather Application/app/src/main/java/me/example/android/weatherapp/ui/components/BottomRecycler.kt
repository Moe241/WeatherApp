package me.example.android.weatherapp.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import me.example.android.weatherapp.ui.theme.dimensions
import weatherapp.domain.models.Hourly
import weatherapp.domain.models.PreferenceUnits

@ExperimentalAnimationApi
@Composable
fun BottomRecycler(
    hourly: List<Hourly>,
    preferenceUnits: PreferenceUnits,
) {

    ConstraintLayout {

        val (textHourly, textWeekly, hourlyRow) = createRefs()

        // Hourly text
        Text(
            text = "Today",
            style = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = dimensions.forecastTextSize
            ),
            modifier = Modifier
                .constrainAs(textHourly) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
                .padding(start = 8.dp)
        )


        // Hourly horizontal list
        LazyRow(
            modifier = Modifier
                .constrainAs(hourlyRow) {
                    top.linkTo(textHourly.bottom)
                    top.linkTo(textHourly.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .clip(RoundedCornerShape(16.dp))
                .fillMaxWidth()
        ) {
            items(hourly) { item ->
                HourlyItem(
                    hourly = item,
                    timeUnit = preferenceUnits.time,
                    temperatureUnit = preferenceUnits.temperature
                )
            }
        }
    }

}