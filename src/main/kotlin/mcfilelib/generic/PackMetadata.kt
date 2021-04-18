package mcfilelib.generic

import com.google.gson.JsonObject
import mcfilelib.util.ifKey
import mcfilelib.util.loadJson
import neatlin.file.div
import neatlin.getEntryAsAWTImage
import java.awt.image.BufferedImage
import java.io.File
import java.util.zip.ZipFile
import javax.imageio.ImageIO

class PackMetadata {
    var name: String
        private set
    var format: Int? = null
        private set
    var description: String? = null
        private set
    var icon: BufferedImage? = null
        private set

    constructor(name: String, format: Int?, description: String?, icon: BufferedImage?) {
        this.name = name
        this.format = format
        this.description = description
        this.icon = icon
    }

    constructor(file: File) {
        name = file.nameWithoutExtension
        if (file.extension == "zip") {
            val zipFile = ZipFile(file)
            readPackJson(zipFile.loadJson("pack.mcmeta"))
            icon = zipFile.getEntryAsAWTImage("pack.png")
        }
        else {
            val mcmetaFile = file/"pack.mcmeta"
            val imageFile = (file/"pack.png")
            icon = if (imageFile.exists()) ImageIO.read(imageFile) else null
            if (mcmetaFile.exists()) readPackJson(loadJson(mcmetaFile.toPath()))
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