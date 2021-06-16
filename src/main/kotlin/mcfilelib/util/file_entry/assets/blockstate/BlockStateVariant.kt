package mcfilelib.util.file_entry.assets.blockstate

import kotlinx.serialization.json.*

class BlockStateVariant {
    val name: Map<String, String>?
    val models: Set<BlockStateModel>

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

    private fun keyToMap(string: String): Map<String, String> {
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
        if (name == null) return ""

        val pairStrings = mutableListOf<String>()
        for (entry in name.entries) {
            pairStrings += "${entry.key}=${entry.value}"
        }
        return pairStrings.joinToString(separator = ",")
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