package mcfilelib.util.file_entry.assets.blockstate

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import neatlin.fillList
import java.util.concurrent.locks.Condition


class BlockStateMultipartEntry {
    // Any of these conditions has to be true
    val conditions: List<BlockStateCondition>
    val model: BlockStateModel

    constructor(multipart: JsonObject) {
        val rawCondition = multipart["when"]!!.jsonObject

        fun hasDirectionKey(condition: JsonObject): Boolean = "north" in condition || "east" in condition || "south" in condition || "west" in condition
        conditions = when {
            "OR" in rawCondition -> {
                val conditionsOR = rawCondition["OR"]!!.jsonArray.map { it.jsonObject }
                fillList {
                    if (conditionsOR.all { hasDirectionKey(it) }) {
                        for (jsonElement in conditionsOR) {
                            add(BlockStateCondition(jsonElement.jsonObject))
                        }
                    }
                }
            }
            hasDirectionKey(rawCondition) -> {
                listOf(multipart["when"]!!.jsonObject).map { BlockStateCondition(it) }
            }
            else -> throw Exception()
        }
        model = BlockStateModel(multipart["apply"]!!.jsonObject)
    }
}