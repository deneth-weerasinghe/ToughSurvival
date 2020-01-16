package com.example.toughsurvival.itemdata;

public class ItemHydration implements IItemHydration {

    private final int testHydration = 2;

    @Override
    public int getHydration() {
        return testHydration;
    }
}
