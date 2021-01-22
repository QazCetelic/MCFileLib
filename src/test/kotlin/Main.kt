package testing

import mcfilelib.util.Launchers

//This is used for testing stuff
fun main() {
    var occurrences = mutableMapOf<String, Int>()

    val launchers = Launchers.getAll()
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