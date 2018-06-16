/*
 * Created on Jun 13, 2018
 */
package com.mattwhitlock.common.ranges;

import java.util.Collection;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.TreeSet;

/**
 * A set of {@link LongRange}s.
 * 
 * @author Matt Whitlock
 */
public final class LongRangeSet extends TreeSet<LongRange> {

	private static final long serialVersionUID = 1L;

	public LongRangeSet() {
	}

	public LongRangeSet(LongRange range) {
		add(range);
	}

	@Override
	public LongRangeSet clone() {
		return (LongRangeSet) super.clone();
	}

	/**
	 * Returns the number of discrete {@link LongRange}s in this {@link LongRangeSet}. This is the number of elements
	 * that would be returned via {@link #iterator()}. Note that the {@link #add(LongRange) add} and
	 * {@link #remove(LongRange) remove} methods do not necessarily increment or decrement this count, even if they do
	 * alter this {@link LongRangeSet}.
	 */
	@Override
	public int size() {
		return super.size();
	}

	@Override
	@Deprecated
	public final boolean contains(Object o) {
		return contains((LongRange) o);
	}

	/**
	 * Returns {@code true} if this {@link LongRangeSet} completely contains the given {@link LongRange}.
	 */
	public boolean contains(LongRange range) {
		LongRange r;
		return (r = floor(range)) != null && r.contains(range) || (r = higher(range)) != null && r.contains(range);
	}

	/**
	 * Returns {@code true} if this {@link LongRangeSet} intersects the given {@link LongRange}.
	 */
	public boolean intersects(LongRange range) {
		LongRange r;
		return (r = floor(range)) != null && r.intersects(range) || (r = higher(range)) != null && r.contains(range);
	}

	/**
	 * Adds the given {@link LongRange} to this {@link LongRangeSet}.
	 */
	@Override
	public boolean add(LongRange range) {
		if (contains(range)) {
			return false;
		}
		NavigableSet<LongRange> set = headSet(range, false);
		if (!set.isEmpty()) {
			LongRange merged = set.last().union(range);
			if (merged != null) {
				range = merged;
				set.pollLast();
			}
		}
		set = tailSet(range, true);
		for (Iterator<LongRange> it = set.iterator(); it.hasNext();) {
			LongRange merged = it.next().union(range);
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
	public final boolean remove(Object o) {
		return remove((LongRange) o);
	}

	/**
	 * Removes the given {@link LongRange} from this {@link LongRangeSet}.
	 */
	public boolean remove(LongRange range) {
		boolean changed = false;
		NavigableSet<LongRange> set = headSet(range, false);
		if (!set.isEmpty()) {
			LongRange last = set.last();
			if (last.intersects(range)) {
				set.pollLast();
				if (!range.contains(last)) {
					LongRange diff = last.difference(range);
					if (diff == null) {
						super.add(new LongRange(last.lower, last.lowerExists, last.lowerInclusive, range.lower, range.lowerExists, !range.lowerInclusive));
						super.add(new LongRange(range.upper, range.upperExists, !range.upperInclusive, last.upper, last.upperExists, last.upperInclusive));
						return true;
					}
					super.add(diff);
				}
				changed = true;
			}
		}
		set = tailSet(range, true);
		for (Iterator<LongRange> it = set.iterator(); it.hasNext();) {
			LongRange next = it.next();
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
	 * Adds to this {@link LongRangeSet} all of the ranges in the given {@link Collection}.
	 */
	@Override
	public boolean addAll(Collection<? extends LongRange> ranges) {
		boolean changed = false;
		for (LongRange range : ranges) {
			changed |= add(range);
		}
		return changed;
	}

	/**
	 * Returns {@code true} if this {@link LongRangeSet} contains all of the ranges in the given {@link Collection}.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean containsAll(Collection<?> ranges) {
		for (LongRange range : (Collection<? extends LongRange>) ranges) {
			if (!contains(range)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns {@code true} if this {@link LongRangeSet} intersects the given {@link LongRangeSet}.
	 */
	public boolean intersects(LongRangeSet set) {
		for (LongRange range : this) {
			if (set.intersects(range)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes from this {@link LongRangeSet} all of the ranges in the given {@link Collection}.
	 * 
	 * @return {@code true} if this {@link LongRangeSet} was altered.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean removeAll(Collection<?> ranges) {
		boolean changed = false;
		for (LongRange range : (Collection<? extends LongRange>) ranges) {
			changed |= remove(range);
		}
		return changed;
	}

	/**
	 * Retains in this {@link LongRangeSet} only the ranges present in the given {@link Collection}.
	 * 
	 * @return {@code true} if this {@link LongRangeSet} was altered.
	 */
	@Override
	public boolean retainAll(Collection<?> ranges) {
		LongRangeSet set = new LongRangeSet(LongRange.allValues);
		set.removeAll(ranges);
		return removeAll(set);
	}

	/**
	 * Returns the least value in this {@link LongRangeSet}. Returns {@code null} if this {@link LongRangeSet} includes
	 * a range with no lower bound.
	 * 
	 * @throws NoSuchElementException
	 *         if this {@link LongRangeSet} is empty or its least bound is exclusive.
	 */
	public Long least() {
		LongRange first = first();
		if (first.lowerInclusive) {
			return first.lower;
		}
		if (!first.lowerExists) {
			return null;
		}
		throw new NoSuchElementException("lower bound is exclusive");
	}

	/**
	 * Returns the greatest value in this {@link LongRangeSet}. Returns {@code null} if this {@link LongRangeSet}
	 * includes a range with no upper bound.
	 * 
	 * @throws NoSuchElementException
	 *         if this {@link LongRangeSet} is empty or its greatest bound is exclusive.
	 */
	public Long greatest() {
		LongRange last = last();
		if (last.upperInclusive) {
			return last.upper;
		}
		if (!last.upperExists) {
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
		for (Iterator<LongRange> it = iterator();;) {
			sb.append(it.next());
			if (!it.hasNext()) {
				break;
			}
			sb.append(", ");
		}
		return sb.append(" }");
	}

}
