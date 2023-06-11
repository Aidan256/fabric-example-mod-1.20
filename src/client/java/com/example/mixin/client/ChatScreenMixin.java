package com.example.mixin.client;

import java.util.Arrays;

import com.example.Commands.Command;
import com.example.Commands.CommandRegistry;
import com.example.Spaceclientmod;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.text.Text;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {
	@Redirect(method = "sendMessage",at = @At(value="INVOKE",target="Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendChatMessage(Ljava/lang/String;)V"))
	private void redirector(ClientPlayNetworkHandler instance, String message) {
		if(message.startsWith(Spaceclientmod.PREFIX)) {
			String trimmedMessage = message.substring(Spaceclientmod.PREFIX.length());
			if(trimmedMessage.isEmpty()||trimmedMessage.isBlank()) return;
			String[] messageSplit = trimmedMessage.trim().split(" +");
			String command = messageSplit[0];
			String[] args = Arrays.copyOfRange(messageSplit, 1, messageSplit.length);
			Command toRun = CommandRegistry.getByAlias(command);
			if(toRun==null) {
				MinecraftClient.getInstance().player.sendMessage(Text.of("Command not found"));
			}else {
				toRun.execute(args);
			}
		}else {
			instance.sendChatMessage(message);
		}
		
	}
}
