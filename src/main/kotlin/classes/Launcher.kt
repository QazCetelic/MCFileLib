package classes

import util.LauncherType
import util.Launchers
import util.FileEditable
import java.nio.file.Path

class Launcher(path: Path): FileEditable(path) {
    val type = Launchers.fromPath(path.toString())
    val instances: List<Instance>
    init {
        val file = path.toFile()

        //Verify
        if (!file.exists()) throw Exception("Invalid Launcher: Directory doesn't exist")
        if (type == LauncherType.UNKNOWN) throw Exception("Invalid Launcher: Unknown launcherType")

        val foundInstances = ArrayList<Instance>()
        val instancesPath = this.path.resolve(type.instanceFolder)
        //Vanilla is structured as ONE instance, that's why the Launcher object uses it's own path to create an Instance object
        if (type == LauncherType.VANILLA) {
            foundInstances.add(Instance(this.path, LauncherType.VANILLA))
        }
        else {
            val instancesFolderFiles = instancesPath.toFile().listFiles()
            instancesFolderFiles?.forEach {
                //Prevents MultiMC's folder sneaking in
                if (it.name != "_MMC_TEMP" && it.isDirectory) foundInstances += Instance(it.toPath(), type)
            }
        }
        instances = foundInstances
    }

    override fun toString(): String = "(${type.displayName}: ${this.path})"
}