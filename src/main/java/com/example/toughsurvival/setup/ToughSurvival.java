package com.example.toughsurvival.setup;

import com.example.toughsurvival.itemdata.IItemHydration;
import com.example.toughsurvival.itemdata.ItemHydration;
import com.example.toughsurvival.itemdata.ItemStorage;
import com.example.toughsurvival.items.ModItems;
import com.example.toughsurvival.networking.PacketManager;
import com.example.toughsurvival.playerdata.hydrationdata.Hydration;
import com.example.toughsurvival.playerdata.hydrationdata.HydrationStorage;
import com.example.toughsurvival.playerdata.hydrationdata.IHydration;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file

@Mod(ToughSurvival.MOD_ID)
public class ToughSurvival {

    public static final String MOD_ID = "toughsurvival"; // useful value in other situations
    public static final Logger LOGGER = LogManager.getLogger(); // built in logger to console

    public ToughSurvival() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get(). getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());

        LOGGER.info("REGISTERING HYDRATION CAPABILITY");
        CapabilityManager.INSTANCE.register(IHydration.class, new HydrationStorage(), Hydration::new);
        CapabilityManager.INSTANCE.register(IItemHydration.class, new ItemStorage(), () -> new ItemHydration(null));
        PacketManager.register();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }


    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
            event.getRegistry().registerAll(
                    ModItems.appleJuice.setRegistryName(ToughSurvival.MOD_ID, "apple_juice"),
                    ModItems.berryJuice.setRegistryName(ToughSurvival.MOD_ID, "berry_juice")
                            );
        }
    }
}

