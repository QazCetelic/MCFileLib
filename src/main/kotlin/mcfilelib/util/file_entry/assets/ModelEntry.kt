package mcfilelib.util.file_entry.assets

import mcfilelib.util.file_entry.fileEntryString
import java.nio.file.Path

class ModelEntry(val path: Path) {
    override fun toString(): String = fileEntryString("Model", "models", path)
}