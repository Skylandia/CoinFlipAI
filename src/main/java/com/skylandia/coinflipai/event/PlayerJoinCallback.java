package com.skylandia.coinflipai.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface PlayerJoinCallback {
	Event<PlayerJoinCallback> EVENT = EventFactory.createArrayBacked(PlayerJoinCallback.class,
		(listeners) -> (username) -> {
			for (PlayerJoinCallback listener : listeners) {
				listener.onPlayerJoin(username);
			}
		});

	void onPlayerJoin(String username);
}