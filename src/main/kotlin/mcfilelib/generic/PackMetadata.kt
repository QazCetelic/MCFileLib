package mcfilelib.generic

import com.google.gson.JsonObject
import mcfilelib.util.ifKey
import mcfilelib.util.loadJson
import neatlin.div
import neatlin.getEntryAsAWTImage
import neatlin.toPath
import java.awt.image.BufferedImage
import java.nio.file.Path
import java.util.zip.ZipFile
import javax.imageio.ImageIO

class PackMetadata(path: Path) {
    var format: Int? = null
        private set
    var description: String? = null
        private set
    var icon: BufferedImage? = null
        private set

    init {
        val packFile = path.toFile()
        if (packFile.extension == "zip") {
            val zipFile = ZipFile(packFile)
            readPackJson(zipFile.loadJson("pack.mcmeta"))
            icon = zipFile.getEntryAsAWTImage("pack.png")
        }
        else {
            val mcmetaPath = path/"pack.mcmeta"
            val imageFile = (path/"pack.png").toFile()
            icon = if (imageFile.exists()) ImageIO.read(imageFile) else null
            if (mcmetaPath.toFile().exists()) readPackJson(loadJson(mcmetaPath))
        }
    }

    private fun readPackJson(json: JsonObject) {
        json.ifKey("pack") { pack ->
            val packObject = pack.asJsonObject
            packObject.ifKey("pack_format") {
                format = it.asInt
            }
            packObject.ifKey("description") {
                description = it.asString
            }
        }
    }
}