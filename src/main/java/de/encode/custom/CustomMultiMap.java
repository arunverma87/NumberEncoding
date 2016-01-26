/**
 *
 */
package de.encode.custom;

import java.util.List;

/**
 * Custom multimap contract for basic operation
 *
 * @author arunv
 *
 */
public interface CustomMultiMap<K, V> {

	public List<V> get(K key);

	public boolean put(K key, V value);

	public boolean contains(K key);

	public int size();

}
