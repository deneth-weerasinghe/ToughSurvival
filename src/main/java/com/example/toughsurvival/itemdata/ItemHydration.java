package com.example.toughsurvival.itemdata;

import net.minecraft.item.ItemStack;

public class ItemHydration implements IItemHydration {

    private int itemHydration;

    public ItemHydration(Integer value){
        this.setHydration(value);
    }

    @Override
    public int getHydration() {
        return itemHydration;
    }

    @Override
    public void setHydration(int value) {
        this.itemHydration = value;
    }

    public static IItemHydration getFromItem(ItemStack itemStack){
        return itemStack.getCapability(ItemHydrProvider.ITEM_HYDRATION, null).orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!"));
    }
}
