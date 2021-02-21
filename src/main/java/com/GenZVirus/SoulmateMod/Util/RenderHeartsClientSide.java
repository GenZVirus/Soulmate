package com.GenZVirus.SoulmateMod.Util;

import java.util.Random;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;

public class RenderHeartsClientSide {

	public static void renderHearts(PlayerEntity player, PlayerEntity player2) {
		for (int i = 0; i < 7; ++i) {
			Random rand = new Random();
			double d0 = rand.nextGaussian() * 0.02D;
			double d1 = rand.nextGaussian() * 0.02D;
			double d2 = rand.nextGaussian() * 0.02D;
			player.world.addParticle(ParticleTypes.HEART, player.getPosXRandom(1.0D), player.getPosYRandom() + 0.5D, player.getPosZRandom(1.0D), d0, d1, d2);
			player2.world.addParticle(ParticleTypes.HEART, player2.getPosXRandom(1.0D), player2.getPosYRandom() + 0.5D, player2.getPosZRandom(1.0D), d0, d1, d2);
		}
	}

}
