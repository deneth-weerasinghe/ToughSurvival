package com.denethweerasinghe.toughsurvival.handlers;

import com.denethweerasinghe.toughsurvival.items.itemdata.IItemHydration;
import com.denethweerasinghe.toughsurvival.items.itemdata.ItemHydration;
import com.denethweerasinghe.toughsurvival.playerdata.hydrationdata.Hydration;
import com.denethweerasinghe.toughsurvival.playerdata.hydrationdata.IHydration;
import com.denethweerasinghe.toughsurvival.setup.ToughSurvival;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.function.Predicate;

public class HydrationEvents {

    private final Predicate<PlayerEntity> predicate;
    private boolean enable;

    public HydrationEvents(Predicate<PlayerEntity> predicate) {
        this.predicate = predicate;
    }


    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event){


        PlayerEntity player = event.player;
        World world = player.world;

        // checks if on the server and checks if firing once or twice per tick
        // as ticks have two phases
        if (!world.isRemote) {

            // checks if health regeneration has been enabled or not
            GameRules.BooleanValue regen = world.getGameRules().get(GameRules.NATURAL_REGENERATION);

            // get hydration form player
            IHydration cap = Hydration.getFromPlayer(player);
            int hydration = cap.getHydration();

            if (event.phase == TickEvent.Phase.START) {

                // applies status effects to the player according to the hydration value
                applyEffects(player, hydration);

                float decay = Hydration.DEFAULT_DECAY;
                // additional checks modifying how fast decay is applied
                if (player.isSprinting()) {
                    decay += 0.1F;
                }
                if (player.isSwimming()) {
                    decay += 1;
                }

                cap.setDecayFactor(cap.getDecayFactor() - decay);
                if (cap.getDecayFactor() == 0) {
                    ToughSurvival.LOGGER.debug("applying decay");

                    // reset decay
                    cap.setDecayFactor(36);
                    cap.setHydration(hydration - 1);

                    // sync with client
                    Hydration.updateClient((ServerPlayerEntity) player, cap);
                }

                // if player passes necessary conditions then disable regeneration
                if (regen.get() && this.predicate.test(player)){
                    regen.set(false, world.getServer());
                    this.enable = true;
                }
            }
            else if (this.enable){
                regen.set(true, world.getServer());
                this.enable = false;
            }
        }
    }

    @SubscribeEvent
    public void onJumping(LivingEvent.LivingJumpEvent event){

        Entity entity = event.getEntity();

        // if the entity is a player
        // if on server side
        if (entity instanceof PlayerEntity && !entity.world.isRemote()){
            IHydration cap = Hydration.getFromPlayer((PlayerEntity) entity);
            cap.setDecayFactor(cap.getDecayFactor() - 2.5F);
        }
    }

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


    private static void applyEffects(PlayerEntity player, int hydration){

        // effects will get worse at this low hydration value
        int amplifier = 0;
        if (hydration <= 6){
            amplifier = 1;
        }

        if (hydration <= 12){
            player.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 5, amplifier, true, true));
        }
        if (hydration <= 10){
            player.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 5, amplifier, true, true));
        }
        if (hydration <= 8){
            player.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 5, amplifier, true, true));
        }
    }
}
