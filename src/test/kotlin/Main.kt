package testing

import classes.Instance
import util.LauncherType
import java.nio.file.Paths

//This is used for testing stuff
fun main() {
    val instance = Instance(Paths.get("/home/qaz/.local/share/multimc/instances/1.16.1 Vanilla"), LauncherType.MULTIMC)
    println(instance.name)

    instance.resourcepacks.forEach {
        val result = it.contentGroupEntries["minecraft"]
        println("This resourcepack ${
            if (result != null && result.includesOptifine) "does"
            else "doesn't"
        } include Optifine support.")
    }
}