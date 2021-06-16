package mcfilelib.util.file_entry

import java.nio.file.Path

fun fileEntryString(entryTypeName: String?, entryFolderName: String, path: Path): String {
    val parts = "$path".split(entryFolderName)
    val relative = parts[parts.lastIndex].removePrefix("/")
    val prefix = if (entryTypeName != null) "${entryTypeName}Entry: " else ""

    return "$prefix${relative.removeSuffix("." + path.toFile().extension)}"
}