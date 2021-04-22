#Name: RecipeChanges.zs
#Author: Feed the Beast

print("Initializing 'RecipeChanges.zs'...");

	#Initial Inventory
mods.initialinventory.InvHandler.addStartingItem(<ftbquests:book>);

	#Remove ChunkLoaders (use FTBU to manage chunkloading)
recipes.remove(<ic2:te:82>);

	#Atempt to fix TiC grout shapless recipe
recipes.addShapeless(<tconstruct:soil> * 2, [<minecraft:sand>, <minecraft:gravel>, <minecraft:clay_ball>]);

	#Fix wood gear recipe in grindstone
recipes.remove(<appliedenergistics2:material:40>);
recipes.addShaped(<appliedenergistics2:material:40>,
	[
		[null, <ore:stickWood>, null],
		[<ore:stickWood>, null, <ore:stickWood>],
		[null, <ore:stickWood>, null]
	]
);

	#Remove ExU2 Sickles to prevent overlap with Thermal Foundation Sickles
recipes.remove(<extrautils2:sickle_wood>);
recipes.remove(<extrautils2:sickle_stone>);
recipes.remove(<extrautils2:sickle_iron>);
recipes.remove(<extrautils2:sickle_gold>);
recipes.remove(<extrautils2:sickle_diamond>);

	#Items removed due to bugs
recipes.remove(<actuallyadditions:item_bag>);
<actuallyadditions:item_bag>.addTooltip(format.darkRed("Disabled due to bug!"));
recipes.remove(<extrautils2:bagofholding>);
<extrautils2:bagofholding>.addTooltip(format.darkRed("Disabled due to bug!"));

	#Reduce crafting of bronze ingots to 2 from 4 in crafting table, various machine outputs are still 4
recipes.remove(<forestry:ingot_bronze>);
recipes.addShapeless(<thermalfoundation:material:163> * 2,
	[<ore:ingotCopper>, <ore:ingotCopper>, <ore:ingotCopper>, <ore:ingotTin>]
);

	#Fix bronze gear recipe
recipes.addShaped(<thermalfoundation:material:291>,
	[
		[null, <ore:ingotBronze>, null],
		[<ore:ingotBronze>, <minecraft:iron_ingot>, <ore:ingotBronze>],
		[null, <ore:ingotBronze>, null]
	]
);

	#Experience Solidifier
recipes.remove(<actuallyadditions:block_xp_solidifier>);
recipes.addShaped(<actuallyadditions:block_xp_solidifier>,
	[
		[<minecraft:emerald>, <minecraft:emerald>, <minecraft:emerald>],
		[<actuallyadditions:block_crystal_empowered:2>, <actuallyadditions:item_misc:8>, <actuallyadditions:block_crystal_empowered:2>],
		[<minecraft:emerald>, <minecraft:emerald>, <minecraft:emerald>]
	]
);

	#Architecture Craft Pulley
recipes.remove(<architecturecraft:largepulley>);
recipes.addShaped(<architecturecraft:largepulley>,
	[
		[null, <ore:slabWood>, null],
		[<ore:slabWood>, <ore:plankWood>, <ore:slabWood>],
		[null, <ore:slabWood>, null]
	]
);

	#Removed Charcoal Block (chisel) to allow AA's version be default which is registered as a fuel type
recipes.remove(<chisel:block_charcoal2:1>);

	#flint recipe
recipes.addShapeless(<minecraft:flint>,
	[<minecraft:gravel>,<minecraft:gravel>,<minecraft:gravel>]
);

	#Fix C&B Wrench Recipe conflict
recipes.remove(<chiselsandbits:wrench_wood>);
recipes.addShaped(<chiselsandbits:wrench_wood>,
	[
		[<minecraft:planks>, null, <minecraft:planks>],
		[null, <minecraft:stick>, null],
		[null, <minecraft:stick>, null]
	]
);

	#project e collectors
recipes.remove(<projecte:collector_mk3>);
recipes.remove(<projecte:collector_mk2>);
recipes.remove(<projecte:collector_mk1>);

recipes.addShaped(<projecte:collector_mk3>, 
	[
		[<minecraft:glowstone>, <thermalfoundation:glass:7>, <minecraft:glowstone>],
		[<minecraft:glowstone>, <ic2:nuclear:10>, <minecraft:glowstone>],
		[<minecraft:glowstone>, <projecte:collector_mk2>, <minecraft:glowstone>]
	]
);

