package com.denethweerasinghe.toughsurvival.playerdata.wetness;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class WetnessStorage implements Capability.IStorage<IWetness> {

    private static final String WETNESS_TAG = "playerWetness";
    private static final String GROWTH_TAG = "wetnessGrowth";
    private static final String TIMER_TAG = "wetnessTimer";

    @Override
    public INBT writeNBT(Capability<IWetness> capability, IWetness instance, Direction side) {

        CompoundNBT tag = new CompoundNBT();
        tag.putInt(WETNESS_TAG, instance.getWetness());
        tag.putFloat(GROWTH_TAG, instance.getGrowth());
        tag.putInt(TIMER_TAG, instance.getTimer());
        return tag;
    }

    @Override
    public void readNBT(Capability<IWetness> capability, IWetness instance, Direction side, INBT nbt) {

        CompoundNBT tag = (CompoundNBT) nbt;
        instance.setWetness(tag.getInt(WETNESS_TAG));
        instance.setGrowth(tag.getFloat(GROWTH_TAG));
        instance.setTimer(tag.getInt(TIMER_TAG));
    }
}
