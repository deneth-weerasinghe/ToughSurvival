package com.denethweerasinghe.toughsurvival.handlers;

import com.denethweerasinghe.toughsurvival.items.ModItems;
import com.denethweerasinghe.toughsurvival.playerdata.hydration.Hydration;
import com.denethweerasinghe.toughsurvival.playerdata.hydration.IHydration;
import com.denethweerasinghe.toughsurvival.playerdata.wetness.IWetness;
import com.denethweerasinghe.toughsurvival.playerdata.wetness.Wetness;
import com.denethweerasinghe.toughsurvival.setup.ToughSurvival;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
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
            IHydration hydrCap = Hydration.getFromPlayer(player);
            IWetness wetCap = Wetness.getFromPlayer(player);

            if (testblock == Blocks.COBBLESTONE){
                hydrCap.setHydration(hydrCap.getHydration() + 1);
                Hydration.updateClient((ServerPlayerEntity) player, hydrCap);
            }
            else if (testblock == Blocks.DIAMOND_BLOCK){
                hydrCap.setHydration(hydrCap.getHydration() - 1);
                Hydration.updateClient((ServerPlayerEntity) player, hydrCap);
            }
            else if (testblock == Blocks.IRON_BLOCK){
                if (event.getItemStack().getItem() == ModItems.berryJuice){
                    ToughSurvival.LOGGER.debug("ITEM TEST");
                    Hydration.updateClient((ServerPlayerEntity) player, hydrCap);
                }
            }
            else if (testblock == Blocks.GOLD_BLOCK){
                wetCap.setWetness(wetCap.getWetness()-1);
                Wetness.updateClient((ServerPlayerEntity) player, wetCap);
                Minecraft.getInstance().player.sendChatMessage(String.valueOf(wetCap.getWetness()));

            }
            else if (testblock == Blocks.LAPIS_BLOCK){
                wetCap.setWetness(wetCap.getWetness()+1);
                Wetness.updateClient((ServerPlayerEntity) player, wetCap);
                Minecraft.getInstance().player.sendChatMessage(String.valueOf(wetCap.getWetness()));
            }
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
