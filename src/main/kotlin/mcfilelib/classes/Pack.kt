package mcfilelib.classes

import div
import fillMap
import forCheck
import mcfilelib.util.FileEditable
import mcfilelib.util.PackData
import mcfilelib.util.file_entry.assets.ContentGroupEntry
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
        PackData(path).also {
            format = it.format
            description = it.description
            icon = it.image
        }

        val file = path.toFile()
        if (file.extension == "zip") {
            modSupport = null
            contentGroupEntries = mapOf()
        }
        else {
            val files = (path/"assets").toFile().listFiles()
            if (files != null) {
                contentGroupEntries = fillMap {
                    files.forEach {
                        set(it.name, ContentGroupEntry(it.toPath()))
                    }
                }

                modSupport = contentGroupEntries.values.forCheck { !it.vanilla || it.includesOptifine }
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