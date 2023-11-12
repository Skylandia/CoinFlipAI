package com.skylandia.coinflipai.handler;

import com.skylandia.coinflipai.CoinFlip;
import com.skylandia.coinflipai.event.ChatMessageCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaymentHandler implements ChatMessageCallback {
	@Override
	public void onChatMessage(String chatMessage) {
		Pattern pattern = Pattern.compile("^\\$(.+) has been received from (?:\\[\\w+] )?(\\.?\\w+)\\.$");
		Matcher matcher = pattern.matcher(chatMessage);
		if (matcher.find()) {
			// Initialize variables
			double wager = Double.parseDouble(matcher.group(1).replace(",", ""));
			String username = matcher.group(2);

			CoinFlip.onPayment(username, wager);
		}
	}
}
