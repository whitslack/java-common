/*
 * Created on Aug 6, 2006
 */
package com.mattwhitlock.common.ranges;

import java.util.Collection;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.TreeSet;

/**
 * A set of {@link Range}s.
 * 
 * @author Matt Whitlock
 */
public final class RangeSet<T extends Comparable<? super T>> extends TreeSet<Range<T>> {

	private static final long serialVersionUID = 1L;

	public RangeSet() {
	}

	public RangeSet(Range<T> range) {
		add(range);
	}

	@Override
	@SuppressWarnings("unchecked")
	public RangeSet<T> clone() {
		return (RangeSet<T>) super.clone();
	}

	/**
	 * Returns the number of discrete {@link Range}s in this {@link RangeSet}. This is the number of elements that would
	 * be returned via {@link #iterator()}. Note that the {@link #add(Range) add} and {@link #remove(Range) remove}
	 * methods do not necessarily increment or decrement this count, even if they do alter this {@link RangeSet}.
	 */
	@Override
	public int size() {
		return super.size();
	}

	@Override
	@Deprecated
	@SuppressWarnings("unchecked")
	public final boolean contains(Object o) {
		return contains((Range<T>) o);
	}

	/**
	 * Returns {@code true} if this {@link RangeSet} completely contains the given {@link Range}.
	 */
	public boolean contains(Range<T> range) {
		Range<T> r;
		return (r = floor(range)) != null && r.contains(range) || (r = higher(range)) != null && r.contains(range);
	}

	/**
	 * Returns {@code true} if this {@link RangeSet} intersects the given {@link Range}.
	 */
	public boolean intersects(Range<T> range) {
		Range<T> r;
		return (r = floor(range)) != null && r.intersects(range) || (r = higher(range)) != null && r.contains(range);
	}

	/**
	 * Adds the given {@link Range} to this {@link RangeSet}.
	 */
	@Override
	public boolean add(Range<T> range) {
		if (contains(range)) {
			return false;
		}
		NavigableSet<Range<T>> set = headSet(range, false);
		if (!set.isEmpty()) {
			Range<T> merged = set.last().union(range);
			if (merged != null) {
				range = merged;
				set.pollLast();
			}
		}
		set = tailSet(range, true);
		for (Iterator<Range<T>> it = set.iterator(); it.hasNext();) {
			Range<T> merged = it.next().union(range);
			if (merged == null) {
				break;
			}
			range = merged;
			it.remove();
		}
		return super.add(range);
	}

	@Override
	@Deprecated
	@SuppressWarnings("unchecked")
	public final boolean remove(Object o) {
		return remove((Range<T>) o);
	}

	/**
	 * Removes the given {@link Range} from this {@link RangeSet}.
	 */
	public boolean remove(Range<T> range) {
		boolean changed = false;
		NavigableSet<Range<T>> set = headSet(range, false);
		if (!set.isEmpty()) {
			Range<T> last = set.last();
			if (last.intersects(range)) {
				set.pollLast();
				if (!range.contains(last)) {
					Range<T> diff = last.difference(range);
					if (diff == null) {
						super.add(new Range<>(last.lower, last.lowerInclusive, range.lower, !range.lowerInclusive));
						super.add(new Range<>(range.upper, !range.upperInclusive, last.upper, last.upperInclusive));
						return true;
					}
					super.add(diff);
				}
				changed = true;
			}
		}
		set = tailSet(range, true);
		for (Iterator<Range<T>> it = set.iterator(); it.hasNext();) {
			Range<T> next = it.next();
			if (!next.intersects(range)) {
				break;
			}
			it.remove();
			if (!range.contains(next)) {
				super.add(next.difference(range));
				return true;
			}
			changed = true;
		}
		return changed;
	}

	/**
	 * Adds to this {@link RangeSet} all of the ranges in the given {@link Collection}.
	 */
	@Override
	public boolean addAll(Collection<? extends Range<T>> ranges) {
		boolean changed = false;
		for (Range<T> range : ranges) {
			changed |= add(range);
		}
		return changed;
	}

	/**
	 * Returns {@code true} if this {@link RangeSet} contains all of the ranges in the given {@link Collection}.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean containsAll(Collection<?> ranges) {
		for (Range<T> range : (Collection<? extends Range<T>>) ranges) {
			if (!contains(range)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns {@code true} if this {@link RangeSet} intersects the given {@link RangeSet}.
	 */
	public boolean intersects(RangeSet<T> set) {
		for (Range<T> range : this) {
			if (set.intersects(range)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes from this {@link RangeSet} all of the ranges in the given {@link Collection}.
	 * 
	 * @return {@code true} if this {@link RangeSet} was altered.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean removeAll(Collection<?> ranges) {
		boolean changed = false;
		for (Range<T> range : (Collection<? extends Range<T>>) ranges) {
			changed |= remove(range);
		}
		return changed;
	}

	/**
	 * Retains in this {@link RangeSet} only the ranges present in the given {@link Collection}.
	 * 
	 * @return {@code true} if this {@link RangeSet} was altered.
	 */
	@Override
	public boolean retainAll(Collection<?> ranges) {
		RangeSet<T> set = new RangeSet<>(Range.<T> allValues());
		set.removeAll(ranges);
		return removeAll(set);
	}

	/**
	 * Returns the least value in this {@link RangeSet}. Returns {@code null} if this {@link RangeSet} includes a range
	 * with no lower bound.
	 * 
	 * @throws NoSuchElementException
	 *         if this {@link RangeSet} is empty or its least bound is exclusive.
	 */
	public T least() {
		Range<T> first = first();
		if (first.lowerInclusive) {
			return first.lower;
		}
		if (first.lower == null) {
			return null;
		}
		throw new NoSuchElementException("lower bound is exclusive");
	}

	/**
	 * Returns the greatest value in this {@link RangeSet}. Returns {@code null} if this {@link RangeSet} includes a
	 * range with no upper bound.
	 * 
	 * @throws NoSuchElementException
	 *         if this {@link RangeSet} is empty or its greatest bound is exclusive.
	 */
	public T greatest() {
		Range<T> last = last();
		if (last.upperInclusive) {
			return last.upper;
		}
		if (last.upper == null) {
			return null;
		}
		throw new NoSuchElementException("upper bound is exclusive");
	}

	@Override
	public final String toString() {
		return appendTo(new StringBuilder()).toString();
	}

	public StringBuilder appendTo(StringBuilder sb) {
		if (isEmpty()) {
			return sb.append("{ }");
		}
		sb.append("{ ");
		for (Iterator<Range<T>> it = iterator();;) {
			sb.append(it.next());
			if (!it.hasNext()) {
				break;
			}
			sb.append(", ");
		}
		return sb.append(" }");
	}

}
