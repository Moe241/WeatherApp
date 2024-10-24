package weatherapp.data.remote

import kotlinx.coroutines.runBlocking
import weatherapp.data.base.ApiBaseTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNull
import org.junit.Test

class OpenWeatherApiServiceTestApi: ApiBaseTest() {

    @Test
    fun `test current weather api`() = runBlocking {

        enqueueResponse("current_weather.json")
        val response = service.fetchCurrentWeather("api_key", "query")
        val request = mockWebServer.takeRequest()

        assertThat(request.path, `is`("/weather?appid=api_key&q=query"))

        assertThat(response, IsNull.notNullValue())

        assertThat(response.code(), `is`(200))

        assertThat(response.body()?.base, `is`("stations"))
        assertThat(response.body()?.clouds?.all, `is`(25))

    }

    @Test
    fun `one call weather api`() = runBlocking {

        enqueueResponse("one_call_weather.json")
        val response = service.fetchOneCall("api_key", 1.0, 1.0)
        val request = mockWebServer.takeRequest()

        assertThat(request.path, `is`("/onecall?appid=api_key&lat=1&lon=2"))

        assertThat(response, IsNull.notNullValue())

        assertThat(response.code(), `is`(200))

        assertThat(response.body()?.timezone, `is`("Asia/Shanghai"))
        assertThat(response.body()?.current?.weather?.size, `is`(1))

    }

    @Test
    fun `five day weather api`() = runBlocking {

        enqueueResponse("five_day_weather.json")
        val response = service.fetchFiveDayWeather("api_key", "query")
        val request = mockWebServer.takeRequest()

        assertThat(request.path, `is`("/forecast?appid=api_key&q=query"))

        assertThat(response, IsNull.notNullValue())

        assertThat(response.code(), `is`(200))

        assertThat(response.body()?.list?.size, `is`(40))
        assertThat(response.body()?.city?.name, `is`("Beijing"))

    }

    @Test
    fun `test error 400`() = runBlocking {

        enqueueResponse(fileName = "error.json", code = 400)
        val response = service.fetchOneCall("api_key", 1.0, 1.0)
        val request = mockWebServer.takeRequest()

        assertThat(request.path, `is`("/onecall?appid=api_key&lat=1.0&lon=1.0"))

        assertThat(response, IsNull.notNullValue())

        assertThat(response.code(), `is`(400))

        assertThat(response.body(), IsNull.nullValue())

    }

}