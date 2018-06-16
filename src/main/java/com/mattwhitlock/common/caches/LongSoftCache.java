/*
 * Created on Jun 14, 2018
 */
package com.mattwhitlock.common.caches;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * An {@link AbstractLongReferenceCache} whose values are held by instances of {@link SoftReference}.
 * 
 * @author Matt Whitlock
 */
public class LongSoftCache<V> extends AbstractLongReferenceCache<V, LongSoftCache.Entry<V>> {

	public static class Entry<V> extends SoftReference<V> implements AbstractLongCache.Entry<V> {

		protected final long key;

		protected Entry(long key, V value, ReferenceQueue<? super V> queue) {
			super(value, queue);
			this.key = key;
		}

		@Override
		public long getLongKey() {
			return key;
		}

		@Override
		public V getValue() {
			return get();
		}

		@Override
		public void removedFrom(AbstractCache<Long, V, ?> cache) {
			clear();
		}

		@Override
		public boolean equals(Object obj) {
			return AbstractLongCache.Entry.equals(this, obj);
		}

		@Override
		public int hashCode() {
			return AbstractLongCache.Entry.hashCode(this);
		}

		@Override
		public String toString() {
			return AbstractLongCache.Entry.toString(this);
		}

	}

	public LongSoftCache() {
		super(new HashMap<>());
	}

	@Override
	protected Entry<V> newEntry(long key, V value) {
		return new Entry<>(key, value, queue);
	}

}
