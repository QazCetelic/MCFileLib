package mcfilelib.util.file_entry.assets

import java.nio.file.Path

class ContentGroupEntry(val path: Path) {
    val vanilla: Boolean
    val includesOptifine: Boolean

    val blockstates: Map<String, BlockStateEntry>
    val models: Map<String, ModelEntry>
    val textures: Map<String, TextureEntry>

    init {
        val file = path.toFile()
        vanilla = (file.nameWithoutExtension == "minecraft" || file.nameWithoutExtension == "realms")
        includesOptifine = path.resolve("optifine").toFile().exists()

        val foundBlockStates = mutableMapOf<String, BlockStateEntry>()
        val blockStatesDirectory = path.resolve("blockstates").toFile().walk()
        blockStatesDirectory.forEach {
            if (it.isFile) foundBlockStates[it.toRelativeString(path.toFile())] = BlockStateEntry(it.toPath())
        }
        blockstates = foundBlockStates

        val foundModels = mutableMapOf<String, ModelEntry>()
        val modelDirectory = path.resolve("models").toFile().walk()
        modelDirectory.forEach {
            if (it.isFile) foundModels[it.toRelativeString(path.toFile())] = ModelEntry(it.toPath())
        }
        models = foundModels

        val foundTextures = mutableMapOf<String, TextureEntry>()
        val texturesDirectory = path.resolve("textures").toFile().walk()
        texturesDirectory.forEach {
            if (it.isFile) foundTextures[it.toRelativeString(path.toFile())] = TextureEntry(it.toPath())
        }
        textures = foundTextures
    }
}