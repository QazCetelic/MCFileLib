package testing

import div
import mcfilelib.generic.*
import mcfilelib.util.LauncherType
import toi8
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.system.measureTimeMillis
import kotlin.test.assertTrue

fun tests(projectPathString: String) {
    val resourcesPath = Paths.get(projectPathString)/"src"/"test"/"kotlin"/"resources"
    testMods(resourcesPath/"mods")
    testWorld(resourcesPath/"worlds")
    testScreenshots(resourcesPath/"screenshots")
    testInstance(resourcesPath/"instances")
    testLaunchers(resourcesPath/"launchers")

    /*
    ocurrences()
    resourcePacks()
    screenshots()
     */
}

fun testMods(mods: Path) {
    // FORGE MODS
        // This mod is a bit newer…
        val jeiMod = Mod(mods/"jei_1.12.2-4.16.1.301.jar")
        assertTrue { jeiMod.name == "Just Enough Items" }
        assertTrue { jeiMod.modVersion == "4.16.1.301" }
        assertTrue { jeiMod.mcVersion == "1.12.2" }
        assertTrue { "mezz" in jeiMod.authors }
        assertTrue { !jeiMod.disabled }
        assertTrue { jeiMod.license == null }
        // …and this one a bit older to test if it works with older formats…
        val mantleMod = Mod(mods/"Mantle-1.8.9-0.9.2.jar")
        assertTrue { mantleMod.name == "Mantle" }
        assertTrue { mantleMod.modVersion == "0.9.2" }
        assertTrue { mantleMod.mcVersion == "1.8.9" }
        assertTrue { "boni" in mantleMod.authors && "progWML6" in mantleMod.authors && "Alexbegt" in mantleMod.authors }
        assertTrue { !mantleMod.disabled }
        assertTrue { mantleMod.license == null }
        // …and this one because it has an dependency
        val immersiveEngineeringMod = Mod(mods/"ImmersiveEngineering-1.16.5-4.2.1-131.jar")
        assertTrue { immersiveEngineeringMod.name == "Immersive Engineering" }
        assertTrue { immersiveEngineeringMod.modVersion == "1.16.5-4.2.1-131" }
        assertTrue { immersiveEngineeringMod.mcVersion == "1.16.5" }
        assertTrue { "BluSunrize" in immersiveEngineeringMod.authors && "Damien A.W. Hazard" in immersiveEngineeringMod.authors }
    // FABRIC MODS
        // none
}

fun testWorld(worlds: Path) {
    val time = measureTimeMillis {
        val world = World(worlds/"New World")
        assertTrue { world.dataPacks.size == 1 }
        assertTrue { world.icon?.height == 64 }
        assertTrue { world.name == "New World" }
        // Datapacks
        val datapack = world.dataPacks[0]
        assertTrue { datapack.name == "Compass+Tracker+v2" }
        assertTrue { datapack.format == 6 }
        assertTrue { datapack.versionRange.toString() == "1.16.2-1.16.4" }
        assertTrue { datapack.description == "Track up to 50 entities with a specialized compass" }
        assertTrue { datapack.icon != null }
        assertTrue { datapack.modSupport == null }
    }
    //println("Worlds test finished in ${time}ms")
}

fun testScreenshots(screenshots: Path) {
    val time = measureTimeMillis {
        val screenshot1 = Screenshot(screenshots/"2021-03-02_15.59.30.png")
        assertTrue { screenshot1.time.year == 2021L && screenshot1.time.month == 3.toi8() && screenshot1.time.day == 2.toi8() && screenshot1.time.hour == 15.toi8() && screenshot1.time.minute == 59.toi8() && screenshot1.time.second == 30.toi8() }
        val screenshot2 = Screenshot(screenshots/"2021-03-02_15.59.43.png")
        assertTrue { screenshot2.time.year == 2021L && screenshot2.time.month == 3.toi8() && screenshot2.time.day == 2.toi8() && screenshot2.time.hour == 15.toi8() && screenshot2.time.minute == 59.toi8() && screenshot2.time.second == 43.toi8() }
    }
    //println("Screenshots test finished in ${time}ms")
}

fun testInstance(instances: Path) {
    val multiMCPackPath = instances/"FTB Sky Adventures"
    val time = measureTimeMillis {
        val instance = Instance(multiMCPackPath, LauncherType.MULTIMC)
    }
    //println("Instance test finished in ${time}ms")
}

fun testLaunchers(launchers: Path) {
    val time = measureTimeMillis {
        val multiMC = Launcher(launchers/"multimc", LauncherType.MULTIMC)
        val authors = mutableListOf<String>()
        for (instance in multiMC.instances) for (mod in instance.mods) for (author in mod.authors) {
            authors += author
        }

        val authorOccurrences = authors.groupingBy { it }.eachCount().filter { it.value > 1 }

        println(authorOccurrences["Stan Hebben"])
        println(authorOccurrences["BlayTheNinth"])
        println(authorOccurrences["McJty"])
    }
    //println("Launcher test finished in ${time}ms")
}