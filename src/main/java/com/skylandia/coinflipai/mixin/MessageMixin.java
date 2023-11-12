package com.skylandia.coinflipai.mixin;

import com.skylandia.coinflipai.event.ChatMessageCallback;

import com.skylandia.coinflipai.event.DirectMessageCallback;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Mixin(ClientPlayNetworkHandler.class)
public class MessageMixin {
	@Inject(method = "onGameMessage", at = @At("TAIL"))
	public void interceptGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
		String chatMessage = packet.content().getString();
		ChatMessageCallback.EVENT.invoker().onChatMessage(chatMessage);

		String regex = "\\[(\\w+) -> me] (.*)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(chatMessage);
		if (matcher.find()) {
			String username = matcher.group(1);
			String message = matcher.group(2);
			DirectMessageCallback.EVENT.invoker().onDirectMessage(username, message);
		}
	}
}