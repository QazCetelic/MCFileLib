package classes.used.file_entry.config

import util.loadJson
import java.nio.file.Path
import java.util.*

class Config(path: Path): ConfigEntry(path) {
    val type = when (this.path.toFile().extension.toLowerCase()) {
        "json" -> ConfigTypes.JSON
        "properties" -> ConfigTypes.PROPERTIES
        "toml" -> ConfigTypes.TOML
        "nbt" -> ConfigTypes.NBT
        else -> ConfigTypes.OTHER
    }

    val asJsonObject
        get() = loadJson(path)
    val asProperties
        get() = Properties().load(path.toFile().inputStream())
    //TODO: add asTOML()
    //TODO: add asNBT()

    enum class ConfigTypes(standardExtractionMethod: Boolean) {
        JSON(true),
        PROPERTIES(true),
        TOML(true),
        NBT(true),
        OTHER(false),
    }
}