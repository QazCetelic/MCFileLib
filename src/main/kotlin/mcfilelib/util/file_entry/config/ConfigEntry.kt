package mcfilelib.util.file_entry.config

import mcfilelib.util.file_entry.fileEntryString
import java.nio.file.Path

abstract class ConfigEntry(val path: Path) {
    val name = fileEntryString(null, "config", path)

    override fun toString(): String {
        return when (this) {
            is ConfigFile -> "ConfigFile: $name"
            is ConfigDirectory -> "ConfigDirectory: $name"
            else -> throw Exception("This shouldn't happen, 'this' should be either a ConfigFile or a ConfigDirectory")
        }
    }
}