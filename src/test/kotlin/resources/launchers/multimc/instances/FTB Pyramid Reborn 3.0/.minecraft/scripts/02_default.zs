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
 
//Book Crate
mods.jei.JEI.addItem(<immersiveengineering:wooden_device0>.withTag({display: {Name: "Book Crate"}, inventory: [{Slot: 0 as byte, id: "immersiveengineering:tool", Count: 1 as byte, Damage: 3 as short}, {Slot: 1 as byte, id: "draconicevolution:info_tablet", Count: 1 as byte, Damage: 0 as short}, {Slot: 2 as byte, id: "actuallyadditions:item_booklet", Count: 1 as byte, Damage: 0 as short}, {Slot: 3 as byte, id: "extrautils2:book", Count: 1 as byte, Damage: 0 as short}, {Slot: 4 as byte, id: "botania:lexicon", Count: 1 as byte, tag: {}, Damage: 0 as short}, {Slot: 5 as byte, id: "integrateddynamics:on_the_dynamics_of_integration", Count: 1 as byte, Damage: 0 as short}, {Slot: 6 as byte, id: "theoneprobe:probenote", Count: 1 as byte, Damage: 0 as short}, {Slot: 7 as byte, id: "xnet:xnet_manual", Count: 1 as byte, Damage: 0 as short}, {Slot: 8 as byte, id: "rftools:rftools_shape_manual", Count: 1 as byte, Damage: 0 as short}, {Slot: 9 as byte, id: "rftools:rftools_manual", Count: 1 as byte, Damage: 0 as short}, {Slot: 10 as byte, id: "minecraft:book", Count: 1 as byte, tag: {Guide: "", display: {Name: "Guide Book"}}, Damage: 0 as short}, {Slot: 11 as byte, id: "industrialforegoing:book_manual", Count: 1 as byte, Damage: 0 as short}, {Slot: 12 as byte, id: "forestry:book_forester", Count: 1 as byte, Damage: 0 as short}]}));
mods.jei.JEI.addDescription(<immersiveengineering:wooden_device0>.withTag({display: {Name: "Book Crate"}, inventory: [{Slot: 0 as byte, id: "immersiveengineering:tool", Count: 1 as byte, Damage: 3 as short}, {Slot: 1 as byte, id: "draconicevolution:info_tablet", Count: 1 as byte, Damage: 0 as short}, {Slot: 2 as byte, id: "actuallyadditions:item_booklet", Count: 1 as byte, Damage: 0 as short}, {Slot: 3 as byte, id: "extrautils2:book", Count: 1 as byte, Damage: 0 as short}, {Slot: 4 as byte, id: "botania:lexicon", Count: 1 as byte, tag: {}, Damage: 0 as short}, {Slot: 5 as byte, id: "integrateddynamics:on_the_dynamics_of_integration", Count: 1 as byte, Damage: 0 as short}, {Slot: 6 as byte, id: "theoneprobe:probenote", Count: 1 as byte, Damage: 0 as short}, {Slot: 7 as byte, id: "xnet:xnet_manual", Count: 1 as byte, Damage: 0 as short}, {Slot: 8 as byte, id: "rftools:rftools_shape_manual", Count: 1 as byte, Damage: 0 as short}, {Slot: 9 as byte, id: "rftools:rftools_manual", Count: 1 as byte, Damage: 0 as short}, {Slot: 10 as byte, id: "minecraft:book", Count: 1 as byte, tag: {Guide: "", display: {Name: "Guide Book"}}, Damage: 0 as short}, {Slot: 11 as byte, id: "industrialforegoing:book_manual", Count: 1 as byte, Damage: 0 as short}, {Slot: 12 as byte, id: "forestry:book_forester", Count: 1 as byte, Damage: 0 as short}]}),"This crate contains all the books and guides you would ever need!");
recipes.addShaped(<immersiveengineering:wooden_device0>.withTag({display: {Name: "Book Crate"}, inventory: [{Slot: 0 as byte, id: "immersiveengineering:tool", Count: 1 as byte, Damage: 3 as short}, {Slot: 1 as byte, id: "draconicevolution:info_tablet", Count: 1 as byte, Damage: 0 as short}, {Slot: 2 as byte, id: "actuallyadditions:item_booklet", Count: 1 as byte, Damage: 0 as short}, {Slot: 3 as byte, id: "extrautils2:book", Count: 1 as byte, Damage: 0 as short}, {Slot: 4 as byte, id: "botania:lexicon", Count: 1 as byte, tag: {}, Damage: 0 as short}, {Slot: 5 as byte, id: "integrateddynamics:on_the_dynamics_of_integration", Count: 1 as byte, Damage: 0 as short}, {Slot: 6 as byte, id: "theoneprobe:probenote", Count: 1 as byte, Damage: 0 as short}, {Slot: 7 as byte, id: "xnet:xnet_manual", Count: 1 as byte, Damage: 0 as short}, {Slot: 8 as byte, id: "rftools:rftools_shape_manual", Count: 1 as byte, Damage: 0 as short}, {Slot: 9 as byte, id: "rftools:rftools_manual", Count: 1 as byte, Damage: 0 as short}, {Slot: 10 as byte, id: "minecraft:book", Count: 1 as byte, tag: {Guide: "", display: {Name: "Guide Book"}}, Damage: 0 as short}, {Slot: 11 as byte, id: "industrialforegoing:book_manual", Count: 1 as byte, Damage: 0 as short}, {Slot: 12 as byte, id: "forestry:book_forester", Count: 1 as byte, Damage: 0 as short}]}), [
  [<ore:plankTreatedWood>, <ore:plankTreatedWood>, <ore:plankTreatedWood>],
  [<ore:plankTreatedWood>, <minecraft:book>, <ore:plankTreatedWood>],
  [<ore:plankTreatedWood>, <ore:plankTreatedWood>, <ore:plankTreatedWood>]]);
 
