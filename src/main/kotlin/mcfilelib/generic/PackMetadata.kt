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
    var format: Int? = null
        private set
    var description: String? = null
        private set
    var icon: BufferedImage? = null
        private set

    constructor(format: Int?, description: String?, icon: BufferedImage?) {
        this.format = format
        this.description = description
        this.icon = icon
    }

    constructor(file: File) {
        if (file.exists()) {
            if (file.extension == "zip") {
                val zipFile = ZipFile(file)

                readPackJson(zipFile.loadJson("pack.mcmeta"))
                icon = zipFile.getEntryAsAWTImage("pack.png")
            }
            else {
                val mcmetaFile = file/"pack.mcmeta"
                val imageFile = file/"pack.png"

                readPackJson(loadJson(mcmetaFile.toPath()))
                icon = if (imageFile.exists()) ImageIO.read(imageFile) else null
            }
        }
        else throw Exception("Pack file doesn't exist")
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

    override fun toString() = "(Format: $format, Description: $description, Icon: ${
        if (icon != null) "yes"
        else "no"
    })"

    /**
     * Generates JSON like how it's found in the *pack.mcmeta* file. Example:
     *
     * ```
     * {
     *   "pack": {
     *     "pack_format": [format],
     *     "description": "[description]"
     *   }
     * }
     * ```
     */
    fun toMCMetaJSON() = """
        {
          "pack": {
            "pack_format": $format,
            "description": "$description"
          }
        }
    """.trimIndent()
}