#Name: JEI.zs
#Author: Feed the Beast

print("Initializing 'JEI.zs'...");

	#hide from JEI and remove recipe
mods.jei.JEI.removeAndHide(<modcurrency:guidebook>);
mods.jei.JEI.removeAndHide(<modcurrency:blockexchanger>);
mods.jei.JEI.removeAndHide(<modcurrency:blockvending>);
mods.jei.JEI.removeAndHide(<modcurrency:upgrade>);
mods.jei.JEI.removeAndHide(<modcurrency:upgrade:5>);
mods.jei.JEI.removeAndHide(<modcurrency:upgrade:4>);
mods.jei.JEI.removeAndHide(<modcurrency:upgrade:3>);
mods.jei.JEI.removeAndHide(<modcurrency:upgrade:1>);
mods.jei.JEI.removeAndHide(<modcurrency:upgrade:2>);

	#remove items
mods.jei.JEI.removeAndHide(<extrautils2:user>);
mods.jei.JEI.removeAndHide(<exnihilocreatio:block_waterwheel>);
mods.jei.JEI.removeAndHide(<exnihilocreatio:block_axle_stone>);
mods.jei.JEI.removeAndHide(<exnihilocreatio:block_grinder>);
mods.jei.JEI.removeAndHide(<exnihilocreatio:block_auto_sifter>);
mods.jei.JEI.removeAndHide(<quantumstorage:quantumcrafter>);
#mods.jei.JEI.removeAndHide(<lootbags:loot_opener>);
#mods.jei.JEI.removeAndHide(<lootbags:loot_recycler>);


	#tooltips
#<item>.addTooltip(format.red("blahblah"));
<actuallyadditions:item_worm>.addTooltip(format.red("Must be bought with currency from FTB Quests book!"));
#<projecte:item.pe_swrg>.addTooltip(format.red("Must be bought with currency from FTB Quests book!"));
<quantumstorage:quantum_battery>.addTooltip(format.red("Must be bought with currency from FTB Quests book!"));
<torchmaster:mega_torch>.addTooltip(format.red("Must be bought with currency from FTB Quests book!"));
<quantumstorage:quantumstoragedisk>.addTooltip(format.red("Must be bought with currency from FTB Quests book!"));
<quantumstorage:quantumstoragediskfluid>.addTooltip(format.red("Must be bought with currency from FTB Quests book!"));

	#remove recipes for currency items
recipes.remove(<torchmaster:mega_torch>);
recipes.remove(<quantumstorage:quantum_battery>);
recipes.remove(<quantumstorage:quantumstoragedisk>);
recipes.remove(<quantumstorage:quantumstoragediskfluid>);


print("Initialized 'JEI.zs'");