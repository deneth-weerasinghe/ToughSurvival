package com.denethweerasinghe.toughsurvival.handlers;

import com.denethweerasinghe.toughsurvival.itemdata.IItemHydration;
import com.denethweerasinghe.toughsurvival.itemdata.ItemHydration;
import com.denethweerasinghe.toughsurvival.items.ModItems;
import com.denethweerasinghe.toughsurvival.playerdata.hydrationdata.Hydration;
import com.denethweerasinghe.toughsurvival.playerdata.hydrationdata.IHydration;
import com.denethweerasinghe.toughsurvival.setup.ToughSurvival;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class TestHandlers {

    // for testing purposes
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
                cap.setDecayFactor(0);
            }
            else if (testblock == Blocks.IRON_BLOCK){
                if (event.getItemStack().getItem() == ModItems.berryJuice){
                    ToughSurvival.LOGGER.debug("ITEM TEST");
                }
            }
            ToughSurvival.LOGGER.debug("playerHydration = " + cap.getHydration());
            Hydration.updateClient((ServerPlayerEntity) player, cap);
        }
    }

    // for testing purposes
//    @SubscribeEvent
//    public static void onEmptyClick(PlayerInteractEvent.RightClickEmpty event){
//        PlayerEntity player = event.getPlayer();
//        IHydration cap = Hydration.getFromPlayer(player);
//        ToughSurvival.LOGGER.debug("client hydration = " + cap.getHydration());
//    }

}
