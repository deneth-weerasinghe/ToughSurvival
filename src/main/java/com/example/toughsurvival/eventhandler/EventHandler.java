package com.example.toughsurvival.eventhandler;

import com.example.toughsurvival.playerdata.hydrationdata.Hydration;
import com.example.toughsurvival.playerdata.hydrationdata.HydrationProvider;
import com.example.toughsurvival.playerdata.hydrationdata.IHydration;
import com.example.toughsurvival.setup.ToughSurvival;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EventHandler {

    @SubscribeEvent
    public static void onEntityConstruction(AttachCapabilitiesEvent<Entity> event){
        if (event.getObject() instanceof PlayerEntity){
            event.addCapability(new ResourceLocation(ToughSurvival.MOD_ID, "hydration"), new HydrationProvider());
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event){

        PlayerEntity oldPlayer = event.getOriginal(); // the player entity that was deleted
        PlayerEntity newPlayer = event.getPlayer(); // the current player entity

        IHydration oldData = Hydration.getFromPlayer(oldPlayer);
        IHydration newData = Hydration.getFromPlayer(newPlayer);

        if (event.isWasDeath()){ // checks if the cloning is caused by death or changing dimensions
            newData.setHydration(Hydration.RESPAWN_HYDRATION);
            newData.setDecayFactor(Hydration.DEFAULT_DECAY);
        }
        else {
            newData.setHydration(oldData.getHydration());
            newData.setDecayFactor(oldData.getDecayFactor());
        }
    }

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock event){
        World world = event.getWorld();
        if (!world.isRemote){

            Block testblock = world.getBlockState(event.getPos()).getBlock();
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
            Hydration.updateClient((ServerPlayerEntity) player, cap);
        }
    }

    @SubscribeEvent
    public static void onEmptyClick(PlayerInteractEvent.RightClickEmpty event){
        PlayerEntity player = event.getPlayer();
        IHydration cap = Hydration.getFromPlayer(player);
        ToughSurvival.LOGGER.debug("client hydration = " + cap.getHydration());
    }
}
