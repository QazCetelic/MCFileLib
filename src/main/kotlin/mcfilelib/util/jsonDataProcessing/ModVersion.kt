package mcfilelib.util.jsonDataProcessing

import kmp_semver.SemVer
import kmp_semver.SemVerRange
import kmp_semver.toSemVer

// TODO clean this up and figure out a way to do the range validity in a cleaner way
// TODO replace startVersion and endVersion with a semverRange
/**
 * Object for dealing with mod versions
 * [raw] is the version string that was found,
 * [versionRange] is a semantic version range for Minecraft version the mods work with
 */
// TODO IT MAKES NO SENSE THAT THIS IS THE VERSION THE GAME WORKS WITH BECAUSE IT SHOULD BE THE MOD VERSION ITSELF!
class ModVersion {
    /**
     * The raw version string that was found
     */
    val raw: String

    /**
     * Semantic versioning range of the Minecraft version the mod works with
     */
    val versionRange: SemVerRange?

    /**
     * If a version string was actually found
     */
    val found: Boolean

    constructor(string: String?) {
        raw = string ?: ""
        if (string != null) {
            found = true
            /*
                Examples:
                1. "1.16.3,1.17)" -> "1.17"
                2. "stupidVersionName" -> "stupidVersionName"
                3. "(1.12.2)" -> "1.12.2"
            */
            val processedString = string.removeSuffix(")").removePrefix("(")
            // Takes the latest version if it's a range (so if it has one comma)
            if (processedString.filter { it == '-' }.length == 1) processedString.split("-").let {
                val start = it[0].toSemVer()
                val end = it[1].toSemVer()

                // Just returns the string if it's not a valid semantic version
                if (start == null || end == null) {
                    // RANGE IS INVALID
                    versionRange = null
                }
                else {
                    // RANGE IS VALID
                    versionRange = SemVerRange(start, end)
                }
            }
            else {
                // RANGE IS INVALID
                versionRange = null
            }
        }
        else {
            // RANGE IS INVALID
            found = false
            versionRange = null
        }
    }
}