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
            //Maybe not the best way to do it, but I couldn't find another way
            if (it::class == ConfigDirectory::class) ConfigDirectory(it.path).allContents()
            else completeContents[it.path.toString().removePrefix(path.toString()).removePrefix("/")] = it
        }
        return completeContents
    }
}