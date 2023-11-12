package com.skylandia.coinflipai;

import com.skylandia.coinflipai.command.*;
import com.skylandia.coinflipai.event.ChatMessageCallback;
import com.skylandia.coinflipai.event.DirectMessageCallback;
import com.skylandia.coinflipai.handler.*;
import com.skylandia.coinflipai.util.CurrencyFormat;
import com.skylandia.coinflipai.util.ResourceManager;
import com.skylandia.coinflipai.util.coinflip.CoinFlipRecord;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;

public class CoinFlip implements ClientModInitializer {
	public static final String MOD_ID = "coinflip";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ArrayList<CoinFlipRecord> history = new ArrayList<>();

	private static final Random random = new Random();
	private static final double winProbability = 0.45;
	private static final double minBet = 1;
	private static final double maxBet = 1000000;
	private static long lastBigWin = 0;

	public static void onPayment(String username, double wager) {
		// Prevent wagers out of limits
		if (wager < CoinFlip.minBet) {
			OutgoingChatHandler.enqueue(username, new OutgoingChatHandler.DirectMessage(username, String.format("&7Wagers less than &c%s &7are not accepted. No refund will be given.", CurrencyFormat.format(CoinFlip.minBet))));
			return;
		}
		if (wager > CoinFlip.maxBet) {
			OutgoingChatHandler.enqueue(username, new OutgoingChatHandler.DirectMessage(username, String.format("&7Wagers over &c%s &7are not accepted.", CurrencyFormat.format(CoinFlip.maxBet))));
			OutgoingChatHandler.enqueue(username, new OutgoingChatHandler.Payment(username, wager));
			return;
		}

		boolean win = CoinFlip.random.nextDouble() <= CoinFlip.winProbability;

		// Pay players and log the interaction
		ResourceManager.saveRecord(new CoinFlipRecord(username, wager, win));
		OutgoingChatHandler.enqueue(username, new OutgoingChatHandler.Payment(username, win ? 2 * wager : 0.001));

		// Announce big wins (anti-spam restricts to every 2 minutes unless 1 mil bid)
		if (shouldBroadcast(win, wager)) {
			lastBigWin = Instant.now().getEpochSecond();
			OutgoingChatHandler.enqueue(username, new OutgoingChatHandler.Chat(String.format("%s won &a%s&f!", username, CurrencyFormat.format(wager))));
		}
	}

	public static void onReceipt(String username, double winnings) {
		// TODO move announcements here

		// TODO confirm payments went through

		// TODO save failed payments and listen for the recipient to come online
	}

	private static boolean shouldBroadcast(boolean win, double wager) {
		if (!win) return false;
		if (wager < maxBet/4) return false;
		if (wager == maxBet) return true;

		return Instant.now().getEpochSecond() - lastBigWin >= 120;
	}

	@Override
	public void onInitializeClient() {
		ResourceManager.loadHistory();

		ClientTickEvents.END_WORLD_TICK.register(new OutgoingChatHandler());

		ChatMessageCallback.EVENT.register(new PaymentHandler());
		ChatMessageCallback.EVENT.register(new ReceiptHandler());
		ChatMessageCallback.EVENT.register(new BankruptcyHandler());

		DirectMessageCallback.EVENT.register(new AuditMessageHandler());
		DirectMessageCallback.EVENT.register(new UserdataMessageHandler());

		ClientCommandRegistrationCallback.EVENT.register(new AuditCommand());
		ClientCommandRegistrationCallback.EVENT.register(new UserDataCommand());
		ClientCommandRegistrationCallback.EVENT.register(new AntiSpamCommand());
		ClientCommandRegistrationCallback.EVENT.register(new AdCommand());
	}
}