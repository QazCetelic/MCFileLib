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

    // Stores results after first time because the process is relatively intensive due to IO tasks
    val allInstalledLaunchers: List<Launcher> by lazy {
        val installedLaunchers = mutableListOf<Launcher>()
        types.forEach {
            val result = it.toLauncher()
            //result is null when the launcher doesn't exist
            if (result != null) installedLaunchers.add(result)
        }
        installedLaunchers.toList()
    }
}

fun Path.toLauncherType(): LauncherType {
    for (launcherType in Launchers.types) {
        val launcherObject = launcherType.toLauncher() ?: continue
        if (launcherObject.path in this) {
            return launcherType
        } else continue
    }
    return LauncherType.UNKNOWN
}

fun LauncherType.toPath(): Path? = when (OSInfo.os) {
    // TODO This isn't done yet, I need to know the paths that are used on other OS'
    OSInfo.OS.LINUX -> {
        val userHome = Paths.get(System.getProperty("user.home"))
        when (this) {
            LauncherType.VANILLA -> (userHome/".minecraft")
            LauncherType.MULTIMC -> (userHome/".local"/"share"/"multimc")
            LauncherType.TECHNIC -> (userHome/".technic")
            LauncherType.GDLAUNCHER -> (userHome/".config"/"GDLauncher")
            LauncherType.GDLAUNCHER_NEXT -> (userHome/".config"/"gdlauncher_next")
            else -> null
        }
    }
    else -> null
}

fun LauncherType.toLauncher(): Launcher? {
    val path = this.toPath()
    return if (path != null && path.toFile().exists()) {
        Launcher(path, this)
    } else null
}