package testing

import mcfilelib.util.Launchers

fun screenshots() {
    for (launcher in Launchers.allInstalledLaunchers) {
        for (instance in launcher.instances) {
            for (screenshot in instance.screenshots) {
                println(screenshot.path)
            }
        }
    }
}