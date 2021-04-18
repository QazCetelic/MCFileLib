package mcfilelib.util

/**
 * Enums for the different types of mods
 */
enum class ModType {
    Fabric,
    Forge,
    // TODO implement LiteLoader and Rift
    LiteLoader,
    Rift,

    Unknown;
}