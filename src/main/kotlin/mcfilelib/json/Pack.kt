package mcfilelib.json

import com.google.gson.annotations.SerializedName
data class Pack (
    @SerializedName("pack_format") val packFormat : Int,
    @SerializedName("description") val description : String
)