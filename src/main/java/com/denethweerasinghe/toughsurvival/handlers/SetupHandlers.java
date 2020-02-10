package com.denethweerasinghe.toughsurvival.handlers;

import com.denethweerasinghe.toughsurvival.itemdata.ItemHydrProvider;
import com.denethweerasinghe.toughsurvival.playerdata.hydrationdata.Hydration;
import com.denethweerasinghe.toughsurvival.playerdata.hydrationdata.HydrationProvider;
import com.denethweerasinghe.toughsurvival.playerdata.hydrationdata.IHydration;
import com.denethweerasinghe.toughsurvival.setup.ToughSurvival;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SetupHandlers {

    @SubscribeEvent
    public static void onEntityConstruction(AttachCapabilitiesEvent<Entity> event){
        if (event.getObject() instanceof PlayerEntity){
            event.addCapability(new ResourceLocation(ToughSurvival.MOD_ID, "hydration"), new HydrationProvider());
        }
    }

    @SubscribeEvent
    public static void onItemConstruction(AttachCapabilitiesEvent<ItemStack> event){
        ItemStack itemStack = event.getObject();
        Item item = itemStack.getItem();
        if (item.isFood() || item instanceof PotionItem || item == Items.MILK_BUCKET || item == Items.WATER_BUCKET){
            event.addCapability(new ResourceLocation(ToughSurvival.MOD_ID, "item_hydration"), new ItemHydrProvider());
        }
    }

    @SubscribeEvent
    public static void onLogIn(PlayerEvent.PlayerLoggedInEvent event){
        PlayerEntity player = event.getPlayer();
        if (player instanceof ServerPlayerEntity){
            Hydration.updateClient((ServerPlayerEntity) player, Hydration.getFromPlayer(player));
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
}
