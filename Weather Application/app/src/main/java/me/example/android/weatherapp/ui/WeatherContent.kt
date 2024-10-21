package me.example.android.weatherapp.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import me.example.android.weatherapp.MainViewModel
import me.example.android.weatherapp.R
import me.example.android.weatherapp.extensions.toDate
import me.example.android.weatherapp.extensions.toTemperature
import me.example.android.weatherapp.ui.components.BottomRecycler
import me.example.android.weatherapp.ui.components.WeatherToolBar
import me.example.android.weatherapp.ui.theme.Blue
import me.example.android.weatherapp.ui.theme.dimensions
import weatherapp.domain.models.Error
import weatherapp.domain.models.NetworkResult
import weatherapp.domain.models.OneCall
import weatherapp.domain.models.PreferenceUnits
import java.util.Locale

@ExperimentalAnimationApi
@Composable
fun WeatherContent(
    preferenceUnits: PreferenceUnits,
    viewModel: MainViewModel,
    latitude: Double,
    longitude: Double,
    location: String,
    navController: NavController
) {

    Surface(color = MaterialTheme.colors.background) {

        // Boolean state to hold if request was successful
        var isLoaded by rememberSaveable { mutableStateOf(false) }


        // Retry callback
        val retry: () -> Unit = { viewModel.fetchOneCall(latitude, longitude) }

        LaunchedEffect(key1 = "initial") {
            if (!viewModel.isInitialLoadComplete.value) {
                // Fetch data for the current location
                viewModel.fetchInitialData(latitude, longitude)
            }
        }

        // Handle city selections
        LaunchedEffect(key1 = viewModel.selectedLocation.value) {
            viewModel.selectedLocation.value?.let {
                if (viewModel.isInitialLoadComplete.value) {
                    viewModel.fetchOneCall(it.latitude, it.longitude)
                }
            }
        }

        // Update UI according to network result state
        when (val result = viewModel.oneCallWeather.collectAsState().value) {
            is NetworkResult.Loading -> {
                LoadingScreen()
            }

            is NetworkResult.Success -> {
                isLoaded = true
                if (viewModel.isInitialLoadComplete.value) {
                    viewModel.saveWeatherData(
                        LocalContext.current,
                        result.data
                    )
                }
                WeatherScreen(
                    preferenceUnits = preferenceUnits,
                    oneCall = result.data,
                    location,
                    viewModel,
                    onCitySelected = { lat, long ->
                        viewModel.fetchOneCall(lat, long)
                    },
                    navController
                )
            }

            is NetworkResult.Error -> {
                val cachedData = viewModel.getSavedWeatherData(LocalContext.current)
                if (cachedData != null) {
                    WeatherScreen(
                        preferenceUnits = preferenceUnits,
                        oneCall = cachedData,
                        location,
                        viewModel,
                        onCitySelected = { lat, long ->
                            viewModel.fetchOneCall(lat, long)
                        },
                        navController
                    )
                } else {
                    ErrorScreen(error = result.data!!, retry)
                }
            }

            is NetworkResult.Failure -> {
                val cachedData = viewModel.getSavedWeatherData(LocalContext.current)
                if (cachedData != null) {
                    WeatherScreen(
                        preferenceUnits = preferenceUnits,
                        oneCall = cachedData,
                        location,
                        viewModel,
                        onCitySelected = { lat, long ->
                            viewModel.fetchOneCall(lat, long)
                        },
                        navController
                    )
                } else {
                    FailureScreen(message = "Error", retry)
                }
            }

            is NetworkResult.Empty -> {
                LoadingScreen()
            }
        }

    }

}


