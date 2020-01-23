package com.example.toughsurvival.items;

import net.minecraft.item.Item;

public class Drink extends Item {

    public int itemHydration;

    public Drink(Item.Properties properties) {
        super(properties.food(ModFoods.DRINK));
    }

    public int getItemHydration() {
        return itemHydration;
    }

    public void setItemHydration(int value){
        this.itemHydration = value;
    }
}
