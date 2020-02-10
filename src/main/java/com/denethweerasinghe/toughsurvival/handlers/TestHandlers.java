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

    @SubscribeEvent
    public static void onItemUse(LivingEntityUseItemEvent.Finish event){

        Entity entity = event.getEntity();

        if (entity instanceof PlayerEntity && !entity.getEntityWorld().isRemote) {

            ItemStack itemStack = event.getItem();
            Item item = itemStack.getItem();
            IItemHydration cap = ItemHydration.getFromItem(itemStack);

            // checks which items will be assigned a value
            if (item == Items.APPLE){
                cap.setHydration(2);
            }
            else if (item == Items.MELON_SLICE){
                cap.setHydration(3);
            }
            else if (item == Items.CARROT){
                cap.setHydration(2);
            }
            else if (item == Items.SWEET_BERRIES){
                cap.setHydration(2);
            }
            else if (item == Items.BEETROOT){
                cap.setHydration(3);
            }
            else if (item == Items.POTION){
                cap.setHydration(4);
                // water bottles are just special potions hence I have to use NBT tags
                // to get the potion that corresponds to water bottles
                if (itemStack.getTag().getString("Potion").equals("minecraft:water")){
                    cap.setHydration(10);
                }
            }
            else if (item == Items.WATER_BUCKET){
                cap.setHydration(10);
            }
            else if (item == Items.MILK_BUCKET){
                cap.setHydration(6);
            }

            // adding to playerHydration
            IHydration playerCap = Hydration.getFromPlayer((PlayerEntity) entity);
            playerCap.setHydration(playerCap.getHydration() + cap.getHydration());
            Hydration.updateClient((ServerPlayerEntity) entity, playerCap);
        }
    }
}
