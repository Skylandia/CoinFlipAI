package com.skylandia.coinflipai.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface DirectMessageCallback {
	Event<DirectMessageCallback> EVENT = EventFactory.createArrayBacked(DirectMessageCallback.class,
		(listeners) -> (username, msg) -> {
			for (DirectMessageCallback listener : listeners) {
				listener.onDirectMessage(username, msg);
			}
		});

	void onDirectMessage(String username, String msg);
}
