package weatherapp.data.di

import FavoriteListRepositoryImp
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import weatherapp.data.BuildConfig
import weatherapp.data.local.UnitsPreferencesDataStoreImpl
import weatherapp.data.mapper.CurrentWeatherMapper
import weatherapp.data.mapper.FiveDayWeatherMapper
import weatherapp.data.mapper.OneCallMapper
import weatherapp.data.remote.api.OpenWeatherApi
import weatherapp.data.remote.response.CurrentWeatherResponse
import weatherapp.data.remote.response.FiveDayWeatherForecastResponse
import weatherapp.data.remote.response.OneCallResponse
import weatherapp.data.repository.OpenWeatherRepositoryImpl
import weatherapp.domain.datastore.UnitsPreferencesDataStore
import weatherapp.domain.mapper.ResponseMapper
import weatherapp.domain.models.CurrentWeather
import weatherapp.domain.models.FiveDayWeatherForecast
import weatherapp.domain.models.OneCall
import weatherapp.domain.repository.FavoriteRepository
import weatherapp.domain.repository.OpenWeatherRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    private val okHttpClient: OkHttpClient by lazy {
        val interceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY else
                HttpLoggingInterceptor.Level.NONE
        }
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(interceptor)
            .build()
    }

    private val baseUrl: String by lazy {
        BuildConfig.OPEN_WEATHER_BASE_URL
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideUnitsPreferencesDataStore(
        @ApplicationContext appContext: Context
    ): UnitsPreferencesDataStore = UnitsPreferencesDataStoreImpl(appContext)

    @Provides
    @Singleton
    fun provideOpenWeatherApi(): OpenWeatherApi = retrofit.create(OpenWeatherApi::class.java)

    @Provides
    @Singleton
    fun provideCurrentWeatherMapper(): ResponseMapper<CurrentWeatherResponse, CurrentWeather> = CurrentWeatherMapper()

    @Provides
    @Singleton
    fun provideOneCallMapper(): ResponseMapper<OneCallResponse, OneCall> = OneCallMapper()

    @Provides
    @Singleton
    fun provideFiveDayWeatherMapper(): ResponseMapper<FiveDayWeatherForecastResponse, FiveDayWeatherForecast> = FiveDayWeatherMapper()

    @Provides
    @Singleton
    fun provideOpenWeatherRepository(
        openWeatherApi: OpenWeatherApi,
        currentWeatherMapper: ResponseMapper<CurrentWeatherResponse, CurrentWeather>,
        oneCallMapper: ResponseMapper<OneCallResponse, OneCall>,
        fiveDayWeatherMapper: ResponseMapper<FiveDayWeatherForecastResponse, FiveDayWeatherForecast>
    ) : OpenWeatherRepository = OpenWeatherRepositoryImpl(openWeatherApi, currentWeatherMapper, oneCallMapper, fiveDayWeatherMapper)

    @Provides
    @Singleton
    fun provideFavouriteCities(
        @ApplicationContext context: Context
    ) : FavoriteRepository = FavoriteListRepositoryImp(context)
}