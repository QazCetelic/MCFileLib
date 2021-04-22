#Name: Modtweaker.zs
#Author: Feed the Beast and Sunekaer

print("Initializing 'Modtweaker.zs'...");

mods.thermalexpansion.Centrifuge.removeRecipe(<forestry:bee_combs>);
mods.thermalexpansion.Centrifuge.addRecipe([<forestry:beeswax>, <forestry:honey_drop> % 90], <forestry:bee_combs>, null, 2000);
mods.thermalexpansion.Centrifuge.addRecipe([(<minecraft:gold_ingot> * 5) % 10, <minecraft:iron_ingot> % 50], <minecraft:gold_ore>, <liquid:lava>, 2000);

print("Initialized 'Modtweaker.zs'");
