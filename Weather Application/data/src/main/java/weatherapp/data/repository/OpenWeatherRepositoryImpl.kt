package weatherapp.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import weatherapp.data.BuildConfig
import weatherapp.data.remote.api.OpenWeatherApi
import weatherapp.data.remote.response.BaseResponse
import weatherapp.data.remote.response.CurrentWeatherResponse
import weatherapp.data.remote.response.FiveDayWeatherForecastResponse
import weatherapp.data.remote.response.OneCallResponse
import weatherapp.domain.mapper.ResponseMapper
import weatherapp.domain.models.CurrentWeather
import weatherapp.domain.models.FiveDayWeatherForecast
import weatherapp.domain.models.NetworkResult
import weatherapp.domain.models.OneCall
import weatherapp.domain.repository.OpenWeatherRepository
import javax.inject.Inject

class OpenWeatherRepositoryImpl @Inject constructor(
    private val openWeatherApi: OpenWeatherApi,
    private val currentWeatherMapper: ResponseMapper<CurrentWeatherResponse, CurrentWeather>,
    private val oneCallMapper: ResponseMapper<OneCallResponse, OneCall>,
    private val fiveDayWeatherMapper: ResponseMapper<FiveDayWeatherForecastResponse, FiveDayWeatherForecast>
) : OpenWeatherRepository, BaseResponse() {

    override suspend fun fetchCurrentWeather(city: String): Flow<NetworkResult<CurrentWeather>> = flow {
        emit(NetworkResult.Loading)
        emit(safeApiCall(currentWeatherMapper) {
            openWeatherApi.fetchCurrentWeather(
                BuildConfig.OPEN_WEATHER_API_KEY,
                city
            )
        })
    }.flowOn(Dispatchers.IO)

    override suspend fun fetchOneCall(lat: Double, lon: Double): Flow<NetworkResult<OneCall>> = flow {
        emit(NetworkResult.Loading)
        emit(safeApiCall(oneCallMapper) {
            openWeatherApi.fetchOneCall(
                BuildConfig.OPEN_WEATHER_API_KEY,
                lat,
                lon
            )
        })
    }.flowOn(Dispatchers.IO)

    override suspend fun fetchFiveDayWeather(city: String): Flow<NetworkResult<FiveDayWeatherForecast>> = flow {
        emit(NetworkResult.Loading)
        emit(safeApiCall(fiveDayWeatherMapper) {
            openWeatherApi.fetchFiveDayWeather(
                BuildConfig.OPEN_WEATHER_API_KEY,
                city
            )
        })
    }.flowOn(Dispatchers.IO)

}