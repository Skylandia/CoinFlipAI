package com.skylandia.coinflipai.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.skylandia.coinflipai.CoinFlip;
import com.skylandia.coinflipai.util.coinflip.CoinFlipRecord;
import com.skylandia.coinflipai.util.CurrencyFormat;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class UserDataCommand implements ClientCommandRegistrationCallback {
	@Override
	public void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
		dispatcher.register(
			literal("userdata")
				.then(
					ClientCommandManager.argument("username", StringArgumentType.string())
						.executes(UserDataCommand::userdata)
				)
		);
	}

	private static int userdata(CommandContext<FabricClientCommandSource> ctx) {
		String username = StringArgumentType.getString(ctx, "username");

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
			ctx.getSource().sendFeedback(Text.of("§7No recorded data for " + username + "."));
			return 1;
		}

		float odds = (float) wincount / (losecount + wincount);

		ctx.getSource().sendFeedback(Text.of("§9Userdata for " + username + ":"));
		ctx.getSource().sendFeedback(Text.of(String.format("§aGain§f|§cLoss§f: §a%s §f| §c%s §7(%s)", CurrencyFormat.format(totalWon), CurrencyFormat.format(totalLost), CurrencyFormat.format(totalWon - totalLost))));
		ctx.getSource().sendFeedback(Text.of(String.format("§aW§f|§cL§f: §a%s §f| §c%s §7(%.0f%%)", wincount, losecount, 100 * odds)));
		ctx.getSource().sendFeedback(Text.of(String.format("Streak §aW§f|§cL§f: §a%s §f| §c%s", largestWinstreak, largestLosestreak)));
		return 1;
	}
}
