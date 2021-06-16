package mcfilelib.util.file_entry.assets.blockstate

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

class BlockStateVariant {
    var name: MutableMap<String, String>?
    val models: MutableSet<BlockStateModel>

    constructor(jsonElement: JsonElement, key: String) {
        name =
            if (key == "") null
            else keyToMap(key)

        val foundModels = mutableSetOf<BlockStateModel>()
        when {
            jsonElement is JsonArray -> {
                for (i in jsonElement.indices) {
                    foundModels += BlockStateModel(jsonElement[i].jsonObject)
                }
            }
            jsonElement is JsonObject -> {
                foundModels += BlockStateModel(jsonElement)
            }
        }
        models = foundModels
    }

    private fun keyToMap(string: String): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        if ("," in string) {
            val parts = string.split(",").filter { it != "" }
            for (part in parts) {
                if (part.count { it == '=' } == 1) {
                    val (key, value) = part.split("=")
                    map[key] = value
                }
            }
        }
        return map
    }

    fun nameToString(): String {
        return if (name == null) ""
        else {
            val pairStrings = mutableListOf<String>()
            name?.entries?.forEach { pairStrings += "${it.key}=${it.value}" }
            pairStrings.joinToString(separator = ",")
        }
    }

    override fun toString(): String = buildString {
        append("\"${nameToString()}\": ")
        if (models.size == 1) {
            append(models.first())
        }
        else {
            append("[")
            val modelsList = models.toList()
            // Explicitly adds the weight for all models with the default weight option (even though it has no effect) if one of the models doesn't use the default for aesthetics
            val showWeight = modelsList.any { it.weight != 1 }
            for (i in modelsList.indices) {
                append("\n    ${modelsList[i].toString(showWeight)}")
                if (modelsList.lastIndex != i) append(",")
            }
            append("\n]")
        }
    }
}