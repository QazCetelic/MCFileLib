package classes

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import json.FabricContactData
import util.FileEditable
import java.awt.Image
import java.nio.file.Path
import java.util.*
import java.util.zip.ZipFile
import javax.imageio.ImageIO

class Mod(path: Path): FileEditable(path) {
    lateinit var name: String
        private set
    lateinit var description: String
        private set
    //I forgot if this is for the mc version or mod itself
    lateinit var version: String
        private set
    lateinit var id: String
        private set
    lateinit var license: String
        private set
    lateinit var email: String
        private set
    lateinit var issues: String
        private set
    lateinit var sources: String
        private set
    lateinit var site: String
        private set
    lateinit var iconLocation: String
        private set

    var authors: MutableList<String> = ArrayList<String>()
        private set
    var dependencies: MutableList<ModDependency> = ArrayList<ModDependency>()
        private set

    var type = ModType.Unknown
        private set
    var icon: Image?
        private set
    var disabled: Boolean
        private set
    var clientSide: Boolean? = null
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
                    if (!this::license.isInitialized) license = String(zipFile.getInputStream(entry).readBytes())
                }

                //Gets Fabric mod data
                if (entry.name == "fabric.mod.json") {
                    type = ModType.Fabric
                    val text = String(zipFile.getInputStream(entry).readBytes())
                    val data = Gson().fromJson(text, JsonObject::class.java)
                    //Im doing it like this because the json data is different in some mods and I don't know why, this seems the most reliable option
                    if (data.has("schemaVersion") && data["schemaVersion"].asInt == 1) {
                        if (data.has("name")) name = data["name"].asString
                        if (data.has("description")) description = data["description"].asString
                        if (data.has("id")) id = data["id"].asString
                        if (data.has("version")) version = data["version"].asString
                        if (data.has("authors")) {
                            val jsonArray = data["authors"].asJsonArray.toList()
                            jsonArray.forEach {
                                authors.add(it.asString)
                            }
                        }
                        if (data.has("contact")) {
                            val contactJson = data["contact"].toString()
                            val contactData = Gson().fromJson(contactJson, FabricContactData::class.java)
                            email = contactData.email
                            issues = contactData.issues
                            sources = contactData.sources
                        }
                        if (data.has("icon")) iconLocation = data["icon"].asString
                        if (data.has("depends")) {
                            val dependsData = data["depends"].asJsonObject
                            val entries = ArrayList<ModDependency>()
                            dependsData.entrySet().forEach {
                                //Don't get the depend as value as string
                                entries.add(ModDependency(it))
                            }
                            dependencies = entries
                        }
                        if (data.has("custom") && data["custom"].asJsonObject.has("modmenu:clientsideOnly")) {
                            clientSide = data["custom"].asJsonObject["modmenu:clientsideOnly"].asBoolean
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
                    /*
                    */
                }
            }
            icon = if (this::iconLocation.isInitialized) {
                ImageIO.read(
                    zipFile.getInputStream(
                        zipFile.getEntry(iconLocation)
                    )
                )
            } else null
            
        } else {
            throw Exception("Invalid mod: Not a jar")
        }

        //Makes the ArrayLists read-only
        dependencies = Collections.unmodifiableList(dependencies)
        authors = Collections.unmodifiableList(authors)

        //check if late init vars are initialized and if not set error message
        //todo consider using less lateinit vars
        if (!this::name.isInitialized)          name = "Unable to load name"
        if (!this::description.isInitialized)   description = ""
        if (!this::version.isInitialized)       version = "Unable to load version"
        if (!this::id.isInitialized)            id = "Unable to load id"
        if (!this::license.isInitialized)       license = "Unable to load license"
        if (!this::email.isInitialized)         email = "Unable to load contact email"
        if (!this::issues.isInitialized)        issues = "Unable to load contact information for issues"
        if (!this::sources.isInitialized)       sources = "Unable to load source code location"
        if (!this::site.isInitialized)          site = "Unable to load site url"
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

    enum class ModType {
        Fabric,
        Forge,
        Unknown;
    }

    class ModDependency(input: MutableMap.MutableEntry<String, JsonElement>) {
        val name = input.key
        val requiredVersion = input.value.toString().removeSurrounding("\"")
    }
}