package mcfilelib.util

import java.io.IOException
import java.lang.Exception
import java.util.*


object OSInfo {
    var os: OS

    enum class OS {
        WINDOWS, LINUX, UNIX, SOLARIS, MAC, OTHER;
        lateinit var version: String

        override fun toString(): String = os.name + ": " + os.version
    }

    init {
        try {
            var osString = System.getProperty("os.name") ?: throw IOException("os.name not found")
            osString = osString.toLowerCase(Locale.ENGLISH)
            //Find out which version is used
            os = when {
                //todo: this is untested except for the linux entry, requires testing
                //Linux
                osString.contains("linux") -> OS.LINUX
                //Windows
                osString.contains("windows") -> OS.WINDOWS
                //Unix
                osString.contains("mpe/ix") ||
                osString.contains("freebsd") ||
                osString.contains("irix") ||
                osString.contains("digital unix") ||
                osString.contains("unix") -> OS.UNIX
                //Mac
                osString.contains("mac os") -> OS.MAC
                //Solaris
                osString.contains("sun os") ||
                osString.contains("sunos") ||
                osString.contains("solaris") -> OS.SOLARIS
                //In case everything else fails
                else -> OS.OTHER
            }
            //Gets the version of the os
            os.version = System.getProperty("os.version")
        } catch (ex: Exception) {
            os = OS.OTHER
            os.version = "Failed to get version"
        }
    }

    //The os enum class already has a special toString fun, this calls that in case people use toString on the wrong class or in this case object
    override fun toString(): String = os.toString()
}

//Heavily modified version of https://memorynotfound.com/detect-os-name-version-java/