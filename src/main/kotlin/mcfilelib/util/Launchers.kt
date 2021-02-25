package mcfilelib.util

import div
import fillList
import mcfilelib.generic.Launcher
import mcfilelib.util.LauncherType.*
import java.nio.file.Path
import java.nio.file.Paths

object Launchers {
    val types = listOf (
        GDLAUNCHER,
        GDLAUNCHER_NEXT,
        VANILLA,
        MULTIMC,
        TECHNIC,
    )

    var allInstalledLaunchers: List<Launcher> = collectLaunchers()
        private set

    private fun collectLaunchers() = fillList<Launcher> {
        for (type in types) {
            val result = type.toLauncher()
            //result is null when the launcher doesn't exist
            if (result != null) add(result)
        }
    }

    fun refreshLaunchers() {
        allInstalledLaunchers = collectLaunchers()
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
            VANILLA -> (userHome/".minecraft")
            MULTIMC -> (userHome/".local"/"share"/"multimc")
            TECHNIC -> (userHome/".technic")
            GDLAUNCHER -> (userHome/".config"/"GDLauncher")
            GDLAUNCHER_NEXT -> (userHome/".config"/"gdlauncher_next")
            else -> null
        }
    }
    else -> null
}

fun LauncherType.toLauncher(): Launcher? {
    val path = this.toPath()
    return if (path != null && path.toFile().exists()) Launcher(path, this) else null
}