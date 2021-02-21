package com.GenZVirus.SoulmateMod.Network.Packets;

import java.util.UUID;
import java.util.function.Supplier;

import com.GenZVirus.SoulmateMod.Util.OpenUI;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

public class SendRelationshipRequest {

	public UUID sender;

	public SendRelationshipRequest(UUID sender) {
		this.sender = sender;
	}

	public static void encode(SendRelationshipRequest pkt, PacketBuffer buf) {
		buf.writeUniqueId(pkt.sender);
	}

	public static SendRelationshipRequest decode(PacketBuffer buf) {
		return new SendRelationshipRequest(buf.readUniqueId());
	}

	public static void handle(SendRelationshipRequest pkt, Supplier<NetworkEvent.Context> ctx) {

		ctx.get().enqueueWork(() -> {
			if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
				OpenUI.openUI(pkt.sender);
			}
		});
		ctx.get().setPacketHandled(true);
	}

}
