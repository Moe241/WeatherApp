package me.example.android.weatherapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.example.android.weatherapp.service.LocationInfo
import me.example.android.weatherapp.service.LocationService
import weatherapp.domain.models.FavoriteCity
import weatherapp.domain.models.NetworkResult
import weatherapp.domain.models.OneCall
import weatherapp.domain.repository.FavoriteRepository
import weatherapp.domain.repository.OpenWeatherRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: OpenWeatherRepository,
    private val favoriteRepository: FavoriteRepository,
) : ViewModel() {

    private val _oneCallWeather = MutableStateFlow<NetworkResult<OneCall>?>(NetworkResult.Empty)
    val oneCallWeather: StateFlow<NetworkResult<OneCall>?> = _oneCallWeather

    private val _selectedLocation = MutableStateFlow<LocationInfo?>(null)
    val selectedLocation: StateFlow<LocationInfo?> = _selectedLocation

    private val _isInitialLoadComplete = MutableStateFlow(false)
    val isInitialLoadComplete: StateFlow<Boolean> = _isInitialLoadComplete

    fun fetchInitialData(lat: Double, lon: Double) {
        viewModelScope.launch {
            fetchOneCall(lat, lon)
            _isInitialLoadComplete.value = true
        }
    }
    fun updateSelectedLocation(locationInfo: LocationInfo) {
        _selectedLocation.value = locationInfo
    }

    private val _favoriteCitiesState = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val favoriteCitiesState: StateFlow<Map<String, Boolean>> = _favoriteCitiesState.asStateFlow()


    fun saveCityFavoriteState(cityName: String, isFavorite: Boolean) {
        viewModelScope.launch {
            favoriteRepository.saveFavoriteState(cityName, isFavorite)
            // Update the state
            val currentFavorites = _favoriteCitiesState.value.toMutableMap()
            currentFavorites[cityName] = isFavorite
            _favoriteCitiesState.value = currentFavorites
        }
    }

    private val _favoriteCities = MutableStateFlow<List<FavoriteCity>>(emptyList())
    val favoriteCities: StateFlow<List<FavoriteCity>> = _favoriteCities

    init {
        loadFavoriteCities()
    }

    private fun loadFavoriteCities() {
        viewModelScope.launch {
            val uniqueFavoriteCities = favoriteRepository.getFavouriteList().distinctBy { it.cityName }
            _favoriteCities.value = uniqueFavoriteCities
        }
    }

    fun addFavoriteCity(city: FavoriteCity) {
        viewModelScope.launch {
            favoriteRepository.addFavouriteCity(city)
            loadFavoriteCities() // Refresh the favorite cities list
        }
    }

    fun removeFavoriteCity(city: FavoriteCity) {
        viewModelScope.launch {
            favoriteRepository.removeFavouriteCity(city)
            loadFavoriteCities() // Refresh the favorite cities list
        }
    }
    fun fetchOneCall(lat: Double, lon: Double){
        viewModelScope.launch {
            repository.fetchOneCall(lat, lon).collect { result ->
                _oneCallWeather.value = result
            }
        }
    }

    fun saveWeatherData(context: Context, data: OneCall) {
        val sharedPref = context.getSharedPreferences("WeatherApp", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("weatherData", Gson().toJson(data))
            apply()
        }
    }

    fun getSavedWeatherData(context: Context): OneCall? {
        val sharedPref = context.getSharedPreferences("WeatherApp", Context.MODE_PRIVATE)
        val dataString = sharedPref.getString("weatherData", null)
        return if (dataString != null) Gson().fromJson(dataString, OneCall::class.java) else null
    }

    fun selectFavoriteCity(city: FavoriteCity) {
        viewModelScope.launch {
            _selectedLocation.value = LocationInfo(city.latitude, city.longitude, city.cityName)
            fetchOneCall(city.latitude, city.longitude)
        }
    }
}