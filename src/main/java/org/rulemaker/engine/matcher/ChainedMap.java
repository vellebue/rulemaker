package org.rulemaker.engine.matcher;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A chained map is a map built using several maps placed into a 
 * stack. If you try to add or modify the map you will only interact
 * with the map in the top of the stack. But if you get a value from 
 * this chained map you will get it from a deeper map into the stack
 * if it is not found (null value) in a map near the top. It is
 * useful to simulate variable scopes in nested environments.
 * 
 * @author &Aacute;ngel Garc&iacute;a Bastanchuri
 *
 * @param <K> The Type for the key in this chained map.
 * @param <V> The Type for the value in this chained map.
 */
public class ChainedMap<K,V> implements Map<K,V> {
	
	private Map<K,V> currentMap;
	private ChainedMap<K,V> innerChainedMap;
	
	/**
	 * Builds an empty chained map with a single empty map in the top.
	 */
	public ChainedMap() {
		this(new HashMap<K, V>(), null);
	}
	
	/**
	 * Builds a chained map with a single map from the 
	 * argument constructor.
	 * 
	 * @param map The map used to build this single map.
	 * 
	 * @throws IllegalArgumentException If Map argument is null.
	 * 
	 */
	public ChainedMap(Map<K,V> map) throws IllegalArgumentException {
		this(map, null);
	}
	
	/**
	 * Builds a chained map chaining the map to the top
	 * of the given chained map.
	 * 
	 * @param map The map that must be put on the top of the given 
	 *        chained map.
	 *        
	 * @param chanedMap The chained map used as a base where the map
	 *        will be put on the top.
	 *        
	 * @throws IllegalArgumentException If Map argument is null.
	 * 
	 */
	public ChainedMap(Map<K,V> map, ChainedMap<K,V> chainedMap) throws IllegalArgumentException {
		if (map == null) {
			throw new IllegalArgumentException("Map argument must not be null");
		} else {
			currentMap = map;
			innerChainedMap = chainedMap;
		}
	}
	
	
	public int size() {
		return this.keySet().size();
	}

	public boolean isEmpty() {
		if (innerChainedMap == null) {
			return currentMap.isEmpty();
		} else {
			return currentMap.isEmpty() && innerChainedMap.isEmpty();
		}
	}

	public boolean containsKey(Object key) {
		return currentMap.containsKey(key) || 
			   ((innerChainedMap != null) && innerChainedMap.containsKey(key));
	}

	public boolean containsValue(Object value) {
		return whichKeysMapToAValue(value).size() > 0;
	}

	public V get(Object key) {
		V value = currentMap.get(key);
		if ((value == null) && (innerChainedMap != null)) {
			return innerChainedMap.get(key);
		} else {
			return value;
		}
	}

	public V put(K key, V value) {
		V previousValue = get(key);
		currentMap.put(key, value);
		return previousValue;
	}

	public V remove(Object key) {
		V previousValue = get(key);
		currentMap.remove(key);
		if (innerChainedMap != null) {
			innerChainedMap.remove(key);
		}
		return previousValue;
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		currentMap.putAll(m);
	}

	public void clear() {
		currentMap = new HashMap<K, V>();
		if (innerChainedMap != null) {
			innerChainedMap.clear();
		}
	}

	public Set<K> keySet() {
		Set<K> topKeySet = currentMap.keySet();
		if (innerChainedMap != null) {
			Set<K> resultSet = new HashSet<K>(topKeySet);
			resultSet.addAll(innerChainedMap.keySet());
			return resultSet;
		} else {
			return topKeySet;
		}
	}

	public Collection<V> values() {
		Collection<V> values = new HashSet<V>();
		Set<java.util.Map.Entry<K, V>> entrySet = entrySet();
		for (java.util.Map.Entry<K, V> anEntry : entrySet) {
			values.add(anEntry.getValue());
		}
		return values;
	}

	public Set<java.util.Map.Entry<K, V>> entrySet() {
		Set<java.util.Map.Entry<K, V>> entrySet = currentMap.entrySet();
		Set<java.util.Map.Entry<K, V>> resultingEntrySet = new HashSet<Map.Entry<K,V>>();
		resultingEntrySet.addAll(entrySet);
		if (innerChainedMap != null) {
			Set<java.util.Map.Entry<K, V>> innerMapEntrySet = 
					innerChainedMap.entrySet();
			for (java.util.Map.Entry<K, V> anEntry : innerMapEntrySet) {
				if (!currentMap.containsKey(anEntry.getKey())) {
					resultingEntrySet.add(anEntry);
				}
			}
		}
		return resultingEntrySet;
	}
	
	/**
	 * Gets the set of keys that map to a certain value
	 * in this chained map.
	 * 
	 * @param value the value whose mapping keys are being looked for.
	 * 
	 * @return A set containing those keys that map to the given value.
	 * 
	 */
	private Set<K> whichKeysMapToAValue(Object value) {
		Set<K> resultingKeySet = new HashSet<K>();
		if (innerChainedMap == null) {
			for (K aKey : currentMap.keySet()) {
				if (currentMap.get(aKey).equals(value)) {
					resultingKeySet.add(aKey);
				}
			}
		} else {
			Set<K> topMappingKeys = new ChainedMap<K,V>(currentMap).whichKeysMapToAValue(value);
			Set<K> innerMappingKeys = innerChainedMap.whichKeysMapToAValue(value);
			// top map mapping keys are part of the key set solution
			resultingKeySet.addAll(topMappingKeys);
			// inner mapping keys are part of the solution
			// unless they are "buried" by a different value mapped to the same key
			for (K aKey : innerMappingKeys) {
				if (currentMap.containsKey(aKey)) {
					if (currentMap.get(aKey).equals(value)) {
						resultingKeySet.add(aKey);
					}
				} else {
					resultingKeySet.add(aKey);
				}
			}
		}
		return resultingKeySet;
	}

}
