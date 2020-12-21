package json

import com.google.gson.annotations.SerializedName

data class ResourcePackData (
    @SerializedName("pack") val pack : Pack
)