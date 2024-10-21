package weatherapp.domain.models

import kotlin.collections.List as list

data class FiveDayWeatherForecast (
    var cod : String?,
    var message : Int?,
    var cnt : Int?,
    var list : list<List<Any?>>?,
    var city : City?
)