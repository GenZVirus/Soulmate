package com.GenZVirus.SoulmateMod.Events;

import com.GenZVirus.SoulmateMod.SoulmateMod;
import com.GenZVirus.SoulmateMod.Database.Database;
import com.GenZVirus.SoulmateMod.File.XMLFileJava;
import com.GenZVirus.SoulmateMod.Util.Soulmates;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = SoulmateMod.MOD_ID, bus = Bus.FORGE)
public class PlayerConnectionEvents {

	@SubscribeEvent
	public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent e) {
		if (e.getPlayer().world.isRemote()) { return; }
		XMLFileJava.save((ServerPlayerEntity) e.getPlayer());
	}

	@SubscribeEvent
	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent e) {
		if (e.getPlayer().world.isRemote()) { return; }
		XMLFileJava.load((ServerPlayerEntity) e.getPlayer());
	}

	@SubscribeEvent
	public static void checkSoulmates(WorldTickEvent event) {
		if (event.world.isRemote()) { return; }
		if (event.phase == Phase.START) { return; }
		if (event.world.getPlayers().isEmpty()) { return; }

		for (Soulmates soulmates : Database.LIST) {
			if (!soulmates.isAnyOnline(event.world)) {
				Database.LIST.remove(soulmates);
				break;
			}
		}
	}

}
