package mcfilelib.generic

import neatlin.*
import mcfilelib.util.LauncherType
import mcfilelib.util.toLauncherType
import java.nio.file.Path
import mcfilelib.util.LauncherType.*

class Launcher(
    //Passed on to FileEditable
    val path: Path,
    //Can be given but can also be figured out by automatically
    val type: LauncherType = path.toLauncherType()
) {
    val instances: List<Instance>
    init {
        val file = path.toFile()

        when {
            !file.exists() -> throw Exception("Invalid Launcher: Directory doesn't exist")
            type == UNKNOWN -> throw Exception("Invalid Launcher: Unknown launcherType")
        }

        instances = fillList {
            (path/type.instanceFolder).toFile().listFiles()?.forEach {
                //Prevents MultiMC's folder sneaking in
                if (it.name != "_MMC_TEMP" && it.isDirectory) add(Instance(it.toPath(), type))
            }
        }
    }

    override fun toString(): String = "(${type.displayName}: ${this.path})"
}