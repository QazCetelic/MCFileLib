package testing

import mcfilelib.util.Launchers

fun ocurrences() {
    val occurrences = mutableMapOf<String, Int>()

    val launchers = Launchers.allInstalledLaunchers
    launchers.forEach { launcher ->
        launcher.instances.forEach { instance ->
            instance.allMods.forEach { mod ->
                mod.authors.forEach { author ->
                    if (occurrences[author] != null) occurrences[author] = occurrences[author]!! + 1
                    else occurrences[author] = 1
                }
            }
        }
    }

    occurrences.forEach {
        println(it.key + ": " + it.value)
    }
}