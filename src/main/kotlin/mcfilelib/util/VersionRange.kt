package mcfilelib.util

import kmp_semver.SemVerRange
import kmp_semver.toSemVerOrNull

fun versionRange(from: String, end: String): SemVerRange? {
    return SemVerRange(from.toSemVerOrNull(patchRequired = false) ?: return null, end.toSemVerOrNull(patchRequired = false) ?: return null)
}