package testing

import classes.Instance
import util.LauncherType
import java.nio.file.Paths

fun main() {
    val instance = Instance(Paths.get("/home/qaz/.local/share/multimc/instances/1.16.1 Vanilla"), LauncherType.MULTIMC)
    instance.configs.entries.forEach {
        println("${it.key} : ${it.value.name}")
    }
}