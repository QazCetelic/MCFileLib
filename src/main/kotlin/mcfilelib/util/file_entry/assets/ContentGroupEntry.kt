package mcfilelib.util.file_entry.assets

import mcfilelib.util.file_entry.assets.blockstate.BlockState
import mcfilelib.util.file_entry.fileEntryString
import neatlin.fillMap
import neatlin.io.div
import neatlin.io.zipfile.getEntryAsText
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

class ContentGroupEntry {
    // e.g "…/Faithfull x32/assets/realms/"
    val path: Path?
    // e.g "Realms"
    val name: String

    val vanilla: Boolean
    val includesOptifine: Boolean

    val blockstates: Map<String, BlockState>
    val models: Map<String, ModelEntry>
    val textures: Map<String, TextureEntry>

    constructor(entryPath: Path, packPath: Path) {
        path = entryPath
        name = fileEntryString(null, "assets", path)

        val file = path.toFile()
        vanilla = (file.nameWithoutExtension == "minecraft" || file.nameWithoutExtension == "realms")
        includesOptifine = (path/"optifine").toFile().exists()

        blockstates = fillMap {
            for (blockState in (path/"blockstates").toFile().walk()) if (blockState.isFile) {
                set(fileEntryString(null, "blockstates", blockState.toPath()), BlockState(blockState.toPath()))
            }
        }
        models = fillMap {
            for (model in (path/"models").toFile().walk()) if (model.isFile) {
                set(fileEntryString(null, "models", model.toPath()), ModelEntry(model.toPath()))
            }
        }
        textures = fillMap {
            for (texture in (path/"textures").toFile().walk()) if (texture.isFile) {
                set(fileEntryString(null, "textures", texture.toPath()), TextureEntry(texture.toPath()))
            }
        }
    }

    internal constructor(zipEntries: List<ZipEntry>, zipFile: ZipFile, entryName: String) {
        path = null
        name = entryName

        var foundOptifine = false

        val foundBlockStates = mutableMapOf<String, BlockState>()
        val foundModels = mutableMapOf<String, ModelEntry>()
        val foundTextures = mutableMapOf<String, TextureEntry>()

        for (entry in zipEntries) {
            // e.g. "assets/minecraft/blockstates/acacia_trapdoor.json" -> "acacia_trapdoor"
            fun processPath(path: String, additional: String = ""): String {
                val parts = path.removePrefix("assets/$name/$additional").split(".")
                val partsWithoutExtension = if (parts.size > 1) parts.dropLast(1) else parts
                return partsWithoutExtension.joinToString(separator = ".")
            }
            val parts = processPath("$entry").split("/")
            // TODO "$entry" might not give the same result as the logic for uncompressed folders
            when (parts[0]) {
                "optifine" -> foundOptifine = true
                "blockstates" -> foundBlockStates[processPath("$entry", "blockstates/")] = BlockState(zipFile.getEntryAsText(entry))
                "models" -> foundModels[processPath("$entry", "models/")] = ModelEntry(zipFile.getEntryAsText(entry))
                "textures" -> foundTextures[processPath("$entry", "textures/")] = TextureEntry(zipFile.getEntryAsText(entry))
            }
        }

        blockstates = foundBlockStates
        models = foundModels
        textures = foundTextures

        includesOptifine = foundOptifine

        vanilla = (name == "Minecraft" || name == "Realms") && !includesOptifine
    }

    override fun toString(): String {
                // Gives basic information about the content group
        return  "ContentGroup: (name=$name, path=\"$path\", vanilla=$vanilla, includesOptifine=$includesOptifine, "+
                // All relative paths of the blockstates, models and textures in this content group
                "blockstates=${blockstates.keys}, models=${models.keys}, textures=${textures.keys})"
    }
}