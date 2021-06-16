package mcfilelib.util.file_entry.assets.blockstate

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import java.nio.file.Path

/**
 * Abstract representation of blockstates.
 * Item frames are treated as blocks and use "map=false" for a map-less item frame, and "map=true" for item frames with maps.
 */
class BlockState {
    val path: Path

    val variants: Set<BlockStateVariant> //Map<Map<String, String>?, List<BlockStateModel>> // "when" entry
    val multipart: List<BlockStateMultipartEntry>           // "apply" entry

    constructor(path: Path) {
        this.path = path
        val jsonString: String = path.toFile().readText()
        val root = Json.decodeFromString<JsonElement>(jsonString).jsonObject

        val foundVariants = mutableSetOf<BlockStateVariant>()
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

        variants = foundVariants
        multipart = foundMultiparts
    }

    private fun variantsToString(): String = buildString {
        append("\n    \"variants\": {")
        for (variant in variants) {
            append("\n${variant.toString().prependIndent("        ")}")
        }
        // Drops comma
        append("\n    }")
    }

    private fun multiPartToString(): String = buildString {
        fun partStart() {
            append("\n        {   \"when\": { ")
        }
        fun partEnd(last: Boolean = false) {
            append("\n        }")
            if (!last) append(",")
        }
        fun partApply(blockStateModel: BlockStateModel) {
            append("\n            \"apply\": $blockStateModel")
        }
        fun condition(condition: BlockStateCondition, last: Boolean = false) {
            append("\n                $condition")
            if (!last) append(",")
        }
        fun partMultiple(conditions: List<BlockStateCondition>, blockStateModel: BlockStateModel, last: Boolean = false) {
            partStart()
            append(" \"OR\": [")
            for (i in conditions.indices) {
                condition(conditions[i], (conditions.lastIndex == i))
            }
            append("\n            ]},")
            partApply(blockStateModel)
            partEnd(last)
        }
        fun partSingle(condition: BlockStateCondition, blockStateModel: BlockStateModel, last: Boolean = false) {
            partStart()
            condition(condition, last = true)
            append("\n            }")
            partApply(blockStateModel)
            partEnd(last)
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