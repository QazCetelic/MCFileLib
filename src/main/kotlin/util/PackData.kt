package main.util

import main.json.Base
import com.google.gson.Gson
import java.awt.image.BufferedImage
import java.io.File
import java.util.zip.ZipFile
import javax.imageio.ImageIO

internal class PackData(path: String) {
    /**
     Is used for both Resource & Data Packs
    */
    val format: Int
    val description: String
    val image: BufferedImage?
    init {
        val data =
            if (File(path).extension == "zip") {
                val zipFile = ZipFile(path)
                image = ImageIO.read(zipFile.getInputStream(zipFile.getEntry("pack.png")))
                //Returns pack data
                getPackJson(String(zipFile.getInputStream(
                    zipFile.getEntry("pack.mcmeta")).readBytes()
                ))
            }
            else {
                val imageFile = File("$path/pack.png")
                image = if (imageFile.exists()) {
                    ImageIO.read(imageFile)
                } else null
                //Checks if the file actually exists
                val metaFile = File("$path/pack.mcmeta")
                if (metaFile.exists()) {
                    //Returns pack data
                    getPackJson(metaFile.readText())
                } else null

            }

        if (data != null) {
            format = data.pack.pack_format
            description = data.pack.description
        }
        else {
            //In case the loading of the mcmeta file fails, for example when the file doesn't exist.
            format = -1
            description = "Failed to load description"
        }
    }

    private fun getPackJson(string: String?): Base? = if (string != null && string.isNotEmpty()) {
        Gson().fromJson(string, Base::class.java)
    } else null
}