package classes

import util.FileEditable
import java.io.File
import java.nio.file.Path
import javax.imageio.ImageIO

class World(path: Path): FileEditable(path) {
    val name = path.toFile().nameWithoutExtension
    //Uses lazy initialization because the images may be quite large which could become problematic when working with lots of Worlds
    val icon by lazy {
        val iconFile = File("$path/icon.png")
        if (iconFile.exists()) ImageIO.read(iconFile)
        else null
    }
    //A list of the DataPacks of the dataclasses.readonly.main.classes.main.classes.World
    val dataPacks = run {
        val list = ArrayList<DataPack>()
        //Checks if the dataclasses.readonly.main.classes.main.classes.DataPack folder is actually a folder, and not just a file.
        File("$path/datapacks/").listFiles()?.forEach {
            list += DataPack(it.toPath())
        }
        list.toList()
    }

    override fun toString(): String = "(name=$name, path=$path, datapacks=$dataPacks)"
}