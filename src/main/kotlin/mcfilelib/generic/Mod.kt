package mcfilelib.generic

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import neatlin.*
import mcfilelib.generic.Mod.ModType.*
import mcfilelib.util.FileEditable
import mcfilelib.util.contains
import mcfilelib.util.ifKey
import mcfilelib.util.jsonDataProcessing.MCVersion
import mcfilelib.util.jsonDataProcessing.ModVersion
import mcfilelib.util.jsonDataProcessing.addAuthorsFromArray
import mcfilelib.util.jsonDataProcessing.addAuthorsFromString
import neatlin.file.isEmpty
import neatlin.get
import org.tomlj.Toml
import java.nio.file.Path
import java.util.zip.ZipFile

/**
 * Object for viewing mod meta-data such as: name, description, version, dependencies, icon etc.
 */
class Mod(path: Path): FileEditable(path) {
    var name: String? = null
        private set
    var description: String? = null
        private set
    //I forgot if this is for the mc version or mod itself todo make sure modVersion and MCVersion don't get confused
    lateinit var modVersion: ModVersion
        private set
    // todo convert this to a range
    var mcVersion: MCVersion? = null
        private set
    var modloaderVersion: String? = null
        private set

    /**
     * Name without capitals or spaces
     */
    var id: String? = null
        private set

    //Todo detect type of license and store as enum
    var license: String? = null
        private set
    var email: String? = null
        private set
    var issues: String? = null
        private set
    var sources: String? = null
        private set

    /**
     * Mod's web site, forum thread, info/support page or anything on the web that's like that.
     * The mod users can click on this link in-game and it will take them to the specified link.
     */
    var site: String? = null
        private set
    /**
     * This is only used by older mods, new ones don't have it anymore (not confirmed yet)
     */
    var updateURL: String? = null
        private set
    var updateJsonURL: String? = null
        private set
    var credits: String? = null
        private set

    /**
     * Relative path to the icon
     */
    var iconPath: String? = null
        private set

    // todo implement this in forge
    var clientSide: Boolean? = null
        private set

    /**
     * What kind of mod it is, like Forge, Fabric, Rift or LiteLoader
     */
    var type = Unknown
        private set
    var disabled: Boolean
        private set

    var authors: List<String>
        private set
    //TODO Split hard and soft dependencies
    var dependencies: List<ModDependency>
        private set

