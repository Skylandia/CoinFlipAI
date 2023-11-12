package com.skylandia.coinflipai.util;

import java.util.ArrayList;
import java.util.Map;

public class RoundRobinQueue<K, V> {

	private final MapList<K, ArrayList<V>> queue;

	public RoundRobinQueue() {
		queue = new MapList<>();
	}

	public void enqueue(K key, V value) {
		ArrayList<V> values = new ArrayList<>();
		if (queue.containsKey(key)) {
			values = queue.get(key);
		}
		values.add(value);
		queue.put(key, values);
	}

	public V dequeue() {
		// Grab the top entry from the queue
		Map.Entry<K, ArrayList<V>> entry = queue.remove(0);
		K key = entry.getKey();
		ArrayList<V> values = entry.getValue();

		// Extract the value from the value list
		V value = values.remove(0);

		// Add the remaining values to the back of the queue
		if (!values.isEmpty()) {
			queue.put(key, values);
		}

		// Return the dequeued value
		return value;
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}
}
