package classes.used.config

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
}