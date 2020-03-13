package com.denethweerasinghe.toughsurvival.playerdata.wetness;

import com.denethweerasinghe.toughsurvival.networking.PacketManager;
import com.denethweerasinghe.toughsurvival.networking.WetnessSync;
import com.denethweerasinghe.toughsurvival.playerdata.PlayerProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class Wetness implements IWetness {

    //useful constants
    public final static int MAX_WETNESS = 100;
    public final static float DEFAULT_GROWTH = -1F;
    public final static int TIMER = 10;

    private int wetness;
    private int timer;
    private float growthFactor;

    public Wetness(){
        this.wetness = 0;
        this.growthFactor = DEFAULT_GROWTH;
    }

    @Override
    public void setWetness(int value) {
        this.wetness = Math.min(value, MAX_WETNESS);
        this.wetness = Math.max(wetness, 0);
    }

    @Override
    public int getWetness() {
        return wetness;
    }

    @Override
    public void setTimer(int value) {
        this.timer = value;
    }

    @Override
    public int getTimer() {
        return timer;
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

    public static void updateClient(ServerPlayerEntity player, IWetness cap) {
        PacketManager.sendTo(player, new WetnessSync(player.getEntityId(), (CompoundNBT) PlayerProvider.WETNESS.writeNBT(cap, null)));
    }

}
