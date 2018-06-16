/*
 * Created on Jun 14, 2018
 */
package com.mattwhitlock.common.caches;

import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.SortedMap;

/**
 * A {@code long}-keyed {@link AbstractNavigableCache}.
 * 
 * @author Matt Whitlock
 */
public abstract class AbstractNavigableLongCache<V, E extends AbstractLongCache.Entry<V>> extends AbstractNavigableCache<Long, V, E> {

	private static class NaturalEntryComparator implements Comparator<AbstractLongCache.Entry<?>>, EntryComparator<Long> {

		static final NaturalEntryComparator instance = new NaturalEntryComparator();

		/**
		 * Not instantiable.
		 */
		private NaturalEntryComparator() {
		}

		@Override
		public Comparator<? super Long> keyComparator() {
			return null;
		}

		@Override
		public int compare(AbstractLongCache.Entry<?> o1, AbstractLongCache.Entry<?> o2) {
			return Long.compare(o1.getLongKey(), o2.getLongKey());
		}

	}

	protected static final Comparator<AbstractLongCache.Entry<?>> entryComparator = NaturalEntryComparator.instance;

	protected AbstractNavigableLongCache(NavigableMap<E, E> map) {
		super(map);
		assert map.comparator() == entryComparator;
	}

	@Override
	protected final E newEntry(Long key, V value) {
		return newEntry(key.longValue(), value);
	}

	protected abstract E newEntry(long key, V value);

	@Override
	public final boolean containsKey(Object key) {
		return key instanceof Long && containsKey(((Long) key).longValue());
	}

	public boolean containsKey(long key) {
		return map.containsKey(new AbstractLongCache.Entry.Probe<>(key));
	}

	@Override
	public final V get(Object key) {
		return key instanceof Long ? get(((Long) key).longValue()) : null;
	}

	/**
	 * @see #get(Object)
	 */
	public V get(long key) {
		E entry = map.get(new AbstractLongCache.Entry.Probe<>(key));
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
		E removed = map.remove(new AbstractLongCache.Entry.Probe<>(key));
		if (removed == null) {
			return null;
		}
		removed(removed);
		return removed.getValue();
	}

	/**
	 * @see #firstKey()
	 */
	public long firstLongKey() {
		return ((NavigableMap<E, E>) map).firstKey().getLongKey();
	}

	/**
	 * @see #lastKey()
	 */
	public long lastLongKey() {
		return ((NavigableMap<E, E>) map).lastKey().getLongKey();
	}

	@Override
	public final Map.Entry<Long, V> lowerEntry(Long key) {
		return lowerEntry(key.longValue());
	}

	/**
	 * @see #lowerEntry(Long)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map.Entry<Long, V> lowerEntry(long key) {
		return nextValidEntry(((NavigableMap) map).headMap(new AbstractLongCache.Entry.Probe(key), false).navigableKeySet().descendingIterator());
	}

	@Override
	public final Long lowerKey(Long key) {
		return lowerKey(key.longValue());
	}

	/**
	 * @see #lowerKey(Long)
	 */
	public Long lowerKey(long key) {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		E entry = (E) ((NavigableMap) map).lowerKey(new AbstractLongCache.Entry.Probe(key));
		return entry == null ? null : entry.getKey();
	}

	@Override
	public final Map.Entry<Long, V> floorEntry(Long key) {
		return floorEntry(key.longValue());
	}

	/**
	 * @see #floorEntry(Long)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map.Entry<Long, V> floorEntry(long key) {
		return nextValidEntry(((NavigableMap) map).headMap(new AbstractLongCache.Entry.Probe(key), true).navigableKeySet().descendingIterator());
	}

	@Override
	public final Long floorKey(Long key) {
		return floorKey(key.longValue());
	}

	/**
	 * @see #floorKey(Long)
	 */
	public Long floorKey(long key) {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		E entry = (E) ((NavigableMap) map).floorKey(new AbstractLongCache.Entry.Probe(key));
		return entry == null ? null : entry.getKey();
	}

	@Override
	public final Map.Entry<Long, V> ceilingEntry(Long key) {
		return ceilingEntry(key.longValue());
	}

	/**
	 * @see #ceilingEntry(Long)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map.Entry<Long, V> ceilingEntry(long key) {
		return nextValidEntry(((NavigableMap) map).tailMap(new AbstractLongCache.Entry.Probe(key), true).navigableKeySet().iterator());
	}

	@Override
	public final Long ceilingKey(Long key) {
		return ceilingKey(key.longValue());
	}

	/**
	 * @see #ceilingKey(Long)
	 */
	public Long ceilingKey(long key) {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		E entry = (E) ((NavigableMap) map).ceilingKey(new AbstractLongCache.Entry.Probe(key));
		return entry == null ? null : entry.getKey();
	}

	@Override
	public final Map.Entry<Long, V> higherEntry(Long key) {
		return higherEntry(key.longValue());
	}

	/**
	 * @see #higherEntry(Long)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map.Entry<Long, V> higherEntry(long key) {
		return nextValidEntry(((NavigableMap) map).tailMap(new AbstractLongCache.Entry.Probe(key), false).navigableKeySet().iterator());
	}

	@Override
	public final Long higherKey(Long key) {
		return higherKey(key.longValue());
	}

	/**
	 * @see #higherKey(Long)
	 */
	public Long higherKey(long key) {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		E entry = (E) ((NavigableMap) map).higherKey(new AbstractLongCache.Entry.Probe(key));
		return entry == null ? null : entry.getKey();
	}

	/**
	 * @see #subMap(Object, Object)
	 */
	public final SortedMap<Long, V> subMap(long fromKey, long toKey) {
		return subMap(fromKey, true, toKey, false);
	}

	@Override
	public final NavigableMap<Long, V> subMap(Long fromKey, boolean fromInclusive, Long toKey, boolean toInclusive) {
		return subMap(fromKey.longValue(), fromInclusive, toKey.longValue(), toInclusive);
	}

	/**
	 * @see #subMap(Long, boolean, Long, boolean)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public NavigableMap<Long, V> subMap(long fromKey, boolean fromInclusive, long toKey, boolean toInclusive) {
		return new NavigableCacheView(((NavigableMap) map).subMap(new AbstractLongCache.Entry.Probe(fromKey), fromInclusive, new AbstractLongCache.Entry.Probe(toKey), toInclusive), comparator());
	}

	/**
	 * @see #headMap(Object)
	 */
	public final SortedMap<Long, V> headMap(long toKey) {
		return headMap(toKey, false);
	}

	@Override
	public final NavigableMap<Long, V> headMap(Long toKey, boolean inclusive) {
		return headMap(toKey.longValue(), inclusive);
	}

	/**
	 * @see #headMap(Long, boolean)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public NavigableMap<Long, V> headMap(long toKey, boolean inclusive) {
		return new NavigableCacheView(((NavigableMap) map).headMap(new AbstractLongCache.Entry.Probe(toKey), inclusive), comparator());
	}

	/**
	 * @see #tailMap(Object)
	 */
	public final SortedMap<Long, V> tailMap(long fromKey) {
		return tailMap(fromKey, true);
	}

	@Override
	public final NavigableMap<Long, V> tailMap(Long fromKey, boolean inclusive) {
		return super.tailMap(fromKey.longValue(), inclusive);
	}

	/**
	 * @see #tailMap(Long, boolean)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public NavigableMap<Long, V> tailMap(long fromKey, boolean inclusive) {
		return new NavigableCacheView(((NavigableMap) map).tailMap(new AbstractLongCache.Entry.Probe(fromKey), inclusive), comparator());
	}

}
