package com.GenZVirus.SoulmateMod.Events;

import java.util.List;
import java.util.UUID;

import com.GenZVirus.SoulmateMod.SoulmateMod;
import com.GenZVirus.SoulmateMod.Database.Database;
import com.GenZVirus.SoulmateMod.Init.ItemInit;
import com.GenZVirus.SoulmateMod.Network.PacketHandlerCommon;
import com.GenZVirus.SoulmateMod.Network.Packets.SendRelationshipRequest;
import com.google.common.collect.Lists;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.network.NetworkDirection;

@Mod.EventBusSubscriber(modid = SoulmateMod.MOD_ID, bus = Bus.FORGE)
public class SoulmateEffects {

	@SubscribeEvent
	public static void onRoseUsed(EntityInteract event) {
		if (!(event.getTarget() instanceof PlayerEntity)) { return; }
		if (event.getPlayer().world.isRemote()) {
			if (event.getItemStack().getItem().equals(ItemInit.ROSE.get())) {
				event.getPlayer().getCooldownTracker().setCooldown(event.getItemStack().getItem(), 100);
			}
			return;
		}
		if (event.getItemStack().getItem().equals(ItemInit.ROSE.get())) {
			if (Database.areSoulmates(event.getPlayer().getUniqueID(), event.getTarget().getUniqueID())) { return; }
			event.getPlayer().getCooldownTracker().setCooldown(event.getItemStack().getItem(), 100);
			PacketHandlerCommon.INSTANCE.sendTo(new SendRelationshipRequest(event.getPlayer().getUniqueID()), ((ServerPlayerEntity) event.getTarget()).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
		}
	}

	@SubscribeEvent(receiveCanceled = true)
	public static void extinguish(LivingAttackEvent event) {
		if (event.getEntityLiving().world.isRemote()) { return; }
		if (!(event.getEntityLiving() instanceof PlayerEntity)) { return; }
		if (!(event.getSource().getTrueSource() instanceof PlayerEntity)) { return; }
		if (Database.areSoulmates(event.getEntityLiving().getUniqueID(), event.getSource().getTrueSource().getUniqueID())) {
			event.setCanceled(true);
			if (event.getEntityLiving().isBurning()) {
				event.getEntityLiving().extinguish();
			}
		}
	}

	@SubscribeEvent
	public static void StrengthBuff(LivingAttackEvent event) {
		if (event.getEntityLiving().world.isRemote()) { return; }
		if (!(event.getEntityLiving() instanceof PlayerEntity)) { return; }
		PlayerEntity player = (PlayerEntity) event.getEntityLiving();
		if (event.getSource().getTrueSource() == null) { return; }
		if (Database.hasPlayerInAGroup(player.getUniqueID()) && !Database.areSoulmates(player.getUniqueID(), event.getSource().getTrueSource().getUniqueID())) {
			UUID partnerID = Database.getParnetOf(player.getUniqueID());
			if (partnerID != null) {
				PlayerEntity partner = player.world.getPlayerByUuid(partnerID);
				if (partner != null)
					if (!partner.isPotionActive(Effects.STRENGTH))
						partner.addPotionEffect(new EffectInstance(Effects.STRENGTH, 100));
			}
		}
	}

	@SubscribeEvent
	public static void healing(PlayerTickEvent event) {
		if (event.player.world.isRemote()) { return; }

		// search for entities

		AxisAlignedBB CUBE_BOX = VoxelShapes.fullCube().getBoundingBox();
		AxisAlignedBB aabb = CUBE_BOX.offset(event.player.getPositionVec()).grow(5);
		List<Entity> list = event.player.world.getEntitiesWithinAABBExcludingEntity(event.player, aabb);
		List<PlayerEntity> players = Lists.newArrayList();

		// filter entities that are not players players

		for (Entity entity : list) {
			if (entity instanceof PlayerEntity) {
				players.add((PlayerEntity) entity);
			}
		}

		// check if they are soulmates

		for (PlayerEntity player : players) {
			if (Database.areSoulmates(event.player.getUniqueID(), player.getUniqueID())) {
				if (!player.isPotionActive(Effects.REGENERATION))
					player.addPotionEffect(new EffectInstance(Effects.REGENERATION, 100));
			}
		}
	}

	@SubscribeEvent
	public static void helpingHand(EntityInteract event) {
		if (event.getEntityLiving().world.isRemote()) { return; }
		if (Database.areSoulmates(event.getPlayer().getUniqueID(), event.getTarget().getUniqueID())) {
			int x = event.getPlayer().getPosition().getX();
			int y = event.getPlayer().getPosition().getY();
			int z = event.getPlayer().getPosition().getZ();
			event.getTarget().setPositionAndUpdate(x, y, z);
		}
	}

	@SubscribeEvent
	public static void healCooldown(ServerTickEvent event) {
		if (event.phase == Phase.START) { return; }
	}

}
