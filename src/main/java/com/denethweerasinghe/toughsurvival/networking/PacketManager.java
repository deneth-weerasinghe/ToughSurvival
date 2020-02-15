package com.denethweerasinghe.toughsurvival.networking;

import com.denethweerasinghe.toughsurvival.setup.ToughSurvival;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketManager {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(
            ToughSurvival.MOD_ID, "toughsurvival"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register(){
        int id = 0;
        INSTANCE.registerMessage(
                id++,
                PlayerDataSync.class,
                PlayerDataSync::encode,
                PlayerDataSync::decode,
                PlayerDataSync::handle);
    }

    public static void sendToServer(Object msg){
        INSTANCE.sendToServer(msg);
    }

    public static void sendTo(ServerPlayerEntity player, Object msg){
        INSTANCE.sendTo(msg, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

}