@ExperimentalAnimationApi
@Composable
fun WeatherScreen(
    preferenceUnits: PreferenceUnits,
    oneCall: OneCall,
    location: String,
    viewModel: MainViewModel,
    onCitySelected: (latitude: Double, longitude: Double) -> Unit,
    navController: NavController
) {
    val selectedLocation = viewModel.selectedLocation.collectAsState()
    Box {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.clouds),
            contentDescription = "background_image",
            contentScale = ContentScale.Crop
        )

        Scaffold(
            backgroundColor = Color.Transparent,
            topBar = {
                WeatherToolBar(
                    onCitySelected,
                    viewModel,
                    navController

                )
            },
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                val (topLayout, bottomLayout) = createRefs()
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Text(
                        text = selectedLocation.value?.location ?: location,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = oneCall.current?.dt?.toDate()!!,
                        style = TextStyle(fontSize = 15.sp, color = Color.White)
                    )
                }
                Column(
                    modifier = Modifier
                        .constrainAs(topLayout) {
                            top.linkTo(parent.top)
                        }
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = dimensions.weatherIconPadding)
                            .wrapContentSize(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        // Circle Shape
                        Box(
                            modifier = Modifier
                                .size(180.dp)
                                .background(Color(0xFF3593BC), shape = CircleShape)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            // Main temp on screen in circle
                            Text(
                                text = oneCall.current?.temp!!.toTemperature(preferenceUnits.temperature),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 38.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.Center)
                            )
                            Text(
                                text = oneCall.current!!.weather?.get(0)!!.description!!.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(
                                        Locale.getDefault()
                                    ) else it.toString()
                                },
                                style = TextStyle(
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center,
                                ),
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 30.dp)
                            )

                        }
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Min Today: ${oneCall.hourly?.first()?.temp!!.toTemperature(preferenceUnits.temperature)}", color = Color.White, fontWeight = FontWeight.Bold)
                            Text(text = "Max Today: ${oneCall.hourly?.last()?.temp!!.toTemperature(preferenceUnits.temperature)}", color = Color.White, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(30.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 15.dp, end = 15.dp)
                        ) {
                            BottomRecycler(
                                oneCall.hourly!!,
                                preferenceUnits,
                            )
                        }
                    }

                }

                // Bottom composable containing hourly items
                Column(
                    modifier = Modifier
                        .constrainAs(bottomLayout) {
                            bottom.linkTo(parent.bottom)
                        }
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp, bottom = 20.dp)
                ) {
                    ForecastContent(
                        dailyItems = oneCall.daily,
                        preferenceUnits = preferenceUnits,
                        viewModel = viewModel
                    )
                }

            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun LoadingScreen() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {

        val (centerLayout) = createRefs()

        // Open weather image
        CircularProgressIndicator(
            modifier = Modifier
                .constrainAs(centerLayout) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .padding(0.dp, 0.dp, 8.dp, 0.dp),
            color = Blue
        )
    }
}

@Composable
fun ErrorScreen(
    error: Error,
    retry: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, bottom = 50.dp, end = 20.dp)
    ) {

        val (titleLayout, descriptionLayout, retryButton) = createRefs()

        // Ooops text
        Text(
            text = "Ooops!",
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .constrainAs(titleLayout) {
                    bottom.linkTo(descriptionLayout.top)
                    start.linkTo(parent.start)
                }
                .height(IntrinsicSize.Min)
                .padding(0.dp, 0.dp, 0.dp, 10.dp)
        )

        // Error message text
        Text(
            text = error.message,
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp
            ),
            modifier = Modifier
                .constrainAs(descriptionLayout) {
                    start.linkTo(parent.start)
                    bottom.linkTo(retryButton.top)
                }
                .width(300.dp)
                .height(IntrinsicSize.Max)
                .padding(bottom = 10.dp)
        )

        // Retry button
        Button(
            onClick = {
                retry.invoke()
            },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .constrainAs(retryButton) {
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                }
                .padding(end = 5.dp),
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text(text = "Retry")
        }

    }
}

@Composable
fun FailureScreen(
    message: String,
    retry: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, bottom = 50.dp, end = 20.dp)
    ) {

        val (titleLayout, descriptionLayout, retryButton) = createRefs()

        // Ooops text
        Text(
            text = "Ooops!",
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .constrainAs(titleLayout) {
                    bottom.linkTo(descriptionLayout.top)
                    start.linkTo(parent.start)
                }
                .height(IntrinsicSize.Min)
                .padding(0.dp, 0.dp, 0.dp, 10.dp)
        )

        // Error message text
        Text(
            text = message,
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp
            ),
            modifier = Modifier
                .constrainAs(descriptionLayout) {
                    start.linkTo(parent.start)
                    bottom.linkTo(retryButton.top)
                }
                .width(300.dp)
                .height(IntrinsicSize.Max)
                .padding(bottom = 10.dp)
        )

        // Retry button
        Button(
            onClick = {
                retry.invoke()
            },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .constrainAs(retryButton) {
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                }
                .padding(end = 5.dp),
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text(text = "Retry")
        }

    }
}