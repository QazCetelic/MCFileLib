package mcfilelib.util

import kmp_semver.*

fun versionRange(from: String, end: String): SemVerRange? {
    return SemVerRange(from.toSemVer(patchRequired = false) ?: return null, end.toSemVer(patchRequired = false) ?: return null)
}