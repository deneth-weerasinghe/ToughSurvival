package com.denethweerasinghe.toughsurvival.playerdata.wetness;

import com.denethweerasinghe.toughsurvival.playerdata.PlayerProvider;
import net.minecraft.entity.player.PlayerEntity;

public class Wetness implements IWetness {

    //useful constants
    public final static int MAX_WETNESS = 100;
    public final static float DEFAULT_GROWTH = 1F;

    private int wetness;
    private float growthFactor;

    public Wetness(){
        this.wetness = 0;
        this.growthFactor = DEFAULT_GROWTH;
    }

    @Override
    public void setWetness(int value) {
        this.wetness = Math.min(value, MAX_WETNESS);
    }

    @Override
    public int getWetness() {
        return wetness;
    }

    @Override
    public void setGrowth(float value) {
        this.growthFactor = value;
    }

    @Override
    public float getGrowth() {
        return growthFactor;
    }

    public static IWetness getFromPlayer(PlayerEntity player){
        return player.getCapability(PlayerProvider.WETNESS, null).orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!"));
    }
}
