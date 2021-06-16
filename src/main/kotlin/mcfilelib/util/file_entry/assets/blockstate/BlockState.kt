package mcfilelib.util.file_entry.assets.blockstate

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import java.nio.file.Path

/**
 * Abstract representation of blockstates.
 * Item frames are treated as blocks and use "map=false" for a map-less item frame, and "map=true" for item frames with maps.
 */
class BlockState {
    val path: Path?

    val variants: MutableList<BlockStateVariant> //Map<Map<String, String>?, List<BlockStateModel>> // "when" entry
    val multipart: MutableList<BlockStateMultipartEntry>           // "apply" entry

    private fun processRootObject(root: JsonObject): Pair<MutableList<BlockStateVariant>, MutableList<BlockStateMultipartEntry>> {
        val foundVariants = mutableListOf<BlockStateVariant>()
        if ("variants" in root) {
            val variants = root["variants"]!!.jsonObject
            for (entry in variants.entries) {
                foundVariants += BlockStateVariant(entry.value, entry.key)
            }
        }

        val foundMultiparts = mutableListOf<BlockStateMultipartEntry>()
        if ("multipart" in root) {
            val multiparts = root["multipart"]!!.jsonArray
            for (jsonElement in multiparts) {
                foundMultiparts += BlockStateMultipartEntry(jsonElement.jsonObject)
            }
        }

        return Pair(foundVariants, foundMultiparts)
    }

    constructor(path: Path) {
        this.path = path
        val jsonString: String = path.toFile().readText()
        val root = Json.decodeFromString<JsonObject>(jsonString)
        with(processRootObject(root)) {
            variants = first
            multipart = second
        }
    }

    constructor(jsonString: String) {
        path = null
        val root = Json.decodeFromString<JsonObject>(jsonString)
        with(processRootObject(root)) {
            variants = first
            multipart = second
        }
    }

    private fun variantsToString(): String = buildString {
        append("\n    \"variants\": {")
        append(variants.joinToString(separator = ",") { "\n${"$it".prependIndent("        ")}" })
        append("\n    }")
    }

    private fun multiPartToString(): String = buildString {
        val partStart:                                      String  = "\n        {   \"when\": { "
        fun partEnd(last: Boolean = false):                 String  = "\n        }${if (!last) "," else ""}"
        fun partApply(blockStateModel: BlockStateModel):    String  = "\n            \"apply\": $blockStateModel"
        fun condition(condition: BlockStateCondition):      String  = "\n                $condition"

        fun partMultiple(conditions: List<BlockStateCondition>, blockStateModel: BlockStateModel, last: Boolean = false) {
            append(partStart)
            append(" \"OR\": [")
            append(conditions.joinToString(separator = ",") { condition(it) })
            append("\n            ]},")
            append(partApply(blockStateModel))
            append(partEnd(last))
        }
        fun partSingle(condition: BlockStateCondition, blockStateModel: BlockStateModel, last: Boolean = false) {
            append(partStart)
            append(condition(condition))
            append("\n            }")
            append(partApply(blockStateModel))
            append(partEnd(last))
        }

        append("\n    \"multipart\": [")
        for (i in multipart.indices) {
            val isLast = (multipart.lastIndex == i)
            if (multipart[i].conditions.size != 1) partMultiple(multipart[i].conditions, multipart[i].model, isLast)
            else partSingle(multipart[i].conditions.first(), multipart[i].model, isLast)
        }
        append("\n    ]")
    }

    override fun toString(): String = buildString {
        append("{")
        if (variants.isNotEmpty()) {
            append(variantsToString())
        }
        if (multipart.isNotEmpty()) {
            if (variants.isNotEmpty()) append(",")
            append(multiPartToString())
        }
        append("\n}")
    }
}