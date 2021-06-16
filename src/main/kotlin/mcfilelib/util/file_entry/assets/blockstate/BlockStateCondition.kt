package mcfilelib.util.file_entry.assets.blockstate

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

class BlockStateCondition {
    private val map: MutableMap<String, Set<String>>
    operator fun get(key: String) = map[key]
    operator fun set(key: String, value: String) {
        map[key] = value.split("|").toSet()
    }
    operator fun set(key: String, value: Set<String>) {
        map[key] = value
    }

    constructor(jsonObject: JsonObject) {
        val tempMap = mutableMapOf<String, Set<String>>()
        for (entry in jsonObject.entries) {
            tempMap[entry.key] = entry.value.jsonPrimitive.content.split("|").toSet()
        }
        map = tempMap
    }

    var north: Set<String>?
        get() = map["north"]
        set(value) {
            map["north"] = value ?: emptySet()
        }
    var east: Set<String>?
        get() = map["east"]
        set(value) {
            map["east"] = value ?: emptySet()
        }
    var south: Set<String>?
        get() = map["south"]
        set(value) {
            map["south"] = value ?: emptySet()
        }
    var west: Set<String>?
        get() = map["west"]
        set(value) {
            map["west"] = value ?: emptySet()
        }

    override fun toString(): String = buildString {
        append("{ ")
        val entries = map.entries.toList()
        for (i in entries.indices) {
            append("\"${entries[i].key}\":\"${entries[i].value.joinToString(separator = "|")}\"")
            if (entries.lastIndex != i) append(",")
        }
        append(" }")
    }
}