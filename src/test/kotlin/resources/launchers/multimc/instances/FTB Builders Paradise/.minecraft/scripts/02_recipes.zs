import crafttweaker.item.IItemStack;

#Name: 02_recipes.zs
#Author: Feed the Beast and Sunekaer

print("Initializing '02_recipes.zs'...");

recipes.addShaped(<immersiveengineering:storage_slab> * 6, [
  [<ore:blockCopper>, <ore:blockCopper>, <ore:blockCopper>]
]);

recipes.addShaped(<immersiveengineering:storage_slab:1> * 6, [
  [<ore:blockAluminum>, <ore:blockAluminum>, <ore:blockAluminum>]
]);

recipes.addShaped(<immersiveengineering:storage_slab:2> * 6, [
  [<ore:blockLead>, <ore:blockLead>, <ore:blockLead>]
]);

recipes.addShaped(<immersiveengineering:storage_slab:3> * 6, [
  [<ore:blockSilver>, <ore:blockSilver>, <ore:blockSilver>]
]);

recipes.addShaped(<immersiveengineering:storage_slab:4> * 6, [
  [<ore:blockNickel>, <ore:blockNickel>, <ore:blockNickel>]
]);

recipes.addShaped(<immersiveengineering:storage_slab:6> * 6, [
  [<ore:blockConstantan>, <ore:blockConstantan>, <ore:blockConstantan>]
]);

recipes.addShaped(<immersiveengineering:storage_slab:7> * 6, [
  [<ore:blockElectrum>, <ore:blockElectrum>, <ore:blockElectrum>]
]);

recipes.addShaped(<immersiveengineering:storage_slab:8> * 6, [
  [<ore:blockSteel>, <ore:blockSteel>, <ore:blockSteel>]
]);

recipes.addShaped(<morebeautifulbuttons:button_press>, [
  [<minecraft:stick>, <minecraft:stick>, <minecraft:stick>],
  [<minecraft:stick>, <minecraft:wooden_button>, <minecraft:stick>],
  [<minecraft:stick>, <minecraft:stick>, <minecraft:stick>]
]);

recipes.addShaped(<littletiles:ltcoloredblock:13> * 6, [
  [<minecraft:planks>, <minecraft:planks>, <minecraft:planks>],
  [<minecraft:dye:15>, <minecraft:planks>, <minecraft:dye:15>],
  [<minecraft:planks>, <minecraft:planks>, <minecraft:planks>]
]);

recipes.addShaped(<minecraft:stick> * 16, [
  [<ore:logWood>],
  [<ore:logWood>]
]);

recipes.addShaped(<minecraft:stone_slab> * 6, [
  [<minecraft:stone>, <minecraft:stone>, <minecraft:stone>]
]);

recipes.addShaped(<rustic:crop_stake>, [
  [<ore:plankWood>],
  [<ore:stickWood>],
  [<ore:plankWood>]
]);

recipes.addShapeless(<rustic:painted_wood_white>, [<quark:stained_planks>]);
recipes.addShapeless(<rustic:painted_wood_orange>, [<quark:stained_planks:1>]);
recipes.addShapeless(<rustic:painted_wood_magenta>, [<quark:stained_planks:2>]);
recipes.addShapeless(<rustic:painted_wood_light_blue>, [<quark:stained_planks:3>]);
recipes.addShapeless(<rustic:painted_wood_yellow>, [<quark:stained_planks:4>]);
recipes.addShapeless(<rustic:painted_wood_lime>, [<quark:stained_planks:5>]);
recipes.addShapeless(<rustic:painted_wood_pink>, [<quark:stained_planks:6>]);
recipes.addShapeless(<rustic:painted_wood_gray>, [<quark:stained_planks:7>]);
recipes.addShapeless(<rustic:painted_wood_silver>, [<quark:stained_planks:8>]);
recipes.addShapeless(<rustic:painted_wood_cyan>, [<quark:stained_planks:9>]);
recipes.addShapeless(<rustic:painted_wood_purple>, [<quark:stained_planks:10>]);
recipes.addShapeless(<rustic:painted_wood_blue>, [<quark:stained_planks:11>]);
recipes.addShapeless(<rustic:painted_wood_brown>, [<quark:stained_planks:12>]);
recipes.addShapeless(<rustic:painted_wood_green>, [<quark:stained_planks:13>]);
recipes.addShapeless(<rustic:painted_wood_red>, [<quark:stained_planks:14>]);
recipes.addShapeless(<rustic:painted_wood_black>, [<quark:stained_planks:15>]);

