package com.skylandia.coinflipai.handler;

import com.skylandia.coinflipai.event.ChatMessageCallback;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BankruptcyHandler implements ChatMessageCallback {
	@Override
	public void onChatMessage(String text) {
		Pattern pattern = Pattern.compile("^You do not have sufficient funds\\.$");
		Matcher matcher = pattern.matcher(text);

		if (matcher.find()) {
			OutgoingChatHandler.dispatchSendable(new OutgoingChatHandler.Command("payoff"));
			OutgoingChatHandler.enqueue(UUID.randomUUID().toString(), new OutgoingChatHandler.Chat("&7CoinFlipAI Disabled"));
			OutgoingChatHandler.enqueue(UUID.randomUUID().toString(), new OutgoingChatHandler.Chat("&7If you ran into a problem using CoinFlipAI, don't hesitate to /mail me."));
		}
	}
}