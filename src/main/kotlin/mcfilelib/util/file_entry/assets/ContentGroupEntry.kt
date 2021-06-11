package mcfilelib.util.file_entry.assets

import neatlin.fillMap
import neatlin.io.div
import java.nio.file.Path

class ContentGroupEntry(val path: Path) {
    // e.g. "…/resourcepacks/Faithfull x32/assets/minecraft" -> "/minecraft" -> "minecraft" -> "Minecraft"
    val name: String = "$path".split("assets")[1].removePrefix("/").capitalize()
    val vanilla: Boolean
    val includesOptifine: Boolean

    val blockstates: Map<String, BlockStateEntry>
    val models: Map<String, ModelEntry>
    val textures: Map<String, TextureEntry>

    init {
        val file = path.toFile()
        vanilla = (file.nameWithoutExtension == "minecraft" || file.nameWithoutExtension == "realms")
        includesOptifine = path.resolve("optifine").toFile().exists()

        blockstates = fillMap {
            for (blockState in (path/"blockstates").toFile().walk()) {
                if (blockState.isFile) set(blockState.toRelativeString(path.toFile()).removePrefix("blockstates/"), BlockStateEntry(blockState.toPath()))
            }
        }

        models = fillMap {
            for (model in (path/"models").toFile().walk()) {
                if (model.isFile) set(model.toRelativeString(path.toFile()).removePrefix("models/"), ModelEntry(model.toPath()))
            }
        }

        textures = fillMap {
            for (texture in (path/"textures").toFile().walk()) {
                if (texture.isFile) set(texture.toRelativeString(path.toFile()).removePrefix("textures/"), TextureEntry(texture.toPath()))
            }
        }
    }

    override fun toString(): String {
                // Gives basic information about the content group
        return  "ContentGroup: (name=$name, path=\"$path\", vanilla=$vanilla, includesOptifine=$includesOptifine, " +
                // All relative paths of the blockstates, models and textures in this content group
                "blockstates=${blockstates.keys}, models=${models.keys}, textures=${textures.keys})"
    }
}