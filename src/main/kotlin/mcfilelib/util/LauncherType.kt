package mcfilelib.util

enum class LauncherType(val displayName: String, val instanceFolder: String, val subfolder: String = "") {
    VANILLA(
        displayName = "Vanilla",
        instanceFolder = ""
    ),
    //GDLauncher has 2 entries because the way data is stored is changed. The second one is called "_NEXT" because that was used for the folder name.
    GDLAUNCHER(
        displayName = "GDLauncher",
        instanceFolder = "GDLauncher/instances/packs/"
    ),
    GDLAUNCHER_NEXT(
        displayName = "GDLauncher (New)",
        instanceFolder = "instances/"
    ),
    MULTIMC(
        displayName = "MultiMC",
        instanceFolder = "instances/",
        subfolder = ".minecraft/"
    ),
    TECHNIC(
        displayName = "Technic",
        instanceFolder = "modpacks/"
    ),

    //This is used instead of null
    UNKNOWN(
        displayName = "",
        instanceFolder = ""
    )
}