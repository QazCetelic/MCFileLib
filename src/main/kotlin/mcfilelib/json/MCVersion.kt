package mcfilelib.json

import kmp_semver.*

class MCVersion(string: String?) {
    val raw: String = string ?: ""
    val semVer: SemVer? = string?.toSemVerOrNull()
    val found: Boolean = (string != null)

    override fun toString() = raw
}