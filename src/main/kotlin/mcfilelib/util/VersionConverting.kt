package mcfilelib.util

import kmp_semver.SemVer
import kmp_semver.SemVerRange
import kmp_semver.contains
import kmp_semver.toSemVerOrNull

fun fromVersionToFormat(version: String?): Int? {
    return if (version != null) {
        val versionAsSemVer = version.toSemVerOrNull() ?: return null
        //Find the right format for the provided version
        when {
            SemVer("1.6.1") > SemVer(version) -> 0
            versionAsSemVer in versionRange("1.6.1", "1.8.9")!!   -> 1
            versionAsSemVer in versionRange("1.9", "1.10.2")!!    -> 2
            versionAsSemVer in versionRange("1.11", "1.12.2")!!   -> 3
            versionAsSemVer in versionRange("1.13", "1.14.4")!!   -> 4
            versionAsSemVer in versionRange("1.15", "1.16.1")!!   -> 5
            versionAsSemVer in versionRange("1.16.2", "1.16.4")!! -> 6
            versionAsSemVer >= SemVer("1.17") -> 7
            else -> null
        }
    } else null
}

fun fromFormatToRange(format: Int?, isResourcePack: Boolean): SemVerRange? {
    return when (format) {
        1 -> if (isResourcePack)    versionRange("1.6.1", "1.8.9")    else null
        2 -> if (isResourcePack)    versionRange("1.9", "1.10.2")     else null
        3 -> if (isResourcePack)    versionRange("1.11", "1.12.2")    else null
        4 ->                        versionRange("1.13", "1.14.4")
        5 ->                        versionRange("1.15", "1.16.1")
        6 ->                        versionRange("1.16.2", "1.16.4")
        7 ->                        versionRange("1.17", "2.0")
        else -> null
    }
}

fun isVersion(string: String): Boolean {
    var valid = false
    if ("." in string) {
        valid = true
        val parts = string.split(".")
        parts.forEach {
            if (it.toIntOrNull() == null) valid = false
        }
    }
    return valid
}