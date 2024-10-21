package weatherapp.domain.repository

import weatherapp.domain.models.FavoriteCity

interface FavoriteRepository {

    fun saveFavoriteState(cityName: String, isFavorite: Boolean)

    fun getFavoriteState(cityName: String): Boolean

    fun saveFavoriteCities(cities : List<FavoriteCity>)

    fun getFavouriteList() : List<FavoriteCity>

    fun addFavouriteCity(city : FavoriteCity)

    fun removeFavouriteCity(city: FavoriteCity)
}