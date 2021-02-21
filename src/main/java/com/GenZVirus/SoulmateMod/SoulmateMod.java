package com.GenZVirus.SoulmateMod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.GenZVirus.SoulmateMod.Init.ItemInit;
import com.GenZVirus.SoulmateMod.Network.PacketHandlerCommon;

import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("soulmate")
public class SoulmateMod {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger();

	/*
	 * Mod id reference
	 */
	public static final String MOD_ID = "soulmate";

	public static SoulmateMod instance;

	public SoulmateMod() {
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		modEventBus.addListener(this::setup);
		modEventBus.addListener(this::doClientStuff);

		// Registering custom items

		ItemInit.ITEMS.register(modEventBus);
		LOGGER.info("Items loaded successfully");

		instance = this;

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void setup(final FMLCommonSetupEvent event) {
		// Initializing the PacketHandler

		PacketHandlerCommon.init();
		LOGGER.info("Packets loaded successfully");
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
	}

	// You can use SubscribeEvent and let the Event Bus discover methods to call
	@SubscribeEvent
	public void onServerStarting(FMLServerStartingEvent event) {
	}

	// You can use EventBusSubscriber to automatically subscribe events on the
	// contained class (this is subscribing to the MOD
	// Event bus for receiving Registry Events)
	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
		@SubscribeEvent
		public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
		}
	}
}
