package main.util

import main.util.Util.mayAppendSlash
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

class Launchers {
    //Returns null if path is invalid
    fun fromPath(path: String): LauncherType {
        val file = File(path)
        return if (file.isDirectory) {
            when (file.name) {
                "gdlauncher" -> {
                    //Checks if it's not the second folder in the GDLauncher folder ("../GDLauncher/GDLauncher/" instead of "../GDLauncher/")
                    //todo fix caps, it might be gdlauncer instead of GDLauncher
                    if (file.path.removeSuffix("/GDLauncher").endsWith("GDLauncher")) LauncherType.UNKNOWN
                    else LauncherType.GDLAUNCHER
                }
                "gdlauncher_next" -> LauncherType.GDLAUNCHER_NEXT
                ".minecraft" -> LauncherType.VANILLA
                "multimc" -> LauncherType.MULTIMC
                ".technic" -> LauncherType.TECHNIC
                //In case a path to an invalid folder was given
                else -> LauncherType.UNKNOWN
            }
        } else LauncherType.UNKNOWN
    }

    fun fromType(type: LauncherType): Path? = when (OSInfo.os) {
        //todo: This isn't done yet
        OSInfo.OS.LINUX -> {
            val userHome = mayAppendSlash(System.getProperty("user.home"))
            when (type) {
                LauncherType.VANILLA -> ifExists("$userHome/.minecraft/")
                LauncherType.MULTIMC -> ifExists("$userHome/.local/share/multimc/")
                LauncherType.TECHNIC -> ifExists("$userHome/.technic/")
                else -> null
            }
        }
        else -> null
    }

    private fun ifExists(string: String) = if (File(string).exists()) Paths.get(string) else null
}