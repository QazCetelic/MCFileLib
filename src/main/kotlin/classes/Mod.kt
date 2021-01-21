package classes

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import json.FabricContactData
import util.FileEditable
import util.ifKey
import java.awt.Image
import java.nio.file.Path
import java.util.*
import java.util.zip.ZipFile
import javax.imageio.ImageIO

/**
 * Object for viewing mod meta-data such as: name, description, version, dependencies, icon etc.
 */
class Mod(path: Path): FileEditable(path) {
    var name: String? = null
        private set
    var description: String? = null
        private set
    //I forgot if this is for the mc version or mod itself todo make sure modVersion and MCVersion don't get confused
    var modVersion: String? = null
        private set
    var mcVersion: String? = null
        private set

    /**
     * Name without capitals or spaces
     */
    var id: String? = null
        private set
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
    var iconPath: String? = null
        private set

    var clientSide: Boolean? = null
        private set
    var icon: Image?
        private set

    var type = ModType.Unknown
        private set
    var disabled: Boolean
        private set

    var authors: MutableList<String> = ArrayList<String>()
        private set
    //TODO Split hard and soft dependencies
    var dependencies: MutableList<ModDependency> = ArrayList<ModDependency>()
        private set

    init {
        val file = path.toFile()
        if (!file.exists()) throw Exception("Invalid mod: File doesn't exist")

        disabled = file.extension == "disabled"

        //Checks if it's actually in a mod file format (".jar")
        if (file.extension == "jar" || file.name.endsWith(".jar.disabled")) {
            //Looks through the data of the mod itself
            val zipFile = ZipFile(file)
            for (entry in zipFile.entries()) {
                //Finds the license..
                if (entry.name == "LICENSE") {
                    //..but only assigns it if it's not assigned already
                    if (license == null) license = String(zipFile.getInputStream(entry).readBytes())
                }

                //Gets Fabric mod data
                if (entry.name == "fabric.mod.json") {
                    type = ModType.Fabric
                    val text = String(zipFile.getInputStream(entry).readBytes())
                    val json = Gson().fromJson(text, JsonObject::class.java)
                    //Im doing it like this because the json data is different in some mods and I don't know why, this seems the most reliable option
                    if (json.has("schemaVersion") && json["schemaVersion"].asInt == 1) {
                        json.ifKey("name") { name = it.asString }
                        json.ifKey("description") { description = it.asString }
                        json.ifKey("id") { id = it.asString }
                        json.ifKey("version") { modVersion = it.asString }
                        json.ifKey("authors") {
                            val authorList = it.asJsonArray.toList()
                            authorList.forEach { author ->
                                authors.add(author.asString)
                            }
                        }
                        json.ifKey("contact") { contactJson ->
                            //TODO reconsider having a special class for extracting the JSON, I could also use .ifKey(){}
                            val contactData = Gson().fromJson(contactJson.toString(), FabricContactData::class.java)
                            email = contactData.email
                            issues = contactData.issues
                            sources = contactData.sources
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

                if (entry.name == "mcmod.info") {
                    type = ModType.Forge
                    val data = Gson().fromJson(
                        String(zipFile.getInputStream(entry).readBytes()),
                        JsonElement::class.java
                    )
                    when {
                        data.isJsonObject -> processForgeModJSON(data.asJsonObject)
                        data.isJsonArray -> {
                            val jsonArray = data.asJsonArray
                            //it currently only takes the first entry, TODO maybe process more
                            if (jsonArray.size() > 0) {
                                processForgeModJSON(jsonArray[0].asJsonObject)
                            }
                        }
                    }
                }
            }
            icon =  if (iconPath != null) {
                        val entry = zipFile.getEntry(iconPath)
                        if (entry != null) ImageIO.read(zipFile.getInputStream(entry))
                        else null
                    } else null
        } else throw Exception("Invalid mod: Not a jar")

        //Makes the lists read-only
        dependencies = Collections.unmodifiableList(dependencies)
        authors = Collections.unmodifiableList(authors)
    }

    private fun processForgeModJSON(JSON: JsonObject) {
        fun processForgeModListEntry(modListEntry: JsonObject) {
            // Credits to mcpcfanc at https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/2405990-mcmod-info-file-guide-and-help
            modListEntry.ifKey("modid") { id = it.asString}
            modListEntry.ifKey("name") { name = it.asString}
            modListEntry.ifKey("description") { description = it.asString }
            modListEntry.ifKey("url") { site = it.asString }
            modListEntry.ifKey("version") { modVersion = it.asString }
            modListEntry.ifKey("mcversion") { mcVersion = it.asString }

            // Version specific
            // updateUrl only works on older versions
            modListEntry.ifKey("updateUrl") { updateURL = it.asString }
            // "authorList" is for 1.7+
            modListEntry.ifKey("authorList") {
                it.asJsonArray.forEach { author ->
                    authors.add(author.asString)
                }
            }
            // "authors" is for below 1.7
            modListEntry.ifKey("authors") {
                it.asJsonArray.forEach { author ->
                    authors.add(author.asString)
                }
            }
            modListEntry.ifKey("logoFile") { iconPath = it.asString }
            modListEntry.ifKey("dependencies") {
                it.asJsonArray.forEach { dependency ->
                    //TODO parse mod dependencies correctly
                    dependencies.add(ModDependency(dependency.asString, "Unknown version"))
                }
            }
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

            modListEntry.ifKey("credits") { credits = it.asString }
            modListEntry.ifKey("parent") { parent = it.asString } //This might only be useful when processing submods
            modListEntry.ifKey("screenshots") {
                it.asJsonArray.forEach {
                    authors.add(it.asString)
                }
            }
            */
        }
        JSON.ifKey("modinfoversion") {
            //TODO support more different modinfo versions
            if (it.asInt != 2) throw Exception(it.asString + " is not supported!")
        }
        if (JSON.isJsonArray) processForgeModListEntry(JSON["modlist"].asJsonArray[0].asJsonObject)
        else processForgeModListEntry(JSON)
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
        val version: String
    )
}