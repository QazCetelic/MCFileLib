package main.json

import com.google.gson.annotations.SerializedName
data class Pack (
    @SerializedName("pack_format") val pack_format : Int,
    @SerializedName("description") val description : String
)