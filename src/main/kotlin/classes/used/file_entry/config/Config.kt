package classes.used.file_entry.config

import com.google.gson.Gson
import classes.used.file_entry.config.ConfigEntry
import java.nio.file.Path
import java.util.*

class Config(path: Path): ConfigEntry(path) {
    val type = this.path.toFile().extension
    val data: Any? by lazy {
        when (type) {
            "properties" -> Properties().load(this.path.toFile().inputStream())
            "json" -> Gson().fromJson(this.path.toFile().readText(), Any::class.java)
            else -> null
        }
    }
}