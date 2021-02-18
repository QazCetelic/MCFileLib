package mcfilelib.util

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.io.IOException
import java.nio.file.Path

/**
 * Allows you to write paths in your code like this example: basePath/"folder"/"text.txt".
 */
operator fun Path.div(string: String): Path = this.resolve(string)

/**
 * Fetches file from path and parses the text containing it to a JsonObject, returns empty JsonObject if it fails.
 */
fun loadJson(path: Path): JsonObject {
    //JSON to return
    var json = JsonObject()
    //File containing JSON
    val jsonFile = path.toFile()

    //Extract JSON
    if (!jsonFile.isDirectory) {
        try {
            //Takes main.json text from file and turns it into an object
            val jsonText = jsonFile.readText()
            if (jsonText.isNotBlank()) json = Gson().fromJson(jsonText, JsonObject::class.java)
        }
        catch (e: IOException) {e.printStackTrace()}
    }
    return json
}

/**
 * A function to reduce the code needed for getting values from json, because it's quite common
 */
fun JsonObject.ifKey(key: String, exception: (() -> Unit)? = null, lambda: (json: JsonElement) -> Unit): Boolean {
    return if (this.has(key)) {
        lambda(this[key])
        true
    } else {
        if (exception != null) exception()
        false
    }
}

fun String.undev(): String {
    val chars = this.toCharArray()
    if (chars.size > 1) {
        var isDivided = false
        for (i in chars.indices) {
            if (chars[i] == '-' || chars[i] == '_' && chars.size > i + 1 && chars[i+1].isLetter() ) {
                chars[i] = ' '
                chars[i+1] = chars[i+1].toUpperCase()
                isDivided = true
            }
        }
        if (isDivided) {
            chars[0] = chars[0].toUpperCase()
        }
    }
    val sb = StringBuilder()
    for (char in chars) {
        sb.append(char)
    }
    return sb.toString()
}

fun splitArgumentString(argumentString: String): List<String> {
    return argumentString.removePrefix("--").split(" --")
}