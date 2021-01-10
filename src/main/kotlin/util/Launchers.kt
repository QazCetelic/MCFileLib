package util

import classes.Launcher
import main.util.OSInfo
import main.util.Util.mayAppendSlash
import java.io.File
import java.nio.file.Paths

object Launchers {
    fun fromPath(path: String): LauncherType {
        val file = File(path)
        return if (file.isDirectory) {
            when (file.name) {
                "gdlauncher" -> {
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

    fun fromType(type: LauncherType): Launcher? = when (OSInfo.os) {
        //todo: This isn't done yet
        OSInfo.OS.LINUX -> {
            val userHome = mayAppendSlash(System.getProperty("user.home"))
            when (type) {
                LauncherType.VANILLA -> toLauncherIfExists("$userHome/.minecraft/")
                LauncherType.MULTIMC -> toLauncherIfExists("$userHome/.local/share/multimc/")
                LauncherType.TECHNIC -> toLauncherIfExists("$userHome/.technic/")
                else -> null
            }
        }
        else -> null
    }

    private fun toLauncherIfExists(string: String): Launcher? {
        return if (File(string).exists()) {
            Launcher(Paths.get(string))
        } else null
    }

    fun getAll(): List<Launcher> {
        val launchers = ArrayList<Launcher>()
        listOf (
            LauncherType.VANILLA,
            LauncherType.MULTIMC,
            //LauncherType.TECHNIC,
        ).forEach {
            val result = fromType(it)
            if (result != null) {
                println("Adding ${result.path} as $it")
                launchers.add(result)
            }
        }
        return launchers.toList()
    }
}