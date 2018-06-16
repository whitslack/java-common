/*
 * Created on Jun 14, 2018
 */
package com.mattwhitlock.common.caches;

import java.lang.ref.SoftReference;
import java.util.Comparator;
import java.util.TreeMap;

/**
 * An {@link AbstractNavigableReferenceCache} whose values are held by instances of {@link SoftReference}.
 * 
 * @author Matt Whitlock
 */
public class NavigableSoftCache<K, V> extends AbstractNavigableReferenceCache<K, V, SoftCache.Entry<K, V>> {

	public NavigableSoftCache() {
		this(null);
	}

	public NavigableSoftCache(Comparator<? super K> comparator) {
		super(new TreeMap<>(entryComparator(comparator)));
	}

	@Override
	protected SoftCache.Entry<K, V> newEntry(K key, V value) {
		return new SoftCache.Entry<>(key, value, queue);
	}

}
