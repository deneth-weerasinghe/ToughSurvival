package com.denethweerasinghe.toughsurvival.handlers;

import com.denethweerasinghe.toughsurvival.items.ModItems;
import com.denethweerasinghe.toughsurvival.items.itemdata.IItemHydration;
import com.denethweerasinghe.toughsurvival.items.itemdata.ItemHydration;
import com.denethweerasinghe.toughsurvival.playerdata.hydration.Hydration;
import com.denethweerasinghe.toughsurvival.playerdata.hydration.IHydration;
import com.denethweerasinghe.toughsurvival.playerdata.wetness.IWetness;
import com.denethweerasinghe.toughsurvival.playerdata.wetness.Wetness;
import com.denethweerasinghe.toughsurvival.setup.ToughSurvival;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.function.Predicate;

import static com.denethweerasinghe.toughsurvival.playerdata.hydration.Hydration.DEFAULT_DECAY;
import static com.denethweerasinghe.toughsurvival.playerdata.hydration.Hydration.MAX_DECAY;

public class PlayerEvents {

    private static final DamageSource DEHYDRATION = new DamageSource("dehydration").setDamageBypassesArmor().setDamageIsAbsolute();

    private final Predicate<PlayerEntity> predicate;
    private boolean enable;

    public PlayerEvents(Predicate<PlayerEntity> predicate) {
        this.predicate = predicate;
    }


    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event){


        PlayerEntity player = event.player;
        World world = player.world;

        // checks if on the server and checks if firing once or twice per tick
        // as ticks have two phases
        if (!world.isRemote && !(player.isCreative() || player.isSpectator())) {

            // checks if health regeneration has been enabled or not
            GameRules.BooleanValue regen = world.getGameRules().get(GameRules.NATURAL_REGENERATION);

            // get hydration form player
            IHydration hydrCap = Hydration.getFromPlayer(player);
            IWetness wetCap = Wetness.getFromPlayer(player);

            if (event.phase == TickEvent.Phase.START) {

                applyHydrEffects(player, hydrCap.getHydration());
                manipulateHydr(player, hydrCap, wetCap);
                manipulateWet(player, world, wetCap);

                // sync server and client
                Hydration.updateClient((ServerPlayerEntity) player, hydrCap);
                Wetness.updateClient((ServerPlayerEntity) player, wetCap);


//                if player passes necessary conditions then disable regeneration
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
    public void onItemUse(LivingEntityUseItemEvent.Finish event){

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
//                 water bottles are just special potions hence I have to use NBT tags
//                 to get the potion that corresponds to water bottles
                if (itemStack.getTag().getString("Potion").equals("minecraft:water")){
                    cap.setHydration(10);
                }
                else {
                    cap.setHydration(4);
                }
            }
            else if (item == Items.WATER_BUCKET){
                cap.setHydration(10);
            }
            else if (item == Items.MILK_BUCKET){
                cap.setHydration(6);
            }
            else if (item instanceof ModItems.Drink){
                cap.setHydration(((ModItems.Drink) item).getHydration());
            }

            // adding to playerHydration
            IHydration playerCap = Hydration.getFromPlayer((PlayerEntity) entity);
            playerCap.setHydration(playerCap.getHydration() + cap.getHydration());
            Hydration.updateClient((ServerPlayerEntity) entity, playerCap);
        }
    }



    private static void manipulateHydr(PlayerEntity player, IHydration hydrCap, IWetness wetCap){

        int hydration = hydrCap.getHydration();

        // applies status effects to the player according to the hydration value

        float decay = DEFAULT_DECAY;
        // additional checks modifying how fast decay is applied
        if (player.isSprinting()) {
            decay += 0.1F;
        }
        if (player.isSwimming()) {
            decay += 1;
        }

        decay += wetCap.getWetness() * -0.006F * DEFAULT_DECAY;

//        ToughSurvival.LOGGER.debug(hydrCap.getDecayFactor());
        // apply decay
        hydrCap.setDecayFactor(hydrCap.getDecayFactor() - decay);
        if (hydrCap.getDecayFactor() == 0) {
            ToughSurvival.LOGGER.debug("applying decay");

            // reset decay
            hydrCap.setDecayFactor(MAX_DECAY);
            hydrCap.setHydration(hydration - 1);

            ToughSurvival.LOGGER.debug("T I M E R "+Minecraft.getInstance().world.getGameTime());
        }

    }

    private static void manipulateWet(PlayerEntity player, World world, IWetness cap){

        BlockPos pos = player.getPosition();
        Biome biome = world.getBiome(pos);

        int wetness = cap.getWetness();
        boolean isInWater = world.getFluidState(pos).isTagged(FluidTags.WATER);
        boolean isInSnow = biome.getPrecipitation() == Biome.RainType.SNOW;

        // check if wetness should be increased at all (so can start timer)
        if (isInWater || world.isRaining()){

            // 6 extra wetness per 60 ticks -> 1 per 10 ticks
            if (isInWater){
                wetness += 6;
            }
            // 3 extra wetness per 60 ticks -> 1 per 20 ticks
            if (world.isRaining()){
                wetness += 3;
                // add 1/3 that of raining instead of the full 3 points
                // so that instead of +1 every 20 ticks it's +1 every 60 ticks
                if (isInSnow){
                    wetness -= 2;
                }
            }
        }
        else{
              if (wetness > 0){
                wetness--;
            }
        }

        cap.setTimer(cap.getTimer() + 1);

        // actually add the wetness
        if (cap.getTimer() == 20){
            cap.setTimer(0);
            cap.setWetness(wetness);
        }



    }

    private static void applyHydrEffects(PlayerEntity player, int hydration){

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

        // damages the player entity
        if (hydration == 0){
            player.attackEntityFrom(DEHYDRATION, 1.0F);
        }
    }
}
