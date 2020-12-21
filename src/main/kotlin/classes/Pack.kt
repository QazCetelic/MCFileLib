package classes

import main.util.VersionConverter
import util.FileEditable
import util.PackData
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Path

abstract class Pack(path: Path, isResourcePack: Boolean): FileEditable(path) {
    val name: String = path.toFile().nameWithoutExtension
    val format: Int
    val description: String
    val icon: BufferedImage?
    val modSupport: Boolean
    init {
        //Gets metadata from main.json file
        val packData = PackData(path)
        format = packData.format
        description = packData.description
        icon = packData.image

        //Checks if the assets folder contains unusual folders indicating mod support
        val assetsFiles = File("$path/assets").listFiles()
        var foundModSupport = false
        assetsFiles?.forEach {
            //Checks if entry is not normal
            if (!(it.name == "minecraft" || it.name == "realms")) {
                foundModSupport = true
            }
        }
        modSupport = foundModSupport
    }
    open val versionRange = VersionConverter().fromFormatToRange(format, isResourcePack)
    override fun toString() = "(name=$name, path=$path, format=$format${
        if (versionRange != null) " (${versionRange})"
        else ""
    }, description=$description, icon=${
        if (icon != null) "yes" else "no"
    }, modsupport=$modSupport)"

}