    init {
        val modFile = path.toFile()

        // Some launchers disable mods by renaming it to disable so the game doesn't recognize them
        disabled = (modFile.extension == "disabled")

        val tempAuthors = mutableListOf<String>()
        val tempDependencies = mutableListOf<ModDependency>()

        // Checks if the file isn't invalid
        when {
            !modFile.exists() -> throw Exception("File doesn't exist")
            // Checks if it's actually in a mod file format (".jar")
            !(modFile.extension == "jar" || modFile.name.endsWith(".jar.disabled")) -> throw Exception("Not a jar")
            // Checks if the zip file isn't empty because that would cause an exception
            modFile.isEmpty() -> throw Exception("Mod is empty")
        }

        // Looks through the data of the mod itself
        val zipFile = ZipFile(modFile)

        // Finds the license but only assigns it if it's not assigned already
        zipFile["LICENSE"]?.let { entry ->
            if (license == null) license = zipFile.getEntryAsText(entry)
        }

        // Gets Fabric mod data
        zipFile["fabric.mod.json"]?.let { entry ->
            type = Fabric
            val text = zipFile.getEntryAsText(entry)
            val json = Gson().fromJson(text, JsonObject::class.java)
            // Im doing it like this because the json data is different in some mods and I don't know why, this seems the most reliable option
            if ("schemaVersion" in json && json["schemaVersion"].asInt == 1) {
                json.ifKey("name") { name = it.asString }
                json.ifKey("description") { description = it.asString }
                json.ifKey("id") { id = it.asString }
                // TODO ALSO EXTRACT MINECRAFT VERSION
                json.ifKey("version") { modVersion = ModVersion(it.asString) }
                json.ifKey("authors") { tempAuthors.addAuthorsFromArray(it) }
                json.ifKey("contact") { contactJson ->
                    //TODO reconsider having a special class for extracting the JSON, I could also use .ifKey(){}
                    val contactData = Gson().fromJson(contactJson.toString(), JsonObject::class.java)
                    contactData.ifKey("email") { email = it.asString }
                    contactData.ifKey("issues") { issues = it.asString }
                    contactData.ifKey("sources") { sources = it.asString }
                }
                json.ifKey("icon") { iconPath = it.asString }
                json.ifKey("depends") {
                    val entries = mutableListOf<ModDependency>()
                    it.asJsonObject.entrySet().forEach { entry ->
                        //Don't get the depend as value as string
                        //todo fix this: entries.add(ModDependency(entry.key, entry.value.asString))
                    }
                    dependencies = entries
                }
                json.ifKey("custom") {
                    it.asJsonObject.ifKey("modmenu:clientsideOnly") { clientSideEntry ->
                        clientSide = clientSideEntry.asBoolean
                    }
                }
            } else throw Exception("Invalid schema version") //In case it changes in the future
        }

        //Gets Forge mod data as Toml
        zipFile["META-INF/mods.toml"]?.let { entry ->
            // Using Toml is a pita, I'll use json instead
            val jsonData = Toml.parse(zipFile.getEntryAsText(entry)).toJson()
            val jsonObject = Gson().fromJson(jsonData, JsonObject::class.java)
            jsonObject.ifKey("mods") { mods ->
                // Currently only process one mode in multi-mod jars, todo consider changing that
                val modJson = mods.asJsonArray[0].asJsonObject
                modJson.ifKey("modId") { id = it.asString }
                modJson.ifKey("displayName") { name = it.asString }
                modJson.ifKey("description") { description = it.asString }
                modJson.ifKey("displayURL") { updateURL = it.asString }
                modJson.ifKey("authors") { tempAuthors.addAuthorsFromString(it) }
                modJson.ifKey("credits") { credits = it.asString }
                modJson.ifKey("logoFile") { iconPath = it.asString }
                modJson.ifKey("updateJSONURL") { updateJsonURL = it.asString }
                modJson.ifKey("version") {
                    if (it.asString == "\${file.jarVersion}") {
                        if ("v" in modFile.nameWithoutExtension) {
                            val versionString = modFile.nameWithoutExtension.split("v")[1]
                            // Checks for a dot because versions (e.g. 1.2.3) contain dots.
                            if ('.' in versionString) {
                                // Checks if it contains several digits…
                                var digits = 0
                                for (char in versionString) {
                                    if (char.isDigit()) digits++
                                }
                                if (digits >= 3) {
                                    // …and adds the string if so
                                    modVersion = ModVersion(versionString)
                                }
                            }
                        }
                    }
                    else modVersion = ModVersion(it.asString) }
                }
            jsonObject.ifKey("issueTrackerURL") { issues = it.asString }
            jsonObject.ifKey("dependencies") { dependenciesElement ->
                val dependencies = dependenciesElement.asJsonObject
                for (key in dependencies.keySet()) {
                    val modDependencies = dependencies[key].asJsonArray
                    for (modDependencyJson in modDependencies) {
                        val modDependency = modDependencyJson.asJsonObject
                        when (modDependency["modId"].asString) {
                            "forge" -> modloaderVersion = modDependency["versionRange"].asString
                            "minecraft" -> mcVersion = MCVersion(modDependency["versionRange"].asString.removePrefix("[").removeSuffix("]"))
                        }
                    }
                }
            }
        }

        // Gets Forge mod data as json
        zipFile["mcmod.info"]?.let { entry ->
            type = Forge
            fun processEntry(modListEntry: JsonObject) {
                modListEntry.ifKey("modinfoversion") {
                    //TODO support more different modinfo versions
                    if (it.asInt != 2) throw Exception("${it.asString} is not supported!")
                }

                /*
                    Credits to mcpcfanc at https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/2405990-mcmod-info-file-guide-and-help
                    https://forums.minecraftforge.net/topic/6811-mcmodinfo/
                 */

                modListEntry.ifKey("modid") { id = it.asString}
                modListEntry.ifKey("name") { name = it.asString}
                modListEntry.ifKey("description") { description = it.asString }
                modListEntry.ifKey("url") { site = it.asString }
                modListEntry.ifKey("version") { modVersion = ModVersion(it.asString) }
                modListEntry.ifKey("mcversion") { mcVersion = MCVersion(it.asString) }

                // Version specific
                // updateUrl only works on older versions
                modListEntry.ifKey("updateUrl") { updateURL = it.asString }
                // "authorList" is for 1.7+
                modListEntry.ifKey("authorList") { tempAuthors.addAuthorsFromArray(it) }
                // "authors" is for below 1.7
                modListEntry.ifKey("authors") { tempAuthors.addAuthorsFromArray(it) }
                modListEntry.ifKey("logoFile") { iconPath = it.asString }
                modListEntry.ifKey("dependencies") { dependencies ->
                    dependencies.asJsonArray.forEach { dependency ->
                        //TODO parse mod dependencies correctly
                        tempDependencies.add(ModDependency(dependency.asString, "Unknown version"))
                    }
                }
                modListEntry.ifKey("credits") { credits = it.asString }
                /*
                Unsure if I should implement this

                modListEntry.ifKey("dependants") {
                    it.asJsonArray.forEach {
                        dependants.add(it.asString)
                    }
                }
                modListEntry.ifKey("requiredMods") {
                    it.asJsonArray.forEach {
                        hardDependencies.add(it.asString)
                    }
                }

                "requiredMods, useDependencyInformation, and requiredMods are not included in most MCMOD.INFO files, so they are not in the example above. They can be manually added and will still be functional."

                    It's unknown if these are actually functional

                modListEntry.ifKey("parent") { parent = it.asString } //This might only be useful when processing submods
                modListEntry.ifKey("screenshots") {
                    it.asJsonArray.forEach {
                        authors.add(it.asString)
                    }
                }
                */
            }

            //Only converts to json if the file is not blank
            val text = zipFile.getEntryAsText(entry)
            if (text.isNotBlank()) {
                //Try catch block in case the json is invalid (Yes, these things actually occur, looking at you F5 Fix!)
                runCatching {
                    val data = Gson().fromJson(
                        text,
                        JsonElement::class.java
                    )
                    when {
                        data == null -> {/* Do nothing */}
                        data.isJsonObject -> processEntry(data.asJsonObject)
                        data.isJsonArray -> {
                            val jsonArray = data.asJsonArray
                            // it currently only takes the first entry, TODO maybe process more
                            if (jsonArray.size() != 0) processEntry(jsonArray[0].asJsonObject)
                        }
                    }
                }
            }
        }

        dependencies = tempDependencies.toList()
        authors = tempAuthors.toList()
        if (!this::modVersion.isInitialized) modVersion = ModVersion(null)
    }

    /**
     * Enums for the different types of mods
     */
    enum class ModType {
        Fabric,
        Forge,
        // LiteLoader and Rift aren't implemented yet.
        LiteLoader,
        Rift,
        Unknown;
    }

    /**
     * Class that is used to more easily work with mod dependencies
     */
    data class ModDependency(
        val name: String,
        val version: String,
    )

    override fun toString() = "$name: v$modVersion for $mcVersion by ${
        // Lists all the authors, like this: 1, 2, 3 and 4
        buildString {
            for (i in authors.indices) {
                append(authors[i])
                when {
                    i < authors.size - 2 -> append(", ")
                    i == authors.size - 2 -> append(" and ")
                }
            }
        }
    }"
}