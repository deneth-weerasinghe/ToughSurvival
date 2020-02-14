package com.denethweerasinghe.toughsurvival.playerdata;

import com.denethweerasinghe.toughsurvival.playerdata.hydration.IHydration;
import com.denethweerasinghe.toughsurvival.playerdata.wetness.IWetness;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

public class PlayerProvider<X> implements ICapabilitySerializable<CompoundNBT> {

    // the annotation sets this field to an instance of the capability once registered
    @CapabilityInject(IHydration.class)
    public static Capability<IHydration> PLAYER_HYDRATION = null;
    @CapabilityInject(IWetness.class)
    public static Capability<IWetness> WETNESS = null;

    // this holds a temporary value
    private final LazyOptional<X> instance;
    private final Capability<X> inject;

    public PlayerProvider(Capability<X> inject) {
        instance = LazyOptional.of(inject::getDefaultInstance);
        this.inject = inject;
    }

    @Nonnull
    @Override // getter for the capability
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        return cap == inject ? instance.cast() : LazyOptional.empty();
    }

    @Override // calls the writeNBT method from the HydrationStorage class
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) inject.getStorage().writeNBT(inject, this.instance.orElseThrow(
                () -> new IllegalArgumentException("LazyOptional must not be empty!")), null);
    }

    @Override // calls the readNBT method from the HydrationStorage class
    public void deserializeNBT(CompoundNBT nbt) {
        inject.getStorage().readNBT(inject, this.instance.orElseThrow(
                () -> new IllegalArgumentException("LazyOptional must not be empty!")), null, nbt);
    }
}
