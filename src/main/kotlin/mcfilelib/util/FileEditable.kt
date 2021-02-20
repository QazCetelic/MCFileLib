package mcfilelib.util

import java.nio.file.Path

abstract class FileEditable(path: Path) {
    var path = path
        private set
}