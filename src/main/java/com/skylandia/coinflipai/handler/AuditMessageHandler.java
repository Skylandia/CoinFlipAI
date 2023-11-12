package com.skylandia.coinflipai.handler;

import com.skylandia.coinflipai.CoinFlip;
import com.skylandia.coinflipai.util.coinflip.CoinFlipRecord;
import com.skylandia.coinflipai.event.DirectMessageCallback;


public class AuditMessageHandler implements DirectMessageCallback {
	@Override
	public void onDirectMessage(String username, String msg) {
		if (msg.equalsIgnoreCase("audit")) {
			double totalReceived = 0;
			double totalPaid = 0;
			int wins = 0;
			int losses = 0;

			for (CoinFlipRecord record : CoinFlip.history) {
				totalReceived += record.wager();
				totalPaid += record.win() ? 2 * record.wager() : 0.001;
				if (record.win()) {
					wins += 1;
				} else {
					losses += 1;
				}
			}
			float odds = (float) wins / CoinFlip.history.size();
			// too spammy
//			OutgoingChatHandler.enqueueDirectMessage(username, String.format("bal dif: &b%s", CurrencyFormat.format(totalReceived-totalPaid)));
			OutgoingChatHandler.enqueue(username, new OutgoingChatHandler.DirectMessage(username, String.format("&aW&f|&cL&f: &a%s &f| &c%s &7(%.0f%%)", wins, losses, odds*100)));
		}
	}
}
