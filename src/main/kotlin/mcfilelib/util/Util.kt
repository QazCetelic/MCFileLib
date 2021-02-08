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
fun JsonObject.ifKey(key: String, lambda: (json: JsonElement) -> Unit) {
    if (this.has(key)) lambda(this[key])
}
