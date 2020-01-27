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

        if (!world.isRemote && event.phase == TickEvent.Phase.START){

            IHydration cap = Hydration.getFromPlayer(player);
            cap.incrementTimer();
            ToughSurvival.LOGGER.debug(cap.getDecayTimer());

            if (cap.getDecayTimer() == Hydration.TIMER_END){
                ToughSurvival.LOGGER.debug("3600 reached");
                cap.setDecayTimer(0);
                int test = (int) (cap.getHydration() - cap.getDecayFactor());
                cap.setHydration(test);
                Hydration.updateClient((ServerPlayerEntity) player, cap);
            }
        }
    }

    @SubscribeEvent
    public static void onJumping(LivingEvent.LivingJumpEvent event){

        Entity entity = event.getEntity();

        if (entity instanceof PlayerEntity && !entity.world.isRemote()){
            IHydration cap = Hydration.getFromPlayer((PlayerEntity) entity);
            ToughSurvival.LOGGER.debug("this is the player");

            float multiplier = 0.25F;

            if (entity.isSprinting()){
                multiplier+= 0.1F;
            }
            cap.setDecayFactor(cap.getDecayFactor() + 1*multiplier);
        }
    }

//    @SubscribeEvent
//    public static void onSwimming(){
//
//    }
}
