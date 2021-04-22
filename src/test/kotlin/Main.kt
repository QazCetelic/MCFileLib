import neatlin.div
import neatlin.toPath
import tests.asserting.instances
import tests.asserting.launchers
import tests.asserting.mods
import tests.asserting.*
import tests.printing.ocurrences
import tests.printing.resourcePacks

//This is used for testing stuff
fun main() {
    val resourcesPath = "/home/qaz/Projects/Programming/MCFileLib/src/test/kotlin/resources".toPath()
    mods(resourcesPath/"mods")
    worlds(resourcesPath/"worlds")
    screenshots(resourcesPath/"screenshots")
    instances(resourcesPath/"instances")
    launchers(resourcesPath/"launchers")
    resourcepacks(resourcesPath/"resourcepacks")

    // This prints out data that is collected for debugging
    if (false) {
        ocurrences()
        resourcePacks()
    }
}