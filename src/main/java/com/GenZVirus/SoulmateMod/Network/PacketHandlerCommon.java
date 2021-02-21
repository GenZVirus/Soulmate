package com.GenZVirus.SoulmateMod.Network;

import com.GenZVirus.SoulmateMod.SoulmateMod;
import com.GenZVirus.SoulmateMod.Network.Packets.SendPlayerHearts;
import com.GenZVirus.SoulmateMod.Network.Packets.SendRelationshipAnswer;
import com.GenZVirus.SoulmateMod.Network.Packets.SendRelationshipRequest;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandlerCommon {

	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(SoulmateMod.MOD_ID, "soulmate"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

	public static void init() {
		int id = 0;

		INSTANCE.messageBuilder(SendRelationshipRequest.class, id++).encoder(SendRelationshipRequest::encode).decoder(SendRelationshipRequest::decode).consumer(SendRelationshipRequest::handle).add();
		INSTANCE.messageBuilder(SendRelationshipAnswer.class, id++).encoder(SendRelationshipAnswer::encode).decoder(SendRelationshipAnswer::decode).consumer(SendRelationshipAnswer::handle).add();
		INSTANCE.messageBuilder(SendPlayerHearts.class, id++).encoder(SendPlayerHearts::encode).decoder(SendPlayerHearts::decode).consumer(SendPlayerHearts::handle).add();
	}
}
