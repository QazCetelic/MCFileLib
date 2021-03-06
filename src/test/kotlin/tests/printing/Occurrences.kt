package tests.printing

import mcfilelib.util.Launchers

fun ocurrences() {
    println("Running occurrences:")
    val occurrences = mutableMapOf<String, Int>()

    val launchers = Launchers.allInstalledLaunchers
    launchers.forEach { launcher ->
        launcher.instances.forEach { instance ->
            instance.mods.forEach { mod ->
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