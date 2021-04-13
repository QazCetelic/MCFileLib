package mcfilelib.generic

import mcfilelib.util.Time
import java.nio.file.Path
import javax.imageio.ImageIO

/**
 * Object for managing screenshots in an instance, the image is not stored in the object itself to reduce memory usage.
 * You can get the time the screenshot was taken from the time val.
 * It includes a path to the file on disk, for special actions.
 */
class Screenshot(val path: Path) {
    /**
     * Time in YEAR-MONTH-DAY-HOUR-MINUTE-SECOND
     */
    val time: Time

    /**
     * The getter directly gets the Image from disk, it is not stored in the object
     */
    val image
        get() = ImageIO.read(path.toFile())
    init {
        val file = path.toFile()
        val name = file.nameWithoutExtension
        //Check regex for the screenshot name format
        time = if ("[0-9]{4}-[0-9]{2}-[0-9]{2}_[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}".toRegex().containsMatchIn(name)) {
            if (file.extension.equals("png", ignoreCase = true)) {
                //Replace custom class with already existing class
                Time (
                    //Gets the time from the string
                    year = name.substring(0..3).toLong(),
                    month = name.substring(5..6).toByte(),
                    day = name.substring(8..9).toByte(),
                    hour = name.substring(11..12).toByte(),
                    minute = name.substring(14..15).toByte(),
                    second = name.substring(17..18).toByte(),
                )
            } else throw Exception("Invalid screenshot: Invalid file format")
        } else throw Exception("Invalid screenshot: Invalid time format")
    }

    /**
     * Returns "Screenshot: $time"
     */
    override fun toString() = "Screenshot: $time"
}