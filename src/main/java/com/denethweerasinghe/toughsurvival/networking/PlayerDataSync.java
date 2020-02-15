package com.denethweerasinghe.toughsurvival.networking;

import com.denethweerasinghe.toughsurvival.playerdata.PlayerProvider;
import com.denethweerasinghe.toughsurvival.playerdata.hydration.Hydration;
import com.denethweerasinghe.toughsurvival.playerdata.hydration.IHydration;
import com.denethweerasinghe.toughsurvival.playerdata.wetness.IWetness;
import com.denethweerasinghe.toughsurvival.playerdata.wetness.Wetness;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerDataSync {

    private final CompoundNBT nbt;

    public PlayerDataSync(int entityId, CompoundNBT nbt){
        nbt.putInt("entityId", entityId);
        this.nbt = nbt;
    }

    public PlayerDataSync(CompoundNBT nbt){
        this.nbt = nbt;
    }

    public static void encode(PlayerDataSync msg, PacketBuffer buf){
        buf.writeCompoundTag(msg.nbt);
    }

    public static PlayerDataSync decode(PacketBuffer buf){
        return new PlayerDataSync(buf.readCompoundTag());
    }

    public static void  handle(PlayerDataSync msg, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = (PlayerEntity) Minecraft.getInstance().world.getEntityByID(msg.nbt.getInt("entityId"));
            IHydration hydrCap = Hydration.getFromPlayer(player);
            IWetness wetCap = Wetness.getFromPlayer(player);

            PlayerProvider.PLAYER_HYDRATION.readNBT(hydrCap, null, msg.nbt);
            PlayerProvider.WETNESS.readNBT(wetCap, null, msg.nbt);
        });
        ctx.get().setPacketHandled(true);
    }

}
