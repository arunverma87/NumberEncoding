/**
 *
 */
package de.encode.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.encode.dictionary.Node;

/**
 *
 * The custom implementation for creating a multimap of objects. It stores
 * multiple values for same key so as to handle the {@link Node} structure
 * efficiently
 *
 * @author arunv
 *
 */
public class CustomArrayListMultiMap<K, V> implements CustomMultiMap<K, V> {

	private List<V> values;
	private Map<K, List<V>> keys;
	int size = 0;

	public CustomArrayListMultiMap() {
		keys = new HashMap<K, List<V>>();
	}

	/* (non-Javadoc)
	 * @see de.encode.custom.CustomMultiMap#get(java.lang.Object)
	 */
	@Override
	public List<V> get(K key) {
		if (keys.containsKey(key)) {
			return keys.get(key);
		} else
			return new ArrayList<>();
	}

	/* (non-Javadoc)
	 * @see de.encode.custom.CustomMultiMap#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean put(K key, V value) {
		if (!keys.containsKey(key)) {
			createAndInsert(key, value);
		} else {
			keys.get(key).add(value);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see arun.code.Onetomanymap#size()
	 */
	@Override
	public int size() {
		for (K key : keys.keySet()) {
			size += ((List<V>) keys.get(key)).size();
		}
		return size;
	}

	private boolean createAndInsert(K key, V value) {
		try {
			values = new ArrayList<>();
			values.add(value);
			keys.put(key, values);
		} catch (Exception ex) {
			throw ex;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see arun.code.multimap.Multimap#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(K key) {
		return keys.containsKey(key);
	}
}
