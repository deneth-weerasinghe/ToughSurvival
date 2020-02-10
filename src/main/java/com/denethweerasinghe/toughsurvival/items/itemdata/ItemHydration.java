package com.denethweerasinghe.toughsurvival.items.itemdata;

import net.minecraft.item.ItemStack;

public class ItemHydration implements IItemHydration {

    private int itemHydration;

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
