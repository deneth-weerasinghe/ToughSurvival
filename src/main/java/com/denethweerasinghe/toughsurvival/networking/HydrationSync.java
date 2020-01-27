package com.denethweerasinghe.toughsurvival.networking;

import com.denethweerasinghe.toughsurvival.playerdata.hydrationdata.Hydration;
import com.denethweerasinghe.toughsurvival.playerdata.hydrationdata.HydrationProvider;
import com.denethweerasinghe.toughsurvival.playerdata.hydrationdata.IHydration;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class HydrationSync {

    private final CompoundNBT nbt;

    public HydrationSync(int entityId, CompoundNBT nbt){
        nbt.putInt("entityId", entityId);
        this.nbt = nbt;
    }

    public HydrationSync(CompoundNBT nbt){
        this.nbt = nbt;
    }

    public static void encode(HydrationSync msg, PacketBuffer buf){
        buf.writeCompoundTag(msg.nbt);
    }

    public static HydrationSync decode(PacketBuffer buf){
        return new HydrationSync(buf.readCompoundTag());
    }

    public static void  handle(HydrationSync msg, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = (PlayerEntity) Minecraft.getInstance().world.getEntityByID(msg.nbt.getInt("entityId"));
            IHydration cap = Hydration.getFromPlayer(player);

            HydrationProvider.PLAYER_HYDRATION.readNBT(cap, null, msg.nbt);
        });
        ctx.get().setPacketHandled(true);
    }

}
