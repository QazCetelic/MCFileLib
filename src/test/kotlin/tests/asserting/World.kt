package tests.asserting

import mcfilelib.generic.World
import neatlin.div
import neatlin.io.div
import java.nio.file.Path
import kotlin.system.measureTimeMillis
import kotlin.test.assertTrue

fun worlds(worlds: Path) {
    val time = measureTimeMillis {
        val world = World(worlds/"New World")
        assertTrue { world.dataPacks.size == 1 }
        assertTrue { world.icon?.height == 64 }
        assertTrue { world.name == "New World" }
        // Datapacks
        val datapack = world.dataPacks[0]
        assertTrue { datapack.name == "Compass+Tracker+v2" }
        assertTrue { datapack.format == 6 }
        assertTrue { datapack.versionRange!!.start.toString() == "1.16.2" && datapack.versionRange!!.end.toString() == "1.16.4" }
        assertTrue { datapack.description == "Track up to 50 entities with a specialized compass" }
        assertTrue { datapack.icon != null }
        assertTrue { datapack.modSupport == null }
    }
    println("Worlds test finished in ${time}ms")
}