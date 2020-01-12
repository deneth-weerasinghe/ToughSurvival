package com.example.toughsurvival.playerdata;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class HydrationProvider implements ICapabilitySerializable<CompoundNBT> {

    @CapabilityInject(IHydration.class)
    // the annotation sets this field to an instance of the capability once registered
    public static Capability<IHydration> PLAYER_HYDRATION = null;
    // this holds a temporary value
    private final LazyOptional<IHydration> instance = LazyOptional.of(PLAYER_HYDRATION::getDefaultInstance);


    @Override // getter for the capability
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == PLAYER_HYDRATION ? instance.cast() : LazyOptional.empty();
    }

    @Override // calls the writeNBT method from the HydrationStorage class
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) PLAYER_HYDRATION.getStorage().writeNBT(PLAYER_HYDRATION, this.instance.orElseThrow(
                () -> new IllegalArgumentException("LazyOptional must not be empty!")), null);
    }

    @Override // calls the readNBT method from the HydrationStorage class
    public void deserializeNBT(CompoundNBT nbt) {
        PLAYER_HYDRATION.getStorage().readNBT(PLAYER_HYDRATION, this.instance.orElseThrow(
                () -> new IllegalArgumentException("LazyOptional must not be empty!")), null, null);
    }
}
