package mcfilelib.util.file_entry.config

import fillMap
import java.nio.file.Path

/**
 * Used to represent the file tree the config files are stored in
 */
class ConfigDirectory(path: Path): ConfigEntry(path) {
    /**
     * All the entries contained in this [ConfigDirectory], are **lazily loaded** to decrease resource usage with large instances.
     */
    val contents by lazy {
        fillMap<String, ConfigEntry> {
            path.toFile().listFiles()?.forEach {
                if (it.isDirectory) set(it.name, ConfigDirectory(it.toPath()))
                else set(it.name, ConfigFile(it.toPath()))
            }
        }
    }

    /**
     * Takes all [ConfigEntry]'s and returns a map with only the [ConfigFile]s, removing the directories and providing direct access
     * @return Map<String, ConfigFile>
     */
    fun getAllConfigFiles(): Map<String, ConfigFile> = fillMap {
        // Gets all entries from the config directory it is called from and goes through them…
        for (configEntry in this@ConfigDirectory.getAllConfigEntries()) if (configEntry.value is ConfigFile) {
            // …and adds them to a map
            set(configEntry.key, configEntry.value as ConfigFile)
        }
    }

    /**
     * Get all [ConfigEntry]'s from the directory it is called from.
     * @return Map<String, ConfigEntry>
     */
    fun getAllConfigEntries(): Map<String, ConfigEntry> = fillMap {
        // Goes through all the config entries and adds them to the map
        for (configEntry in contents.values) when (configEntry) {
            // Recursively gets all entries from the directories
            is ConfigDirectory -> putAll(configEntry.getAllConfigEntries())
            // Simply adds config
            is ConfigFile -> set(relativePath(configEntry.path), configEntry)
        }
    }

    // A string with the local path is used to access the config files in a map, they are created using this function
    private fun relativePath(path: Path) = path.toFile().toRelativeString(path.toFile())
}