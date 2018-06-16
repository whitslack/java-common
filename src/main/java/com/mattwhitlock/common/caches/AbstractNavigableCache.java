/*
 * Created on Jun 14, 2018
 */
package com.mattwhitlock.common.caches;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.SortedMap;
import java.util.SortedSet;

/**
 * An {@link AbstractCache} whose mappings are {@linkplain NavigableMap navigable}.
 * 
 * @author Matt Whitlock
 */
public abstract class AbstractNavigableCache<K, V, E extends AbstractCache.Entry<K, V>> extends AbstractCache<K, V, E> implements NavigableMap<K, V> {

	protected interface EntryComparator<K> {

		Comparator<? super K> keyComparator();

	}

	private static class NaturalEntryComparator<K extends Comparable<? super K>> implements Comparator<AbstractCache.Entry<K, ?>>, EntryComparator<K> {

		@SuppressWarnings("rawtypes")
		static final NaturalEntryComparator instance = new NaturalEntryComparator<>();

		/**
		 * Not instantiable.
		 */
		private NaturalEntryComparator() {
		}

		@Override
		public Comparator<? super K> keyComparator() {
			return null;
		}

		@Override
		public int compare(AbstractCache.Entry<K, ?> o1, AbstractCache.Entry<K, ?> o2) {
			return o1.getKey().compareTo(o2.getKey());
		}

	}

	private static class DelegatingEntryComparator<K> implements Comparator<AbstractCache.Entry<K, ?>>, EntryComparator<K> {

		final Comparator<? super K> keyComparator;

		DelegatingEntryComparator(Comparator<? super K> keyComparator) {
			Objects.requireNonNull(keyComparator);
			this.keyComparator = keyComparator;
		}

		@Override
		public Comparator<? super K> keyComparator() {
			return keyComparator;
		}

		@Override
		public int compare(AbstractCache.Entry<K, ?> o1, AbstractCache.Entry<K, ?> o2) {
			return keyComparator.compare(o1.getKey(), o2.getKey());
		}

	}

	protected class NavigableCacheView extends AbstractNavigableCache<K, V, E> {

		final Comparator<? super K> keyComparator;

		NavigableCacheView(NavigableMap<E, E> map, Comparator<? super K> keyComparator) {
			super(map);
			this.keyComparator = keyComparator;
		}

		@Override
		public Comparator<? super K> comparator() {
			return keyComparator;
		}

		@Override
		protected E newEntry(K key, V value) {
			return AbstractNavigableCache.this.newEntry(key, value);
		}

		@Override
		protected void removed(AbstractCache.Entry<K, V> entry) {
			AbstractNavigableCache.this.removed(entry);
		}

	}

	protected class KeySetView extends AbstractCache<K, V, E>.KeySetView implements NavigableSet<K> {

		KeySetView() {
		}

		@Override
		public Comparator<? super K> comparator() {
			return AbstractNavigableCache.this.comparator();
		}

		@Override
		public K first() {
			return firstKey();
		}

		@Override
		public K last() {
			return lastKey();
		}

		@Override
		public K lower(K e) {
			return lowerKey(e);
		}

		@Override
		public K floor(K e) {
			return floorKey(e);
		}

		@Override
		public K ceiling(K e) {
			return ceilingKey(e);
		}

		@Override
		public K higher(K e) {
			return higherKey(e);
		}

		@Override
		public K pollFirst() {
			Map.Entry<E, E> entry = ((NavigableMap<E, E>) map).pollFirstEntry();
			return entry == null ? null : entry.getKey().getKey();
		}

		@Override
		public K pollLast() {
			Map.Entry<E, E> entry = ((NavigableMap<E, E>) map).pollLastEntry();
			return entry == null ? null : entry.getKey().getKey();
		}

		@Override
		public NavigableSet<K> descendingSet() {
			return descendingKeySet();
		}

		@Override
		public Iterator<K> descendingIterator() {
			return descendingSet().iterator();
		}

		@Override
		public final SortedSet<K> subSet(K fromElement, K toElement) {
			return subSet(fromElement, true, toElement, false);
		}

		@Override
		public NavigableSet<K> subSet(K fromElement, boolean fromInclusive, K toElement, boolean toInclusive) {
			return subMap(fromElement, fromInclusive, toElement, toInclusive).navigableKeySet();
		}

		@Override
		public final SortedSet<K> headSet(K toElement) {
			return headSet(toElement, false);
		}

		@Override
		public NavigableSet<K> headSet(K toElement, boolean inclusive) {
			return headMap(toElement, inclusive).navigableKeySet();
		}

		@Override
		public final SortedSet<K> tailSet(K fromElement) {
			return tailSet(fromElement, true);
		}

		@Override
		public NavigableSet<K> tailSet(K fromElement, boolean inclusive) {
			return tailMap(fromElement, inclusive).navigableKeySet();
		}

	}

	protected static final Comparator<AbstractCache.Entry<Comparable<?>, ?>> entryComparator = NaturalEntryComparator.instance;

