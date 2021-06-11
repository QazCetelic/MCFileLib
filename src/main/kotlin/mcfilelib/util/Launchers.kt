package mcfilelib.util

import mcfilelib.generic.Launcher
import mcfilelib.util.LauncherType.*
import neatlin.fillList
import neatlin.io.div
import neatlin.io.exists
import neatlin.io.toPath
import java.nio.file.Path

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

    /**
     * @return Boolean: If the installed launchers actually changed
     */
    fun refreshLaunchers(): Boolean {
        val newLauncherList = collectLaunchers()
        val changed = newLauncherList != allInstalledLaunchers
        allInstalledLaunchers = newLauncherList
        return changed
    }
}

fun Path.toLauncherType(): LauncherType {
    for (launcherType in Launchers.types) {
        val launcherObject = launcherType.toLauncher() ?: continue
        if (launcherObject.path in this) {
            return launcherType
        } else continue
    }
    return UNKNOWN
}

fun LauncherType.toPath(): Path? = when (os) {
    // TODO This isn't done yet, I need to know the paths that are used on other OS'
    OS.LINUX -> {
        val userHome = System.getProperty("user.home").toPath()
        when (this) {
            VANILLA -> (userHome/".minecraft")
            MULTIMC -> (userHome/".local"/"share"/"multimc")
            TECHNIC -> (userHome/".technic")
            GDLAUNCHER -> (userHome/".config"/"GDLauncher")
            GDLAUNCHER_NEXT -> (userHome/".config"/"gdlauncher_next")
            else -> null
        }
    }
    else -> TODO("Not implemented")
}

fun LauncherType.toLauncher(): Launcher? {
    return this.toPath()?.let {
        if (it.exists()) Launcher(it, this) else null
    }
}