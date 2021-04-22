import crafttweaker.item.IItemStack;

#Name: 01_Remove.zs
#Author: Feed the Beast and Sunekaer

print("Initializing '01_Remove.zs'...");

val RemoveArray = [
  <immersiveengineering:storage_slab>,
  <immersiveengineering:storage_slab:1>,
  <immersiveengineering:storage_slab:2>,
  <immersiveengineering:storage_slab:3>,
  <immersiveengineering:storage_slab:4>,
  <immersiveengineering:storage_slab:6>,
  <immersiveengineering:storage_slab:7>,
  <immersiveengineering:storage_slab:8>,
  <immersiveengineering:stone_decoration:3>,
  <immersiveengineering:material:6>,
  <morebeautifulbuttons:button_press>,
  <quark:charcoal_block>,
  <forestry:charcoal>,
  <chisel:block_charcoal2:1>,
  <minecraft:stone_slab>,
  <rustic:crop_stake>,
  <rustic:painted_wood_cyan>,
  <rustic:painted_wood_white>,
  <rustic:painted_wood_orange>,
  <rustic:painted_wood_magenta>,
  <rustic:painted_wood_light_blue>,
  <rustic:painted_wood_yellow>,
  <rustic:painted_wood_lime>,
  <rustic:painted_wood_pink>,
  <rustic:painted_wood_gray>,
  <rustic:painted_wood_silver>,
  <rustic:painted_wood_cyan>,
  <rustic:painted_wood_purple>,
  <rustic:painted_wood_blue>,
  <rustic:painted_wood_brown>,
  <rustic:painted_wood_green>,
  <rustic:painted_wood_red>,
  <rustic:painted_wood_black>,
  <cfm:hedge_oak>,
  <cfm:hedge_spruce>,
  <cfm:hedge_birch>,
  <cfm:hedge_jungle>,
  <cfm:hedge_acacia>,
  <cfm:hedge_dark_oak>,
  <littletiles:lttransparentcoloredblock:2>,
  <marblecraftingtable:marble_crafting_table>
] as IItemStack[];

val RemoveAndhideArray = [
  <extrabitmanipulation:diamond_nugget>,
  <malisisdoors:trapdoor_acacia>,
  <malisisdoors:trapdoor_birch>,
  <malisisdoors:trapdoor_dark_oak>,
  <malisisdoors:trapdoor_jungle>,
  <malisisdoors:trapdoor_spruce>,
  <quark:iron_ladder>,
  <appliedenergistics2:material:40>
] as IItemStack[];

val RemoveByNameArray = [
  "thermalfoundation:material_97",
  "littletiles:ltcoloredblock_13"
] as string[];

################################################################################

for item in RemoveArray {
    recipes.remove(item);
}

for item in RemoveAndhideArray {
    mods.jei.JEI.removeAndHide(item);
}

for item in RemoveByNameArray {
    recipes.removeByRecipeName(item);
}


print("Initialized '01_Remove.zs'");
