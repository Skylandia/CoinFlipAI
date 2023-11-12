package com.skylandia.coinflipai.util;

public interface Sendable {
	String content();
	Type type();

	enum Type {
		CHAT,
		COMMAND
	}
}