recipes.addShaped(<projecte:collector_mk2>,
	[
		[<minecraft:glowstone>, <thermalfoundation:glass_alloy:7>, <minecraft:glowstone>],
		[<minecraft:glowstone>, <draconicevolution:draconic_block>, <minecraft:glowstone>],
		[<minecraft:glowstone>, <projecte:collector_mk1>, <minecraft:glowstone>]
	]
);

recipes.addShaped(<projecte:collector_mk1>,
	[
		[<minecraft:glowstone>, <thermalfoundation:glass_alloy:5>, <minecraft:glowstone>],
		[<minecraft:glowstone>, <botania:storage:1>, <minecraft:glowstone>],
		[<minecraft:glowstone>, <projecte:rm_furnace>, <minecraft:glowstone>]
	]
);

	#swiftwolf flying ring
recipes.remove(<projecte:item.pe_swrg>);

recipes.addShaped(<projecte:item.pe_swrg>,
	[
		[<simplyjetpacks:metaitemmods:10>, <projecte:item.pe_matter:1>, <simplyjetpacks:metaitemmods:10>],
		[<projecte:item.pe_matter:1>, <projecte:item.pe_ring_iron_band>, <projecte:item.pe_matter:1>],
		[<simplyjetpacks:metaitemmods:30>, <projecte:item.pe_matter:1>, <simplyjetpacks:metaitemmods:30>]
	]
);

	#fix RTG Fuel recipes
recipes.remove(<ic2:nuclear:10>);

recipes.addShaped(<ic2:nuclear:10>,
	[
		[<ic2:plate:12>, <ic2:nuclear:3>, <ic2:plate:12>],
		[<ic2:plate:12>, <ic2:nuclear:3>, <ic2:plate:12>],
		[<ic2:plate:12>, <ic2:nuclear:3>, <ic2:plate:12>]
	]
);

recipes.addShaped(<ic2:nuclear:10>,
	[
		[<ic2:plate:12>, <ic2:plate:12>, <ic2:plate:12>],
		[<ic2:nuclear:3>, <ic2:nuclear:3>, <ic2:nuclear:3>],
		[<ic2:plate:12>, <ic2:plate:12>, <ic2:plate:12>]
	]
);

	#alt iridium reinforced plate recipe
recipes.addShaped(<ic2:crafting:4>,
	[
		[<thermalfoundation:material:135>, <ic2:crafting:3>, <thermalfoundation:material:135>],
		[<ic2:crafting:3>, <minecraft:diamond>, <ic2:crafting:3>],
		[<thermalfoundation:material:135>, <ic2:crafting:3>, <thermalfoundation:material:135>]
	]
);

	#bee hive recipes

mods.forestry.Carpenter.addRecipe(<extrabees:hive>,
 	[
 		[<minecraft:potion>.withTag({Potion: "minecraft:water"}),<minecraft:potion>.withTag({Potion: "minecraft:water"}),<minecraft:potion>.withTag({Potion: "minecraft:water"})],
 		[<minecraft:potion>.withTag({Potion: "minecraft:water"}),<minecraft:hay_block>,<minecraft:potion>.withTag({Potion: "minecraft:water"})],
 		[<minecraft:potion>.withTag({Potion: "minecraft:water"}),<minecraft:potion>.withTag({Potion: "minecraft:water"}),<minecraft:potion>.withTag({Potion: "minecraft:water"})]
 	], 
 	60, <liquid:seed.oil> * 1000
);

mods.forestry.Carpenter.addRecipe(<extrabees:hive:1>,
 	[
 		[<ore:stone>,<ore:stone>,<ore:stone>],
 		[<ore:stone>,<minecraft:hay_block>,<ore:stone>],
 		[<ore:stone>,<ore:stone>,<ore:stone>]
 	], 
 	60, <liquid:seed.oil> * 1000
);

mods.forestry.Carpenter.addRecipe(<extrabees:hive:2>,
 	[
 		[<minecraft:netherrack>,<minecraft:netherrack>,<minecraft:netherrack>],
 		[<minecraft:netherrack>,<minecraft:hay_block>,<minecraft:netherrack>],
 		[<minecraft:netherrack>,<minecraft:netherrack>,<minecraft:netherrack>]
 	], 
 	60, <liquid:seed.oil> * 1000
);

