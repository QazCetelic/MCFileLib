package tests.asserting

import mcfilelib.generic.Mod
import neatlin.div
import neatlin.io.div
import java.nio.file.Path
import kotlin.test.assertTrue

fun mods(mods: Path) {
    // FORGE MODS
    // This mod is a bit newer…
    val jeiMod = Mod(mods/"jei_1.12.2-4.16.1.301.jar")
    assertTrue { jeiMod.name == "Just Enough Items" }
    assertTrue { jeiMod.modVersion.raw == "4.16.1.301" }
    assertTrue { jeiMod.mcVersion.raw == "1.12.2" }
    assertTrue { "mezz" in jeiMod.authors }
    assertTrue { !jeiMod.disabled }
    assertTrue { jeiMod.license == null }
    // …and this one a bit older to test if it works with older formats…
    val mantleMod = Mod(mods/"Mantle-1.8.9-0.9.2.jar")
    assertTrue { mantleMod.name == "Mantle" }
    assertTrue { mantleMod.modVersion.raw == "0.9.2" }
    assertTrue { mantleMod.mcVersion.raw == "1.8.9" }
    assertTrue { "boni" in mantleMod.authors && "progWML6" in mantleMod.authors && "Alexbegt" in mantleMod.authors }
    assertTrue { !mantleMod.disabled }
    assertTrue { mantleMod.license == null }
    // …and this one because it has an dependency
    val immersiveEngineeringMod = Mod(mods/"ImmersiveEngineering-1.16.5-4.2.1-131.jar")
    assertTrue { immersiveEngineeringMod.name == "Immersive Engineering" }
    assertTrue { immersiveEngineeringMod.modVersion.raw == "1.16.5-4.2.1-131" }
    assertTrue { immersiveEngineeringMod.mcVersion.raw == "1.16.5" }
    assertTrue { "BluSunrize" in immersiveEngineeringMod.authors && "Damien A.W. Hazard" in immersiveEngineeringMod.authors }
    // FABRIC MODS
    // A newer create mod
    val createMod = Mod(mods/"create-mc1.16.5_v0.3.1a.jar")
    assertTrue { createMod.name == "Create" }
    assertTrue { createMod.modVersion.raw == "0.3.1a" }
    assertTrue { createMod.mcVersion.raw == "1.16.3,1.17)" }
    assertTrue { "simibubi" in createMod.authors }
}