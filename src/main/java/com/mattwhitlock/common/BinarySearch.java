/*
 * Created on Feb 1, 2018
 */
package com.mattwhitlock.common;

import java.util.Comparator;
import java.util.function.ToIntFunction;

/**
 * A utility class for performing binary searches in sorted arrays.
 * 
 * @author Matt Whitlock
 */
public final class BinarySearch {

	/**
	 * Not instantiable.
	 */
	private BinarySearch() {
	}

	/**
	 * Returns <code>{@link #find(Object[], int, int, ToIntFunction) find}(array, 0, array.length, compare)</code>.
	 */
	public static final <T> int find(T[] array, ToIntFunction<T> compare) {
		return find(array, 0, array.length, compare);
	}

	/**
	 * Returns
	 * <code>{@link #find(Object[], int, int, ToIntFunction) find}(array, 0, array.length, sought::{@link Comparable#compareTo(Object) compareTo})</code>.
	 */
	public static final <T extends Comparable<? super T>> int find(T[] array, T sought) {
		return find(array, 0, array.length, sought::compareTo);
	}

	/**
	 * Returns
	 * <code>{@link #find(Object[], int, int, ToIntFunction) find}(array, 0, array.length, e -> comparator.{@link Comparator#compare(Object, Object) compare}(sought, e))</code>.
	 */
	public static final <T> int find(T[] array, Comparator<? super T> comparator, T sought) {
		return find(array, 0, array.length, e -> comparator.compare(sought, e));
	}

	/**
	 * Performs a binary search over a sorted array.
	 * 
	 * @param array
	 *        The sorted array to search.
	 * @param low
	 *        The lowest index to search (inclusive).
	 * @param high
	 *        The highest index to search (exclusive).
	 * @param compare
	 *        A comparison function that returns negative if the sought object compares less than the function's
	 *        argument, positive if the sought object compares greater than the function's argument, or zero if the
	 *        sought object compares equal to the function's argument.
	 * @return The index of an element in the array to which the sought object compares equal, or the bitwise complement
	 *         of the index at which the sought object could be inserted into the array to maintain proper sorting. If
	 *         the sought object compares equal to multiple elements of the array, then the index of any of these
	 *         elements may be returned.
	 */
	public static <T> int find(T[] array, int low, int high, ToIntFunction<T> compare) {
		while (low < high) {
			int mid = low + high >>> 1, c = compare.applyAsInt(array[mid]);
			if (c > 0) {
				low = mid + 1;
			}
			else if (c < 0) {
				high = mid;
			}
			else {
				return mid;
			}
		}
		return ~low;
	}

	/**
	 * Returns
	 * <code>{@link #lowerBound(Object[], int, int, ToIntFunction) lowerBound}(array, 0, array.length, compare)</code>.
	 */
	public static final <T> int lowerBound(T[] array, ToIntFunction<T> compare) {
		return lowerBound(array, 0, array.length, compare);
	}

	/**
	 * Returns
	 * <code>{@link #lowerBound(Object[], int, int, ToIntFunction) lowerBound}(array, 0, array.length, sought::{@link Comparable#compareTo(Object) compareTo})</code>.
	 */
	public static final <T extends Comparable<? super T>> int lowerBound(T[] array, T sought) {
		return lowerBound(array, 0, array.length, sought::compareTo);
	}

	/**
	 * Returns
	 * <code>{@link #lowerBound(Object[], int, int, ToIntFunction) lowerBound}(array, 0, array.length, e -> comparator.{@link Comparator#compare(Object, Object) compare}(sought, e))</code>.
	 */
	public static final <T> int lowerBound(T[] array, Comparator<? super T> comparator, T sought) {
		return lowerBound(array, 0, array.length, e -> comparator.compare(sought, e));
	}

	/**
	 * Performs a binary search over a sorted array.
	 * 
	 * @param array
	 *        The sorted array to search.
	 * @param low
	 *        The lowest index to search (inclusive).
	 * @param high
	 *        The highest index to search (exclusive).
	 * @param compare
	 *        A comparison function that returns negative if the sought object compares less than the function's
	 *        argument, positive if the sought object compares greater than the function's argument, or zero if the
	 *        sought object compares equal to the function's argument.
	 * @return The index of the first element in the array that compares <em>not less</em> than the sought object, or
	 *         {@code high} if there is no such element.
	 */
	public static <T> int lowerBound(T[] array, int low, int high, ToIntFunction<T> compare) {
		while (low < high) {
			int mid = low + high >>> 1, c = compare.applyAsInt(array[mid]);
			if (c > 0) {
				low = mid + 1;
			}
			else if (c < 0) {
				high = mid;
			}
			else {
				high = mid;
				if (low < high && compare.applyAsInt(array[high - 1]) == 0) {
					--high;
					while (low < high) {
						if (compare.applyAsInt(array[mid = low + high >>> 1]) > 0) {
							low = mid + 1;
						}
						else {
							high = mid;
						}
					}
				}
				return high;
			}
		}
		return low;
	}

	/**
	 * Returns
	 * <code>{@link #upperBound(Object[], int, int, ToIntFunction) upperBound}(array, 0, array.length, compare)</code>.
	 */
	public static final <T> int upperBound(T[] array, ToIntFunction<T> compare) {
		return upperBound(array, 0, array.length, compare);
	}

