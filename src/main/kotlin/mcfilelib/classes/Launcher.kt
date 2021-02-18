package mcfilelib.classes

import mcfilelib.util.FileEditable
import mcfilelib.util.LauncherType
import mcfilelib.util.div
import mcfilelib.util.toLauncherType
import java.nio.file.Path

class Launcher(
    //Passed on to FileEditable
    path: Path,
    //Can be given but can also be figured out by automatically
    val type: LauncherType = path.toLauncherType()
): FileEditable(path) {
    val instances: List<Instance>
    init {
        val file = path.toFile()

        //Verify
        if (!file.exists()) throw Exception("Invalid Launcher: Directory doesn't exist")
        if (type == LauncherType.UNKNOWN) throw Exception("Invalid Launcher: Unknown launcherType")

        instances = mutableListOf<Instance>().also { list ->
            val instancesPath = this.path/type.instanceFolder
            val instancesFolderFiles = instancesPath.toFile().listFiles()
            instancesFolderFiles?.forEach {
                //Prevents MultiMC's folder sneaking in
                if (it.name != "_MMC_TEMP" && it.isDirectory) list += Instance(it.toPath(), type)
            }
        }
    }

    override fun toString(): String = "(${type.displayName}: ${this.path})"
}