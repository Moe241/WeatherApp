package weatherapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("1h") var one_h : Double? = null,
    @SerializedName("3h") var three_h : Double? = null
)
