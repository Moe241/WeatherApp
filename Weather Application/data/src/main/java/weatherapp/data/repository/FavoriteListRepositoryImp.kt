import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import weatherapp.data.remote.response.BaseResponse
import weatherapp.data.repository.TinyDB
import weatherapp.domain.models.FavoriteCity
import weatherapp.domain.repository.FavoriteRepository
import javax.inject.Inject

class FavoriteListRepositoryImp @Inject constructor(
    @ApplicationContext context: Context,
) : FavoriteRepository, BaseResponse() {

    private val tinyDB = TinyDB(context)
    private val gson = Gson()

    override fun saveFavoriteState(cityName: String, isFavorite: Boolean) {
        tinyDB.putBoolean(cityName, isFavorite)
    }

    override fun getFavoriteState(cityName: String): Boolean {
        return tinyDB.getBoolean(cityName)
    }

    override fun saveFavoriteCities(cities: List<FavoriteCity>) {
        val json = gson.toJson(cities)
        tinyDB.putString("favorite_cities", json)
    }

    override fun getFavouriteList(): List<FavoriteCity> {
        val json = tinyDB.getString("favorite_cities")
        return if (json.isNullOrEmpty()) {
            emptyList()
        } else {
            val type = object : TypeToken<List<FavoriteCity>>() {}.type
            gson.fromJson(json, type)
        }
    }

    override fun addFavouriteCity(city: FavoriteCity) {
        val cities = getFavouriteList().toMutableList()
        cities.add(city)
        saveFavoriteCities(cities)
    }

    override fun removeFavouriteCity(city: FavoriteCity) {
        val cities = getFavouriteList().filterNot { it == city }
        saveFavoriteCities(cities)
    }
}
