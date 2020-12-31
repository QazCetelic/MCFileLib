package classes.used.file_entry.config

import com.google.gson.JsonObject
import main.util.JsonLoader
import java.nio.file.Path

class Config(path: Path): ConfigEntry(path) {
    val type = this.path.toFile().extension
    fun asJsonObject(): JsonObject = JsonLoader(path)
}