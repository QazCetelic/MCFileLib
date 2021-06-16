package mcfilelib.util.file_entry.assets

import mcfilelib.util.file_entry.assets.blockstate.BlockState
import mcfilelib.util.file_entry.fileEntryString
import neatlin.fillMap
import neatlin.io.div
import java.nio.file.Path

class ContentGroupEntry(packPath: Path, val path: Path) {
    val name: String = fileEntryString(null, "assets", path).capitalize()
    val vanilla: Boolean
    val includesOptifine: Boolean

    val blockstates: Map<String, BlockState>
    val models: Map<String, ModelEntry>
    val textures: Map<String, TextureEntry>

    init {
        val file = path.toFile()
        vanilla = (file.nameWithoutExtension == "minecraft" || file.nameWithoutExtension == "realms")
        includesOptifine = path.resolve("optifine").toFile().exists()

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

    override fun toString(): String {
                // Gives basic information about the content group
        return  "ContentGroup: (name=$name, path=\"$path\", vanilla=$vanilla, includesOptifine=$includesOptifine, "+
                // All relative paths of the blockstates, models and textures in this content group
                "blockstates=${blockstates.keys}, models=${models.keys}, textures=${textures.keys})"
    }
}