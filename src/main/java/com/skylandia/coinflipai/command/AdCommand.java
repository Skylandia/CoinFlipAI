package com.skylandia.coinflipai.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import com.skylandia.coinflipai.handler.OutgoingChatHandler;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import net.minecraft.command.CommandRegistryAccess;

import java.util.UUID;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class AdCommand implements ClientCommandRegistrationCallback {
	@Override
	public void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
		dispatcher.register(
			literal("ad")
				.executes(AdCommand::execute)
		);
	}

	private static int execute(CommandContext<FabricClientCommandSource> ctx) {
		OutgoingChatHandler.enqueue(UUID.randomUUID().toString(), new OutgoingChatHandler.Chat("&e45% &fodds to &adouble &fyour money! &b/ad &fto see how!"));
		return 1;
	}
}