package mcfilelib.generic

import mcfilelib.json.MCVersion
import mcfilelib.json.ModVersion
import mcfilelib.util.ModDependency
import mcfilelib.util.ModType

class ModMetadata {
    val id: String?
    val name: String?
    val description: String?
    val clientSide: Boolean?
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
    val site: String?
    val updateURL: String?
    val updateJsonURL: String?
    val iconPath: String?

    constructor(
        id: String?,
        name: String?,
        description: String?,
        clientSide: Boolean?,
        type: ModType,
        authors: List<String>,
        dependencies: List<ModDependency>,
        modVersion: ModVersion,
        mcVersion: MCVersion,
        modloaderVersion: String?,
        license: String?,
        credits: String?,
        email: String?,
        issues: String?,
        sources: String?,
        site: String?,
        updateURL: String?,
        updateJsonURL: String?,
        iconPath: String?,
    ) {
        this.id                 = id
        this.name               = name
        this.description        = description
        this.clientSide         = clientSide
        this.type               = type
        this.authors            = authors
        this.dependencies       = dependencies
        this.modVersion         = modVersion
        this.mcVersion          = mcVersion
        this.modloaderVersion   = modloaderVersion
        this.license            = license
        this.credits            = credits
        this.email              = email
        this.issues             = issues
        this.sources            = sources
        this.site               = site
        this.updateURL          = updateURL
        this.updateJsonURL      = updateJsonURL
        this.iconPath           = iconPath
    }
}