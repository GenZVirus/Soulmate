package com.GenZVirus.SoulmateMod.Database;

import java.util.List;
import java.util.UUID;

import com.GenZVirus.SoulmateMod.File.XMLFileJava;
import com.GenZVirus.SoulmateMod.Util.Soulmates;
import com.google.common.collect.Lists;

public class Database {
	public static List<Soulmates> LIST = Lists.newArrayList();

	public static boolean hasPlayerInAGroup(UUID player) {
		for (Soulmates soulmates : Database.LIST) {
			if (soulmates.groupHas(player)) { return true; }
		}
		return false;
	}

	public static void savePlayer(UUID player) {
		for (Soulmates soulmates : Database.LIST) {
			if (soulmates.groupHas(player)) {
				XMLFileJava.editElement("Soulmate", soulmates.getPartnerOf(player).toString(), player);
			}
		}
	}
	
	public static UUID getParnetOf(UUID player) {
		for (Soulmates soulmates : Database.LIST) {
			if (soulmates.groupHas(player)) {
				return soulmates.getPartnerOf(player);
			}
		}
		return null;
	}
	
	public static boolean areSoulmates(UUID player1, UUID player2) {
		if(player1.equals(getParnetOf(player2))) {
			return true;
		}
		return false;
	}
	
}
