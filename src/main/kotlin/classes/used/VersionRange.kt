package classes.used

import com.vdurmont.semver4j.Semver

class VersionRange(from: String, to: String) {
    private val semverFrom = Semver(from, Semver.SemverType.LOOSE)
    private val semverTo = Semver(to, Semver.SemverType.LOOSE)

    operator fun contains(string: String): Boolean {
        val semver = Semver(string, Semver.SemverType.LOOSE)
        return (semver >= semverFrom) and (semver <= semverTo)
    }

    override fun toString(): String = "$semverFrom-$semverTo"
}