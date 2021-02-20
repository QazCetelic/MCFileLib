package mcfilelib.util

import com.google.gson.Gson
import mcfilelib.json.ResourcePackData
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Path
import java.util.zip.ZipFile
import javax.imageio.ImageIO

//TODO Consider this with Pack.kt
/**
 * Is used for extracting data from packs
 */
internal class PackData(path: Path) {
    val format: Int
    val description: String
    val image: BufferedImage?
    init {
        val file = path.toFile()
        val data =
            if (file.extension == "zip") {
                val zipFile = ZipFile(file)
                image = ImageIO.read(zipFile.getInputStream(zipFile.getEntry("pack.png")))
                //Returns pack data
                getPackJson(String(zipFile.getInputStream(
                    zipFile.getEntry("pack.mcmeta")).readBytes()
                ))
            }
            else {
                val imageFile = File("$path/pack.png")
                image = if (imageFile.exists()) ImageIO.read(imageFile) else null
                //Checks if the file actually exists
                val metaFile = File("$path/pack.mcmeta")
                if (metaFile.exists()) {
                    //Returns pack data
                    getPackJson(metaFile.readText())
                } else null

            }

        if (data != null) {
            format = data.pack.packFormat
            description = data.pack.description
        }
        else {
            //In case the loading of the mcmeta file fails, for example when the file doesn't exist.
            format = -1
            description = "Failed to load description"
        }
    }

    private fun getPackJson(string: String?): ResourcePackData? = if (string != null && string.isNotEmpty()) {
        Gson().fromJson(string, ResourcePackData::class.java)
    } else null
}