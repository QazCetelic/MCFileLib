package classes

import classes.used.file_entry.config.ConfigDirectory
import com.google.gson.JsonObject
import main.util.VersionConverter
import util.*
import java.nio.file.Path

class Instance(path: Path, type: LauncherType): FileEditable(path) {
    var version: String? = null
        private set

    val modloaders: List<ModLoader>

    var resourceFormat = -1
        private set

    init {
        val file = path.toFile()
        if (!file.exists()) throw Exception("Files don't exist")

        val foundModLoaders = ArrayList<ModLoader>()

        //Uses hardcoded methods of data extraction because every launcher does it different
        when(type) {
            //GDLauncher Next is the modern version, this is for compatibility and only supports the forge dataclasses.readonly.main.classes.main.classes.ModLoader
            LauncherType.GDLAUNCHER -> {
                val json = fetchJsonData(path/"config.json")
                version = json["version"].asString
                json.ifKey("forgeVersion") {
                    foundModLoaders.add(ModLoader(
                        "Forge",
                        json["forgeVersion"].asString.replace("forge-", "")
                    ))
                }
                resourceFormat = VersionConverter().fromVersionToFormat(version)
            }
            LauncherType.GDLAUNCHER_NEXT -> {
                fetchJsonData(path/"config.json").ifKey("modloader") { modLoader ->
                    //TODO find better way to extract data, this is stupid and unreliable
                    val modLoaderJsonArray = modLoader.asJsonArray
                    if (modLoaderJsonArray.size() == 3)
                        foundModLoaders.add(ModLoader(
                            //ModLoader name
                            modLoaderJsonArray[0].asString,
                            //ModLoader version
                            modLoaderJsonArray[2].asString
                        ))
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
                else fetchJsonData(path/".main.json").ifKey("assets") {
                    version = file.name
                    resourceFormat = VersionConverter().fromVersionToFormat(it.asString)
                }
            }
            LauncherType.MULTIMC -> {
                //Gets version from jsonData
                val json = fetchJsonData(path/"mmc-pack.main.json")
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
                val json = fetchJsonData((path/"bin"/"version.json"))
                json.ifKey("id") {
                    val string = it.asString
                    if (string.contains("-")) {
                        val splitString = string.split("-")
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
        var name = path.toFile().name
        if (type == LauncherType.MULTIMC) {
            //Gets name from "instance.cfg" instead of file name because renaming instance in MultiMC doesn't seem te rename folder
            (path/"instance.cfg").toFile().forEachLine {
                if (it.startsWith("name=")) name = it.removePrefix("name=")
            }
        }
        name
    }

    val resourcepacks = run {
        //Create path to the folder containing the ResourcePacks
        val resourcePackFolderFiles = if (type == LauncherType.MULTIMC) {
            (path/".minecraft"/"resourcepacks").toFile().listFiles()
        }
        else (path/"resourcepacks").toFile().listFiles()
        val foundResourcePacks = ArrayList<ResourcePack>()
        resourcePackFolderFiles?.forEach {
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

    //TODO consider using map with mod id as key instead
    val mods = run {
        val foundMods = mutableListOf<Mod>()
        (path/"${type.subfolder}mods").toFile().listFiles()?.forEach {
            if (it.extension == "jar" || it.name.endsWith(".jar.disabled")) foundMods += Mod(it.toPath())
        }
        foundMods.toList()
    }

    private fun fetchJsonData(jsonLocation: Path): JsonObject {
        //TODO consider merging this function with JsonLoader()
        val file = jsonLocation.toFile()
        return if (file.exists() && file.isFile) loadJson(jsonLocation)
        else JsonObject()
    }
}