package classes.used.file_entry

import com.google.gson.Gson
import java.nio.file.Path
import java.util.*

class ConfigEntry(path: Path): FileEntry(path) {
    val type = this.path.toFile().extension
    val data: Any? by lazy {
        when (type) {
            "properties" -> Properties().load(this.path.toFile().inputStream())
            "json" -> Gson().fromJson(this.path.toFile().readText(), Any::class.java)
            else -> null
        }
    }
}