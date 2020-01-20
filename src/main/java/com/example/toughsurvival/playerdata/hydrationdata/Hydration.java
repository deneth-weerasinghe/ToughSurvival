package com.example.toughsurvival.playerdata.hydrationdata;

import com.example.toughsurvival.networking.HydrationSync;
import com.example.toughsurvival.networking.PacketManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class Hydration implements IHydration {

    // will be useful in other situations for instance when drawing the hydration bar
    public static final int MAX_HYDRATION = 20;
    public static final int RESPAWN_HYDRATION = 16;
    public static final float DEFAULT_DECAY = 1f;
    public static final int TIMER_END = 3600;

    private int playerHydration;
    private float decayFactor;
    private int decayTimer;

    public Hydration() {
        this.playerHydration = MAX_HYDRATION;
        this.decayFactor = DEFAULT_DECAY;
        this.decayTimer = 0;
    }

    @Override
    public void setHydration(int value) {

        this.playerHydration = value;

        // range check: hydration can never be lower than 0 or greater than 20
        if (this.playerHydration > MAX_HYDRATION) {
            this.playerHydration = MAX_HYDRATION;
        }
        else if (this.playerHydration < 0){
            this.playerHydration = 0;
        }
    }

    @Override
    public int getHydration() {
        return playerHydration;
    }

    @Override
    public void setDecayFactor(float value) {
        this.decayFactor = value;
    }

    @Override
    public float getDecayFactor() {
        return decayFactor;
    }

    @Override
    public void setDecayTimer(int value) {
        this.decayTimer = value;
    }

    @Override
    public void incrementTimer() {
        this.decayTimer++;
    }

    @Override
    public int getDecayTimer() {
        return decayTimer;
    }

    public static IHydration getFromPlayer(PlayerEntity player) {
        return player.getCapability(HydrationProvider.PLAYER_HYDRATION, null).orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!"));
    }

    public static void updateClient(ServerPlayerEntity player, IHydration cap) {
        PacketManager.sendTo(player, new HydrationSync(player.getEntityId(), (CompoundNBT) HydrationProvider.PLAYER_HYDRATION.writeNBT(cap, null)));
    }
}
