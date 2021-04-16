package mcfilelib.util

import mcfilelib.util.OS.*
import java.util.*

val os: OS = run {
    val osString = System.getProperty("os.name") ?.apply { toUpperCase(Locale.ENGLISH) }
    //Find out which version is used
    when {
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
    }.also {
        //Gets the version of the os
        it.version = System.getProperty("os.version")
    }
}

enum class OS {
    WINDOWS, LINUX, UNIX, SOLARIS, MAC, OTHER;
    lateinit var version: String

    override fun toString(): String = os.name + ": " + os.version
}