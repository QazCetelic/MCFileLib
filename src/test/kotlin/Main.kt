import mcfilelib.generic.ResourcePack
import neatlin.io.Locations
import neatlin.io.div
import neatlin.io.toPath
import tests.asserting.*
import tests.printing.ocurrences
import tests.printing.resourcePacks

//This is used for testing stuff
fun main() {
    val resourcePack = ResourcePack("/home/qaz/Projects/Programming/MCFileLib/src/test/kotlin/resources/resourcepacks/Faithfull x32".toPath())
    for (contentGroupEntry in resourcePack.contentGroupEntries.values) {
        println(contentGroupEntry)
    }

    /*
    val path = Locations.HOME/"Projects/Programming/MCFileLib/src/test/kotlin/resources"
    mods(path/"mods")
    worlds(path/"worlds")
    screenshots(path/"screenshots")
    instances(path/"instances")
    launchers(path/"launchers")
    resourcepacks(path/"resourcepacks")

    // This prints out data that is collected for debugging
    if (false) {
        ocurrences()
        resourcePacks()
    }
     */
}