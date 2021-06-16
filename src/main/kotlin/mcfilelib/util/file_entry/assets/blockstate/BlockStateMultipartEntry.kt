package mcfilelib.util.file_entry.assets.blockstate

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject


class BlockStateMultipartEntry {
    // Any of these conditions has to be true
    var conditions: MutableList<BlockStateCondition>
    val model: BlockStateModel

    constructor(multipart: JsonObject) {
        val rawCondition = multipart["when"]!!.jsonObject

        fun hasDirectionKey(condition: JsonObject): Boolean = "north" in condition || "east" in condition || "south" in condition || "west" in condition
        conditions = when {
            "OR" in rawCondition -> {
                val foundConditions = mutableListOf<BlockStateCondition>()
                for (jsonElement in rawCondition["OR"]!!.jsonArray) {
                    if (jsonElement is JsonObject) {
                        foundConditions += BlockStateCondition(jsonElement)
                    }
                }
                foundConditions
            }
            hasDirectionKey(rawCondition) -> {
                mutableListOf(rawCondition).map { BlockStateCondition(it) }
            }
            else -> emptyList()
        }.toMutableList()
        model = BlockStateModel(multipart["apply"]!!.jsonObject)
    }
}