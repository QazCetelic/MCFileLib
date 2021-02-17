package mcfilelib.util

import com.vdurmont.semver4j.Semver

fun fromVersionToFormat(version: String?): Int? {
    return if (version != null) {
        //Checks if it actually contains numbers, and returns -1 if it doesn't
        if (!version.contains(Regex("[0-9]"))) null
        //Find the right format for the provided version
        else when {
            Semver("1.6.1") > Semver(version, Semver.SemverType.LOOSE) -> 0
            version in VersionRange("1.6.1", "1.8.9") -> 1
            version in VersionRange("1.9", "1.10.2") -> 2
            version in VersionRange("1.11", "1.12.2") -> 3
            version in VersionRange("1.13", "1.14.4") -> 4
            version in VersionRange("1.15", "1.16.1") -> 5
            version in VersionRange("1.16.2", "1.16.4") -> 6
            Semver(version, Semver.SemverType.LOOSE) >= Semver("1.17", Semver.SemverType.LOOSE) -> 7
            else -> null
        }
    } else null
}

fun fromFormatToRange(format: Int?, isResourcePack: Boolean) = when (format) {
    1 -> if (isResourcePack)    VersionRange("1.6.1", "1.8.9")    else null
    2 -> if (isResourcePack)    VersionRange("1.9", "1.10.2")     else null
    3 -> if (isResourcePack)    VersionRange("1.11", "1.12.2")    else null
    4 ->                        VersionRange("1.13", "1.14.4")
    5 ->                        VersionRange("1.15", "1.16.1")
    6 ->                        VersionRange("1.16.2", "1.16.4")
    7 ->                        VersionRange("1.17", "2.0")
    else -> null
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