package com.denethweerasinghe.toughsurvival.playerdata.hydration;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class HydrationStorage implements Capability.IStorage<IHydration> {

    private static final String HYDRATION_TAG = "playerHydration";
    private static final String HYDRATION_DECAY_TAG = "hydrationDecay";

    @Override
    public INBT writeNBT(Capability<IHydration> capability, IHydration instance, Direction side) {

        CompoundNBT tag = new CompoundNBT();
        tag.putInt(HYDRATION_TAG, instance.getHydration());
        tag.putFloat(HYDRATION_DECAY_TAG, instance.getDecayFactor());
        return tag;
    }

    @Override
    public void readNBT(Capability<IHydration> capability, IHydration instance, Direction side, INBT nbt) {

        CompoundNBT tag = (CompoundNBT) nbt;
        instance.setHydration(tag.getInt(HYDRATION_TAG));
        instance.setDecayFactor(tag.getFloat(HYDRATION_DECAY_TAG));
    }
}