recipes.addShaped(<appliedenergistics2:material:13>, [
  [<ore:ingotIron>, <ore:ingotIron>, <ore:ingotIron>],
  [<ore:ingotIron>, <ore:crystalPureCertusQuartz>, <ore:ingotIron>],
  [<ore:ingotIron>, <ore:ingotIron>, <ore:ingotIron>]
]);

recipes.addShaped(<appliedenergistics2:material:14>, [
  [<ore:ingotIron>, <ore:ingotIron>, <ore:ingotIron>],
  [<ore:ingotIron>, <ore:gemDiamond>, <ore:ingotIron>],
  [<ore:ingotIron>, <ore:ingotIron>, <ore:ingotIron>]
]);

recipes.addShaped(<appliedenergistics2:material:15>, [
  [<ore:ingotIron>, <ore:ingotIron>, <ore:ingotIron>],
  [<ore:ingotIron>, <ore:ingotGold>, <ore:ingotIron>],
  [<ore:ingotIron>, <ore:ingotIron>, <ore:ingotIron>]
]);

recipes.addShaped(<appliedenergistics2:material:19>, [
  [<ore:ingotIron>, <ore:ingotIron>, <ore:ingotIron>],
  [<ore:ingotIron>, <ore:itemSilicon>, <ore:ingotIron>],
  [<ore:ingotIron>, <ore:ingotIron>, <ore:ingotIron>]
]);

recipes.addShaped(<appliedenergistics2:material:21>.withTag({}), [
  [<ore:ingotIron>, <ore:ingotIron>, <ore:ingotIron>],
  [<ore:ingotIron>, <minecraft:name_tag>, <ore:ingotIron>],
  [<ore:ingotIron>, <ore:ingotIron>, <ore:ingotIron>]
]);

recipes.addShaped(<minecraft:name_tag>, [
  [<ore:dyeBlack>, <minecraft:leather>],
  [<ore:string>]
]);

recipes.addShaped(<appliedenergistics2:sky_stone_block>, [
  [<ore:coal>, <ore:coal>, <ore:coal>],
  [<minecraft:lava_bucket>, <ore:stone>, <minecraft:lava_bucket>],
  [<ore:coal>, <ore:coal>, <ore:coal>]
]);

//BoP and TC Amber converstion recipe
recipes.addShapeless(<biomesoplenty:gem:7>, [<thaumcraft:amber>]);
recipes.addShapeless(<thaumcraft:amber>, [<biomesoplenty:gem:7>]);
recipes.addShapeless(<biomesoplenty:gem_block:7>, [<thaumcraft:amber_block>]);
recipes.addShapeless(<thaumcraft:amber_block>, [<biomesoplenty:gem_block:7>]);


recipes.addShaped(<cfm:hedge_oak>, [
  [<minecraft:leaves>,<ore:stickWood>,<minecraft:leaves>],
  [<minecraft:leaves>,<ore:stickWood>,<minecraft:leaves>]
]);

recipes.addShaped(<cfm:hedge_spruce>, [
  [<minecraft:leaves:1>,<ore:stickWood>,<minecraft:leaves:1>],
  [<minecraft:leaves:1>,<ore:stickWood>,<minecraft:leaves:1>]
]);

recipes.addShaped(<cfm:hedge_birch>, [
  [<minecraft:leaves:2>,<ore:stickWood>,<minecraft:leaves:2>],
  [<minecraft:leaves:2>,<ore:stickWood>,<minecraft:leaves:2>]
]);

recipes.addShaped(<cfm:hedge_jungle>, [
  [<minecraft:leaves:3>,<ore:stickWood>,<minecraft:leaves:3>],
  [<minecraft:leaves:3>,<ore:stickWood>,<minecraft:leaves:3>]
]);

recipes.addShaped(<cfm:hedge_acacia>, [
  [<minecraft:leaves2>,<ore:stickWood>,<minecraft:leaves2>],
  [<minecraft:leaves2>,<ore:stickWood>,<minecraft:leaves2>]
]);

recipes.addShaped(<cfm:hedge_dark_oak>, [
  [<minecraft:leaves2:1>,<ore:stickWood>,<minecraft:leaves2:1>],
  [<minecraft:leaves2:1>,<ore:stickWood>,<minecraft:leaves2:1>]
]);

recipes.addShaped(<littletiles:lttransparentcoloredblock:2>, [
[null, null, null],
[<minecraft:stained_glass>, <minecraft:stained_glass>, <minecraft:stained_glass>],
[null, null, null]
]);

recipes.addShaped(<marblecraftingtable:marble_crafting_table>, [
[null, <ore:stoneMarble>, null],
[<ore:stoneMarble>, <ore:workbench>, <ore:stoneMarble>],
[null, <ore:stoneMarble>, null]
]);

print("Initialized '02_recipes.zs'");
