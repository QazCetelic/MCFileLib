package mcfilelib.classes

import mcfilelib.util.*
import mcfilelib.util.file_entry.config.ConfigDirectory
import java.io.File
import java.nio.file.Path
import java.util.*

class Instance(path: Path, type: LauncherType): FileEditable(path) {
    var version: String? = null
        private set

    val modloaders: List<ModLoader>

    var resourceFormat = -1
        private set

    init {
        val file = path.toFile()
        if (!file.exists()) throw Exception("Files don't exist")

        val foundModLoaders = mutableListOf<ModLoader>()

        //Uses hardcoded methods of data extraction because every launcher does it different
        when(type) {
            //GDLauncher Next is the modern version, this is for compatibility and only supports the forge dataclasses.readonly.main.classes.main.mcfilelib.classes.ModLoader
            LauncherType.GDLAUNCHER -> {
                val json = loadJson(path/"config.json")
                json.ifKey("version") { version = it.asString }
                json.ifKey("forgeVersion") {
                    if (!it.isJsonNull) {
                        foundModLoaders.add(
                            ModLoader(
                                "Forge",
                                it.asString.replace("forge-", "")
                            )
                        )
                    }
                }
                resourceFormat = VersionConverter().fromVersionToFormat(version)
            }
            LauncherType.GDLAUNCHER_NEXT -> {
                loadJson(path/"config.json").ifKey("modloader") { modLoader ->
                    //TODO find better way to extract data, this is stupid and unreliable
                    val modLoaderJsonArray = modLoader.asJsonArray
                    if (modLoaderJsonArray.size() == 3)
                        foundModLoaders.add(
                            ModLoader(
                            //ModLoader name
                            modLoaderJsonArray[0].asString,
                            //ModLoader version
                            modLoaderJsonArray[2].asString
                        )
                        )
                    version = modLoaderJsonArray[1].asString
                    resourceFormat = VersionConverter().fromVersionToFormat(version)
                }
            }
            LauncherType.VANILLA -> {
                //Checks if it contains a dot because names like "1.16.2" do and names like "20w27a" (Snapshots) don't
                if (file.name.contains(".")) {
                    //Only take the first part if the name has 2 parts
                    if (file.name.contains("-")) {
                        version = file.name.split("-")[0]
                        resourceFormat = VersionConverter().fromVersionToFormat(version)
                    }
                    else {
                        version = file.name
                        resourceFormat = VersionConverter().fromVersionToFormat(version)
                    }
                }
                //In case it's a weird name, like snapshots
                else loadJson(path/".main.json").ifKey("assets") {
                    version = file.name
                    resourceFormat = VersionConverter().fromVersionToFormat(it.asString)
                }
            }
            LauncherType.MULTIMC -> {
                //Gets version from jsonData
                val json = loadJson(path/"mmc-pack.json")
                //Checks if the "components" array exists and if so, extracts the array
                json.ifKey("components") {
                    it.asJsonArray.forEach { entry ->
                        //Each "component" object has a name, this loops through the array and checks if the name matches to something that is needed
                        when (entry.asJsonObject["cachedName"].asString) {
                            //Gets Minecraft version
                            "Minecraft" -> {
                                version = entry.asJsonObject["version"].asString
                            }
                            //Gets modloader versions
                            "Fabric Loader" -> {
                                foundModLoaders.add(ModLoader("Fabric", entry.asJsonObject["version"].asString))
                            }
                            "Forge" -> {
                                foundModLoaders.add(ModLoader("Forge", entry.asJsonObject["version"].asString))
                            }
                        }
                    }
                }
                resourceFormat = VersionConverter().fromVersionToFormat(version)
            }
            LauncherType.TECHNIC -> {
                val json = loadJson(path/"bin"/"version.json")
                json.ifKey("id") {
                    if (it.asString.contains("-")) {
                        val splitString = it.asString.split("-")
                        version = splitString[0]
                        if (splitString[1].startsWith("Forge")) foundModLoaders.add(ModLoader("Forge", splitString[1].removePrefix("Forge")))
                    }
                }
                //TODO add support to get description and other data
            }
            else -> throw Exception("${type.displayName} is unsupported")
        }
        modloaders = foundModLoaders
    }

    val configs = ConfigDirectory(path/".minecraft"/"config")

    val name = run {
        var name = path.toFile().name // todo fix NeatKotlin lib and add .undev() to this to fix name
        when (type) {
            LauncherType.VANILLA -> name = "Default Instance"
            LauncherType.MULTIMC -> {
                //Gets name from "instance.cfg" instead of file name because renaming instance in MultiMC doesn't seem te rename folder
                (path/"instance.cfg").toFile().forEachLine {
                    if (it.startsWith("name=")) name = it.removePrefix("name=")
                }
            }
        }
        name
    }

    val resourcepacks = run {
        val foundResourcePacks = mutableListOf<ResourcePack>()

        fun getPackFiles(name: String): Array<File>? = (
            if (type == LauncherType.MULTIMC) (path/".minecraft"/name)
            else (path/name)
        ).toFile().listFiles()

        getPackFiles("resourcepacks")?.forEach {
            foundResourcePacks += ResourcePack(it.toPath())
        }
        getPackFiles("texturepacks")?.forEach {
            foundResourcePacks += ResourcePack(it.toPath())
        }
        foundResourcePacks.toList()
    }

    val screenshots = run {
        val screenshotFiles = (path/"${
            if (type == LauncherType.MULTIMC) ".minecraft/"
            else ""
        }screenshots").toFile().listFiles()
        val foundScreenshots = mutableListOf<Screenshot>()
        screenshotFiles?.forEach {
            foundScreenshots += Screenshot(it.toPath())
        }
        foundScreenshots.toList()
    }

    /**
     * The icon of the Instance, uses icon.png or background.png if not found
     */
    val iconPath = run {
        var result: Path? = null
        for (image in listOf("icon", "background")) {
            val file = if (type.subfolder != "") {
                (path/type.subfolder/"$image.png").toFile()
            } else (path/"$image.png").toFile()
            if (file.exists()) {
                result = file.toPath()
                break
            }
        }
        return@run result
    }

    // TODO consider using map with mod id as key instead
    val mods = run {
        var unknownIDNameGeneratorNumber = 1
        val foundMods = mutableMapOf<String, Mod>()
        (path/"${type.subfolder}mods").toFile().listFiles()?.forEach {
            if (it.extension == "jar" || it.name.endsWith(".jar.disabled")) {
                val mod = Mod(it.toPath())
                // In case that the mod id is unknown
                if (mod.id == null) {
                    if (mod.name != null) foundMods[mod.name!!.replace(" ", "_")] = mod
                    else foundMods["unknownMod$unknownIDNameGeneratorNumber"] = mod
                    unknownIDNameGeneratorNumber++
                } else foundMods[mod.id!!] = mod
            }
        }
        Collections.unmodifiableMap(foundMods)
    }

    // Special getter to make code more readable when people don't want to use the map
    @Deprecated("Use mods.values instead", ReplaceWith("mods.values"))
    val allMods get() = mods.values
}