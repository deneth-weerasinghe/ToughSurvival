package com.denethweerasinghe.toughsurvival.networking;

import com.denethweerasinghe.toughsurvival.playerdata.PlayerProvider;

import com.denethweerasinghe.toughsurvival.playerdata.wetness.IWetness;
import com.denethweerasinghe.toughsurvival.playerdata.wetness.Wetness;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class WetnessSync {

    private final CompoundNBT nbt;

    public WetnessSync(int entityId, CompoundNBT nbt){
        nbt.putInt("entityId", entityId);
        this.nbt = nbt;
    }

    public WetnessSync(CompoundNBT nbt){
        this.nbt = nbt;
    }

    public static void encode(WetnessSync msg, PacketBuffer buf){
        buf.writeCompoundTag(msg.nbt);
    }

    public static WetnessSync decode(PacketBuffer buf){
        return new WetnessSync(buf.readCompoundTag());
    }

    public static void  handle(WetnessSync msg, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = (PlayerEntity) Minecraft.getInstance().world.getEntityByID(msg.nbt.getInt("entityId"));
            IWetness cap = Wetness.getFromPlayer(player);

            PlayerProvider.WETNESS.readNBT(cap, null, msg.nbt);
        });
        ctx.get().setPacketHandled(true);
    }

}
