package mcfilelib.generic

import mcfilelib.json.MCVersion
import mcfilelib.json.ModVersion
import mcfilelib.util.ModDependency
import mcfilelib.util.ModType
import mcfilelib.util.loadModMetadata
import neatlin.hash.Hash
import neatlin.hash.invoke
import neatlin.io.isEmpty
import neatlin.io.toPath
import java.nio.file.Files
import java.nio.file.Path

/**
 * Object for viewing mod meta-data such as: name, description, version, dependencies, icon etc.
 */
class Mod(path: Path) {
    var path = path
        private set

    // Otherwise it's renamed when it checks it when creating the object
    private var disabledFirstSet = true
    var disabled = path.toFile().endsWith(".disabled")
        set(value) {
            // Doesn't rename the file when changing the disabled property on the first set
            if (!disabledFirstSet) {
                val oldPath = path
                if (value != field) {
                    path = when (value) {
                        true -> "$path.disabled".toPath()
                        false -> "$path".removeSuffix(".disabled").toPath()
                    }
                    Files.move(oldPath, path)
                }
            }
            else disabledFirstSet = false
            field = value
        }

    val id: String?
    val name: String?
    val description: String?
    val clientSide: Boolean?
    /**
     * What kind of mod it is, like Forge, Fabric, Rift or LiteLoader
     */
    val type: ModType
    val authors: List<String>
    val dependencies: List<ModDependency>
    val modVersion: ModVersion
    val mcVersion: MCVersion
    val modloaderVersion: String?
    val license: String?
    val credits: String?
    val email: String?
    val issues: String?
    val sources: String?
    /**
     * Mod's web site, forum thread, info/support page or anything on the web that's like that.
     * The mod users can click on this link in-game and it will take them to the specified link.
     */
    val site: String?
    /**
     * This is only used by older mods, new ones don't have it anymore (not confirmed yet)
     */
    val updateURL: String?
    val updateJsonURL: String?
    /**
     * Relative path to the icon
     */
    val iconPath: String?

    init {
        val modFile = path.toFile()

        // Some launchers disable mods by renaming it to disable so the game doesn't recognize them
        disabled = (modFile.extension == "disabled")

        // Checks if file is valid
        when {
            !modFile.exists() -> throw Exception("$modFile doesn't exist")
            // Checks if it's actually in a mod file format (".jar")
            !(modFile.extension == "jar" || modFile.name.endsWith(".jar.disabled")) -> throw Exception("Not a jar")
            // Checks if the zip file isn't empty
            modFile.isEmpty() -> throw Exception("$modFile is empty")
        }

        loadModMetadata(modFile).let { 
            id                  = it.id
            name                = it.name
            description         = it.description
            clientSide          = it.clientSide
            type                = it.type
            authors             = it.authors
            dependencies        = it.dependencies
            modVersion          = it.modVersion
            mcVersion           = it.mcVersion
            modloaderVersion    = it.modloaderVersion
            license             = it.license
            credits             = it.credits
            email               = it.email
            issues              = it.issues
            sources             = it.sources
            site                = it.site
            updateURL           = it.updateURL
            updateJsonURL       = it.updateJsonURL
            iconPath            = it.iconPath
        }
    }

    /**
     * Gets the SHA-256 hash of the mod
     */
    fun generateHash() = Hash.SHA256(path.toFile())

    fun toModMetadata() = ModMetadata(
        id,
        name,
        description,
        clientSide,
        type,
        authors,
        dependencies,
        modVersion,
        mcVersion,
        modloaderVersion,
        license,
        credits,
        email,
        issues,
        sources,
        site,
        updateURL,
        updateJsonURL,
        iconPath,
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