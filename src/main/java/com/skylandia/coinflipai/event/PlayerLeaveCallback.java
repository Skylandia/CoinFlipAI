package com.skylandia.coinflipai.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface PlayerLeaveCallback {
	Event<PlayerLeaveCallback> EVENT = EventFactory.createArrayBacked(PlayerLeaveCallback.class,
		(listeners) -> (username) -> {
			for (PlayerLeaveCallback listener : listeners) {
				listener.onPlayerLeave(username);
			}
		});

	void onPlayerLeave(String username);
}
