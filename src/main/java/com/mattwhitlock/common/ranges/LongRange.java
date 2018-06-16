/*
 * Created on Jun 13, 2018
 */
package com.mattwhitlock.common.ranges;

import java.io.Serializable;
import java.util.SortedSet;

/**
 * A range of long integers. Both the lower bound and the upper bound are optional, and, if present, they can be
 * inclusive or exclusive.
 * 
 * @author Matt Whitlock
 */
public final class LongRange implements Comparable<LongRange>, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * A {@link LongRange} that contains all possible values.
	 */
	public static final LongRange allValues = new LongRange(0, false, false, 0, false, false);

	/**
	 * The lower bound of this {@link LongRange}, if {@link #lowerExists} is {@code true}.
	 */
	public final long lower;

	/**
	 * The upper bound of this {@link LongRange}, if {@link #upperExists} is {@code true}.
	 */
	public final long upper;

	/**
	 * Whether this {@link LongRange} has a lower bound.
	 */
	public final boolean lowerExists;

	/**
	 * Whether this {@link LongRange} has an upper bound.
	 */
	public final boolean upperExists;

	/**
	 * Whether the {@linkplain #lower lower bound} of this {@link LongRange} is inclusive. If this {@link LongRange} has
	 * no lower bound, then this value must be {@code false}.
	 */
	public final boolean lowerInclusive;

	/**
	 * Whether the {@linkplain #upper upper bound} of this {@link LongRange} is inclusive. If this {@link LongRange} has
	 * no upper bound, then this value must be {@code false}.
	 */
	public final boolean upperInclusive;

	/**
	 * Constructs a {@link LongRange} with the given bounds.
	 * 
	 * @param lower
	 *        the lower bound for the {@link LongRange}, if the lower bound exists.
	 * @param lowerExists
	 *        whether the lower bound exists.
	 * @param lowerInclusive
	 *        whether the lower bound is inclusive. Must be {@code false} if {@code lowerExists} is {@code false}.
	 * @param upper
	 *        the upper bound for the {@link LongRange}, if the uppor bound exists.
	 * @param upperExists
	 *        whether the upper bound exists.
	 * @param upperInclusive
	 *        whether the upper bound is inclusive. Must be {@code false} if {@code upperExists} is {@code false}.
	 * @throws IllegalArgumentException
	 *         if the given combination of bounds is not possible.
	 * @see #to(long, boolean)
	 * @see #from(long, boolean)
	 * @see #ofValue(long)
	 * @see #allValues
	 */
	public LongRange(long lower, boolean lowerExists, boolean lowerInclusive, long upper, boolean upperExists, boolean upperInclusive) {
		if (lowerExists && upperExists && (lowerInclusive && upperInclusive ? lower > upper : lower >= upper)) {
			throw new IllegalArgumentException(append(new StringBuilder("illegal bounds: "), lower, lowerExists, lowerInclusive, upper, upperExists, upperInclusive).toString());
		}
		if (lowerInclusive && !lowerExists || upperInclusive && !upperExists) {
			throw new IllegalArgumentException("non-existent bound cannot be inclusive");
		}
		this.lower = lower;
		this.upper = upper;
		this.lowerExists = lowerExists;
		this.upperExists = upperExists;
		this.lowerInclusive = lowerInclusive;
		this.upperInclusive = upperInclusive;
	}

	public LongRange(long lower, boolean lowerInclusive, long upper, boolean upperInclusive) {
		this(lower, true, lowerInclusive, upper, true, upperInclusive);
	}

	/**
	 * Constructs a {@link LongRange} with a lower bound but no upper bound.
	 * 
	 * @param lower
	 *        the lower bound for the {@link LongRange}.
	 * @param lowerInclusive
	 *        whether the lower bound is inclusive.
	 * @see #to(long, boolean)
	 * @see #ofValue(long)
	 * @see #allValues
	 */
	public static LongRange from(long lower, boolean lowerInclusive) {
		return new LongRange(lower, true, lowerInclusive, 0, false, false);
	}

	/**
	 * Constructs a {@link LongRange} with an upper bound but no lower bound.
	 * 
	 * @param upper
	 *        the upper bound for the {@link LongRange}.
	 * @param upperInclusive
	 *        whether the upper bound is inclusive.
	 * @see #from(long, boolean)
	 * @see #ofValue(long)
	 * @see #allValues
	 */
	public static LongRange to(long upper, boolean upperInclusive) {
		return new LongRange(0, false, false, upper, true, upperInclusive);
	}

	/**
	 * Constructs a {@link LongRange} containing a single value.
	 * 
	 * @param value
	 *        the single value for the {@link LongRange} to contain.
	 */
	public static LongRange ofValue(long value) {
		return new LongRange(value, true, true, value, true, true);
	}

	/**
	 * Returns {@code true} if this {@link LongRange} contains the given value.
	 * 
	 * @param value
	 *        the value whose presence in this {@link LongRange} is to be tested.
	 */
	public boolean contains(long value) {
		return compareLowerLower(lower, lowerExists, lowerInclusive, value, true, true) <= 0 && compareUpperUpper(upper, upperExists, upperInclusive, value, true, true) >= 0;
	}

	/**
	 * Returns {@code true} if this {@link LongRange} completely includes the given {@link LongRange}.
	 * 
	 * @param o
	 *        the {@link LongRange} whose membership in this {@link LongRange} is to be tested.
	 */
	public boolean contains(LongRange o) {
		return contains(o.lower, o.lowerExists, o.lowerInclusive, o.upper, o.upperExists, o.upperInclusive);
	}

	/**
	 * Returns {@code true} if this {@link LongRange} contains every value in the given {@link Iterable}.
	 * 
	 * @param c
	 *        the {@link Iterable} whose elements are to be tested for membership in this {@link LongRange}.
	 * @throws NullPointerException
	 *         if the given {@link Iterable} contains a {@code null} element.
	 */
	public boolean containsAll(Iterable<? extends Long> c) {
		for (Long o : c) {
			if (!contains(o)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns {@code true} if this {@link LongRange} contains every value in the given {@link SortedSet}. Only the
	 * first and last element of the {@link SortedSet} are examined.
	 * 
	 * @param s
	 *        the {@link SortedSet} whose elements are to be tested for membership in this {@link LongRange}.
	 * @throws NullPointerException
	 *         if the first or last element of the {@link SortedSet} is {@code null}.
	 */
	public boolean containsAll(SortedSet<? extends Long> s) {
		return s.isEmpty() || contains(s.first()) && contains(s.last());
	}

	/**
	 * Returns {@code true} if this {@link LongRange} contains any values in the given {@link LongRange}.
	 * 
	 * @param o
	 *        the {@link LongRange} to intersect with this {@link LongRange}.
	 */
	public boolean intersects(LongRange o) {
		return compareLowerUpper(lower, lowerExists, lowerInclusive, o.upper, o.upperExists, o.upperInclusive) <= 0 && compareUpperLower(upper, upperExists, upperInclusive, o.lower, o.lowerExists, o.lowerInclusive) >= 0;
	}

	/**
	 * Returns a {@link LongRange} that is the difference of this {@link LongRange} with the given {@link LongRange}.
	 * That is, the returned {@link LongRange} contains all values that are in this {@link LongRange} but are not in the
	 * given {@link LongRange}. Returns {@code null} if the difference cannot be represented by a single range or if the
	 * difference is the empty set.
	 * 
	 * @param o
	 *        the {@link LongRange} to difference with this {@link LongRange}.
	 */
	public LongRange difference(LongRange o) {
		if (!intersects(o)) {
			return this;
		}
		int l = compareLowerLower(lower, lowerExists, lowerInclusive, o.lower, o.lowerExists, o.lowerInclusive), u = compareUpperUpper(upper, upperExists, upperInclusive, o.upper, o.upperExists, o.upperInclusive);
		return l < 0 ? u > 0 ? null : new LongRange(lower, lowerExists, lowerInclusive, o.lower, o.lowerExists, !o.lowerInclusive) : u > 0 ? new LongRange(o.upper, o.upperExists, !o.upperInclusive, upper, upperExists, upperInclusive) : null;
	}

	/**
	 * Returns a {@link LongRange} that is the intersection of this {@link LongRange} with the given {@link LongRange}.
	 * Returns {@code null} if the ranges are disjoint, as they have no intersection.
	 * 
	 * @param o
	 *        the {@link LongRange} to intersect with this {@link LongRange}.
	 */
	public LongRange intersection(LongRange o) {
		if (!intersects(o)) {
			return null;
		}
		int l = compareLowerLower(lower, lowerExists, lowerInclusive, o.lower, o.lowerExists, o.lowerInclusive), u = compareUpperUpper(upper, upperExists, upperInclusive, o.upper, o.upperExists, o.upperInclusive);
		return l < 0 ? u < 0 ? new LongRange(o.lower, o.lowerExists, o.lowerInclusive, upper, upperExists, upperInclusive) : o : u > 0 ? new LongRange(lower, lowerExists, lowerInclusive, o.upper, o.upperExists, o.upperInclusive) : this;
	}

	/**
	 * Returns a {@link LongRange} that is the union of this {@link LongRange} with the given {@link LongRange}. Returns
	 * {@code null} if the ranges are disjoint, as there would be no way to represent their union as a single range.
	 * 
	 * @param o
	 *        the {@link LongRange} to union with this {@link LongRange}.
	 */
	public LongRange union(LongRange o) {
		if (compareLowerUpper(lower, lowerExists, lowerInclusive || o.upperInclusive, o.upper, o.upperExists, true) > 0 || compareUpperLower(upper, upperExists, upperInclusive || o.lowerInclusive, o.lower, o.lowerExists, true) < 0) {
			return null;
		}
		int l = compareLowerLower(lower, lowerExists, lowerInclusive, o.lower, o.lowerExists, o.lowerInclusive), u = compareUpperUpper(upper, upperExists, upperInclusive, o.upper, o.upperExists, o.upperInclusive);
		return l < 0 ? u < 0 ? new LongRange(lower, lowerExists, lowerInclusive, o.upper, o.upperExists, o.upperInclusive) : this : u > 0 ? new LongRange(o.lower, o.lowerExists, o.lowerInclusive, upper, upperExists, upperInclusive) : o;
	}

	@Override
	public int compareTo(LongRange o) {
		int c = compareLowerLower(lower, lowerExists, lowerInclusive, o.lower, o.lowerExists, o.lowerInclusive);
		return c == 0 ? compareUpperUpper(upper, upperExists, upperInclusive, o.upper, o.upperExists, o.upperInclusive) : c;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		LongRange o = (LongRange) obj;
		return lower == o.lower && upper == o.upper && lowerExists == o.lowerExists && upperExists == o.upperExists && lowerInclusive == o.lowerInclusive && upperInclusive == o.upperInclusive;
	}

	@Override
	public int hashCode() {
		return Long.hashCode(lower) + Long.hashCode(upper) * 65521;
	}

	@Override
	public final String toString() {
		return appendTo(new StringBuilder()).toString();
	}

	public StringBuilder appendTo(StringBuilder sb) {
		return append(sb, lower, lowerExists, lowerInclusive, upper, upperExists, upperInclusive);
	}

	private static StringBuilder append(StringBuilder sb, long lower, boolean lowerExists, boolean lowerInclusive, long upper, boolean upperExists, boolean upperInclusive) {
		sb.append(lowerInclusive ? '[' : '(');
		if (lowerExists) {
			sb.append(lower);
		}
		sb.append(", ");
		if (upperExists) {
			sb.append(upper);
		}
		return sb.append(upperInclusive ? ']' : ')');
	}

	private boolean contains(long lower, boolean lowerExists, boolean lowerInclusive, long upper, boolean upperExists, boolean upperInclusive) {
		return compareLowerLower(this.lower, this.lowerExists, this.lowerInclusive, lower, lowerExists, lowerInclusive) <= 0 && compareUpperUpper(this.upper, this.upperExists, this.upperInclusive, upper, upperExists, upperInclusive) >= 0;
	}

	private static int compareLowerLower(long lower1, boolean lower1Exists, boolean lower1Inclusive, long lower2, boolean lower2Exists, boolean lower2Inclusive) {
		return lower1Exists ? lower2Exists ? lower1 == lower2 ? lower1Inclusive == lower2Inclusive ? 0 : lower1Inclusive ? -1 : 1 : Long.compare(lower1, lower2) : 1 : lower2Exists ? -1 : 0;
	}

	private static int compareLowerUpper(long lower, boolean lowerExists, boolean lowerInclusive, long upper, boolean upperExists, boolean upperInclusive) {
		return lowerExists && upperExists ? lower == upper ? lowerInclusive && upperInclusive ? 0 : 1 : Long.compare(lower, upper) : -1;
	}

	private static int compareUpperLower(long upper, boolean upperExists, boolean upperInclusive, long lower, boolean lowerExists, boolean lowerInclusive) {
		return upperExists && lowerExists ? upper == lower ? upperInclusive && lowerInclusive ? 0 : -1 : Long.compare(upper, lower) : 1;
	}

	private static int compareUpperUpper(long upper1, boolean upper1Exists, boolean upper1Inclusive, long upper2, boolean upper2Exists, boolean upper2Inclusive) {
		return upper1Exists ? upper2Exists ? upper1 == upper2 ? upper1Inclusive == upper2Inclusive ? 0 : upper1Inclusive ? 1 : -1 : Long.compare(upper1, upper2) : -1 : upper2Exists ? 1 : 0;
	}

}
