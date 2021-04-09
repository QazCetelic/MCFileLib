package mcfilelib.util.file_entry.config

import mcfilelib.util.FileEditable
import java.nio.file.Path

abstract class ConfigEntry(path: Path): FileEditable(path) {
    val name = path.toFile().nameWithoutExtension
}