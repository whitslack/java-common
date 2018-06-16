/*
 * Created on Aug 4, 2006
 */
package com.mattwhitlock.common.ranges;

import java.io.Serializable;
import java.util.Objects;
import java.util.SortedSet;

/**
 * A generic range of any {@link Comparable} values. Both the lower bound and the upper bound are optional, and, if
 * present, they can be inclusive or exclusive.
 * 
 * @author Matt Whitlock
 */
public final class Range<T extends Comparable<? super T>> implements Comparable<Range<T>>, Serializable {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static final Range allValues = new Range(null, false, null, false);

	/**
	 * The lower bound of this {@link Range}, or {@code null} if this {@link Range} has no lower bound.
	 */
	public final T lower;

	/**
	 * The upper bound of this {@link Range}, or {@code null} if this {@link Range} has no upper bound.
	 */
	public final T upper;

	/**
	 * Whether the {@linkplain #lower lower bound} of this {@link Range} is inclusive. If this {@link Range} has no
	 * lower bound, then this value must be {@code false}.
	 */
	public final boolean lowerInclusive;

	/**
	 * Whether the {@linkplain #upper upper bound} of this {@link Range} is inclusive. If this {@link Range} has no
	 * upper bound, then this value must be {@code false}.
	 */
	public final boolean upperInclusive;

	/**
	 * Constructs a {@link Range} with the given bounds.
	 * 
	 * @param lower
	 *        the lower bound for the {@link Range}, or {@code null} for no lower bound.
	 * @param lowerInclusive
	 *        whether the lower bound is inclusive. Must be {@code false} if {@code lower} is {@code null}.
	 * @param upper
	 *        the upper bound for the {@link Range}, or {@code null} for no upper bound.
	 * @param upperInclusive
	 *        whether the upper bound is inclusive. Must be {@code false} if {@code upper} is {@code null}.
	 * @throws IllegalArgumentException
	 *         if the given combination of bounds is not possible.
	 * @see #to(Comparable, boolean)
	 * @see #from(Comparable, boolean)
	 * @see #ofValue(Comparable)
	 * @see #allValues()
	 */
	public Range(T lower, boolean lowerInclusive, T upper, boolean upperInclusive) {
		if (lower != null && upper != null && (lowerInclusive && upperInclusive ? lower.compareTo(upper) > 0 : lower.compareTo(upper) >= 0)) {
			throw new IllegalArgumentException(append(new StringBuilder("illegal bounds: "), lower, lowerInclusive, upper, upperInclusive).toString());
		}
		if (lowerInclusive && lower == null || upperInclusive && upper == null) {
			throw new IllegalArgumentException("non-existent bound cannot be inclusive");
		}
		this.lower = lower;
		this.upper = upper;
		this.lowerInclusive = lowerInclusive;
		this.upperInclusive = upperInclusive;
	}

	/**
	 * Constructs a {@link Range} with a lower bound but no upper bound.
	 * 
	 * @param lower
	 *        the lower bound for the {@link Range}.
	 * @param lowerInclusive
	 *        whether the lower bound is inclusive.
	 * @throws NullPointerException
	 *         if {@code lower} is {@code null}.
	 * @see #to(Comparable, boolean)
	 * @see #ofValue(Comparable)
	 * @see #allValues()
	 */
	public static <T extends Comparable<? super T>> Range<T> from(T lower, boolean lowerInclusive) {
		Objects.requireNonNull(lower);
		return new Range<>(lower, lowerInclusive, null, false);
	}

	/**
	 * Constructs a {@link Range} with an upper bound but no lower bound.
	 * 
	 * @param upper
	 *        the upper bound for the {@link Range}.
	 * @param upperInclusive
	 *        whether the upper bound is inclusive.
	 * @throws NullPointerException
	 *         if {@code upper} is {@code null}.
	 * @see #from(Comparable, boolean)
	 * @see #ofValue(Comparable)
	 * @see #allValues()
	 */
	public static <T extends Comparable<? super T>> Range<T> to(T upper, boolean upperInclusive) {
		Objects.requireNonNull(upper);
		return new Range<>(null, false, upper, upperInclusive);
	}

