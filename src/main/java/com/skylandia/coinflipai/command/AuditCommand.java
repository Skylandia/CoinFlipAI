package com.skylandia.coinflipai.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import com.skylandia.coinflipai.CoinFlip;
import com.skylandia.coinflipai.util.coinflip.CoinFlipRecord;
import com.skylandia.coinflipai.util.CurrencyFormat;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class AuditCommand implements ClientCommandRegistrationCallback {
	@Override
	public void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
		dispatcher.register(literal("audit").executes(AuditCommand::audit));
	}

	private static int audit(CommandContext<FabricClientCommandSource> ctx) {
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

		ctx.getSource().sendFeedback(Text.of("§9Audit:"));
		ctx.getSource().sendFeedback(Text.of(String.format("bal dif: §b%s", CurrencyFormat.format(totalReceived-totalPaid))));
		ctx.getSource().sendFeedback(Text.of(String.format("§aW§f|§cL§f: §a%s §f| §c%s §7(%.0f%%)", wins, losses, odds*100)));
		return 1;
	}
}
