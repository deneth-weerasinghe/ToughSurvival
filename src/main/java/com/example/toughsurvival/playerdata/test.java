package com.example.toughsurvival.playerdata;

import com.example.toughsurvival.setup.ToughSurvival;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class test {

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock event){
        World world = event.getWorld();
        if (!world.isRemote){
            ToughSurvival.LOGGER.debug("SERVER");
//            if (world.getBlockState(event.getPos()).getBlock() == Blocks.COBBLESTONE){
//                PlayerEntity player = event.getPlayer();
//                IHydration cap = Hydration.getFromPlayer(player);
//                cap.setHydration(cap.getHydration() + 10);
//                ToughSurvival.LOGGER.debug("playerHydration = " + cap.getHydration());
//            }
        }
    }
}
