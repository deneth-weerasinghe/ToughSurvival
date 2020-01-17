package com.example.toughsurvival.itemdata;

public class ItemHydration implements IItemHydration {

    private int testHydration = 2;

    @Override
    public int getHydration() {
        return testHydration;
    }

    @Override
    public void setHydration(int value) {
        this.testHydration = value;
    }
}
