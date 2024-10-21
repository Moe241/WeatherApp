package me.example.android.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import me.example.android.weatherapp.service.LocationInfo
import me.example.android.weatherapp.ui.EnableGpsContent
import me.example.android.weatherapp.ui.FavoriteContent
import me.example.android.weatherapp.ui.LoadingContent
import me.example.android.weatherapp.ui.WeatherContent
import me.example.android.weatherapp.ui.theme.OpenWeatherTheme
import weatherapp.domain.datastore.UnitsPreferencesDataStore
import weatherapp.domain.models.PreferenceUnits
import javax.inject.Inject

// Api -> RepoImpl -> Repo -> ViewModel -> UI

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var unitsPreferencesDataStore: UnitsPreferencesDataStore


    private val viewModel: MainViewModel by viewModels()

    @OptIn(InternalCoroutinesApi::class, androidx.compose.material.ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val preferenceUnits = unitsPreferencesDataStore.preferencesUnits.collectAsState(
                PreferenceUnits("°C", "m/s", "hPa", "12-hour")
            ).value



            val navController = rememberNavController()

            // Weather navigation callback
            val navigateToWeather: (LocationInfo) -> Unit = { locationInfo ->
                navController.navigate("weather/${locationInfo.latitude}/${locationInfo.longitude}/${locationInfo.location}") {
                    launchSingleTop = true
                    popUpTo("loading") { inclusive = true }
                }
            }

            OpenWeatherTheme {
                NavHost(navController, startDestination = "loading") {

                    composable(route = "loading") {
                        LoadingContent(
                            navigateToWeather
                        )
                    }

                    composable(route = "weather/{latitude}/{longitude}/{location}") { backStackEntry ->
                        val latitude = backStackEntry.arguments?.getString("latitude")?.toDouble()
                        val longitude = backStackEntry.arguments?.getString("longitude")?.toDouble()
                        val location = backStackEntry.arguments?.getString("location")
                        WeatherContent(
                            preferenceUnits,
                            viewModel,
                            latitude!!,
                            longitude!!,
                            location!!,
                            navController
                        )
                    }

                    composable(route = "favorites") {
                        FavoriteContent(viewModel, navController)

                    }
                }
            }

        }

    }

}

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun MainPreview() {
    OpenWeatherTheme {
        EnableGpsContent()
    }
}