package classes.used.file_entry

import util.FileEditable
import java.nio.file.Path

abstract class GenericEntry(path: Path): FileEditable(path) {
    val name: String
    init {
        val file = path.toFile()
        name = file.nameWithoutExtension
    }
}