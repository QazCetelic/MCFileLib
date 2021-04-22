import crafttweaker.item.IItemStack;

#Name: JEI.zs
#Author: Feed the Beast and Sunekaer

print("Initializing 'JEI.zs'...");

val Hide = [
  <compactstorage:compactchest:4>
] as IItemStack[];

for i in Hide {
  mods.jei.JEI.hide(i);
}

mods.jei.JEI.addDescription(<compactstorage:compactchest>,"Crafted in the Chest Builder");
mods.jei.JEI.addDescription(<compactstorage:backpack>,"Crafted in the Chest Builder");

var stoneFacade = <appliedenergistics2:facade>.withTag({damage: 0, item: "minecraft:stone"}) as IItemStack;
mods.jei.JEI.addDescription(stoneFacade, "Facades are not disabled in this pack; just hidden in JEI. To craft facades, place the block you want to create a facade out of in the centre of the crafting grid (3x3) and four AE2 cable anchors on all adjacent sides of that block. If no output appears then you cannot create a facade with that block.");


print("Initialized 'JEI.zs'");
