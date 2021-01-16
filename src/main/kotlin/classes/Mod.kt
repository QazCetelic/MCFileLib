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
 * Object for viewing mod meta-data such as: name, description, version, dependancies, icon etc.
 */
class Mod(path: Path): FileEditable(path) {
    var name: String? = null
        private set
    var description: String? = null
        private set
    //I forgot if this is for the mc version or mod itself
    var version: String? = null
        private set
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
    var site: String? = null
        private set
    var iconLocation: String? = null
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
                        json.ifKey("version") { version = it.asString }
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
                        json.ifKey("icon") { iconLocation = json["icon"].asString }
                        json.ifKey("depends") {
                            val entries = mutableListOf<ModDependency>()
                            it.asJsonObject.entrySet().forEach { entry ->
                                //Don't get the depend as value as string
                                entries.add(ModDependency(entry))
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
                    val text = String(zipFile.getInputStream(entry).readBytes())
                    val data = Gson().fromJson(text, JsonElement::class.java)
                    when {
                        data.isJsonObject -> {
                            val jsonObject = data.asJsonObject
                            processForgeModJSON(jsonObject)
                        }
                        data.isJsonArray -> {
                            val jsonArray = data.asJsonArray
                            //it currently only takes the first entry, todo maybe process more
                            if (jsonArray.size() > 0) {
                                processForgeModJSON(jsonArray[0].asJsonObject)
                            }
                        }
                    }
                }
            }
            icon = if (iconLocation != null) {
                ImageIO.read(
                    zipFile.getInputStream(
                        zipFile.getEntry(iconLocation!!)
                    )
                )
            } else null
        } else {
            throw Exception("Invalid mod: Not a jar")
        }

        //Makes the lists read-only
        dependencies = Collections.unmodifiableList(dependencies)
        authors = Collections.unmodifiableList(authors)
    }

    private fun processForgeModJSON(jsonObject: JsonObject) {
        fun processForgeModListEntry(modListEntry: JsonObject) {
            if (modListEntry.has("modid"))          id = modListEntry["modid"].asString
            if (modListEntry.has("name"))           name = modListEntry["name"].asString
            if (modListEntry.has("description"))    description = modListEntry["description"].asString
            if (modListEntry.has("url"))            site = modListEntry["url"].asString
            if (modListEntry.has("authorList"))     modListEntry["authorList"].asJsonArray.forEach {
                                                                    authors.add(it.asString)
                                                                }
        }

        // The modinfo file is different for several mods, todo this could use a rework
        if (jsonObject.has("modinfoversion")) {
            //todo add support for other modinfoversions
            if (jsonObject["modinfoversion"].asInt != 2) throw Exception(jsonObject["modinfoversion"].asString + " is not supported!")
        }
        if (jsonObject.has("modlist")) {
            // only takes the first entry currently, todo reconsideration required
            processForgeModListEntry(jsonObject["modlist"].asJsonArray[0].asJsonObject)
        } else {
            processForgeModListEntry(jsonObject)
        }
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
     * Class that is used to more easily work with mod dependencies, basically a data class except for the fact that it cleans the input.
     */
    class ModDependency(input: MutableMap.MutableEntry<String, JsonElement>) {
        val name = input.key
        val requiredVersion = input.value.toString().removeSurrounding("\"")
    }
}