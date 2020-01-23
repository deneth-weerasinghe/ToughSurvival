package com.example.toughsurvival.items;

import com.example.toughsurvival.setup.ToughSurvival;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

@net.minecraftforge.registries.ObjectHolder(ToughSurvival.MOD_ID)
public class ModItems {
    public final static Item apple_juice = null;

    public static Item appleJuice = new Item(new Item.Properties().food(ModFoods.DRINK).group(ItemGroup.FOOD));
    public static Item berryJuice = new Drink(new Item.Properties());




}
