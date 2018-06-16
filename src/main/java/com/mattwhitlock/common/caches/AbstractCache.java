/*
 * Created on Jun 13, 2018
 */
package com.mattwhitlock.common.caches;

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

/**
 * A key-value cache that supports mappings whose values may be spontaneously erased. Such erased mappings are
 * automatically culled from the cache during iterator traversal. This class is not inherently thread-safe.
 * 
 * @author Matt Whitlock
 */
public abstract class AbstractCache<K, V, E extends AbstractCache.Entry<K, V>> implements Map<K, V> {

	/**
	 * A key-value mapping whose value may be spontaneously erased.
	 */
	protected interface Entry<K, V> {

		static class Probe<K, V> implements Entry<K, V> {

			final K key;

			Probe(K key) {
				this.key = key;
			}

			@Override
			public K getKey() {
				return key;
			}

			@Override
			public V getValue() {
				return null;
			}

			@Override
			public void removedFrom(AbstractCache<K, V, ?> cache) {
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
				return Objects.toString(key);
			}

		}

		/**
		 * Returns the key with which this entry was created.
		 */
		K getKey();

		/**
		 * Returns the value with which this entry was created, or {@code null} if the value has been spontaneously
		 * erased.
		 */
		V getValue();

		/**
		 * Notifies this entry that it has been removed from the cache that had held it. This method may erase the
		 * entry's value or perform any other relevant bookkeeping, but it must not structurally modify the cache.
		 */
		void removedFrom(AbstractCache<K, V, ?> cache);

		/**
		 * Convenience method for concrete implementations. This method would be {@code default} except that methods of
		 * {@link Object} may not be overridden by interface default methods.
		 */
		static boolean equals(Entry<?, ?> e1, Object o2) {
			return e1 == o2 || o2 instanceof Entry<?, ?> && Objects.equals(e1.getKey(), ((Entry<?, ?>) o2).getKey());
		}

		/**
		 * Convenience method for concrete implementations. This method would be {@code default} except that methods of
		 * {@link Object} may not be overridden by interface default methods.
		 */
		static int hashCode(Entry<?, ?> e) {
			return Objects.hashCode(e.getKey());
		}

		/**
		 * Convenience method for concrete implementations. This method would be {@code default} except that methods of
		 * {@link Object} may not be overridden by interface default methods.
		 */
		static String toString(Entry<?, ?> e) {
			return Objects.toString(e.getKey()) + '=' + Objects.toString(e.getValue());
		}

	}

	protected class KeySetView extends AbstractSet<K> {

		KeySetView() {
		}

		@Override
		public int size() {
			return AbstractCache.this.size();
		}

		@Override
		public boolean isEmpty() {
			return AbstractCache.this.isEmpty();
		}

		@Override
		public boolean contains(Object o) {
			return AbstractCache.this.containsKey(o);
		}

		@Override
		public Iterator<K> iterator() {
			return new KeyIterator(map.keySet().iterator());
		}

	}

	protected class ValuesCollectionView extends AbstractCollection<V> {

		ValuesCollectionView() {
		}

		@Override
		public int size() {
			return AbstractCache.this.size();
		}

		@Override
		public boolean isEmpty() {
			return AbstractCache.this.isEmpty();
		}

		@Override
		public Iterator<V> iterator() {
			return new ValueIterator(map.keySet().iterator());
		}

	}

	protected class EntrySetView extends AbstractSet<Map.Entry<K, V>> {

		EntrySetView() {
		}

		@Override
		public int size() {
			return AbstractCache.this.size();
		}

		@Override
		public boolean isEmpty() {
			return AbstractCache.this.isEmpty();
		}

		@Override
		public boolean contains(Object o) {
			if (!(o instanceof Map.Entry<?, ?>)) {
				return false;
			}
			Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
			E entry = map.get(new Entry.Probe<>(e.getKey()));
			return entry != null && Objects.equals(e.getValue(), entry.getValue());
		}

		@Override
		public Iterator<Map.Entry<K, V>> iterator() {
			return new EntryIterator(map.keySet().iterator());
		}

