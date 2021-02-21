package com.GenZVirus.SoulmateMod.Util;

import java.util.UUID;

import net.minecraft.world.World;

public class Soulmates {
	public UUID player1, player2;

	public Soulmates(UUID player1, UUID player2) {
		this.player1 = player1;
		this.player2 = player2;
	}
	
	public UUID getPlayer1() {
		return player1;
	}
	
	public UUID getPlayer2() {
		return player2;
	}
	
	public boolean groupHas(UUID player) {
		return player.equals(getPlayer1()) || player.equals(getPlayer2());
	}
	
	public UUID getPartnerOf(UUID player) {
		return player.equals(getPlayer1()) ? getPlayer2() : getPlayer1();
	}
	
	public boolean isAnyOnline(World world) {
		return world.getPlayerByUuid(getPlayer1()) != null || world.getPlayerByUuid(getPlayer2()) != null; 
	}

}
