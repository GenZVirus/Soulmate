package com.GenZVirus.SoulmateMod.Network.Packets;

import java.util.UUID;
import java.util.function.Supplier;

import com.GenZVirus.SoulmateMod.Database.Database;
import com.GenZVirus.SoulmateMod.File.XMLFileJava;
import com.GenZVirus.SoulmateMod.Util.Soulmates;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

public class SendRelationshipAnswer {

	public UUID sender, soulmate;
	public boolean accepted;

	public SendRelationshipAnswer(UUID sender, UUID soulmate, boolean accepted) {
		this.sender = sender;
		this.soulmate = soulmate;
		this.accepted = accepted;
	}

	public static void encode(SendRelationshipAnswer pkt, PacketBuffer buf) {
		buf.writeUniqueId(pkt.sender);
		buf.writeUniqueId(pkt.soulmate);
		buf.writeBoolean(pkt.accepted);
	}

	public static SendRelationshipAnswer decode(PacketBuffer buf) {
		return new SendRelationshipAnswer(buf.readUniqueId(), buf.readUniqueId(), buf.readBoolean());
	}

	public static void handle(SendRelationshipAnswer pkt, Supplier<NetworkEvent.Context> ctx) {

		ctx.get().enqueueWork(() -> {
			if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) {
				if(pkt.accepted) {
					
					UUID cheated = UUID.fromString(XMLFileJava.readElement("Soulmate", pkt.soulmate));
					if(!cheated.toString().equals(XMLFileJava.default_player_uuid)) {
						XMLFileJava.resetToDefault(cheated);
					}
					XMLFileJava.editElement("Soulmate", pkt.soulmate.toString(), pkt.sender);
					cheated = UUID.fromString(XMLFileJava.readElement("Soulmate", pkt.sender));
					if(!cheated.toString().equals(XMLFileJava.default_player_uuid)) {
						XMLFileJava.resetToDefault(cheated);
					}
					XMLFileJava.editElement("Soulmate", pkt.sender.toString(), pkt.soulmate);
					Database.LIST.add(new Soulmates(pkt.soulmate, pkt.sender));
				}
			}
		});
		ctx.get().setPacketHandled(true);
	}	
}
