package classes

import classes.used.file_entry.config.Config
import classes.used.file_entry.config.ConfigDirectory
import classes.used.file_entry.config.ConfigEntry
import com.google.gson.JsonObject
import main.classes.classes.ResourcePack
import main.util.JsonLoader
import util.LauncherType
import main.util.VersionConverter
import util.FileEditable
import java.io.File
import java.nio.file.Path
import kotlin.collections.ArrayList

class Instance(path: Path, type: LauncherType): FileEditable(path) {
    var version = "Unable to load version"
        private set

    val modloaders: List<ModLoader>

    var resourceFormat = -1
        private set

    init {
        val file = path.toFile()
        if (!file.exists()) throw Exception("Files don't exist")

        val foundModloaders = ArrayList<ModLoader>()

        //Uses hardcoded methods of data extraction because every launcher does it different
        when(type) {
            //GDLauncher Next is the modern version, this is for compatibility and only supports the forge dataclasses.readonly.main.classes.main.classes.ModLoader
            LauncherType.GDLAUNCHER -> {
                val jsonData = fetchJsonData("/file_entry.main.json")
                version = jsonData["version"].asString
                if (jsonData.has("forgeVersion"))
                    foundModloaders.add(ModLoader(
                            "Forge",
                            jsonData["forgeVersion"].asString.replace("forge-", "")
                    ))
                resourceFormat = VersionConverter().fromVersionToFormat(version)
            }
            LauncherType.GDLAUNCHER_NEXT -> {
                val jsonData = fetchJsonData("/file_entry.main.json")
                if (jsonData.has("modloader")) {
                    //todo find better way to extract data, this is stupid and unreliable
                    val modloaderJsonArray = jsonData["modloader"].asJsonArray
                    if (modloaderJsonArray.size() == 3)
                        foundModloaders.add(ModLoader(
                                //ModLoader name
                                modloaderJsonArray[0].asString,
                                //ModLoader version
                                modloaderJsonArray[2].asString
                        ))
                    version = modloaderJsonArray[1].asString
                }
                resourceFormat = VersionConverter().fromVersionToFormat(version)
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
                else {
                    val jsonData = fetchJsonData("/${file.name}.main.json")
                    if (jsonData.has("assets")) {
                        version = file.name
                        resourceFormat = VersionConverter().fromVersionToFormat(jsonData["assets"].asString)
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
                                version = it.asJsonObject["version"].asString
                            }

                            //Gets modloader versions
                            "Fabric Loader" -> {
                                foundModloaders.add(ModLoader("Fabric", it.asJsonObject["version"].asString))
                            }
                            "Forge" -> {
                                foundModloaders.add(ModLoader("Forge", it.asJsonObject["version"].asString))
                            }
                        }
                    }
                }
                resourceFormat = VersionConverter().fromVersionToFormat(version)
            }
            LauncherType.TECHNIC -> {
                //todo add technic support
            }
            else -> throw Exception("${type.displayName} is unsupported")
        }
        modloaders = foundModloaders
    }

    val configs = ConfigDirectory(path.resolve(".minecraft").resolve("config"))

    val name = run {
        var name = path.toFile().name
        if (type == LauncherType.MULTIMC) {
            //Gets name from "instance.cfg" instead of file name because renaming instance in MultiMC doesn't seem te rename folder
            path.resolve("instance.cfg").toFile().forEachLine {
                if (it.startsWith("name=")) name = it.removePrefix("name=")
            }
        }
        name
    }

    val resourcepacks = run {
        //Create path to the folder containing the ResourcePacks
        val resourcePackFolderFiles = if (type == LauncherType.MULTIMC) {
            path.resolve(".minecraft").resolve("resourcepacks").toFile().listFiles()
        }
        else {
            path.resolve("resourcepacks").toFile().listFiles()
        }
        val foundResourcePacks = ArrayList<ResourcePack>()
        resourcePackFolderFiles?.forEach {
            foundResourcePacks += ResourcePack(it.toPath())
        }
        foundResourcePacks.toList()
    }

    val screenshots = run {
        val screenshotFiles = path.resolve("${
            if (type == LauncherType.MULTIMC) ".minecraft/"
            else ""
        }screenshots").toFile().listFiles()
        val foundScreenshots = ArrayList<Screenshot>()
        screenshotFiles?.forEach {
            foundScreenshots += Screenshot(it.toPath())
        }
        foundScreenshots.toList()
    }

    val mods = run {
        val modFiles = path.resolve("${type.subfolder}mods").toFile().listFiles()
        val foundMods = ArrayList<Mod>()
        modFiles?.forEach {
            if (it.extension == "jar" || it.name.endsWith(".jar.disabled")) foundMods += Mod(it.toPath())
        }
        foundMods.toList()
    }

    private fun fetchJsonData(jsonLocation: String): JsonObject {
        val jsonPath = path.resolveSibling(jsonLocation)
        val file = jsonPath.toFile()
        return if (file.exists() && file.isFile) {
            JsonLoader(jsonPath)
        }
        else JsonObject()
    }
}