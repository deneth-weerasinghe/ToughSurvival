package com.denethweerasinghe.toughsurvival.playerdata.hydrationdata;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class HydrationStorage implements Capability.IStorage<IHydration> {

    private static final String HYDRATION_TAG = "playerHydration";
    private static final String HYDRATION_DECAY_TAG = "hydrationDecay";
    private static final String HYDRATION_TIMER_TAG = "timer";
    private static final String TIMER_END = "timerEnd";

    @Override
    public INBT writeNBT(Capability<IHydration> capability, IHydration instance, Direction side) {

        CompoundNBT tag = new CompoundNBT();
        tag.putInt(HYDRATION_TAG, instance.getHydration());
        tag.putFloat(HYDRATION_DECAY_TAG, instance.getDecayFactor());
        tag.putInt(HYDRATION_TIMER_TAG, instance.getDecayTimer());
        tag.putInt(TIMER_END, instance.getTimerEnd());
        return tag;
    }

    @Override
    public void readNBT(Capability<IHydration> capability, IHydration instance, Direction side, INBT nbt) {

        CompoundNBT tag = (CompoundNBT) nbt;
        instance.setHydration(tag.getInt(HYDRATION_TAG));
        instance.setDecayFactor(tag.getFloat(HYDRATION_DECAY_TAG));
        instance.setDecayTimer(tag.getInt(HYDRATION_TIMER_TAG));
        instance.setTimerEnd(tag.getInt(TIMER_END));
    }
}