	/**
	 * Returns
	 * <code>{@link #upperBound(Object[], int, int, ToIntFunction) upperBound}(array, 0, array.length, sought::{@link Comparable#compareTo(Object) compareTo})</code>.
	 */
	public static final <T extends Comparable<? super T>> int upperBound(T[] array, T sought) {
		return upperBound(array, 0, array.length, sought::compareTo);
	}

	/**
	 * Returns
	 * <code>{@link #upperBound(Object[], int, int, ToIntFunction) upperBound}(array, 0, array.length, e -> comparator.{@link Comparator#compare(Object, Object) compare}(sought, e))</code>.
	 */
	public static final <T> int upperBound(T[] array, Comparator<? super T> comparator, T sought) {
		return upperBound(array, 0, array.length, e -> comparator.compare(sought, e));
	}

	/**
	 * Performs a binary search over a sorted array.
	 * 
	 * @param array
	 *        The sorted array to search.
	 * @param low
	 *        The lowest index to search (inclusive).
	 * @param high
	 *        The highest index to search (exclusive).
	 * @param compare
	 *        A comparison function that returns negative if the sought object compares less than the function's
	 *        argument, positive if the sought object compares greater than the function's argument, or zero if the
	 *        sought object compares equal to the function's argument.
	 * @return The index of the first element in the array that compares <em>greater</em> than the sought object, or
	 *         {@code high} if there is no such element.
	 */
	public static <T> int upperBound(T[] array, int low, int high, ToIntFunction<T> compare) {
		while (low < high) {
			int mid = low + high >>> 1, c = compare.applyAsInt(array[mid]);
			if (c > 0) {
				low = mid + 1;
			}
			else if (c < 0) {
				high = mid;
			}
			else {
				low = mid + 1;
				if (low < high && compare.applyAsInt(array[low]) == 0) {
					++low;
					while (low < high) {
						if (compare.applyAsInt(array[mid = low + high >>> 1]) >= 0) {
							low = mid + 1;
						}
						else {
							high = mid;
						}
					}
				}
				return low;
			}
		}
		return low;
	}

	/**
	 * Returns
	 * <code>{@link #equalRange(Object[], int, int, ToIntFunction) equalRange}(array, 0, array.length, compare)</code>.
	 */
	public static final <T> int[] equalRange(T[] array, ToIntFunction<T> compare) {
		return equalRange(array, 0, array.length, compare);
	}

	/**
	 * Returns
	 * <code>{@link #equalRange(Object[], int, int, ToIntFunction) equalRange}(array, 0, array.length, sought::{@link Comparable#compareTo(Object) compareTo})</code>.
	 */
	public static final <T extends Comparable<? super T>> int[] equalRange(T[] array, T sought) {
		return equalRange(array, 0, array.length, sought::compareTo);
	}

	/**
	 * Returns
	 * <code>{@link #equalRange(Object[], int, int, ToIntFunction) equalRange}(array, 0, array.length, e -> comparator.{@link Comparator#compare(Object, Object) compare}(sought, e))</code>.
	 */
	public static final <T> int[] equalRange(T[] array, Comparator<? super T> comparator, T sought) {
		return equalRange(array, 0, array.length, e -> comparator.compare(sought, e));
	}

	/**
	 * Performs a binary search over a sorted array.
	 * 
	 * @param array
	 *        The sorted array to search.
	 * @param low
	 *        The lowest index to search (inclusive).
	 * @param high
	 *        The highest index to search (exclusive).
	 * @param compare
	 *        A comparison function that returns negative if the sought object compares less than the function's
	 *        argument, positive if the sought object compares greater than the function's argument, or zero if the
	 *        sought object compares equal to the function's argument.
	 * @return The indices of the first element in the array that compares <em>not less</em> than the sought object and
	 *         the first element in the array that compares <em>greater</em> than the sought object, or
	 *         <code>{ high, high }</code> if there is no such element.
	 */
	public static <T> int[] equalRange(T[] array, int low, int high, ToIntFunction<T> compare) {
		while (low < high) {
			int mid = low + high >>> 1, c = compare.applyAsInt(array[mid]);
			if (c > 0) {
				low = mid + 1;
			}
			else if (c < 0) {
				high = mid;
			}
			else {
				int lowerHigh = mid, upperLow = mid + 1;
				if (low < lowerHigh && compare.applyAsInt(array[lowerHigh - 1]) == 0) {
					--lowerHigh;
					while (low < lowerHigh) {
						if (compare.applyAsInt(array[mid = low + lowerHigh >>> 1]) > 0) {
							low = mid + 1;
						}
						else {
							lowerHigh = mid;
						}
					}
				}
				if (upperLow < high && compare.applyAsInt(array[upperLow]) == 0) {
					++upperLow;
					while (upperLow < high) {
						if (compare.applyAsInt(array[mid = upperLow + high >>> 1]) >= 0) {
							upperLow = mid + 1;
						}
						else {
							high = mid;
						}
					}
				}
				return new int[] { lowerHigh, upperLow };
			}
		}
		return new int[] { low, high };
	}

}
