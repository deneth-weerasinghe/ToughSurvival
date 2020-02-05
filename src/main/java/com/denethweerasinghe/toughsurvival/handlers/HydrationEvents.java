package com.denethweerasinghe.toughsurvival.handlers;

import com.denethweerasinghe.toughsurvival.playerdata.hydrationdata.Hydration;
import com.denethweerasinghe.toughsurvival.playerdata.hydrationdata.IHydration;
import com.denethweerasinghe.toughsurvival.setup.ToughSurvival;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
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
            cap.incrementTimer();

            // prints the progress of the timer for debugging
            ToughSurvival.LOGGER.debug(cap.getDecayTimer());

            // if the timer reaches its stopping point
            if (cap.getDecayTimer() == cap.getTimerEnd()){
                ToughSurvival.LOGGER.debug("timer end reached");

                cap.setDecayTimer(0);
                cap.setHydration(cap.getHydration() - 1);
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

            int decrement = (int) (cap.getTimerEnd() - 0.25 * Hydration.DEFAULT_TIMER);
            cap.setTimerEnd(decrement);
            ToughSurvival.LOGGER.debug("Timer decreased by: " + decrement);
        }
    }

//    @SubscribeEvent
//    public static void onSwimming(){
//
//    }
}
