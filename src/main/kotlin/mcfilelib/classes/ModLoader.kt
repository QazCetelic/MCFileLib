package mcfilelib.classes

class ModLoader(
    name: String,
    version: String
) {
    //Todo use enum instead of string for type
    val name = name.capitalize()
    val version = version.removeSurrounding("\"", "\"")
}