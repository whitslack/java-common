/*
 * Created on Jun 14, 2018
 */
package com.mattwhitlock.common.caches;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.NavigableMap;

/**
 * An {@link AbstractNavigableLongCache} whose values are held by instances of a subclass of {@link Reference}. This
 * allows the values to be spontaneously erased by the garbage collector.
 * 
 * @author Matt Whitlock
 */
public abstract class AbstractNavigableLongReferenceCache<V, E extends Reference<V> & AbstractLongCache.Entry<V>> extends AbstractNavigableLongCache<V, E> {

	protected final ReferenceQueue<V> queue = new ReferenceQueue<>();

	protected AbstractNavigableLongReferenceCache(NavigableMap<E, E> map) {
		super(map);
	}

	@Override
	public V get(long key) {
		processQueue();
		return super.get(key);
	}

	protected void processQueue() {
		for (Reference<?> ref; (ref = queue.poll()) != null;) {
			E removed = map.remove(ref);
			if (removed != null) {
				removed(removed);
			}
		}
	}

}
