package com.denethweerasinghe.toughsurvival.items.itemdata;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class ItemStorage implements Capability.IStorage<IItemHydration> {

    @Override
    public INBT writeNBT(Capability<IItemHydration> capability, IItemHydration instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("hydration", instance.getHydration());
        return tag;
    }

    @Override
    public void readNBT(Capability<IItemHydration> capability, IItemHydration instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        instance.setHydration(tag.getInt("hydration"));
    }
}
