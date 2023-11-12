package com.skylandia.coinflipai.handler;

import com.skylandia.coinflipai.CoinFlip;
import com.skylandia.coinflipai.util.coinflip.CoinFlipRecord;
import com.skylandia.coinflipai.event.DirectMessageCallback;
import com.skylandia.coinflipai.util.CurrencyFormat;

public class UserdataMessageHandler implements DirectMessageCallback {
	@Override
	public void onDirectMessage(String username, String message) {
		if (message.equalsIgnoreCase("userdata")) {
			double totalWon = 0;
			double totalLost = 0;

			int wincount = 0;
			int losecount = 0;

			int largestWinstreak = 0;
			int largestLosestreak = 0;

			int tempwin = 0;
			int templose = 0;

			for (CoinFlipRecord record : CoinFlip.history.stream().filter((record) -> record.username().equalsIgnoreCase(username)).toList()) {
				if (record.win()) {
					tempwin += 1;
					templose = 0;

					wincount += 1;
					totalWon += record.wager();
				} else {
					templose += 1;
					tempwin = 0;

					losecount += 1;
					totalLost += record.wager();
					totalWon += 0.001;
				}
				if (tempwin >= largestWinstreak) largestWinstreak = tempwin;
				if (templose >= largestLosestreak) largestLosestreak = templose;
			}

			if (wincount + losecount == 0) {
				OutgoingChatHandler.enqueue(username, new OutgoingChatHandler.DirectMessage(username, "&7No recorded data."));
				return;
			}

			float odds = (float) wincount / (losecount + wincount);

			OutgoingChatHandler.enqueue(username, new OutgoingChatHandler.DirectMessage(username, String.format("&aGain&f|&cLoss&f: &a%s &f| &c%s &7(%s)", CurrencyFormat.format(totalWon), CurrencyFormat.format(totalLost), CurrencyFormat.format(totalWon - totalLost))));
			OutgoingChatHandler.enqueue(username, new OutgoingChatHandler.DirectMessage(username, String.format("&aW&f|&cL&f: &a%s &f| &c%s &7(%.0f%%)", wincount, losecount, 100 * odds)));
//			OutgoingChatHandler.enqueue(String.format("msg %s Streak &aW&f|&cL&f: &a%s &f| &c%s", username, largestWinstreak, largestLosestreak));
		}
	}
}
