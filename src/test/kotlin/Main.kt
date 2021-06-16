import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import mcfilelib.generic.ResourcePack
import mcfilelib.util.Launchers
import mcfilelib.util.file_entry.assets.blockstate.BlockStateCondition
import neatlin.io.toPath
import java.util.zip.ZipFile

//This is used for testing stuff
fun main() {
    val resourcePack = ResourcePack("/home/qaz/Projects/Programming/MCFileLib/src/test/kotlin/resources/resourcepacks/Faithfull x32".toPath())
    for (contentGroupEntry in resourcePack.contentGroupEntries.values) {
        val blockstates = contentGroupEntry.blockstates
        if ("potted_brown_mushroom" in blockstates) {
            println(blockstates["potted_brown_mushroom"])
        }
        if ("spruce_trapdoor" in blockstates) {
            println(blockstates["spruce_trapdoor"])
        }
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