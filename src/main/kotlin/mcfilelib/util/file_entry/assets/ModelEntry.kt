package mcfilelib.util.file_entry.assets

import mcfilelib.util.file_entry.fileEntryString
import java.nio.file.Path

class ModelEntry {
    val path: Path?

    internal constructor(path: Path) {
        this.path = path
    }

    internal constructor(jsonString: String) {
        this.path = null
    }

    override fun toString(): String {
        if (path != null) return fileEntryString("Model", "models", path)
        else TODO()
    }
}