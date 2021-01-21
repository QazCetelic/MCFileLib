# MCFileLib

### A library written in Kotlin to get meta-data from Minecraft related files
Currently in Alpha

- ModPacks (or Instances)
- Mods
  + Fabric Mods
  + Forge Mods (modify format 2 only now)
- DataPacks
  + PackData, for extracting metadata from (zipped) folders
- Packs
  + ResourcePacks
  + DataPacks
- Launchers
  + Returning a path when given a launcher type _Launchers.fromType()_
  + Returning a Launcher type when given a path to the **main folder of the launcher** _Launchers.fromPath()_
  + A function that returns a list with all installed launchers _Launchers.getAll()_
- Worlds
- Screenshots
- VersionConverter
  + Converting a Minecraft version to a ResourcePack format
  + Converting a ResourcePack format to a Minecraft version range (for example: 5 is for 1.15-1.16.1)
- ModLoaders
- Configs

There are also several objects for deserializing JSON data *(main.json)*

## All objects are currently read-only, editing objects will probably be added later
