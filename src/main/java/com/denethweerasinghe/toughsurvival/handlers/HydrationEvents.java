package com.denethweerasinghe.toughsurvival.handlers;

import com.denethweerasinghe.toughsurvival.playerdata.hydrationdata.Hydration;
import com.denethweerasinghe.toughsurvival.playerdata.hydrationdata.IHydration;
import com.denethweerasinghe.toughsurvival.setup.ToughSurvival;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class HydrationEvents {

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event){


        PlayerEntity player = event.player;
        World world = player.world;

        // checks if on the server and checks if firing once or twice per tick
        // as ticks have two phases
        if (!world.isRemote && event.phase == TickEvent.Phase.START){

            IHydration cap = Hydration.getFromPlayer(player);

            float decay = Hydration.DEFAULT_DECAY;

            // additional checks modifying how fast decay is applied
            if (player.isSprinting()){
                decay+= 0.1F;
            }
            if (player.isSwimming()){
                decay+= 1;
            }

            cap.setDecayFactor(cap.getDecayFactor() - decay);

            if (cap.getDecayFactor() == 0){
                ToughSurvival.LOGGER.debug("applying decay");

                // reset decay
                cap.setDecayFactor(36);
                cap.setHydration(cap.getHydration() - 1);

                // sync with client
                Hydration.updateClient((ServerPlayerEntity) player, cap);
            }
        }
    }

    @SubscribeEvent
    public static void onJumping(LivingEvent.LivingJumpEvent event){

        Entity entity = event.getEntity();

        // if the entity is a player
        // if on server side
        if (entity instanceof PlayerEntity && !entity.world.isRemote()){
            IHydration cap = Hydration.getFromPlayer((PlayerEntity) entity);
            cap.setDecayFactor(cap.getDecayFactor() - 2.5F);
        }
    }
}
