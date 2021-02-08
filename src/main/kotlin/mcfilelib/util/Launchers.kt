package mcfilelib.util

import mcfilelib.classes.Launcher
import java.nio.file.Path
import java.nio.file.Paths

object Launchers {
    val types = listOf (
        LauncherType.GDLAUNCHER,
        LauncherType.GDLAUNCHER_NEXT,
        LauncherType.VANILLA,
        LauncherType.MULTIMC,
        LauncherType.TECHNIC,
    )

    fun fromPathToType(path: Path): LauncherType {
        for (launcher in types) {
            val launcherObject = fromTypeToLauncher(launcher) ?: continue
            if (launcherObject.path in path) {
                return launcher
            } else continue
        }
        return LauncherType.UNKNOWN
    }

    fun fromTypeToLauncher(type: LauncherType): Launcher? {
        //Returns Launcher enum or null if the it doesn't exist
        fun toLauncherIfExists(path: Path): Launcher? {
            return if (path.toFile().exists()) {
                Launcher(path, type)
            } else null
        }

        return when (OSInfo.os) {
            // TODO This isn't done yet, I need to know the paths that are used on other OS'
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
    }

    // Stores results after first time because the process is relatively intensive due to IO tasks
    val allInstalledLaunchers: List<Launcher> by lazy {
        val installedLaunchers = ArrayList<Launcher>()
        (
                types
                // temporary disabled GDLAUNCHER, todo re-enable later
                //- LauncherType.GDLAUNCHER
                //- LauncherType.GDLAUNCHER_NEXT
                ).forEach {
                val result = fromTypeToLauncher(it)
                //result is null when the launcher doesn't exist
                if (result != null) installedLaunchers.add(result)
            }
        installedLaunchers.toList()
    }
}