package mcfilelib.classes

import mcfilelib.util.file_entry.assets.ContentGroupEntry
import mcfilelib.util.FileEditable
import mcfilelib.util.PackData
import mcfilelib.util.fromFormatToRange
import java.awt.image.BufferedImage
import java.nio.file.Path

abstract class Pack(path: Path, isResourcePack: Boolean): FileEditable(path) {
    val name: String = path.toFile().nameWithoutExtension
    val format: Int
    val description: String
    val icon: BufferedImage?
    val modSupport: Boolean?
    val contentGroupEntries: Map<String, ContentGroupEntry>

    init {
        //Gets metadata from main.json file
        val packData = PackData(path)
        format = packData.format
        description = packData.description
        icon = packData.image

        val file = path.toFile()
        if (file.extension == "zip") {
            modSupport = null
            contentGroupEntries = mapOf()
        }
        else {
            val files = path.resolve("assets").toFile().listFiles()
            if (files != null) {
                val foundContentGroupEntries = mutableMapOf<String, ContentGroupEntry>()
                files.forEach {
                    foundContentGroupEntries[it.name] = ContentGroupEntry(it.toPath())
                }
                var foundModSupport = false
                foundContentGroupEntries.values.forEach {
                    if (!it.vanilla || it.includesOptifine) foundModSupport = true
                }
                contentGroupEntries = foundContentGroupEntries.toMap()
                modSupport = foundModSupport
            } else {
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