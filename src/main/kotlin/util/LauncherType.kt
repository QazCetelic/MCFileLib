package main.util

enum class LauncherType(val normalName: String, val instanceFolder: String = "instances/") {
    VANILLA( normalName = "Vanilla", instanceFolder = ""),
    //GDLauncher has 2 entries because the way data is stored is changed. The second one is called "_NEXT" because that was used for the folder name.
    GDLAUNCHER(normalName = "GDLauncher", instanceFolder = "GDLauncher/instances/packs/"),
    GDLAUNCHER_NEXT(normalName = "GDLauncher Next"),
    MULTIMC(normalName = "MultiMC"),
    TECHNIC(normalName = "Technic", instanceFolder = "modpacks/"),

    //This is used instead of null
    UNKNOWN(normalName = "")
}