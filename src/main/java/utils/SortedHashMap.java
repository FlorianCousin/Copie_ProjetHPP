package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * This is a sorted HashMap. Of course, the class V must be comparable. This
 * class has been made to match to the post context needs so maybe it lacks
 * other things when used in other contexts.
 * 
 * @author Florian
 * 
 * @param <K>
 * 
 * @param <V>
 * 
 */
public class SortedHashMap<K, V extends Comparable<Object>> extends HashMap<K, V> {

	private static final long serialVersionUID = 1618744532872204965L;

	// Keys of the sorted values in decreasing order
	ArrayList<K> sortedKeys;

	/**
	 * The constructor without parameters
	 */
	public SortedHashMap() {
		super();
		this.sortedKeys = new ArrayList<K>();
	}

	/**
	 * A constructor
	 * 
	 * @param initialCapacity
	 */
	public SortedHashMap(int initialCapacity) {
		super(initialCapacity);
		this.sortedKeys = new ArrayList<K>();
	}

	/**
	 * A copy constructor
	 * 
	 * @param sortedHashMap
	 *            the object to copy
	 */
	public SortedHashMap(SortedHashMap<K, V> sortedHashMap) {
		super(sortedHashMap);
		this.sortedKeys = new ArrayList<K>(sortedHashMap.sortedKeys.size());
		sortedHashMap.sortedKeys.stream().forEach(k -> sortedKeys.add(k));
	}

	/**
	 * Removes all of the mappings from this map. The map will be empty after this
	 * call returns.
	 */
	@Override
	public void clear() {
		sortedKeys.clear();
		super.clear();
	}

	/**
	 * This function returns the ith value of the HashMap
	 * 
	 * @param i
	 *            place of the value to get (careful, the first value is the 0th
	 *            value)
	 * @return the ith value
	 */
	public V getThValue(int i) {
		return super.get(sortedKeys.get(i));
	}

	/**
	 * Associates the specified value with the specified key in this map. If the map
	 * previously contained a mapping for the key, the old value is replaced.
	 * 
	 * @param key
	 *            key with which the specified value is to be associated
	 * @param value
	 *            value to be associated with the specified key
	 * @return the previous value associated with key, or null if there was no
	 *         mapping for key. (A null return can also indicate that the map
	 *         previously associated null with key.)
	 */
	@Override
	public V put(K key, V value) {
		// The couple is put in this HashMap and the returned value is stocked
		V previousKey = super.put(key, value);

		// If the returned value is not null, then this HashMap had the supplied key
		// before, so the supplied key is removed from sortedKeys
		if (previousKey != null) {
			// Using the remove function may not be the most effective way but in the post
			// context, it is not supposed to get in this if statement
			sortedKeys.remove(key);
		}

		// The supplied key is added to sortedKeys so as the list to be sorted depending
		// on the values in descending order
		int index = getPosition(key);
		sortedKeys.add(index, key);

		return previousKey;
	}

	/**
	 * Removes the mapping for the specified key from this map if present.
	 * 
	 * @param key
	 *            key whose mapping is to be removed from the map
	 * @return the previous value associated with key, or null if there was no
	 *         mapping for key. (A null return can also indicate that the map
	 *         previously associated null with key.)
	 */
	@Override
	public V remove(Object key) {
		// Well I suppose it is not the most effective way to do but I do not see an
		// easy other way
		sortedKeys.remove(key);

		return super.remove(key);
	}

	/**
	 * Removes the entry for the specified key only if it is currently mapped to the
	 * specified value.
	 * 
	 * @param key
	 *            key with which the specified value is associated
	 * @param value
	 *            value expected to be associated with the specified key
	 * @return true if the value was removed
	 */
	@Override
	public boolean remove(Object key, Object value) {
		boolean removed = super.remove(key, value);
		if (removed)
			sortedKeys.remove(key);
		return removed;
	}

	/**
	 * This function sort sortedKeys for all the keys contained in the suplied set.
	 * 
	 * @param ck
	 *            Set of keys to be sorted
	 */
	public void sort(Set<K> ck) {
		// The previous keys are removed
		sortedKeys.removeAll(ck);

		// The keys are added at the right position
		sortedKeys.ensureCapacity(sortedKeys.size() + ck.size());
		K keyTmp;
		int indexTmp;
		Iterator<K> it = ck.iterator();
		for (; it.hasNext();) {
			keyTmp = it.next();
			indexTmp = getPosition(keyTmp);
			sortedKeys.add(indexTmp, keyTmp);
		}
	}

	/**
	 * This function sort sortedKeys for the supplied key
	 * 
	 * @param key
	 *            key to be sorted
	 */
	public void sort(K key) {
		// The previous key is removed
		sortedKeys.remove(key);

		// The key is added at the right position
		int index = getPosition(key);
		sortedKeys.add(index, key);
	}

	/**
	 * This function returns the index to insert the supplied key in sortedKeys to
	 * let it sorted. This function uses a binary search.
	 * 
	 * @param key
	 *            the key to insert
	 * @return the index in sortedKeys
	 */
	public int getPosition(K key) {
		// The number of keys in this map
		int s = sortedKeys.size();
		// The value associated with the supplied key
		V value = get(key);
		// The inclusive lower and upper limits to the searched index
		int lower = 0;
		int upper = s;

		// Some useful temporary variables
		int middle;
		V middleValue;
		int comparison;

		while (lower != upper) {
			middle = (upper + lower) / 2;
			middleValue = getThValue(middle);
			comparison = middleValue.compareTo(value);
			// If middleValue is null here, maybe there is a key in the parameter of sort()
			// which is not a key of this

			// If you don't understand why we set upper to middle and lower to middle + 1,
			// think about the case when upper = lower + 1. In taht case, middle is equal to
			// lower so we cannot set upper to middle - 1 otherwise upper could be lower - 1
			if (comparison < 0) {
				upper = middle;
			} else if (comparison > 0) {
				lower = middle + 1;
			} else {
				return middle;
			}
			// The case comparison == 0 is not really supposed to happen
		}

		return lower;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((sortedKeys == null) ? 0 : sortedKeys.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof SortedHashMap)) {
			return false;
		}
		SortedHashMap<K, V> other = (SortedHashMap<K, V>) obj;
		if (sortedKeys == null) {
			if (other.sortedKeys != null) {
				return false;
			}
		} else if (!sortedKeys.equals(other.sortedKeys)) {
			return false;
		}
		return true;
	}

}
