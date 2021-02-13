package testing

import mcfilelib.util.Launchers

fun resourcePacks() {
    for (launcher in Launchers.allInstalledLaunchers) {
        for (instance in launcher.instances) {
            for (resourcepack in instance.resourcepacks) {
                println(resourcepack.name)
            }
        }
    }
}