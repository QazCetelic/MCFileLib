# MCFileLib

### A library written in Kotlin to get meta-data from Minecraft related files
Currently in Alpha

[Examples can be found on the wiki](https://github.com/QazCetelic/MCFileLib/wiki/Examples)

- ModPacks (or Instances)
- Mods
  + Fabric Mods
  + Forge Mods (Modern format only)
- DataPacks
  + PackData, for extracting metadata from (zipped) folders
- Packs
  + ResourcePacks
  + DataPacks
- Launchers
  + Get a launcher object from the type when it exists
  + Get installed launchers
  + Get the type when given a path
  + Should work with:
    - MultiMC
    - GDLauncher
    - Technic
    - of course vanilla too
- Worlds
- Screenshots
- VersionConverter
  + Converting a Minecraft version to a ResourcePack format
  + Converting a ResourcePack format to a Minecraft version range (for example: 5 is for 1.15-1.16.1)
- ModLoaders
- Configs

There are also several objects for deserializing JSON data *(main.json)*

## All objects are currently read-only, editing objects will probably be added later
