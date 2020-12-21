package classes

import com.google.gson.JsonObject
import main.classes.classes.ResourcePack
import main.util.JsonLoader
import main.util.LauncherType
import main.util.VersionConverter
import java.io.File
import java.nio.file.Path
import kotlin.collections.ArrayList

class Instance(val path: Path, launcher: LauncherType) {
    //Read from Json file
    val version: String
    val modloaders: List<ModLoader>
    val resourceFormat: Int

    init {
        val file = path.toFile()
        if (!file.exists()) throw Exception("Files don't exist")

        val foundModloaders = ArrayList<ModLoader>()
        var foundVersion = "Unknown version"
        var foundResourceFormat = -1

        //The data that can be extracted without reading Json
        val name = run {
            var name = file.name
            if (launcher == LauncherType.MULTIMC) {
                //Gets name from "instance.cfg" instead of file name because renaming instance in MultiMC doesn't seem te rename folder
                File("$path/instance.cfg").forEachLine {
                    if (it.startsWith("name=")) name = it.removePrefix("name=")
                }
            }
            name
        }

        //Uses hardcoded methods of data extraction because every launcher does it different
        when(launcher) {
            //GDLauncher Next is the modern version, this is for compatibility and only supports the forge dataclasses.readonly.main.classes.main.classes.ModLoader
            LauncherType.GDLAUNCHER -> {
                val jsonData = fetchJsonData("/config.main.json")
                foundVersion = jsonData["version"].asString
                if (jsonData.has("forgeVersion"))
                    foundModloaders += (ModLoader(
                            "Forge",
                            jsonData["forgeVersion"].asString.replace("forge-", "")
                    ))
                foundResourceFormat = VersionConverter().fromVersionToFormat(foundVersion)
            }
            LauncherType.GDLAUNCHER_NEXT -> {
                val jsonData = fetchJsonData("/config.main.json")
                if (jsonData.has("modloader")) {
                    //todo find better way to extract data, this is stupid and unreliable
                    val modloaderJsonArray = jsonData["modloader"].asJsonArray
                    if (modloaderJsonArray.size() == 3)
                        foundModloaders += (ModLoader(
                                //ModLoader name
                                modloaderJsonArray[0].asString,
                                //ModLoader version
                                modloaderJsonArray[2].asString
                        ))
                    foundVersion = modloaderJsonArray[1].asString
                }
                foundResourceFormat = VersionConverter().fromVersionToFormat(foundVersion)
            }
            LauncherType.VANILLA -> {
                //Checks if it contains a dot because names like "1.16.2" do and names like "20w27a" (Snapshots) don't
                if (name.contains(".")) {
                    //Only take the first part if the name has 2 parts
                    if (name.contains("-")) {
                        foundVersion = name.split("-")[0]
                        foundResourceFormat = VersionConverter().fromVersionToFormat(foundVersion)
                    }
                    else {
                        foundVersion = name
                        foundResourceFormat = VersionConverter().fromVersionToFormat(foundVersion)
                    }
                }
                //In case it's a weird name, like snapshots
                else {
                    val jsonData = fetchJsonData("/$name.main.json")
                    if (jsonData.has("assets")) {
                        foundVersion = name
                        foundResourceFormat = VersionConverter().fromVersionToFormat(jsonData["assets"].asString)
                    }
                }
            }
            LauncherType.MULTIMC -> {
                //Gets version from jsonData
                val jsonData = fetchJsonData("/mmc-pack.main.json")
                //Checks if the "components" array exists and if so, extracts the array
                if (jsonData.has("components")) {
                    jsonData["components"].asJsonArray.forEach {
                        //Each "component" object has a name, this loops through the array and checks if the name matches to something that is needed
                        when (it.asJsonObject["cachedName"].asString) {
                            //Gets Minecraft version
                            "Minecraft" -> {
                                foundVersion = it.asJsonObject["version"].asString
                            }

                            //Gets modloader versions
                            "Fabric Loader" -> {
                                foundModloaders += (ModLoader("Fabric", it.asJsonObject["version"].asString))
                            }
                            "Forge" -> {
                                foundModloaders += (ModLoader("Forge", it.asJsonObject["version"].asString))
                            }
                        }
                    }
                }
                foundResourceFormat = VersionConverter().fromVersionToFormat(foundVersion)
            }
            else -> throw Exception("Unsupported launcher")
        }
        version = foundVersion
        modloaders = foundModloaders
        resourceFormat = foundResourceFormat
    }

    val resourcepacks = run {
        //Create path to the folder containing the ResourcePacks
        val resourcePackFolderFiles = File("$path/resourcepacks").listFiles()
        val foundResourcePacks = ArrayList<ResourcePack>()
        resourcePackFolderFiles?.forEach {
            foundResourcePacks += ResourcePack(it.toPath())
        }
        foundResourcePacks.toList()
    }

    val screenshots = run {
        val screenshotFiles = File("$path/${
            if (launcher == LauncherType.MULTIMC) ".minecraft/"
            else ""
        }screenshots").listFiles()
        val foundScreenshots = ArrayList<Screenshot>()
        screenshotFiles?.forEach {
            foundScreenshots += Screenshot(it.toPath())
        }
        foundScreenshots.toList()
    }

    val mods = run {
        val modFiles = File("$path/${
            if (launcher == LauncherType.MULTIMC) ".minecraft/"
            else ""
        }mods").listFiles()
        val foundMods = ArrayList<Mod>()
        modFiles?.forEach {
            if (it.extension == "jar" || it.extension == "disabled") foundMods += Mod(it.toPath())
        }
        foundMods.toList()
    }

    private fun fetchJsonData(jsonLocation: String): JsonObject {
        val jsonFile = path.resolveSibling(jsonLocation).toFile()
        return if (jsonFile.exists()) {
            JsonLoader(jsonFile.path)
        }
        else JsonObject()
    }
}