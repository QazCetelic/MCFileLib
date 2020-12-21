package util

import java.nio.file.Files
import java.nio.file.Path

abstract class FileEditable(path: Path) {
    var path = path
        private set
    
    fun delete() {
        Files.delete(path)
    }
}