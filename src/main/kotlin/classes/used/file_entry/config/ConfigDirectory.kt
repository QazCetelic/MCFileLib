package classes.used.file_entry.config

import java.nio.file.Path

class ConfigDirectory(path: Path): ConfigEntry(path) {
    val contents = run {
        val result = mutableMapOf<String, ConfigEntry>()
        path.toFile().listFiles()?.forEach {
            if (it.isDirectory) result[it.name] = ConfigDirectory(it.toPath())
            else result[it.name] = Config(it.toPath())
        }
        result.toMap()
    }

    fun allContents(): MutableMap<String, ConfigEntry> {
        val completeContents = mutableMapOf<String, ConfigEntry>()
        contents.values.forEach {
            if (it::class == ConfigDirectory::class) {
                ConfigDirectory(it.path).allContents().forEach { directoryContent ->
                    completeContents[pathInConfigDir(directoryContent.value)] = directoryContent.value
                }
            }
            else completeContents[pathInConfigDir(it)] = it
        }
        return completeContents
    }

    private fun pathInConfigDir(input: ConfigEntry) = input.path.toString().removePrefix(path.toString()).removePrefix("/")
}