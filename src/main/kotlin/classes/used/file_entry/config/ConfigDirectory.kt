package classes.used.file_entry.config

import java.nio.file.Path

class ConfigDirectory(path: Path): ConfigEntry(path) {
    val contents by lazy {
        val result = mutableMapOf<String, ConfigEntry>()
        path.toFile().listFiles()?.forEach {
            if (it.isDirectory) result[it.name] = ConfigDirectory(it.toPath())
            else result[it.name] = Config(it.toPath())
        }
        result.toMap()
    }

    fun getAll(): Map<String, Config> {
        val contents = this.getContentFromFolder()
        val contentsAsConfig = mutableMapOf<String, Config>()
        contents.forEach {
            contentsAsConfig[it.key] = it.value as Config
        }
        return contentsAsConfig.toMap()
    }
    private fun getContentFromFolder(): Map<String, ConfigEntry> {
        val completeContents = mutableMapOf<String, ConfigEntry>()
        contents.values.forEach {
            if (it::class == ConfigDirectory::class) {
                ConfigDirectory(it.path).getContentFromFolder().forEach { directoryContent ->
                    completeContents[pathInConfigDir(directoryContent.value)] = directoryContent.value
                }
            }
            else completeContents[pathInConfigDir(it)] = it
        }
        return completeContents.toMap()
    }

    private fun pathInConfigDir(input: ConfigEntry) = input.path.toFile().toRelativeString(path.toFile())
}