	protected static <K> Comparator<AbstractCache.Entry<K, ?>> entryComparator(Comparator<? super K> delegate) {
		return delegate == null ? NaturalEntryComparator.instance : new DelegatingEntryComparator<>(delegate);
	}

	/**
	 * Constructs an {@link AbstractCache} using the given backing {@link NavigableMap}. The backing map's
	 * {@linkplain SortedMap#comparator() comparator} must implement {@link EntryComparator}.
	 */
	protected AbstractNavigableCache(NavigableMap<E, E> map) {
		super(map);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Comparator<? super K> comparator() {
		return ((EntryComparator<? super K>) ((NavigableMap<E, E>) map).comparator()).keyComparator();
	}

	@Override
	public K firstKey() {
		return ((NavigableMap<E, E>) map).firstKey().getKey();
	}

	@Override
	public K lastKey() {
		return ((NavigableMap<E, E>) map).lastKey().getKey();
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map.Entry<K, V> lowerEntry(K key) {
		return nextValidEntry(((NavigableMap) map).headMap(new AbstractCache.Entry.Probe(key), false).navigableKeySet().descendingIterator());
	}

	@Override
	public K lowerKey(K key) {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		E entry = (E) ((NavigableMap) map).lowerKey(new AbstractCache.Entry.Probe(key));
		return entry == null ? null : entry.getKey();
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map.Entry<K, V> floorEntry(K key) {
		return nextValidEntry(((NavigableMap) map).headMap(new AbstractCache.Entry.Probe(key), true).navigableKeySet().descendingIterator());
	}

	@Override
	public K floorKey(K key) {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		E entry = (E) ((NavigableMap) map).floorKey(new AbstractCache.Entry.Probe(key));
		return entry == null ? null : entry.getKey();
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map.Entry<K, V> ceilingEntry(K key) {
		return nextValidEntry(((NavigableMap) map).tailMap(new AbstractCache.Entry.Probe(key), true).navigableKeySet().iterator());
	}

	@Override
	public K ceilingKey(K key) {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		E entry = (E) ((NavigableMap) map).ceilingKey(new AbstractCache.Entry.Probe(key));
		return entry == null ? null : entry.getKey();
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map.Entry<K, V> higherEntry(K key) {
		return nextValidEntry(((NavigableMap) map).tailMap(new AbstractCache.Entry.Probe(key), false).navigableKeySet().iterator());
	}

	@Override
	public K higherKey(K key) {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		E entry = (E) ((NavigableMap) map).higherKey(new AbstractCache.Entry.Probe(key));
		return entry == null ? null : entry.getKey();
	}

	@Override
	public Map.Entry<K, V> firstEntry() {
		return nextValidEntry(((NavigableMap<E, E>) map).navigableKeySet().iterator());
	}

	@Override
	public Map.Entry<K, V> lastEntry() {
		return nextValidEntry(((NavigableMap<E, E>) map).navigableKeySet().descendingIterator());
	}

	@Override
	public Map.Entry<K, V> pollFirstEntry() {
		Iterator<Map.Entry<K, V>> it = entrySet().iterator();
		if (!it.hasNext()) {
			return null;
		}
		Map.Entry<K, V> entry = it.next();
		it.remove();
		return entry;
	}

	@Override
	public Map.Entry<K, V> pollLastEntry() {
		Iterator<Map.Entry<K, V>> it = descendingMap().entrySet().iterator();
		if (!it.hasNext()) {
			return null;
		}
		Map.Entry<K, V> entry = it.next();
		it.remove();
		return entry;
	}

	@Override
	public NavigableMap<K, V> descendingMap() {
		return new NavigableCacheView(((NavigableMap<E, E>) map).descendingMap(), Collections.reverseOrder(comparator()));
	}

	@Override
	public NavigableSet<K> navigableKeySet() {
		return new KeySetView();
	}

	@Override
	public NavigableSet<K> descendingKeySet() {
		return descendingMap().navigableKeySet();
	}

	@Override
	public final SortedMap<K, V> subMap(K fromKey, K toKey) {
		return subMap(fromKey, true, toKey, false);
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
		return new NavigableCacheView(((NavigableMap) map).subMap(new AbstractCache.Entry.Probe(fromKey), fromInclusive, new AbstractCache.Entry.Probe(toKey), toInclusive), comparator());
	}

	@Override
	public final SortedMap<K, V> headMap(K toKey) {
		return headMap(toKey, false);
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
		return new NavigableCacheView(((NavigableMap) map).headMap(new AbstractCache.Entry.Probe(toKey), inclusive), comparator());
	}

	@Override
	public final SortedMap<K, V> tailMap(K fromKey) {
		return tailMap(fromKey, true);
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
		return new NavigableCacheView(((NavigableMap) map).tailMap(new AbstractCache.Entry.Probe(fromKey), inclusive), comparator());
	}

	protected Map.Entry<K, V> nextValidEntry(Iterator<E> it) {
		while (it.hasNext()) {
			E entry = it.next();
			V value = entry.getValue();
			if (value != null) {
				return new AbstractMap.SimpleImmutableEntry<>(entry.getKey(), value);
			}
			it.remove();
			removed(entry);
		}
		return null;
	}

}
