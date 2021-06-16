package mcfilelib.generic

import mcfilelib.util.file_entry.assets.ContentGroupEntry
import mcfilelib.util.fromFormatToRange
import neatlin.fillMap
import neatlin.hash.Hash
import neatlin.hash.invoke
import neatlin.io.div
import java.awt.image.BufferedImage
import java.lang.Exception
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

abstract class Pack(val path: Path, isResourcePack: Boolean) {
    val name: String
    val format: Int?
    val description: String?
    val icon: BufferedImage?

    val modSupport: Boolean?
    val contentGroupEntries: Map<String, ContentGroupEntry>

    init {
        val packFile = path.toFile()
        name = packFile.nameWithoutExtension

        PackMetadata(packFile).let {
            format      = it.format
            description = it.description
            icon        = it.icon
        }

        if (packFile.extension == "zip") {
            val zipFile = ZipFile(packFile)
            val zipEntries = zipFile.entries().toList()

            // e.g "[minecraft, realms]"
            val contentGroupEntryNames = mutableListOf<String>()
            for (zipEntry in zipFile.entries()) {
                val pathParts = "$zipEntry".split("/")
                if (pathParts.size == 3) {
                    contentGroupEntryNames += pathParts[1]
                }
            }

            // Grouped by content group
            // e.g "…minecraft=[assets/minecraft/, assets/minecraft/blockstates/, assets/minecraft/blockstates/acacia_trapdoor.json, …"
            val grouped: Map<String, List<ZipEntry>> = zipEntries.groupBy {
                for (groupName in contentGroupEntryNames) {
                    if ("$it".startsWith("assets/$groupName/")) return@groupBy groupName
                }
            }   .filter { it.key != Unit }
                .mapKeys { it.key as String }
                // Filters for files, and thus removes directories.
                .mapValues { it.value.filterNot { it.isDirectory } }

            val foundContentGroups = mutableMapOf<String, ContentGroupEntry>()
            for (group in grouped.entries) {
                foundContentGroups[group.key] = ContentGroupEntry(group.value, zipFile, entryName = group.key)
            }
            contentGroupEntries = foundContentGroups
            modSupport = contentGroupEntries.all { it.value.vanilla && !it.value.includesOptifine }
        }
        else {
            val files = (path/"assets").toFile().listFiles()

            var foundContentGroups = mutableMapOf<String, ContentGroupEntry>()
            var foundModSupport: Boolean? = false
            try {
                for (file in files) {
                    foundContentGroups[file.name] = ContentGroupEntry(entryPath = file.toPath(), packPath = path)
                }
                foundModSupport = foundContentGroups.values.any { !it.vanilla || it.includesOptifine }
            }
            catch (e: Exception) {
                // Gives up
                foundContentGroups = mapOf<String, ContentGroupEntry>().toMutableMap()
                foundModSupport = null
            }
            finally {
                contentGroupEntries = foundContentGroups
                modSupport = foundModSupport
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