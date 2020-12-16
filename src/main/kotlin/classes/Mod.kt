package classes

import java.awt.Image
import java.io.File
import java.util.zip.ZipFile
import javax.imageio.ImageIO

class Mod(val path: String) {
    var license: String? = null
    val disabled: Boolean
    val name: String
    val dependencies: List<String>
    val icons: List<Image>
    val type: ModType
    init {
        val file = File(path)
        if (!file.exists()) throw Exception("Invalid mod: File doesn't exist")

        //Temporary arrays so that the final result can be list so that it won't be
        val foundDependencies = ArrayList<String>()
        val foundIcons = ArrayList<Image>()

        if (path.contains(".jar")) {
            if (path.contains(".")) {
                //Process the name
                var foundName = path.split(".")[0]
                if (foundName.endsWith("-1")) foundName = foundName.removeSuffix("1")
                if (foundName.endsWith("mc1")) foundName
                foundName = foundName.removeSurrounding("-")
                name = foundName

                disabled = file.name.endsWith(".disabled")

                //Looks through the data of the mod itself
                //todo: process more data than just dependency's and logos
                val zipFile = ZipFile(path)
                for (entry in zipFile.entries()) {
                    //Finds dependencies by going through the jars
                    if (entry.name.startsWith("META-INF/jars/") && entry.name.endsWith(".jar")) {
                        //Removes the "META-INF/jars/" and ".jar" parts
                        var name = entry.name.drop(14).dropLast(4)
                        //Removes the plus and 11 random characters
                        if ("\\+([0-9]|[A-z]){10}".toRegex().containsMatchIn(name)) name = name.dropLast(12)
                        //Removes the dot at the end that sometimes exists between the version and random characters
                        if (name.endsWith('.')) name = name.dropLast(1)
                        //And then adds it to the temporary array
                        foundDependencies += name
                    }
                    //Finds all possible icons
                    if (entry.name.endsWith("icon.png")) {
                        foundIcons += ImageIO.read(zipFile.getInputStream(entry))
                    }
                    //Finds the license
                    if (entry.name == "LICENSE") {
                        if (license == null) license = String(zipFile.getInputStream(entry).readBytes())
                    }
                }
            }
            else {
                name = "Error"
                disabled = false
            }
        } else throw Exception("Invalid mod: Not a jar")

        //sets ModType to unknown for the time being, todo change it
        type = ModType.UNKNOWN

        //Assigns the temporary arrays to the final lists
        dependencies = foundDependencies
        icons = foundIcons
    }

    //todo implement type checking
    enum class ModType {
        FABRIC,
        FORGE,
        UNKNOWN
    }
}