	/**
	 * Constructs a {@link Range} containing a single value.
	 * 
	 * @param value
	 *        the single value for the {@link Range} to contain.
	 * @throws NullPointerException
	 *         if {@code value} is {@code null}.
	 */
	public static <T extends Comparable<? super T>> Range<T> ofValue(T value) {
		Objects.requireNonNull(value);
		return new Range<>(value, true, value, true);
	}

	/**
	 * Returns a {@link Range} that contains all possible values.
	 */
	public static <T extends Comparable<? super T>> Range<T> allValues() {
		return allValues;
	}

	/**
	 * Returns {@code true} if this {@link Range} contains the given value.
	 * 
	 * @param value
	 *        the value whose presence in this {@link Range} is to be tested.
	 */
	public boolean contains(T value) {
		return compareLowerLower(lower, lowerInclusive, value, true) <= 0 && compareUpperUpper(upper, upperInclusive, value, true) >= 0;
	}

	/**
	 * Returns {@code true} if this {@link Range} completely includes the given {@link Range}.
	 * 
	 * @param o
	 *        the {@link Range} whose membership in this {@link Range} is to be tested.
	 */
	public boolean contains(Range<T> o) {
		return contains(o.lower, o.lowerInclusive, o.upper, o.upperInclusive);
	}

