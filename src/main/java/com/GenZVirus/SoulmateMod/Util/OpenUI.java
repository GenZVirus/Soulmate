package com.GenZVirus.SoulmateMod.Util;

import java.util.UUID;

import com.GenZVirus.SoulmateMod.Client.RequestScreen;

import net.minecraft.client.Minecraft;

public class OpenUI {
	
	public static void openUI(UUID sender) {
		Minecraft.getInstance().displayGuiScreen(new RequestScreen(sender));
	}

}
