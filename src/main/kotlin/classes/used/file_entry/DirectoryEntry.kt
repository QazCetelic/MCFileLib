package classes.used.file_entry

import java.nio.file.Path

class DirectoryEntry(path: Path): GenericEntry(path) {
    val contents = run {
        val result = mutableMapOf<String, GenericEntry>()
        path.toFile().listFiles()?.forEach {
            if (it.isDirectory) result[it.name] = DirectoryEntry(it.toPath())
            else result[it.name] = FileEntry(it.toPath())
        }
        result.toMap()
    }
}