	/**
	 * Returns {@code true} if this {@link Range} contains every value in the given {@link Iterable}.
	 * 
	 * @param c
	 *        the {@link Iterable} whose elements are to be tested for membership in this {@link Range}.
	 */
	public boolean containsAll(Iterable<? extends T> c) {
		for (T o : c) {
			if (!contains(o)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns {@code true} if this {@link Range} contains every value in the given {@link SortedSet}. Only the first
	 * and last element of the {@link SortedSet} are examined.
	 * 
	 * @param s
	 *        the {@link SortedSet} whose elements are to be tested for membership in this {@link Range}.
	 */
	public boolean containsAll(SortedSet<? extends T> s) {
		return s.isEmpty() || contains(s.first()) && contains(s.last());
	}

	/**
	 * Returns {@code true} if this {@link Range} contains any values in the given {@link Range}.
	 * 
	 * @param o
	 *        the {@link Range} to intersect with this {@link Range}.
	 */
	public boolean intersects(Range<T> o) {
		return compareLowerUpper(lower, lowerInclusive, o.upper, o.upperInclusive) <= 0 && compareUpperLower(upper, upperInclusive, o.lower, o.lowerInclusive) >= 0;
	}

	/**
	 * Returns a {@link Range} that is the difference of this {@link Range} with the given {@link Range}. That is, the
	 * returned {@link Range} contains all values that are in this {@link Range} but are not in the given {@link Range}.
	 * Returns {@code null} if the difference cannot be represented by a single range or if the difference is the empty
	 * set.
	 * 
	 * @param o
	 *        the {@link Range} to difference with this {@link Range}.
	 */
	public Range<T> difference(Range<T> o) {
		if (!intersects(o)) {
			return this;
		}
		int l = compareLowerLower(lower, lowerInclusive, o.lower, o.lowerInclusive), u = compareUpperUpper(upper, upperInclusive, o.upper, o.upperInclusive);
		return l < 0 ? u > 0 ? null : new Range<>(lower, lowerInclusive, o.lower, !o.lowerInclusive) : u > 0 ? new Range<>(o.upper, !o.upperInclusive, upper, upperInclusive) : null;
	}

	/**
	 * Returns a {@link Range} that is the intersection of this {@link Range} with the given {@link Range}. Returns
	 * {@code null} if the ranges are disjoint, as they have no intersection.
	 * 
	 * @param o
	 *        the {@link Range} to intersect with this {@link Range}.
	 */
	public Range<T> intersection(Range<T> o) {
		if (!intersects(o)) {
			return null;
		}
		int l = compareLowerLower(lower, lowerInclusive, o.lower, o.lowerInclusive), u = compareUpperUpper(upper, upperInclusive, o.upper, o.upperInclusive);
		return l < 0 ? u < 0 ? new Range<>(o.lower, o.lowerInclusive, upper, upperInclusive) : o : u > 0 ? new Range<>(lower, lowerInclusive, o.upper, o.upperInclusive) : this;
	}

	/**
	 * Returns a {@link Range} that is the union of this {@link Range} with the given {@link Range}. Returns
	 * {@code null} if the ranges are disjoint, as there would be no way to represent their union as a single range.
	 * 
	 * @param o
	 *        the {@link Range} to union with this {@link Range}.
	 */
	public Range<T> union(Range<T> o) {
		if (compareLowerUpper(lower, lowerInclusive || o.upperInclusive, o.upper, true) > 0 || compareUpperLower(upper, upperInclusive || o.lowerInclusive, o.lower, true) < 0) {
			return null;
		}
		int l = compareLowerLower(lower, lowerInclusive, o.lower, o.lowerInclusive), u = compareUpperUpper(upper, upperInclusive, o.upper, o.upperInclusive);
		return l < 0 ? u < 0 ? new Range<>(lower, lowerInclusive, o.upper, o.upperInclusive) : this : u > 0 ? new Range<>(o.lower, o.lowerInclusive, upper, upperInclusive) : o;
	}

	@Override
	public int compareTo(Range<T> o) {
		int c = compareLowerLower(lower, lowerInclusive, o.lower, o.lowerInclusive);
		return c == 0 ? compareUpperUpper(upper, upperInclusive, o.upper, o.upperInclusive) : c;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Range<?> o = (Range<?>) obj;
		return lowerInclusive == o.lowerInclusive && upperInclusive == o.upperInclusive && Objects.equals(lower, o.lower) && Objects.equals(upper, o.upper);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(lower) + Objects.hashCode(upper) * 65521;
	}

	@Override
	public final String toString() {
		return appendTo(new StringBuilder()).toString();
	}

	public StringBuilder appendTo(StringBuilder sb) {
		return append(sb, lower, lowerInclusive, upper, upperInclusive);
	}

	private static StringBuilder append(StringBuilder sb, Object lower, boolean lowerInclusive, Object upper, boolean upperInclusive) {
		sb.append(lowerInclusive ? '[' : '(');
		if (lower != null) {
			sb.append(lower);
		}
		sb.append(", ");
		if (upper != null) {
			sb.append(upper);
		}
		return sb.append(upperInclusive ? ']' : ')');
	}

	private boolean contains(T lower, boolean lowerInclusive, T upper, boolean upperInclusive) {
		return compareLowerLower(this.lower, this.lowerInclusive, lower, lowerInclusive) <= 0 && compareUpperUpper(this.upper, this.upperInclusive, upper, upperInclusive) >= 0;
	}

	private static <T extends Comparable<? super T>> int compareLowerLower(T lower1, boolean lower1Inclusive, T lower2, boolean lower2Inclusive) {
		int c;
		return lower1 == null ? lower2 == null ? 0 : -1 : lower2 == null ? 1 : (c = lower1.compareTo(lower2)) == 0 ? lower1Inclusive == lower2Inclusive ? 0 : lower1Inclusive ? -1 : 1 : c;
	}

	private static <T extends Comparable<? super T>> int compareLowerUpper(T lower, boolean lowerInclusive, T upper, boolean upperInclusive) {
		int c;
		return lower == null || upper == null ? -1 : (c = lower.compareTo(upper)) == 0 ? lowerInclusive && upperInclusive ? 0 : 1 : c;
	}

	private static <T extends Comparable<? super T>> int compareUpperLower(T upper, boolean upperInclusive, T lower, boolean lowerInclusive) {
		int c;
		return upper == null || lower == null ? 1 : (c = upper.compareTo(lower)) == 0 ? upperInclusive && lowerInclusive ? 0 : -1 : c;
	}

	private static <T extends Comparable<? super T>> int compareUpperUpper(T upper1, boolean upper1Inclusive, T upper2, boolean upper2Inclusive) {
		int c;
		return upper1 == null ? upper2 == null ? 0 : 1 : upper2 == null ? -1 : (c = upper1.compareTo(upper2)) == 0 ? upper1Inclusive == upper2Inclusive ? 0 : upper1Inclusive ? 1 : -1 : c;
	}

}
