package mcfilelib.json

import kmp_semver.SemVer
import kmp_semver.toSemVerOrNull

class MCVersion(string: String?) {
    val raw: String = string ?: ""
    val semVer: SemVer? = string?.toSemVerOrNull()
    val found: Boolean = (string != null)

    override fun toString() = raw
}