mods.forestry.Carpenter.addRecipe(<extrabees:hive:3>,
 	[
 		[<ore:stoneMarble>,<ore:stoneMarble>,<ore:stoneMarble>],
 		[<ore:stoneMarble>,<minecraft:hay_block>,<ore:stoneMarble>],
 		[<ore:stoneMarble>,<ore:stoneMarble>,<ore:stoneMarble>]
 	], 
 	60, <liquid:seed.oil> * 1000
);

mods.forestry.Carpenter.addRecipe(<forestry:beehives>,
 	[
 		[<ore:treeSapling>,<ore:treeSapling>,<ore:treeSapling>],
 		[<ore:treeSapling>,<minecraft:hay_block>,<ore:treeSapling>],
 		[<ore:treeSapling>,<ore:stoneMarble>,<ore:treeSapling>]
 	], 
 	60, <liquid:seed.oil> * 1000
);

mods.forestry.Carpenter.addRecipe(<forestry:beehives:1>,
 	[
 		[<ore:flower>,<ore:flower>,<ore:flower>],
 		[<ore:flower>,<minecraft:hay_block>,<ore:flower>],
 		[<ore:flower>,<ore:flower>,<ore:flower>]
 	], 
 	60, <liquid:seed.oil> * 1000
);

mods.forestry.Carpenter.addRecipe(<forestry:beehives:2>,
 	[
 		[<ore:sand>,<ore:sand>,<ore:sand>],
 		[<ore:sand>,<minecraft:hay_block>,<ore:sand>],
 		[<ore:sand>,<ore:sand>,<ore:sand>]
 	], 
 	60, <liquid:seed.oil> * 1000
);

mods.forestry.Carpenter.addRecipe(<forestry:beehives:3>,
 	[
 		[<minecraft:dye:3>,<minecraft:dye:3>,<minecraft:dye:3>],
 		[<minecraft:dye:3>,<minecraft:hay_block>,<minecraft:dye:3>],
 		[<minecraft:dye:3>,<minecraft:dye:3>,<minecraft:dye:3>]
 	], 
 	60, <liquid:seed.oil> * 1000
);

mods.forestry.Carpenter.addRecipe(<forestry:beehives:4>,
 	[
 		[<minecraft:ender_pearl>,<minecraft:ender_pearl>,<minecraft:ender_pearl>],
 		[<minecraft:ender_pearl>,<minecraft:hay_block>,<minecraft:ender_pearl>],
 		[<minecraft:ender_pearl>,<minecraft:ender_pearl>,<minecraft:ender_pearl>]
 	], 
 	60, <liquid:seed.oil> * 1000
);

mods.forestry.Carpenter.addRecipe(<forestry:beehives:5>,
 	[
 		[<minecraft:snowball>,<minecraft:snowball>,<minecraft:snowball>],
 		[<minecraft:snowball>,<minecraft:hay_block>,<minecraft:snowball>],
 		[<minecraft:snowball>,<minecraft:snowball>,<minecraft:snowball>]
 	], 
 	60, <liquid:seed.oil> * 1000
);

mods.forestry.Carpenter.addRecipe(<forestry:beehives:6>,
 	[
 		[<minecraft:vine>,<minecraft:vine>,<minecraft:vine>],
 		[<minecraft:vine>,<minecraft:hay_block>,<minecraft:vine>],
 		[<minecraft:vine>,<minecraft:vine>,<minecraft:vine>]
 	], 
 	60, <liquid:seed.oil> * 1000
);

mods.forestry.Carpenter.addRecipe(<magicbees:hiveblock>,
 	[
 		[<minecraft:rotten_flesh>,<minecraft:rotten_flesh>,<minecraft:rotten_flesh>],
 		[<minecraft:rotten_flesh>,<minecraft:hay_block>,<minecraft:rotten_flesh>],
 		[<minecraft:rotten_flesh>,<minecraft:rotten_flesh>,<minecraft:rotten_flesh>]
 	], 
 	60, <liquid:seed.oil> * 1000
);

mods.forestry.Carpenter.addRecipe(<magicbees:hiveblock:1>,
 	[
 		[<minecraft:dye:15>,<minecraft:dye:15>,<minecraft:dye:15>],
 		[<minecraft:dye:15>,<minecraft:hay_block>,<minecraft:dye:15>],
 		[<minecraft:dye:15>,<minecraft:dye:15>,<minecraft:dye:15>]
 	], 
 	60, <liquid:seed.oil> * 1000
);

