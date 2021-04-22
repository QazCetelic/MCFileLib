package tests.asserting

import mcfilelib.generic.Launcher
import mcfilelib.util.LauncherType
import neatlin.div
import java.nio.file.Path
import kotlin.system.measureTimeMillis
import kotlin.test.assertTrue

/**
 * This might fail because it's made for my local setup
 */
fun launchers(launchers: Path) {
    val time = measureTimeMillis {
        val multiMC = Launcher(launchers/"multimc", LauncherType.MULTIMC)
        val authors = mutableListOf<String>()
        for (instance in multiMC.instances) for (mod in instance.mods) for (author in mod.authors) {
            authors.add(author)
        }
        assertTrue { authors.size == 961 }

        val authorOccurrences = authors.groupingBy{ it }.eachCount().filter { it.value > 1 }
        assertTrue { authorOccurrences["StanHebben"]       == 3 }
        assertTrue { authorOccurrences["Jimeo Wan"]        == 4 }
        assertTrue { authorOccurrences["Daniel Ratcliffe"] == 3 }
        assertTrue { authorOccurrences["Aaron Mills"]      == 2 }
        assertTrue { authorOccurrences["Ollie Lansdell"]   == 4 }
        assertTrue { authorOccurrences["Pamela Collins"]   == 3 }
    }
    println("Launcher test finished in ${time}ms")
}