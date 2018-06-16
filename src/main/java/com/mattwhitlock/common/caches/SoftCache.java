/*
 * Created on Jun 14, 2018
 */
package com.mattwhitlock.common.caches;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * An {@link AbstractReferenceCache} whose values are held by instances of {@link SoftReference}.
 * 
 * @author Matt Whitlock
 */
public class SoftCache<K, V> extends AbstractReferenceCache<K, V, SoftCache.Entry<K, V>> {

	public static class Entry<K, V> extends SoftReference<V> implements AbstractCache.Entry<K, V> {

		protected final K key;

		protected Entry(K key, V value, ReferenceQueue<? super V> queue) {
			super(value, queue);
			this.key = key;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return get();
		}

		@Override
		public void removedFrom(AbstractCache<K, V, ?> cache) {
			clear();
		}

		@Override
		public boolean equals(Object obj) {
			return AbstractCache.Entry.equals(this, obj);
		}

		@Override
		public int hashCode() {
			return AbstractCache.Entry.hashCode(this);
		}

		@Override
		public String toString() {
			return AbstractCache.Entry.toString(this);
		}

	}

	public SoftCache() {
		super(new HashMap<>());
	}

	@Override
	protected Entry<K, V> newEntry(K key, V value) {
		return new Entry<>(key, value, queue);
	}

}
