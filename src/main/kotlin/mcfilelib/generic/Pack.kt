package mcfilelib.generic

import mcfilelib.util.file_entry.assets.ContentGroupEntry
import mcfilelib.util.fromFormatToRange
import neatlin.div
import neatlin.fillMap
import java.awt.image.BufferedImage
import java.nio.file.Path

abstract class Pack(val path: Path, isResourcePack: Boolean) {
    val name: String = path.toFile().nameWithoutExtension
    val format: Int?
    val description: String?
    val icon: BufferedImage?
    val modSupport: Boolean?
    val contentGroupEntries: Map<String, ContentGroupEntry>

    init {
        PackMetadata(path).let {
            format = it.format
            description = it.description
            icon = it.icon
        }

        val file = path.toFile()
        if (file.extension == "zip") {
            // Gives up
            modSupport = null
            contentGroupEntries = mapOf()
        }
        else {
            val files = (path/"assets").toFile().listFiles()
            if (files != null) {
                contentGroupEntries = fillMap {
                    files.forEach { file ->
                        set(file.name, ContentGroupEntry(file.toPath()))
                    }
                }
                modSupport = contentGroupEntries.values.any { !it.vanilla || it.includesOptifine }
            }
            else {
                contentGroupEntries = mapOf()
                modSupport = null
            }
        }
    }

    open val versionRange = fromFormatToRange(format, isResourcePack)
    override fun toString() = "(name=$name, path=$path, format=$format${
        if (versionRange != null) " (${versionRange})"
        else ""
    }, description=$description, icon=${
        if (icon != null) "yes" else "no"
    }, modsupport=$modSupport)"

}