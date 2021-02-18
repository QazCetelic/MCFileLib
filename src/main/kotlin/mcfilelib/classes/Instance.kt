package mcfilelib.classes

import mcfilelib.util.*
import mcfilelib.util.file_entry.config.ConfigDirectory
import java.io.File
import java.nio.file.Path
import java.util.*

class Instance(path: Path, type: LauncherType): FileEditable(path) {
    var version: String? = null
        private set
    var resourceFormat: Int? = null
        private set

    val launcherType = type
    // todo implement this for other launchers than Technic
    var allocatedMemory: Int? = null
        private set
    var javaArguments: List<String> = listOf()
        private set

    val modloaders: List<ModLoader>

    //todo add support for launch arguments

    init {
        val file = path.toFile()
        if (!file.exists()) throw Exception("Doesn't exist")

        val foundModLoaders = mutableListOf<ModLoader>()
        val foundJavaArguments = mutableListOf<String>()

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
            }
            LauncherType.GDLAUNCHER_NEXT -> {
                loadJson(path/"config.json").ifKey("modloader") {
                    // TODO find better way to extract data, this could unreliable
                    val jsonArray = it.asJsonArray
                    if (jsonArray.size() == 3) {
                        foundModLoaders.add(ModLoader(name = jsonArray[0].asString, version = jsonArray[2].asString))
                        version = jsonArray[1].asString
                    }
                }
            }
            LauncherType.VANILLA -> {
                val json = loadJson(path/"${file.name}.json")
                // id key is version
                val id = json["id"].asString
                // Checks if it's a normal version like 1.8.3 and not 20w13b
                if (isVersion(id)) version = id
                else {
                    if (json.has("assets")) {
                        val assetsVersion = json["assets"].asString
                        // resourceFormat is set here instead of just using the given version later because of the way versions are handled for snapshots.
                        resourceFormat = fromVersionToFormat(assetsVersion)
                        version = "$assetsVersion, Snapshot $id"
                    }
                    // In case other json is in the .json file as expected
                    else {
                        // Optifine exception because it creates a json file with the same naming schema as Mojang in does, but fills it with different values -_-
                        if (file.name.toLowerCase().contains("optifine")) {
                            // Uses .ifKey because everything could happen apparently
                            json.ifKey("inheritsFrom") {
                                version = it.asString
                            }
                        } else throw Exception("Invalid Json")
                    }
                }
            }
            LauncherType.MULTIMC -> {
                //Gets version from jsonData
                val json = loadJson(path/"mmc-pack.json")
                //Checks if the "components" array exists and if so, extracts the array
                json.ifKey("components") {
                    for (entry in it.asJsonArray) {
                        //Each "component" object has a name, this loops through the array and checks if the name matches to something that is needed
                        when (entry.asJsonObject["cachedName"].asString) {
                            //Gets Minecraft version
                            "Minecraft" -> version = entry.asJsonObject["version"].asString
                            //Gets modloader versions
                            "Fabric Loader" -> foundModLoaders.add(ModLoader("Fabric", entry.asJsonObject["version"].asString))
                            "Forge" -> foundModLoaders.add(ModLoader("Forge", entry.asJsonObject["version"].asString))
                        }
                    }
                }
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
                json.ifKey("minecraftArguments") {
                    javaArguments = splitArgumentString(it.asString)
                }
                loadJson(path/"bin"/"runData").ifKey("memory") {
                    allocatedMemory = it.asInt
                }
            }
            else -> throw Exception("${type.displayName} is unsupported")
        }
        modloaders = foundModLoaders

        if (resourceFormat == null && version != null && isVersion(version!!)) {
            resourceFormat = fromVersionToFormat(version)
        }
    }

    val configs = ConfigDirectory(path/".minecraft"/"config")

    val name = if (type == LauncherType.MULTIMC) {
        lateinit var foundName: String
        // Gets name from "instance.cfg" instead of file name because renaming instance in MultiMC doesn't seem te rename folder
        (path/"instance.cfg").toFile().forEachLine {
            if (it.startsWith("name=")) foundName = it.removePrefix("name=")
        }
        foundName
    } else path.toFile().name.undev()

    val resourcepacks = run {
        val foundResourcePacks = mutableListOf<ResourcePack>()

        fun getPackFiles(name: String): Array<File>? = (path/(type.subfolder + name)).toFile().listFiles()
        getPackFiles("resourcepacks")?.forEach {
            foundResourcePacks += ResourcePack(it.toPath())
        }
        getPackFiles("texturepacks")?.forEach {
            foundResourcePacks += ResourcePack(it.toPath())
        }
        foundResourcePacks.toList()
    }

    val screenshots = mutableListOf<Screenshot>().also {
        (path/(type.subfolder + "screenshots")).toFile().listFiles()?.forEach { file ->
            it += Screenshot(file.toPath())
        }
    }.toList()

    /**
     * The icon of the Instance, uses icon.png or background.png if not found
     */
    val iconPath = run {
        // Returns the first icon it can find or null
        for (iconName in listOf("icon", "background")) {
            val iconPath = path/(type.subfolder + "$iconName.png")
            if (iconPath.toFile().exists()) {
                return@run iconPath
            }
        }
        return@run null
    }

    val mods = run {
        var unknownIDNameGeneratorNumber = 1
        val foundMods = mutableMapOf<String, Mod>()
        (path/(type.subfolder + "mods")).toFile().listFiles()?.forEach {
            if (it.extension == "jar" || it.name.endsWith(".jar.disabled")) {
                val mod = Mod(it.toPath())

                fun addWithGeneratedID() {
                    /*
                        This part has a lot of safety mechanisms that are very unlikely to be needed.
                        They still exists just in case someone would like to use "unknownMod2" as their mod ID or something silly like that.
                    */
                    // Creates ID from the name, should work most of the time but could theoretically fail
                    fun nameToID() = mod.name!!.replace(" ", "_")
                    fun createIDFromNumber(): String {
                        // Generates a new ID
                        val createdID = "unknownMod$unknownIDNameGeneratorNumber"
                        // Ups the number for the next ID
                        unknownIDNameGeneratorNumber++
                        // Either returns the ID or generates the next one and returns that if it's already used by another mod
                        return if (createdID !in foundMods.keys) createdID else nameToID()
                    }
                    // If a mod ID isn't found it will try to use the name to create an ID to use
                    if (mod.name != null && nameToID() !in foundMods.keys) foundMods[nameToID()] = mod
                    // It will just generate a number when the mod name cannot be used because it's already used as id by another mod or if it doesn't exist.
                    else foundMods[createIDFromNumber()] = mod
                }
                when (mod.id) {
                    // Checks if it can't be added...
                    null -> addWithGeneratedID()
                    in foundMods.keys -> addWithGeneratedID()
                    // ...or if it can
                    else -> foundMods[mod.id!!] = mod
                }
            }
        }
        foundMods.toMap()
    }

    /*
    todo fix launch() command
    fun launch() {
        when (launcherType) {
            LauncherType.MULTIMC -> {
                ProcessBuilder("/home/qaz/.local/share/multimc/MultiMC", "-l", this.path.toFile().name).inheritIO().start()
            }
            else -> TODO("${launcherType.displayName} can't be launched yet")
        }
    }
     */

    // Special getter to make code more readable when people don't want to use the map
    @Deprecated("Use mods.values instead", ReplaceWith("mods.values"))
    val allMods get() = mods.values
}