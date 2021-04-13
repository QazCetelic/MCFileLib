package mcfilelib.generic

import neatlin.*
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Path
import javax.imageio.ImageIO

class World(val path: Path) {
    val name: String
    val dataPacks: List<DataPack>
    init {
        val file = path.toFile()
        if (file.isDirectory) {
            name = path.toFile().name
            dataPacks = fillList {
                File("$path/datapacks/").listFiles()?.forEach {
                    add(DataPack(it.toPath()))
                }
            }
        } else throw Exception("World is not a directory")
    }

    /**
     * The icon isn't stored in the variable to reduce memory usage.
     * The getter attempts to directly get the image from disk, but returns null if the image isn't there.
     */
    val icon: BufferedImage?
        get() = run {
            val iconFile = File("$path/icon.png")
            return if (iconFile.exists()) ImageIO.read(iconFile)
            else null
        }

    override fun toString(): String = "(name=$name, path=$path, datapacks=$dataPacks)"
}