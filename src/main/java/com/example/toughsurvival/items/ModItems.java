package com.example.toughsurvival.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

@net.minecraftforge.registries.ObjectHolder("toughsurvival")
public class ModItems {
    public static final Item APPLE_JUICE = new Item((new Item.Properties()).group(ItemGroup.FOOD).food(ModFoods.APPLE_JUICE));


}
