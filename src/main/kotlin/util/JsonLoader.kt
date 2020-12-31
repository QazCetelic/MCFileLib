package main.util

import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.File
import java.io.IOException
import java.nio.file.Path

//todo: Reconsider using singleton
//Fetches main.json file from path and returns a JsonObject
object JsonLoader {
    operator fun invoke(path: Path): JsonObject {
        //JSON to return
        var json = JsonObject()
        //File containing JSON
        val jsonFile = path.toFile()

        //Extract JSON
        if (!jsonFile.isDirectory) {
            try {
                //Takes main.json text from file and turns it into an object
                val jsonText = jsonFile.readText()
                json = Gson().fromJson(jsonText, JsonObject::class.java)
            }
            catch (e: IOException) {e.printStackTrace()}
        }

        return json
    }
}