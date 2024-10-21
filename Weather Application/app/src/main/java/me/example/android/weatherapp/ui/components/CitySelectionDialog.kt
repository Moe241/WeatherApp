package me.example.android.weatherapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.example.android.weatherapp.MainViewModel
import me.example.android.weatherapp.R
import me.example.android.weatherapp.service.LocationInfo
import weatherapp.domain.models.FavoriteCity

@Composable
fun CitySelectionDialog(
    onDismiss: () -> Unit,
    onCitySelected: (latitude: Double, longitude: Double) -> Unit,
    selectedLocationInfo: (LocationInfo) -> Unit,
    viewModel : MainViewModel
    )

{
    val cities = listOf(
        "Tokyo, Japan" to Pair(35.6895, 139.6917),
        "London, United Kingdom" to Pair(51.5074, -0.1278),
        "Sydney, Australia" to Pair(-33.8688, 151.2093),
        "Paris, France" to Pair(48.8566, 2.3522),
        "Cape Town, South Africa" to Pair(-33.9249, 18.4241),
        "Moscow, Russia" to Pair(55.7558, 37.6176),
        "Rome, Italy" to Pair(41.9028, 12.4964),
        "Toronto, Canada" to Pair(43.6532, -79.3832),
        "Ballerup,Denmark" to Pair(55.73136,12.363552),
        "Kongens Lyngby,Denmark" to Pair(55.7687,12.5000),
        )

    val favoriteMap = viewModel.favoriteCitiesState.collectAsState().value



    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Select a City", fontSize = 20.sp) },
        buttons = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {

                cities.forEach { (cityName, coordinates) ->
                    // Get the favorite state for the current city
                    val isFavorite = favoriteMap[cityName] ?: false

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            onCitySelected(coordinates.first, coordinates.second)
                            val locationInfo = LocationInfo(
                                latitude = coordinates.first,
                                longitude = coordinates.second,
                                location = cityName
                            )
                            selectedLocationInfo(locationInfo)
                        },
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        RadioButton(selected = false, onClick = {})
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = cityName, fontSize = 17.sp)
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = {
                            val newFavoriteState = !isFavorite
                            viewModel.saveCityFavoriteState(cityName, newFavoriteState)

                            val locationInfo = FavoriteCity(
                                latitude = coordinates.first,
                                longitude = coordinates.second,
                                cityName = cityName
                            )
                            if (newFavoriteState) {
                                viewModel.addFavoriteCity(locationInfo)
                            } else {
                                viewModel.removeFavoriteCity(locationInfo)
                            }

                        }) {
                            Icon(
                                painter = painterResource(id = if (isFavorite) R.drawable.favorite_black_24dp else R.drawable.baseline_favorite_border_24),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = if (isFavorite) Color.Red else Color.Gray,
                            )
                        }
                    }
                }
            }
        }
    )
}