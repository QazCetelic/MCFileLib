package mcfilelib.util

import kmp_semver.*

fun versionRange(from: String, end: String): SemVerRange? {
    return SemVerRange(from.toSemVerOrNull(patchRequired = false) ?: return null, end.toSemVerOrNull(patchRequired = false) ?: return null)
}