package mcfilelib.util.file_entry.config

import java.nio.file.Path

abstract class ConfigEntry(val path: Path) {
    val name = path.toFile().nameWithoutExtension
}