package testing

import classes.Instance
import com.google.gson.Gson
import com.google.gson.JsonObject
import util.LauncherType
import java.nio.file.Paths

//This is used for testing stuff
fun main() {
    val instance = Instance(Paths.get("/home/qaz/.local/share/multimc/instances/1.16.1 Vanilla"), LauncherType.MULTIMC)
    println(instance.name)
    val allConfigs = instance.configs.getAll()
    if ("modmenu.json" in allConfigs) {
        val modMenuConfig = allConfigs["modmenu.json"]!!.asJsonObject()
        println(modMenuConfig["sorting"].asString)
    }
}