// -----------------
//  Extra Utilities
// -----------------

// -----------------
//  Coal Coke
// -----------------
mods.immersiveengineering.CokeOven.removeRecipe(<immersiveengineering:material:6>);
mods.immersiveengineering.CokeOven.addRecipe(<thermalfoundation:material:802>, 500, <ore:coal>, 1800);
mods.immersiveengineering.CokeOven.removeRecipe(<immersiveengineering:stone_decoration:3>);
mods.immersiveengineering.CokeOven.addRecipe(<thermalfoundation:storage_resource:1>, 5000, <ore:blockCoal>, 16200);

// Add Pulverized Coal recipe to the Crusher
mods.extrautils2.Crusher.add(<thermalfoundation:material:768>, <minecraft:coal>);

// Add Pulverized Charcoal recipe to the Crusher
mods.extrautils2.Crusher.add(<thermalfoundation:material:769>, <minecraft:coal:1>);

// -----------
//  Minecraft
// -----------

// Changed item frame recipe to be accessible early game for FTB Achievements
recipes.remove(<minecraft:item_frame>);
recipes.addShaped(<minecraft:item_frame> * 1, [
	[<ore:stickWood>, <ore:stickWood>, <ore:stickWood>],
	[<ore:stickWood>, <ore:plankWood>, <ore:stickWood>],
	[<ore:stickWood>, <ore:stickWood>, <ore:stickWood>]
]);

// ------------------
//  FTB Achievements
// ------------------

// Powered crafting table
recipes.addShaped(<ftbachievements:poweredcraftingtable> * 1, [
  [<ore:dustRedstone>, <ore:dustRedstone>, <ore:dustRedstone>],
  [<ore:dustRedstone>, <minecraft:crafting_table>, <ore:dustRedstone>],
  [<ore:dustRedstone>, <ore:dustRedstone>, <ore:dustRedstone>]
]);

// --------------------
//  Actually Additions
// --------------------

// Fix toast recipe conflict
recipes.remove(<actuallyadditions:item_food:10>);
recipes.addShapeless(<actuallyadditions:item_food:10> * 2, [<actuallyadditions:item_knife>.anyDamage(), <minecraft:bread>]);
