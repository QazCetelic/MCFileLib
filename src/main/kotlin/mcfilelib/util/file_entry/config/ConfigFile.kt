package mcfilelib.util.file_entry.config

import mcfilelib.util.loadJson
import org.tomlj.Toml
import java.nio.file.Path
import java.util.*

class ConfigFile(path: Path): ConfigEntry(path) {
    val type = when (path.toFile().extension.toUpperCase()) {
        "JSON" -> ConfigTypes.JSON
        "PROPERTIES" -> ConfigTypes.PROPERTIES
        "TOML" -> ConfigTypes.TOML
        "NBT" -> ConfigTypes.NBT
        else -> ConfigTypes.OTHER
    }

    fun asJsonObject() = loadJson(path)
    fun asProperties() {
        val stream = path.toFile().inputStream()
        val properties = Properties().load(stream)
        stream.close()
        return properties
    }
    fun asToml() = Toml.parse(path)

    enum class ConfigTypes(val standardExtractionMethod: Boolean) {
        JSON(true),
        PROPERTIES(true),
        TOML(true),
        NBT(true),
        OTHER(false),
    }
}