package me.example.android.weatherapp.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import me.example.android.weatherapp.MainViewModel
import me.example.android.weatherapp.R
import timber.log.Timber

@ExperimentalAnimationApi
@Composable
fun WeatherToolBar(
    onCitySelected: (latitude: Double, longitude: Double) -> Unit,
    viewModel : MainViewModel,
    navController: NavController

) {
    var showDialog by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }



    TopAppBar(
        title = {

        },
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
        actions = {
            Box(
                modifier = Modifier
                    .clickable {
                        expanded = !expanded
                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_settings),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp),
                    tint = Color.Black
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .background(Color.White)
                ) {
                    // Select City
                    DropdownMenuItem(onClick = {
                        expanded = false
                        showDialog = true
                    }) {
                        Text("Select City")
                    }

                    // Favorites
                    DropdownMenuItem(onClick = {
                        expanded = false
                        navController.navigate("favorites")
                    }) {
                        Text("Favorites")
                    }
                }
            }
        }
    )

    if (showDialog) {
        CitySelectionDialog(
            onDismiss = { showDialog = false },
            onCitySelected = {latitude, longitude ->
                onCitySelected(latitude, longitude)
                showDialog = false
            },
            selectedLocationInfo = {
                Timber.d("Selected Location Info: $it")
                viewModel.updateSelectedLocation(it)
            },
            viewModel = viewModel
        )
    }
}
