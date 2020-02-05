package com.denethweerasinghe.toughsurvival.playerdata.hydrationdata;

import com.denethweerasinghe.toughsurvival.networking.HydrationSync;
import com.denethweerasinghe.toughsurvival.networking.PacketManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class Hydration implements IHydration {

    // useful constants, especially useful in events
    public static final int MAX_HYDRATION = 20;
    public static final int RESPAWN_HYDRATION = 16;
    public static final float DEFAULT_DECAY = 1F;
    public static final int DEFAULT_TIMER = 3600;

    private int timerEnd;
    private int playerHydration;
    private float decayFactor;
    private int decayTimer;

    public Hydration() {
        this.playerHydration = MAX_HYDRATION;
        this.decayFactor = DEFAULT_DECAY;
        this.decayTimer = 0;
        this.timerEnd = DEFAULT_TIMER;
    }

    @Override
    public void setHydration(int value) {

        //TODO: use Math.min() instead
        this.playerHydration = value;

        // validation check: hydration can never be lower than 0 or greater than 20
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

    @Override
    public void setTimerEnd(int value) {
        this.timerEnd = value;
    }

    @Override
    public int getTimerEnd() {
        return timerEnd;
    }

    public static IHydration getFromPlayer(PlayerEntity player) {
        return player.getCapability(HydrationProvider.PLAYER_HYDRATION, null).orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!"));
    }

    public static void updateClient(ServerPlayerEntity player, IHydration cap) {
        PacketManager.sendTo(player, new HydrationSync(player.getEntityId(), (CompoundNBT) HydrationProvider.PLAYER_HYDRATION.writeNBT(cap, null)));
    }
}
