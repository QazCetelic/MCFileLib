package main.classes.classes

import classes.Instance
import main.util.LauncherType
import main.util.Launchers
import main.util.Util.mayAppendSlash
import java.io.File
import java.nio.file.Path

class Launcher(val path: Path) {
    val launcherType = Launchers().fromPath(path.toString())
    val instances: List<Instance>
    init {
        val file = path.toFile()

        //Verify
        if (file.exists()) throw Exception("Invalid Launcher: Directory doesn't exist")
        if (launcherType == LauncherType.UNKNOWN) throw Exception("Invalid Launcher: Unknown launcherType")

        val foundInstances = ArrayList<Instance>()
        val instancesPath = this.path + launcherType.instanceFolder
        //Vanilla is structured as ONE instance, that's why the Launcher object uses it's own path to create an Instance object
        if (launcherType == LauncherType.VANILLA) {
            foundInstances[0] = Instance(this.path, LauncherType.VANILLA)
        }
        else {
            val instancesFolderFiles = file.listFiles()
            instancesFolderFiles?.forEach {
                if (it.name != "_MMC_TEMP" && it.isDirectory) foundInstances += Instance(it.toPath(), launcherType)
            }
        }
        instances = foundInstances
    }

    override fun toString(): String = "(${launcherType.normalName}: ${this.path})"
}