package com.GenZVirus.SoulmateMod.Network.Packets;

import java.util.UUID;
import java.util.function.Supplier;

import com.GenZVirus.SoulmateMod.Network.PacketHandlerCommon;
import com.GenZVirus.SoulmateMod.Util.RenderHeartsClientSide;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

public class SendPlayerHearts {

	public UUID soulmate;

	public SendPlayerHearts(UUID soulmate) {
		this.soulmate = soulmate;
	}

	public static void encode(SendPlayerHearts pkt, PacketBuffer buf) {
		buf.writeUniqueId(pkt.soulmate);
	}

	public static SendPlayerHearts decode(PacketBuffer buf) {
		return new SendPlayerHearts(buf.readUniqueId());
	}

	public static void handle(SendPlayerHearts pkt, Supplier<NetworkEvent.Context> ctx) {

		ctx.get().enqueueWork(() -> {
			if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) {
				PacketHandlerCommon.INSTANCE.sendTo(new SendPlayerHearts(pkt.soulmate), ((ServerPlayerEntity) ctx.get().getSender().world.getPlayerByUuid(pkt.soulmate)).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
			}
			if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
				RenderHeartsClientSide.renderHearts(ctx.get().getSender(), ctx.get().getSender().world.getPlayerByUuid(pkt.soulmate));
			}
		});
		ctx.get().setPacketHandled(true);
	}
}
