package mcfilelib.generic

import div
import fillList
import fillMap
import mcfilelib.util.*
import mcfilelib.util.LauncherType.*
import mcfilelib.util.file_entry.config.ConfigDirectory
import neatlin.string.undev
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
        fun splitArgumentString(argumentString: String): List<String> = argumentString.removePrefix("--").split(" --")

        //Uses hardcoded methods of data extraction because every launcher does it different
        when(type) {
            //GDLauncher Next is the modern version, this is for compatibility and only supports the forge dataclasses.readonly.main.generic.main.mcfilelib.generic.ModLoader
            GDLAUNCHER -> {
                val json = loadJson(path/"config.json")
                json.ifKey("version") { version = it.asString }
                json.ifKey("forgeVersion") {
                    if (!it.isJsonNull) {
                        foundModLoaders += ModLoader(
                            name = "Forge",
                            version = it.asString.replace("forge-", "")
                        )
                    }
                }
            }
            GDLAUNCHER_NEXT -> {
                loadJson(path/"config.json").ifKey("modloader") {
                    // TODO find better way to extract data, this could unreliable
                    val jsonArray = it.asJsonArray
                    if (jsonArray.size() == 3) {
                        foundModLoaders += ModLoader(
                            name = jsonArray[0].asString,
                            version = jsonArray[2].asString
                        )
                        version = jsonArray[1].asString
                    }
                }
            }
            VANILLA -> {
                val json = loadJson(path/"${file.name}.json")
                // id key is version
                val id = json["id"].asString
                // Checks if it's a normal version like 1.8.3 and not 20w13b
                if (isVersion(id)) version = id
                else {
                    if ("assets" in json) {
                        val assetsVersion = json["assets"].asString
                        // resourceFormat is set here instead of just using the given version later because of the way versions are handled for snapshots.
                        resourceFormat = fromVersionToFormat(assetsVersion)
                        version = "$assetsVersion, Snapshot $id"
                    }
                    // In case other json is in the .json file as expected
                    else {
                        // Optifine exception because it creates a json file with the same naming schema as Mojang in does, but fills it with different values -_-
                        if (file.name.contains("optifine", ignoreCase = true)) {
                            // Uses .ifKey because everything could happen apparently
                            json.ifKey("inheritsFrom") {
                                version = it.asString
                            }
                        } else throw Exception("Invalid Json")
                    }
                }
            }
            MULTIMC -> {
                //Gets version from jsonData
                val json = loadJson(path/"mmc-pack.json")
                //Checks if the "components" array exists and if so, extracts the array
                json.ifKey("components") {
                    for (entry in it.asJsonArray) {
                        // Yes, this ifKey is needed, sometimes the JSON is invalid here but not anywhere else -_-
                        entry.asJsonObject.ifKey("cachedName") {
                            //Each "component" object has a name, this loops through the array and checks if the name matches to something that is needed
                            when (it.asString) {
                                //Gets Minecraft version
                                "Minecraft" -> version = entry.asJsonObject["version"].asString
                                //Gets modloader versions
                                "Fabric Loader" -> foundModLoaders += ModLoader(
                                    name = "Fabric",
                                    version = entry.asJsonObject["version"].asString
                                )
                                "Forge" -> foundModLoaders += ModLoader(
                                    name = "Forge",
                                    version = entry.asJsonObject["version"].asString
                                )
                            }
                        }
                    }
                }
            }
            TECHNIC -> {
                val json = loadJson(path/"bin"/"version.json")
                json.ifKey("id") {
                    if (it.asString.contains("-")) {
                        val splitString = it.asString.split("-")
                        version = splitString[0]
                        if (splitString[1].startsWith("Forge")) foundModLoaders += ModLoader("Forge", splitString[1].removePrefix("Forge"))
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

        if (resourceFormat == null && version != null && isVersion(version!!)) resourceFormat = fromVersionToFormat(version)
    }

    val configs = ConfigDirectory(path/".minecraft"/"config")

    val name = run {
        if (type == MULTIMC) {
            // Gets name from "instance.cfg" instead of file name because renaming instance in MultiMC doesn't seem te rename folder
            for (line in (path/"instance.cfg").toFile().readLines()) {
                if (line.startsWith("name=")) return@run line.removePrefix("name=")
            }
        }
        return@run path.toFile().name.undev()
    }

    private fun getInstanceFiles(name: String) = (path/(launcherType.subfolder + name)).toFile().listFiles()
    val resourcepacks = fillList<ResourcePack> {
        getInstanceFiles("resourcepacks")?.forEach { file ->
            add(ResourcePack(file.toPath()))
        }
        getInstanceFiles("texturepacks")?.forEach { file ->
            add(ResourcePack(file.toPath()))
        }
    }
    val screenshots = fillList<Screenshot> {
        getInstanceFiles("screenshots")?.forEach { file ->
            add(Screenshot(file.toPath()))
        }
    }

    /**
     * The icon of the Instance, uses icon.png or background.png if not found
     */
    val iconPath = run {
        // Returns the first icon it can find or null
        for (iconName in listOf("icon", "background")) {
            val iconPath = path/(type.subfolder + "$iconName.png")
            if (iconPath.toFile().exists()) return@run iconPath
        }
        return@run null
    }

    val modMap = fillMap<String, Mod> {
        var unknownIDNameGeneratorNumber = 1
        getInstanceFiles("mods")?.forEach { file ->
            if (file.extension == "jar" || file.name.endsWith(".jar.disabled")) {
                val mod = Mod(file.toPath())
                // All mods should have a separate id so this shouldn't exit, well theoretically.
                fun addWithGeneratedID() {
                    /*
                        This part has a lot of safety mechanisms that are very unlikely to be needed.
                        They still exists just in case someone would like to use "unknownMod2" as their mod ID or something silly like that.
                        But at this point I wouldn't even be surprised anymore.
                    */
                    fun generateID(): String {
                        fun createIDFromNumber(): String {
                            val createdID = "unknownMod$unknownIDNameGeneratorNumber"
                            // For the next ID
                            unknownIDNameGeneratorNumber++
                            // Either returns the ID or generates the next one and returns that if it's already used by another mod
                            return if (createdID !in keys) createdID else createIDFromNumber()
                        }
                        // Creates ID from the name, should work most of the time but could theoretically fail, it then creates an ID using a number
                        val nameID = mod.name?.replace(" ", "_")
                        return if (nameID != null && nameID !in keys) nameID else createIDFromNumber()
                    }

                    set(generateID(), mod)
                }
                when (mod.id) {
                    // Checks if it can't directly be added...
                    null -> addWithGeneratedID()
                    in keys -> addWithGeneratedID()
                    // ...or if it can
                    else -> set(mod.id!!, mod)
                }
            }
        }
    }
    val mods get() = modMap.values
}