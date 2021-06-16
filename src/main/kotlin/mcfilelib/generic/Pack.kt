package mcfilelib.generic

import mcfilelib.util.file_entry.assets.ContentGroupEntry
import mcfilelib.util.fromFormatToRange
import neatlin.fillMap
import neatlin.hash.Hash
import neatlin.hash.invoke
import neatlin.io.div
import java.awt.image.BufferedImage
import java.nio.file.Path

abstract class Pack(val path: Path, isResourcePack: Boolean) {
    val name: String
    val format: Int?
    val description: String?
    val icon: BufferedImage?

    val modSupport: Boolean?
    val contentGroupEntries: Map<String, ContentGroupEntry>

    init {
        val file = path.toFile()
        name = file.nameWithoutExtension

        PackMetadata(file).let {
            format      = it.format
            description = it.description
            icon        = it.icon
        }

        if (file.extension == "zip") {
            // Gives up
            modSupport          = null
            contentGroupEntries = mapOf() // TODO add support for zips
        }
        else {
            val files = (path/"assets").toFile().listFiles()
            if (files != null) {
                contentGroupEntries = fillMap {
                    files.forEach { file ->
                        set(file.name, ContentGroupEntry(packPath = path, file.toPath()))
                    }
                }
                modSupport = contentGroupEntries.values.any { !it.vanilla || it.includesOptifine }
            }
            else {
                // Gives up
                modSupport          = null
                contentGroupEntries = mapOf()
            }
        }
    }

    open val versionRange = fromFormatToRange(format, isResourcePack)

    /**
     * Gets the SHA-256 hash of the pack
     */
    fun generateHash() = Hash.SHA256(path.toFile())

    /**
     * Exports the metadata so it can be used for other purposes
     */
    fun toPackMetadata() = PackMetadata(format, description, icon)

    override fun toString() = "(name=$name, path=$path, format=$format${
        if (versionRange != null) " ($versionRange)"
        else ""
    }, description=$description, icon=${
        if (icon != null) "yes" else "no"
    }, modsupport=$modSupport)"
}