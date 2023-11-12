package com.skylandia.coinflipai.util;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class MapList<K, V> implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	private final ArrayList<K> keys = new ArrayList<>();
	private final ArrayList<V> values = new ArrayList<>();

	public MapList() {}

	public V put(K key, V value) {
		int index = keys.indexOf(key);
		if (index == -1) {
			keys.add(key);
			values.add(value);
		} else {
			keys.set(index, key);
			return values.set(index, value);
		}
		return null;
	}
	public V remove(K key) {
		int index = keys.indexOf(key);
		if (index >= 0) {
			keys.remove(index);
			return values.remove(index);
		}
		return null;
	}
	public V get(K key) {
		int index = keys.indexOf(key);
		return values.get(index);
	}

	public boolean isEmpty() {
		return keys.isEmpty();
	}

	public Map.Entry<K, V> remove(int index) {
		return Map.entry(keys.remove(index), values.remove(index));
	}

	public boolean containsKey(K key) {
		return keys.contains(key);
	}
	public boolean containsValue(V value) {
		return values.contains(value);
	}
}
