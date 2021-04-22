# Name: FTBPresentsDirewolf20110.zs
# Author: Feed the Beast LLC.

print("Initializing 'FTBPresentsDirewolf20110.zs'...");

recipes.remove(<extrautils2:bagofholding>);
recipes.addShaped(<extrautils2:bagofholding>*1,[[<ore:ingotGold>, <ore:ingotGold>, <ore:ingotGold>], [<ore:chestWood>, <extrautils2:decorativesolidwood:1>, <ore:chestWood>], [<ore:ingotGold>, <ore:ingotGold>, <ore:ingotGold>]]);

recipes.addShaped(<minecraft:dragon_egg>*1,[[null, <draconicevolution:dragon_heart>], [<draconicevolution:awakened_core>, <ore:egg>, <draconicevolution:awakened_core>], [null, <draconicevolution:dragon_heart>]]);

print("Initialized 'FTBPresentsDirewolf20110.zs'");
