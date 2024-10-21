package weatherapp.data.base

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import weatherapp.data.remote.api.OpenWeatherApi
import weatherapp.data.repository.OpenWeatherRepositoryImpl
import weatherapp.domain.repository.OpenWeatherRepository
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations

abstract class BaseMockTest {

    @Mock
    lateinit var openWeatherApi: OpenWeatherApi

    protected lateinit var openWeatherRepository: OpenWeatherRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        openWeatherRepository = OpenWeatherRepositoryImpl(openWeatherApi)
    }

    protected inline fun <reified T : Any> readJsonResponse(fileName: String) : T {
        val fileContent = this::class.java.classLoader.getResource(fileName).readText()
        return Gson().fromJson(fileContent, object : TypeToken<T>() {}.type)
    }

}