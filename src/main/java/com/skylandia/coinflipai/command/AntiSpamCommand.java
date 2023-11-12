package com.skylandia.coinflipai.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;

import com.skylandia.coinflipai.handler.OutgoingChatHandler;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

public class AntiSpamCommand implements ClientCommandRegistrationCallback {
	@Override
	public void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
		dispatcher.register(
			literal("antispam")
				.then(argument("ticks", IntegerArgumentType.integer()))
				.executes(AntiSpamCommand::execute)
		);
	}

	private static int execute(CommandContext<FabricClientCommandSource> ctx) {
		OutgoingChatHandler.cooldown = IntegerArgumentType.getInteger(ctx, "ticks");

		ctx.getSource().sendFeedback(Text.of("Set cooldown to " + OutgoingChatHandler.cooldown + " ticks."));
		return 1;
	}
}