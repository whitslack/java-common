/*
 * Created on Jun 14, 2018
 */
package com.mattwhitlock.common.caches;

import java.lang.ref.SoftReference;
import java.util.TreeMap;

/**
 * An {@link AbstractNavigableLongReferenceCache} whose values are held by instances of {@link SoftReference}.
 * 
 * @author Matt Whitlock
 */
public class NavigableLongSoftCache<V> extends AbstractNavigableLongReferenceCache<V, LongSoftCache.Entry<V>> {

	public NavigableLongSoftCache() {
		super(new TreeMap<>(entryComparator));
	}

	@Override
	protected LongSoftCache.Entry<V> newEntry(long key, V value) {
		return new LongSoftCache.Entry<>(key, value, queue);
	}

}
