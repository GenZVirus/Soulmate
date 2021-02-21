package com.GenZVirus.SoulmateMod.Client;

import java.util.UUID;

import com.GenZVirus.SoulmateMod.Network.PacketHandlerCommon;
import com.GenZVirus.SoulmateMod.Network.Packets.SendPlayerHearts;
import com.GenZVirus.SoulmateMod.Network.Packets.SendRelationshipAnswer;
import com.GenZVirus.SoulmateMod.Util.RenderHeartsClientSide;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class RequestScreen extends Screen {

	public Button accept, reject;
	public Minecraft mc = Minecraft.getInstance();
	public UUID sender;

	public RequestScreen(UUID sender) {
		super(new TranslationTextComponent(""));
		this.sender = sender;
	}

	@Override
	protected void init() {
		super.init();

		
		int x = mc.getMainWindow().getScaledWidth() / 2;
		int y = mc.getMainWindow().getScaledHeight() / 2;
		
		accept = new Button(x - 80, y - 20, 50, 20, new TranslationTextComponent("Accept"), (z) -> {
			PacketHandlerCommon.INSTANCE.sendToServer(new SendRelationshipAnswer(mc.player.getUniqueID(), this.sender, true));
			RenderHeartsClientSide.renderHearts(mc.player, mc.player.world.getPlayerByUuid(this.sender));
			PacketHandlerCommon.INSTANCE.sendToServer(new SendPlayerHearts(sender));
			this.closeScreen();
		});

		reject = new Button(x + 30, y - 20, 50, 20, new TranslationTextComponent("Reject"), (z) -> {
			PacketHandlerCommon.INSTANCE.sendToServer(new SendRelationshipAnswer(mc.player.getUniqueID(), this.sender, false));
			this.closeScreen();
		});

		this.addButton(accept);
		this.addButton(reject);

	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		PlayerEntity player = mc.world.getPlayerByUuid(sender);
		String message = player.getName().getUnformattedComponentText() + " is offering you to become your soulmate. Do you accept?";
		int x = mc.getMainWindow().getScaledWidth() / 2;
		int y = mc.getMainWindow().getScaledHeight() / 2;
		font.drawString(new MatrixStack(), message, x - font.getStringWidth(message) / 2, y - 40, 16777215);
		
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return true;
	}

}
