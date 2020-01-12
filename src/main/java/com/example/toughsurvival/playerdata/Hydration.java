package com.example.toughsurvival.playerdata;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

public class Hydration implements IHydration {

    // will be useful in other situations for instance when drawing the hydration bar
    public static final int MAX_HYDRATION = 20;
    public static final float DEFAULT_DECAY = 1f;

    private int playerHydration;
    private float decayFactor;

    public Hydration() {
        this.playerHydration = MAX_HYDRATION;
        this.decayFactor = DEFAULT_DECAY;
    }

    @Override
    public void setHydration(int value) {
        this.playerHydration = value;
    }

    @Override
    public int getHydration() {

        // range check: hydration can never be lower than 0 or greater than 20
        if (playerHydration > MAX_HYDRATION) {
            return MAX_HYDRATION;
        }
        else if (playerHydration < 0){
            return 0;
        }
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

    public static IHydration getFromPlayer(PlayerEntity player) {
        return null;
    }

    public static void updateClient(ServerPlayerEntity player, IHydration cap) {

    }
}
