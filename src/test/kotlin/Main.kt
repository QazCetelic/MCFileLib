package testing

import classes.Instance
import main.util.LauncherType
import java.nio.file.Paths

fun main() {
    val instance = Instance(Paths.get("/home/qaz/.local/share/multimc/instances/1.16.1 Vanilla"), LauncherType.MULTIMC)
    instance.mods.forEach {
        println("${it.description} ${
            if (it.disabled) " (Disabled)"
            else ""
        }")
    }
}