package mcfilelib.util

import mcfilelib.classes.Launcher
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

object Launchers {
    fun fromPath(path: String): LauncherType {
        val file = File(path)
        return if (file.isDirectory) {
            when (file.name) {
                "GDLauncher" -> LauncherType.GDLAUNCHER
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
        //todo: This isn't done yet, I need to know the paths that are used on other OS'
        OSInfo.OS.LINUX -> {
            val userHome = Paths.get(System.getProperty("user.home"))
            when (type) {
                LauncherType.VANILLA -> toLauncherIfExists(userHome/".minecraft")
                LauncherType.MULTIMC -> toLauncherIfExists(userHome/".local"/"share"/"multimc")
                LauncherType.TECHNIC -> toLauncherIfExists(userHome/".technic")
                LauncherType.GDLAUNCHER -> toLauncherIfExists(userHome/".config"/"GDLauncher")
                LauncherType.GDLAUNCHER_NEXT -> toLauncherIfExists(userHome/".config"/"gdlauncher_next")
                else -> null
            }
        }
        else -> null
    }

    //Returns Launcher enum or null if the it doesn't exist
    private fun toLauncherIfExists(path: Path): Launcher? {
        return if (path.toFile().exists()) {
            Launcher(path)
        } else null
    }

    fun getAllInstalled(): List<Launcher> {
        val launchers = ArrayList<Launcher>()
        listOf (
            LauncherType.VANILLA,
            LauncherType.MULTIMC,
            LauncherType.TECHNIC,
            //LauncherType.GDLAUNCHER,
            //LauncherType.GDLAUNCHER_NEXT  todo fix GDLauncher detection
        ).forEach {
            val result = fromType(it)
            //result is null when the launcher doesn't exist
            if (result != null) launchers.add(result)
        }
        return launchers.toList()
    }
}