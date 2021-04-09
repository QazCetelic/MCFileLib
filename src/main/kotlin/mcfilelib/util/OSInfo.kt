package mcfilelib.util

import mcfilelib.util.OSInfo.OS.*
import java.util.*


object OSInfo {
    var os: OS

    init {
        val osString = System.getProperty("os.name") ?.apply { toUpperCase(Locale.ENGLISH) }
        //Find out which version is used
        this.os = when {
            osString.isNullOrBlank() -> OTHER
            //TODO: This is untested except for the linux entry, requires testing
            //Linux
            "LINUX" in osString -> LINUX
            //Windows
            "WINDOWS" in osString -> WINDOWS
            //Unix
            "MPE/IX" in osString ||
            "FREEBSD" in osString ||
            "IRIX" in osString ||
            "UNIX" in osString -> UNIX
            //Mac
            "MAC OS" in osString -> MAC
            //Solaris
            "SUN OS" in osString ||
            "SUNOS" in osString ||
            "SOLARIS" in osString -> SOLARIS
            //In case everything else fails
            else -> OTHER
        }
        //Gets the version of the os
        os.version = System.getProperty("os.version")
    }

    enum class OS {
        WINDOWS, LINUX, UNIX, SOLARIS, MAC, OTHER;
        lateinit var version: String

        override fun toString(): String = os.name + ": " + os.version
    }

    //The os enum class already has a special toString fun, this calls that in case people use toString on the wrong class or in this case object
    override fun toString(): String = os.toString()
}

//Heavily modified version of https://memorynotfound.com/detect-os-name-version-java/