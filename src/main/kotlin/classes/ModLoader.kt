package classes

class ModLoader(
    name: String,
    version: String
) {
    val name = name.capitalize()
    val version = version.removeSurrounding("\"", "\"")
}