package com.skylandia.coinflipai.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface ChatMessageCallback {
	Event<ChatMessageCallback> EVENT = EventFactory.createArrayBacked(ChatMessageCallback.class,
		(listeners) -> (text) -> {
			for (ChatMessageCallback listener : listeners) {
				listener.onChatMessage(text);
			}
		});

	void onChatMessage(String text);
}
