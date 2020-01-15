package com.example.toughsurvival.playerdata;

import com.example.toughsurvival.setup.ToughSurvival;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class test {

    @SubscribeEvent
    public static void onEntityConstruction(AttachCapabilitiesEvent<Entity> event){
        if (event.getObject() instanceof PlayerEntity){
            event.addCapability(new ResourceLocation(ToughSurvival.MOD_ID, "hydration"), new HydrationProvider());
        }
    }

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock event){
        World world = event.getWorld();
        if (!world.isRemote){

            ToughSurvival.LOGGER.debug("SERVER");
            Block testblock = world.getBlockState(event.getPos()).getBlock();
            ToughSurvival.LOGGER.debug("RIGHT CLICK!");
            PlayerEntity player = event.getPlayer();
            IHydration cap = Hydration.getFromPlayer(player);

            if (testblock == Blocks.COBBLESTONE){
                cap.setHydration(cap.getHydration() + 1);
            }
            else if (testblock == Blocks.DIAMOND_BLOCK){
                cap.setHydration(cap.getHydration() - 1);
            }
            else if (testblock == Blocks.GOLD_BLOCK){
                cap.setHydration(0);
            }
            ToughSurvival.LOGGER.debug("playerHydration = " + cap.getHydration());
        }
    }
}
