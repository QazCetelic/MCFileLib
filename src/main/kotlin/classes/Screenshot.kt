package classes

import main.util.Time
import util.FileEditable
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Path
import javax.imageio.ImageIO

class Screenshot(path: Path): FileEditable(path) {
    val time: Time
    val image: BufferedImage
    init {
        val file = path.toFile()
        val name = file.nameWithoutExtension
        //Check regex for the screenshot name format
        time = if ("[0-9]{4}-[0-9]{2}-[0-9]{2}_[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}".toRegex().containsMatchIn(name)) {
            if (file.extension.toLowerCase() == "png") {
                //Replace custom class with already existing class
                Time (
                        //Gets the time from the string
                        name.substring(0..3).toInt(),
                        name.substring(5..6).toInt(),
                        name.substring(8..9).toInt(),
                        name.substring(11..12).toByte(),
                        name.substring(14..15).toByte(),
                        name.substring(17..18).toByte()
                )
            } else throw Exception("Invalid screenshot: Invalid file format")
        } else throw Exception("Invalid screenshot: Invalid time format")
        image = ImageIO.read(file)
    }

    override fun toString() = "main.classes.main.classes.Screenshot: $time"
}