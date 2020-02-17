package com.denethweerasinghe.toughsurvival.playerdata.hydration;

import com.denethweerasinghe.toughsurvival.networking.HydrationSync;
import com.denethweerasinghe.toughsurvival.networking.PacketManager;
import com.denethweerasinghe.toughsurvival.playerdata.PlayerProvider;
import com.denethweerasinghe.toughsurvival.setup.ToughSurvival;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class Hydration implements IHydration {

    // useful constants, especially useful in events
    public static final int MAX_HYDRATION = 20;
    public static final int RESPAWN_HYDRATION = 16;
    public static final float DEFAULT_DECAY = 0.01F;

    private int playerHydration;
    private float decayFactor;

    public Hydration() {
        this.playerHydration = MAX_HYDRATION;
        this.decayFactor = 36.0F;
    }

    @Override
    public void setHydration(int value) {

        this.playerHydration = Math.min(value, MAX_HYDRATION);
        this.playerHydration = Math.max(playerHydration, 0);
    }

    @Override
    public int getHydration() {
        return playerHydration;
    }

    @Override
    public void setDecayFactor(float value) {
        this.decayFactor = Math.min(value, 36.0F);
        this.decayFactor = Math.max(decayFactor, 0.0F);
//        ToughSurvival.LOGGER.debug("decayFactor = " + this.decayFactor);
    }

    @Override
    public float getDecayFactor() {
        return decayFactor;
    }

    public static IHydration getFromPlayer(PlayerEntity player) {
        return player.getCapability(PlayerProvider.PLAYER_HYDRATION, null).orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!"));
    }

    public static void updateClient(ServerPlayerEntity player, IHydration cap) {
        PacketManager.sendTo(player, new HydrationSync(player.getEntityId(), (CompoundNBT) PlayerProvider.PLAYER_HYDRATION.writeNBT(cap, null)));
    }
}
