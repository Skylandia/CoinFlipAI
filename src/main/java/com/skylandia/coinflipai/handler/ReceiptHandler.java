package com.skylandia.coinflipai.handler;

import com.skylandia.coinflipai.CoinFlip;
import com.skylandia.coinflipai.event.ChatMessageCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReceiptHandler implements ChatMessageCallback {
	@Override
	public void onChatMessage(String text) {
		Pattern pattern = Pattern.compile("^\\$(.+) has been sent to (?:\\[\\w+] )?(\\.?\\w+)\\.$");
		Matcher matcher = pattern.matcher(text);

		if (matcher.find()) {
			double winnings = Double.parseDouble(matcher.group(1).replaceAll(",", ""));
			String username = matcher.group(2);

			CoinFlip.onReceipt(username, winnings);

			return;
		}

		pattern = Pattern.compile("^(?:Error: Player not found\\.|You cannot pay offline users\\.)$");
		matcher = pattern.matcher(text);

		if (matcher.find()) {
			CoinFlip.onReceipt(null, 0);
		}
	}
}
