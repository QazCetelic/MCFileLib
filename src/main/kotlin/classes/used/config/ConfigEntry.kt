package classes.used.config

import util.FileEditable
import java.nio.file.Path

abstract class ConfigEntry(path: Path): FileEditable(path) {
    val name: String
    init {
        val file = path.toFile()
        name = file.nameWithoutExtension
    }
}