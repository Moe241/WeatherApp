package me.example.android.weatherapp.ui

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import me.example.android.weatherapp.MainViewModel
import me.example.android.weatherapp.ui.components.ForecastItem
import weatherapp.domain.models.Daily
import weatherapp.domain.models.NetworkResult
import weatherapp.domain.models.PreferenceUnits

@ExperimentalAnimationApi
@Composable
fun ForecastContent(
    dailyItems : List<Daily>?,
    preferenceUnits: PreferenceUnits,
    viewModel: MainViewModel,
) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                LazyRow(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .fillMaxWidth()
                ) {
                    items(dailyItems!!) { daily ->
                        ForecastItem(
                            daily,
                            preferenceUnits,
                        )
                    }
                }
            }
        }

