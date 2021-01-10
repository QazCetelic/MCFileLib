package testing

import util.LauncherType
import util.Launchers

//This is used for testing stuff
fun main() {
    val launchers = Launchers.getAll()
    launchers.forEach { launcher ->
        launcher.instances.forEach { instance ->
            instance.resourcepacks.forEach { resourcepack ->
                val result = resourcepack.contentGroupEntries["minecraft"]
                if (result != null) {
                    println("${resourcepack.name} (${resourcepack.description}) ${
                        if (result.includesOptifine) "does"
                        else "doesn't"
                    } include Optifine support.")
                }
            }
            instance.mods.forEach { mod ->
                println("The mod ${mod.name}'s description is: ${mod.description}")
            }
        }
    }
}