mods.forestry.Carpenter.addRecipe(<magicbees:hiveblock:2>,
 	[
 		[<thermalfoundation:material:231>,<thermalfoundation:material:231>,<thermalfoundation:material:231>],
 		[<thermalfoundation:material:231>,<minecraft:hay_block>,<thermalfoundation:material:231>],
 		[<thermalfoundation:material:231>,<thermalfoundation:material:231>,<thermalfoundation:material:231>]
 	], 
 	60, <liquid:seed.oil> * 1000
);

mods.forestry.Carpenter.addRecipe(<magicbees:hiveblock:3>,
 	[
 		[<minecraft:redstone>,<minecraft:redstone>,<minecraft:redstone>],
 		[<minecraft:redstone>,<minecraft:hay_block>,<minecraft:redstone>],
 		[<minecraft:redstone>,<minecraft:redstone>,<minecraft:redstone>]
 	], 
 	60, <liquid:seed.oil> * 1000
);

mods.forestry.Carpenter.addRecipe(<magicbees:hiveblock:4>,
 	[
 		[<minecraft:glowstone_dust>,<minecraft:glowstone_dust>,<minecraft:glowstone_dust>],
 		[<minecraft:glowstone_dust>,<minecraft:hay_block>,<minecraft:glowstone_dust>],
 		[<minecraft:glowstone_dust>,<minecraft:glowstone_dust>,<minecraft:glowstone_dust>]
 	], 
 	60, <liquid:seed.oil> * 1000
);

mods.forestry.Carpenter.addRecipe(<magicbees:hiveblock:5>,
 	[
 		[<minecraft:ender_eye>,<minecraft:ender_eye>,<minecraft:ender_eye>],
 		[<minecraft:ender_eye>,<minecraft:hay_block>,<minecraft:ender_eye>],
 		[<minecraft:ender_eye>,<minecraft:ender_eye>,<minecraft:ender_eye>]
 	], 
 	60, <liquid:seed.oil> * 1000
);

	#currency fixes
recipes.addShaped(<modcurrency:coin:3> * 4,
	[
		[<modcurrency:banknote>, null, null],
		[null, null, null],
		[null, null, null]
	]
);

recipes.addShaped(<modcurrency:banknote>,
	[
		[<modcurrency:coin:3>, null, <modcurrency:coin:3>],
		[null, null, null],
		[<modcurrency:coin:3>, null, <modcurrency:coin:3>]
	]
);

recipes.addShaped(<modcurrency:coin:4>,
	[
		[null, null, null],
		[null, <modcurrency:banknote>, null],
		[null, null, null]
	]
);

recipes.addShaped(<modcurrency:banknote>,
	[
		[null, null, null],
		[null, <modcurrency:coin:4>, null],
		[null, null, null]
	]
);

recipes.addShaped(<modcurrency:banknote:1>,
	[
		[<modcurrency:banknote>, null, <modcurrency:banknote>],
		[null, <modcurrency:banknote>, null],
		[<modcurrency:banknote>, null, <modcurrency:banknote>]
	]
);

recipes.addShaped(<modcurrency:banknote> * 5,
	[
		[null, null, null],
		[null, null, null],
		[null, null, <modcurrency:banknote:1>]
	]
);

	#Dark Soularium Recipe Fix
recipes.remove(<simplyjetpacks:metaitemmods:3>);

recipes.addShaped(<simplyjetpacks:metaitemmods:3>, 
	[
		[<enderio:item_alloy_ingot:6>, null, <enderio:item_alloy_ingot:7>],
		[null, <enderio:item_material:14>, null],
		[<enderio:item_alloy_ingot:7>, null, <enderio:item_alloy_ingot:6>]
	]
);

	#RS 64x disk
recipes.addShapeless(<refinedstorage:fluid_storage_part>, [<refinedstorage:fluid_storage_disk>]);
recipes.addShapeless(<refinedstorage:storage_part:3>, [<refinedstorage:storage_disk:3>]);

	#vanilla egg
mods.forestry.Carpenter.addRecipe(<minecraft:egg>,
 	[
 		[<minecraft:wheat_seeds>,<minecraft:wheat_seeds>,<minecraft:wheat_seeds>],
 		[<minecraft:wheat>,<minecraft:wheat>,<minecraft:wheat>],
 		[<minecraft:wheat_seeds>,<minecraft:wheat_seeds>,<minecraft:wheat_seeds>]
 	], 
 	60, <liquid:water> * 1000
);

	#quest book
recipes.addShapeless(<ftbquests:book>, [<minecraft:book>,<minecraft:dye:1>]);

print("Initialized 'RecipeChanges.zs'");