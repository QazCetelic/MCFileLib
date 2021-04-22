/**
 * ------------------------------------------------------------
 *
 * This file is part of the FTB Pyramid Reborn Modpack for Minecraft
 * Copyright (c) 2018 Feed The Beast
 *
 * All Rights Reserved unless otherwise explicitly stated.
 *
 * ------------------------------------------------------------
 */

// Plate hammer crafting
recipes.addShapeless(<thermalfoundation:material:326>, [<immersiveengineering:tool>, <ore:ingotPlatinum>]);
recipes.addShapeless(<thermalfoundation:material:328>, [<immersiveengineering:tool>, <ore:ingotMithril>]);
recipes.addShapeless(<thermalfoundation:material:357>, [<immersiveengineering:tool>, <ore:ingotSignalum>]);
recipes.addShapeless(<thermalfoundation:material:354>, [<immersiveengineering:tool>, <ore:ingotInvar>]);
recipes.addShapeless(<thermalfoundation:material:359>, [<immersiveengineering:tool>, <ore:ingotEnderium>]);
recipes.addShapeless(<thermalfoundation:material:358>, [<immersiveengineering:tool>, <ore:ingotLumium>]);
recipes.addShapeless(<thermalfoundation:material:356>, [<immersiveengineering:tool>, <ore:ingotConstantan>]);
recipes.addShapeless(<thermalfoundation:material:327>, [<immersiveengineering:tool>, <ore:ingotIridium>]);
recipes.addShapeless(<thermalfoundation:material:353>, [<immersiveengineering:tool>, <ore:ingotElectrum>]);
recipes.addShapeless(<thermalfoundation:material:323>, [<immersiveengineering:tool>, <ore:ingotLead>]);
recipes.addShapeless(<thermalfoundation:material:325>, [<immersiveengineering:tool>, <ore:ingotNickel>]);
recipes.addShapeless(<thermalfoundation:material:324>, [<immersiveengineering:tool>, <ore:ingotAluminum>]);
recipes.addShapeless(<thermalfoundation:material:352>, [<immersiveengineering:tool>, <ore:ingotSteel>]);
recipes.addShapeless(<thermalfoundation:material:33>, [<immersiveengineering:tool>, <ore:ingotGold>]);
recipes.addShapeless(<thermalfoundation:material:322>, [<immersiveengineering:tool>, <ore:ingotSilver>]);
recipes.addShapeless(<thermalfoundation:material:321>, [<immersiveengineering:tool>, <ore:ingotTin>]);
recipes.addShapeless(<thermalfoundation:material:320>, [<immersiveengineering:tool>, <ore:ingotCopper>]);
recipes.addShapeless(<thermalfoundation:material:32>, [<immersiveengineering:tool>, <ore:ingotIron>]);

// Ore + Petrotheum now crafts 2 metal dust from TF
recipes.addShapeless(<thermalfoundation:material:768> * 2, [<minecraft:coal_ore>, <thermalfoundation:material:1027>]);
recipes.addShapeless(<thermalfoundation:material:67> * 2, [<thermalfoundation:ore:3>, <thermalfoundation:material:1027>]);
recipes.addShapeless(<thermalfoundation:material> * 2, [<minecraft:iron_ore>, <thermalfoundation:material:1027>]);
recipes.addShapeless(<thermalfoundation:material:1> * 2, [<minecraft:gold_ore>, <thermalfoundation:material:1027>]);
recipes.addShapeless(<thermalfoundation:material:64> * 2, [<thermalfoundation:ore>, <thermalfoundation:material:1027>]);
recipes.addShapeless(<thermalfoundation:material:65> * 2, [<thermalfoundation:ore:1>, <thermalfoundation:material:1027>]);
recipes.addShapeless(<thermalfoundation:material:66> * 2, [<thermalfoundation:ore:2>, <thermalfoundation:material:1027>]);

// Replace Immersive Engineering's Sulfur with TFs'

mods.immersiveengineering.Crusher.removeRecipe(<minecraft:blaze_powder>);
mods.immersiveengineering.Crusher.removeRecipe(<minecraft:dye:4>);
mods.immersiveengineering.Crusher.removeRecipe(<minecraft:quartz>);

mods.immersiveengineering.Crusher.addRecipe(<minecraft:blaze_powder> * 4, <minecraft:blaze_rod>, 3200, <thermalfoundation:material:771>, 0.5);
mods.immersiveengineering.Crusher.addRecipe(<minecraft:dye:4> * 9, <minecraft:lapis_ore>, 6000, <thermalfoundation:material:771>, 0.15);
mods.immersiveengineering.Crusher.addRecipe(<minecraft:quartz> * 3, <ore:oreQuartz>, 6000, <thermalfoundation:material:771>, 0.15);

// Remove obsidian tools, weapons and armour in favour of other
recipes.remove(<actuallyadditions:item_helm_obsidian>);
recipes.remove(<actuallyadditions:item_chest_obsidian>);
recipes.remove(<actuallyadditions:item_pants_obsidian>);
recipes.remove(<actuallyadditions:item_boots_obsidian>);
recipes.remove(<actuallyadditions:item_sword_obsidian>);
recipes.remove(<actuallyadditions:item_pickaxe_obsidian>);
recipes.remove(<actuallyadditions:item_shovel_obsidian>);
recipes.remove(<actuallyadditions:item_axe_obsidian>);
recipes.remove(<actuallyadditions:item_hoe_obsidian>);

// Adds Gear Crafting for all gears in the Metal Press

//mods.immersiveengineering.MetalPress.addRecipe(IItemStack output, IIngredient input, <immersiveengineering:mold:1>, 2400, @Optional int inputSize);
mods.immersiveengineering.MetalPress.addRecipe(<thermalfoundation:material:26>, <minecraft:diamond>, <immersiveengineering:mold:1>, 2400, 4);
mods.immersiveengineering.MetalPress.addRecipe(<thermalfoundation:material:27>, <minecraft:emerald>, <immersiveengineering:mold:1>, 2400, 4);
mods.immersiveengineering.MetalPress.addRecipe(<pneumaticcraft:compressed_iron_gear>, <pneumaticcraft:ingot_iron_compressed>, <immersiveengineering:mold:1>, 2400, 4);