		@Override
		public boolean remove(Object o) {
			if (!(o instanceof Map.Entry<?, ?>)) {
				return false;
			}
			Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
			return AbstractCache.this.remove(e.getKey(), e.getValue());
		}

	}

	private class KeyIterator implements Iterator<K> {

		final Iterator<E> delegate;

		E lastEntry;

		KeyIterator(Iterator<E> delegate) {
			this.delegate = delegate;
		}

		@Override
		public boolean hasNext() {
			return delegate.hasNext();
		}

		@Override
		public K next() {
			return (lastEntry = delegate.next()).getKey();
		}

		@Override
		public void remove() {
			delegate.remove();
			removed(lastEntry);
			lastEntry = null;
		}

	}

	private abstract class AbstractEntryIterator {

		final Iterator<E> delegate;

		E lastEntry, nextEntry;
		V nextValue;

		AbstractEntryIterator(Iterator<E> delegate) {
			this.delegate = delegate;
		}

		public boolean hasNext() {
			if (nextValue == null) {
				lastEntry = null;
				for (;;) {
					if (!delegate.hasNext()) {
						nextEntry = null;
						return false;
					}
					if ((nextValue = (nextEntry = delegate.next()).getValue()) != null) {
						break;
					}
					delegate.remove();
					removed(nextEntry);
				}
			}
			return true;
		}

		public void remove() {
			if (lastEntry == null) {
				throw new IllegalStateException();
			}
			delegate.remove();
			removed(lastEntry);
			lastEntry = null;
		}

	}

	private class ValueIterator extends AbstractEntryIterator implements Iterator<V> {

		ValueIterator(Iterator<E> delegate) {
			super(delegate);
		}

		@Override
		public V next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			V ret = nextValue;
			lastEntry = nextEntry;
			nextEntry = null;
			nextValue = null;
			return ret;
		}

	}

	private class EntryIterator extends AbstractEntryIterator implements Iterator<Map.Entry<K, V>> {

		EntryIterator(Iterator<E> delegate) {
			super(delegate);
		}

		@Override
		public Map.Entry<K, V> next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			Map.Entry<K, V> ret = new AbstractMap.SimpleImmutableEntry<>(nextEntry.getKey(), nextValue);
			lastEntry = nextEntry;
			nextEntry = null;
			nextValue = null;
			return ret;
		}

	}

	protected final Map<E, E> map;

	/**
	 * Constructs an {@link AbstractCache} using the given backing {@link Map}.
	 */
	protected AbstractCache(Map<E, E> map) {
		this.map = map;
	}

	/**
	 * Constructs and returns a new {@link Entry} of the appropriate subclass for this cache.
	 */
	protected abstract E newEntry(K key, V value);

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(new Entry.Probe<>(key));
	}

	@Override
	public boolean containsValue(Object value) {
		return values().contains(value);
	}

	@Override
	public V get(Object key) {
		E entry = map.get(new Entry.Probe<>(key));
		return entry == null ? null : entry.getValue();
	}

	@Override
	public V put(K key, V value) {
		Objects.requireNonNull(value);
		E entry = newEntry(key, value), replaced = map.put(entry, entry);
		if (replaced == null) {
			return null;
		}
		removed(replaced);
		return replaced.getValue();
	}

	@Override
	public V remove(Object key) {
		E removed = map.remove(new Entry.Probe<>(key));
		if (removed == null) {
			return null;
		}
		removed(removed);
		return removed.getValue();
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		m.forEach(this::put);
	}

	@Override
	public void clear() {
		@SuppressWarnings("unchecked")
		E[] cleared = (E[]) map.values().toArray(new Entry<?, ?>[map.size()]);
		map.clear();
		for (E removed : cleared) {
			removed(removed);
		}
	}

	@Override
	public Set<K> keySet() {
		return new KeySetView();
	}

	@Override
	public Collection<V> values() {
		return new ValuesCollectionView();
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		return new EntrySetView();
	}

	/**
	 * Notifies the given {@link Entry} that it has been removed from this cache.
	 */
	protected void removed(AbstractCache.Entry<K, V> entry) {
		entry.removedFrom(this);
	}

}
