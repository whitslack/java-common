/*
 * Created on Jun 13, 2018
 */
package com.mattwhitlock.common.caches;

import java.util.Map;
import java.util.Objects;

/**
 * A {@code long}-keyed {@link AbstractCache}.
 * 
 * @author Matt Whitlock
 */
public abstract class AbstractLongCache<V, E extends AbstractLongCache.Entry<V>> extends AbstractCache<Long, V, E> {

	/**
	 * A {@code long}-keyed {@link AbstractCache.Entry}.
	 */
	protected interface Entry<V> extends AbstractCache.Entry<Long, V> {

		static class Probe<V> implements Entry<V> {

			final long key;

			Probe(long key) {
				this.key = key;
			}

			@Override
			public long getLongKey() {
				return key;
			}

			@Override
			public V getValue() {
				return null;
			}

			@Override
			public void removedFrom(AbstractCache<Long, V, ?> cache) {
				throw new InternalError();
			}

			@Override
			public boolean equals(Object obj) {
				return Entry.equals(this, obj);
			}

			@Override
			public int hashCode() {
				return Entry.hashCode(this);
			}

			@Override
			public String toString() {
				return Long.toString(key);
			}

		}

		@Override
		default Long getKey() {
			return getLongKey();
		}

		/**
		 * Returns the key with which this entry was created.
		 */
		long getLongKey();

		/**
		 * Convenience method for concrete implementations. This method would be {@code default} except that methods of
		 * {@link Object} may not be overridden by interface default methods.
		 */
		static boolean equals(Entry<?> e1, Object o2) {
			return e1 == o2 || o2 instanceof Entry<?> && e1.getLongKey() == ((Entry<?>) o2).getLongKey();
		}

		/**
		 * Convenience method for concrete implementations. This method would be {@code default} except that methods of
		 * {@link Object} may not be overridden by interface default methods.
		 */
		static int hashCode(Entry<?> e) {
			return Long.hashCode(e.getLongKey());
		}

		/**
		 * Convenience method for concrete implementations. This method would be {@code default} except that methods of
		 * {@link Object} may not be overridden by interface default methods.
		 */
		static String toString(Entry<?> e) {
			return Long.toString(e.getLongKey()) + '=' + Objects.toString(e.getValue());
		}

	}

	protected AbstractLongCache(Map<E, E> map) {
		super(map);
	}

	@Override
	protected final E newEntry(Long key, V value) {
		return newEntry(key.longValue(), value);
	}

	/**
	 * Constructs and returns a new {@link Entry} of the appropriate subclass for this cache.
	 */
	protected abstract E newEntry(long key, V value);

	@Override
	public final boolean containsKey(Object key) {
		return key instanceof Long && containsKey(((Long) key).longValue());
	}

	/**
	 * @see #containsKey(Object)
	 */
	public boolean containsKey(long key) {
		return map.containsKey(new Entry.Probe<>(key));
	}

	@Override
	public final V get(Object key) {
		return key instanceof Long ? get(((Long) key).longValue()) : null;
	}

	/**
	 * @see #get(Object)
	 */
	public V get(long key) {
		E entry = map.get(new Entry.Probe<>(key));
		return entry == null ? null : entry.getValue();
	}

	@Override
	public final V put(Long key, V value) {
		return put(key.longValue(), value);
	}

	/**
	 * @see #put(Long, Object)
	 */
	public V put(long key, V value) {
		Objects.requireNonNull(value);
		E entry = newEntry(key, value), replaced = map.put(entry, entry);
		if (replaced == null) {
			return null;
		}
		removed(replaced);
		return replaced.getValue();
	}

	@Override
	public final V remove(Object key) {
		return key instanceof Long ? remove(((Long) key).longValue()) : null;
	}

	/**
	 * @see #remove(Object)
	 */
	public V remove(long key) {
		E removed = map.remove(new Entry.Probe<>(key));
		if (removed == null) {
			return null;
		}
		removed(removed);
		return removed.getValue();
	}

}
