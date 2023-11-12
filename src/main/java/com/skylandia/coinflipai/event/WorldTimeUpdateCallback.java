package com.skylandia.coinflipai.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface WorldTimeUpdateCallback {
	Event<WorldTimeUpdateCallback> EVENT = EventFactory.createArrayBacked(WorldTimeUpdateCallback.class,
		(listeners) -> (text) -> {
			for (WorldTimeUpdateCallback listener : listeners) {
				listener.onWorldTimeUpdate(text);
			}
		});

	void onWorldTimeUpdate(String text);
}
