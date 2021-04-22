package mcfilelib.util

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import neatlin.getEntryAsText
import java.nio.file.Path
import java.util.zip.ZipFile

/**
 * Fetches file from path and parses the text containing it to a JsonObject, returns empty JsonObject if it fails.
 */
fun loadJson(path: Path): JsonObject {
    //File containing JSON
    val jsonFile = path.toFile()

    //Extract JSON
    if (jsonFile.isFile) runCatching {
        return Gson().fromJson(jsonFile.readText(), JsonObject::class.java)
    }
    // Returns empty object if something went wrong
    return JsonObject()
}

fun ZipFile.loadJson(entry: String): JsonObject {
    return try {
        Gson().fromJson(this.getEntryAsText(entry), JsonObject::class.java)
    }
    catch (_: Exception) {
        JsonObject()
    }
}

/**
 * A function to reduce the code needed for getting values from json, because it's quite common
 */
fun JsonObject.ifKey(key: String, lambda: (json: JsonElement) -> Unit): Boolean {
    return if (key in this) {
        lambda(this[key])
        true
    } else false
}

/**
 * "has" function can be used the Kotlin way
 */
operator fun JsonObject.contains(key: String) = this.has(key)