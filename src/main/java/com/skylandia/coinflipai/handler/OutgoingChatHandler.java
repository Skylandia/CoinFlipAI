package com.skylandia.coinflipai.handler;

import com.skylandia.coinflipai.CoinFlip;
import com.skylandia.coinflipai.util.RoundRobinQueue;
import com.skylandia.coinflipai.util.Sendable;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;

public class OutgoingChatHandler implements ClientTickEvents.EndWorldTick {

	private static final RoundRobinQueue<String, Sendable> queue = new RoundRobinQueue<>();

	private static long cooldownTimer = 0;
	public static long cooldown = 25;


	public static void enqueue(String agent, Sendable toSend) {
		queue.enqueue(agent, toSend);
	}

	public static void dispatchSendable(Sendable sendable) {
		cooldownTimer = cooldown;

		ClientPlayNetworkHandler c2sConnection = MinecraftClient.getInstance().getNetworkHandler();
		if (c2sConnection == null) {
			CoinFlip.LOGGER.warn(sendable.type().name().toUpperCase() + " FAILED: " + sendable.content());
			return;
		}

		switch (sendable.type()) {
			case CHAT -> c2sConnection.sendChatMessage(sendable.content());
			case COMMAND -> c2sConnection.sendCommand(sendable.content());
		}
	}

	@Override
	public void onEndTick(ClientWorld world) {
		cooldownTimer--;

		if (!queue.isEmpty() && cooldownTimer <= 0) {
			dispatchSendable(queue.dequeue());
		}
	}

	public record Chat(String message) implements Sendable {
		@Override
		public String content() {
			return message;
		}

		@Override
		public Type type() {
			return Type.CHAT;
		}
	}
	public record Command(String command) implements Sendable{

		@Override
		public String content() {
			return command;
		}

		@Override
		public Type type() {
			return Type.COMMAND;
		}
	}
	public record Payment(String recipient, double amount) implements Sendable {
		@Override
		public String content() {
			return "pay " + recipient + " " + amount;
		}

		@Override
		public Sendable.Type type() {
			return Sendable.Type.COMMAND;
		}
	}
	public record DirectMessage(String recipient, String message) implements Sendable {
		@Override
		public String content() {
			return "msg " + recipient + " " + message;
		}

		@Override
		public Type type() {
			return Type.COMMAND;
		}
	}
}