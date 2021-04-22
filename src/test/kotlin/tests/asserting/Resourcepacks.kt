package tests.asserting

import mcfilelib.generic.ResourcePack
import neatlin.div
import java.nio.file.Path
import kotlin.system.measureTimeMillis
import kotlin.test.assertTrue

fun resourcepacks(packs: Path) {
    val time = measureTimeMillis {
        ResourcePack(packs/"Faithfull x32.zip").let {
            assertTrue { it.description == "§bWebsite §8> §chttps://faithful.team\n§bAuthor §8> §exMrVizzy" }
            assertTrue { it.format == 7 }
            assertTrue { it.icon != null }
            assertTrue { it.name == "Faithfull x32" }
            assertTrue { it.versionRange.toString() == "1.17.0..2.0.0" }
        }

        ResourcePack(packs/"Faithfull x32").let {
            assertTrue { it.description == "§bWebsite §8> §chttps://faithful.team\n§bAuthor §8> §exMrVizzy" }
            assertTrue { it.format == 7 }
            assertTrue { it.icon != null }
            assertTrue { it.name == "Faithfull x32" }
            assertTrue { it.versionRange.toString() == "1.17.0..2.0.0" }
            assertTrue { it.modSupport == true }
            // TODO test contentGroup entries
        }
        ResourcePack(packs/"Unity-1.16.X-Base-2.4.0.zip").let {
            assertTrue { it.description == "Experimental Beauty\n§r§bNow with more variants!" }
            assertTrue { it.format == 6 }
            assertTrue { it.icon != null }
            assertTrue { it.name == "Unity-1.16.X-Base-2.4.0" }
            assertTrue { it.versionRange.toString() == "1.16.2..1.16.4" }
        }
        ResourcePack(packs/"Unity-1.16.X-Base-2.4.0 (copy).zip").let {
            assertTrue { it.description == "Experimental Beauty\n§r§bNow with more variants!" }
            assertTrue { it.format == 6 }
            assertTrue { it.icon != null }
            assertTrue { it.name == "Unity-1.16.X-Base-2.4.0 (copy)" }
            assertTrue { it.versionRange.toString() == "1.16.2..1.16.4" }
        }
    }
    println("ResourcePack test finished in ${time}ms")
}