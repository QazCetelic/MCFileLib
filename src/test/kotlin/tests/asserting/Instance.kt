package tests.asserting

import mcfilelib.generic.Instance
import mcfilelib.util.LauncherType
import neatlin.div
import java.nio.file.Path
import kotlin.system.measureTimeMillis

fun instances(instances: Path) {
    val multiMCPackPath = instances/"FTB Sky Adventures"
    val time = measureTimeMillis {
        val instance = Instance(multiMCPackPath, LauncherType.MULTIMC)
    }
    println("Instance test finished in ${time}ms")
}