package com.example.toughsurvival.itemdata;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemHydrProvider implements ICapabilitySerializable<CompoundNBT> {
    @CapabilityInject(IItemHydration.class)
    public static Capability<IItemHydration> ITEM_HYDRATION = null;
    private final LazyOptional<IItemHydration> instance = LazyOptional.of(ITEM_HYDRATION::getDefaultInstance);

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == ITEM_HYDRATION ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }
}
