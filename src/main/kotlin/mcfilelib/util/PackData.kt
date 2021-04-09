package mcfilelib.util

import com.google.gson.Gson
import mcfilelib.json.ResourcePackData
import neatlin.zipFile.getEntryAsAWTImage
import neatlin.zipFile.getEntryAsText
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Path
import java.util.zip.ZipFile
import javax.imageio.ImageIO

//TODO Consider merging this with Pack.kt
/**
 * Is used for extracting data from packs
 */
internal class PackData(path: Path) {
    var format: Int? = null
    var description: String? = null
    var image: BufferedImage? = null
    init {
        val file = path.toFile()

        fun getPackJson(string: String?): ResourcePackData? = when {
            string != null && string.isNotEmpty() -> Gson().fromJson(string, ResourcePackData::class.java)
            else -> null
        }
        val data =
            if (file.extension == "zip") {
                val zipFile = ZipFile(file)
                image = zipFile.getEntryAsAWTImage("pack.png")
                //Returns pack data
                getPackJson(zipFile.getEntryAsText("pack.mcmeta"))
            }
            else {
                val imageFile = File("$path/pack.png")
                image = if (imageFile.exists()) ImageIO.read(imageFile) else null
                val metaFile = File("$path/pack.mcmeta")
                //Returns pack data if the file actually exists
                if (metaFile.exists()) getPackJson(metaFile.readText()) else null
            }

        //In case the loading of the mcmeta file fails, for example when the file doesn't exist.
        data?.let {
            format = data.pack.packFormat
            description = data.pack.description
        }
    }
}