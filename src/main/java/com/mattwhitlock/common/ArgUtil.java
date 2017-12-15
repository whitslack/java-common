package com.mattwhitlock.common;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * Utilities for checking the validity of arguments and providing meaningful exception messages.
 * 
 * @author Matt Whitlock
 */
/*
 * Implementation note: The conditional expressions in this class are written to catch NaNs. Do not attempt to optimize,
 * for example, (!(a >= b)) into (a < b), as those expressions have different meanings.
 */
public final class ArgUtil {

	/**
	 * Not instantiable.
	 */
	private ArgUtil() {
	}

	/**
	 * Throws a {@link NullPointerException} if {@code arg} is {@code null}.
	 * 
	 * @deprecated Call {@link Objects#requireNonNull(Object, String)} instead.
	 */
	@Deprecated
	public static void checkNotNull(Object arg, String argName) throws NullPointerException {
		Objects.requireNonNull(arg, argName);
	}

	/**
	 * Throws a {@link NullPointerException} if {@code arg} or any element of it is {@code null}.
	 */
	public static void checkNoneNull(Object[] arg, String argName) throws NullPointerException {
		if (arg == null) {
			throw new NullPointerException(argName);
		}
		for (int i = 0; i < arg.length; ++i) {
			if (arg[i] == null) {
				throw new NullPointerException(argName + '[' + i + ']');
			}
		}
	}

	/**
	 * Throws a {@link NullPointerException} if {@code arg} or any element of the specified slice of {@code arg} is
	 * {@code null}. Throws an {@link IndexOutOfBoundsException} if {@code offset} and {@code length} specify an array
	 * slice outside the bounds of {@code arg}.
	 */
	public static void checkNoneNull(Object[] arg, int offset, int length, String argName) throws NullPointerException {
		if (arg == null) {
			throw new NullPointerException(argName);
		}
		checkSlice(arg.length, offset, length, argName);
		length += offset;
		while (offset < length) {
			if (arg[offset] == null) {
				throw new NullPointerException(argName + '[' + offset + ']');
			}
			++offset;
		}
	}

	/**
	 * Throws a {@link NullPointerException} if {@code arg} or any element of it is {@code null}.
	 */
	public static void checkNoneNull(Iterable<?> arg, String argName) throws NullPointerException {
		if (arg == null) {
			throw new NullPointerException(argName);
		}
		int i = 0;
		for (Object element : arg) {
			if (element == null) {
				throw new NullPointerException(argName + " element " + i);
			}
			++i;
		}
	}

	/**
	 * Throws a {@link NullPointerException} if {@code arg} is {@code null} or an {@link IllegalArgumentException} if
	 * {@code arg} is empty.
	 */
	public static void checkNotEmpty(boolean[] arg, String argName) {
		if (arg == null) {
			throw new NullPointerException(argName);
		}
		if (arg.length == 0) {
			throw new IllegalArgumentException(argName + " must not be empty");
		}
	}

	/**
	 * Throws a {@link NullPointerException} if {@code arg} is {@code null} or an {@link IllegalArgumentException} if
	 * {@code arg} is empty.
	 */
	public static void checkNotEmpty(byte[] arg, String argName) {
		if (arg == null) {
			throw new NullPointerException(argName);
		}
		if (arg.length == 0) {
			throw new IllegalArgumentException(argName + " must not be empty");
		}
	}

	/**
	 * Throws a {@link NullPointerException} if {@code arg} is {@code null} or an {@link IllegalArgumentException} if
	 * {@code arg} is empty.
	 */
	public static void checkNotEmpty(short[] arg, String argName) {
		if (arg == null) {
			throw new NullPointerException(argName);
		}
		if (arg.length == 0) {
			throw new IllegalArgumentException(argName + " must not be empty");
		}
	}

	/**
	 * Throws a {@link NullPointerException} if {@code arg} is {@code null} or an {@link IllegalArgumentException} if
	 * {@code arg} is empty.
	 */
	public static void checkNotEmpty(int[] arg, String argName) {
		if (arg == null) {
			throw new NullPointerException(argName);
		}
		if (arg.length == 0) {
			throw new IllegalArgumentException(argName + " must not be empty");
		}
	}

	/**
	 * Throws a {@link NullPointerException} if {@code arg} is {@code null} or an {@link IllegalArgumentException} if
	 * {@code arg} is empty.
	 */
	public static void checkNotEmpty(long[] arg, String argName) {
		if (arg == null) {
			throw new NullPointerException(argName);
		}
		if (arg.length == 0) {
			throw new IllegalArgumentException(argName + " must not be empty");
		}
	}

	/**
	 * Throws a {@link NullPointerException} if {@code arg} is {@code null} or an {@link IllegalArgumentException} if
	 * {@code arg} is empty.
	 */
	public static void checkNotEmpty(float[] arg, String argName) {
		if (arg == null) {
			throw new NullPointerException(argName);
		}
		if (arg.length == 0) {
			throw new IllegalArgumentException(argName + " must not be empty");
		}
	}

	/**
	 * Throws a {@link NullPointerException} if {@code arg} is {@code null} or an {@link IllegalArgumentException} if
	 * {@code arg} is empty.
	 */
	public static void checkNotEmpty(double[] arg, String argName) {
		if (arg == null) {
			throw new NullPointerException(argName);
		}
		if (arg.length == 0) {
			throw new IllegalArgumentException(argName + " must not be empty");
		}
	}

	/**
	 * Throws a {@link NullPointerException} if {@code arg} is {@code null} or an {@link IllegalArgumentException} if
	 * {@code arg} is empty.
	 */
	public static void checkNotEmpty(char[] arg, String argName) {
		if (arg == null) {
			throw new NullPointerException(argName);
		}
		if (arg.length == 0) {
			throw new IllegalArgumentException(argName + " must not be empty");
		}
	}

	/**
	 * Throws a {@link NullPointerException} if {@code arg} is {@code null} or an {@link IllegalArgumentException} if
	 * {@code arg} is empty.
	 */
	public static void checkNotEmpty(Object[] arg, String argName) {
		if (arg == null) {
			throw new NullPointerException(argName);
		}
		if (arg.length == 0) {
			throw new IllegalArgumentException(argName + " must not be empty");
		}
	}

	/**
	 * Throws a {@link NullPointerException} if {@code arg} is {@code null} or an {@link IllegalArgumentException} if
	 * {@code arg} {@linkplain Collection#isEmpty() is empty}.
	 */
	public static void checkNotEmpty(Collection<?> arg, String argName) {
		if (arg == null) {
			throw new NullPointerException(argName);
		}
		if (arg.isEmpty()) {
			throw new IllegalArgumentException(argName + " must not be empty");
		}
	}

	/**
	 * Throws a {@link NullPointerException} if {@code arg} is {@code null} or an {@link IllegalArgumentException} if
	 * {@code arg} {@linkplain Map#isEmpty() is empty}.
	 */
	public static void checkNotEmpty(Map<?, ?> arg, String argName) {
		if (arg == null) {
			throw new NullPointerException(argName);
		}
		if (arg.isEmpty()) {
			throw new IllegalArgumentException(argName + " must not be empty");
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is non-negative.
	 */
	public static void checkNegative(int arg, String argName) throws IllegalArgumentException {
		if (!(arg < 0)) {
			throw new IllegalArgumentException(argName + " must be negative; was: " + arg);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is non-negative.
	 */
	public static void checkNegative(long arg, String argName) throws IllegalArgumentException {
		if (!(arg < 0)) {
			throw new IllegalArgumentException(argName + " must be negative; was: " + arg);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is negative.
	 */
	public static void checkNonNegative(int arg, String argName) throws IllegalArgumentException {
		if (arg < 0) {
			throw new IllegalArgumentException(argName + " must be non-negative; was: " + arg);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is negative.
	 */
	public static void checkNonNegative(long arg, String argName) throws IllegalArgumentException {
		if (arg < 0) {
			throw new IllegalArgumentException(argName + " must be non-negative; was: " + arg);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is non-positive.
	 */
	public static void checkPositive(int arg, String argName) throws IllegalArgumentException {
		if (!(arg > 0)) {
			throw new IllegalArgumentException(argName + " must be positive; was: " + arg);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is non-positive.
	 */
	public static void checkPositive(long arg, String argName) throws IllegalArgumentException {
		if (!(arg > 0)) {
			throw new IllegalArgumentException(argName + " must be positive; was: " + arg);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is positive.
	 */
	public static void checkNonPositive(int arg, String argName) throws IllegalArgumentException {
		if (arg > 0) {
			throw new IllegalArgumentException(argName + " must be non-positive; was: " + arg);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is positive.
	 */
	public static void checkNonPositive(long arg, String argName) throws IllegalArgumentException {
		if (arg > 0) {
			throw new IllegalArgumentException(argName + " must be non-positive; was: " + arg);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is zero.
	 */
	public static void checkNonZero(int arg, String argName) throws IllegalArgumentException {
		if (arg == 0) {
			throw new IllegalArgumentException(argName + " must be non-zero");
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is zero.
	 */
	public static void checkNonZero(long arg, String argName) throws IllegalArgumentException {
		if (arg == 0) {
			throw new IllegalArgumentException(argName + " must be non-zero");
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not less than {@code bound}.
	 */
	public static void checkLessThan(int arg, int bound, String argName) {
		if (!(arg < bound)) {
			throw new IllegalArgumentException(argName + " must be less than " + bound + "; was: " + arg);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not less than {@code bound}.
	 */
	public static void checkLessThan(long arg, long bound, String argName) {
		if (!(arg < bound)) {
			throw new IllegalArgumentException(argName + " must be less than " + bound + "; was: " + arg);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not less than {@code bound}.
	 */
	public static void checkLessThan(float arg, float bound, String argName) {
		if (!(arg < bound)) {
			throw new IllegalArgumentException(argName + " must be less than " + bound + "; was: " + arg);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not less than {@code bound}.
	 */
	public static void checkLessThan(double arg, double bound, String argName) {
		if (!(arg < bound)) {
			throw new IllegalArgumentException(argName + " must be less than " + bound + "; was: " + arg);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not greater than or equal to {@code min}.
	 */
	public static void checkAtLeast(int arg, int min, String argName) {
		if (!(arg >= min)) {
			throw new IllegalArgumentException(argName + " must be at least " + min + "; was: " + arg);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not greater than or equal to {@code min}.
	 */
	public static void checkAtLeast(long arg, long min, String argName) {
		if (!(arg >= min)) {
			throw new IllegalArgumentException(argName + " must be at least " + min + "; was: " + arg);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not greater than or equal to {@code min}.
	 */
	public static void checkAtLeast(float arg, float min, String argName) {
		if (!(arg >= min)) {
			throw new IllegalArgumentException(argName + " must be at least " + min + "; was: " + arg);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not greater than or equal to {@code min}.
	 */
	public static void checkAtLeast(double arg, double min, String argName) {
		if (!(arg >= min)) {
			throw new IllegalArgumentException(argName + " must be at least " + min + "; was: " + arg);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not greater than {@code bound}.
	 */
	public static void checkGreaterThan(int arg, int bound, String argName) {
		if (!(arg > bound)) {
			throw new IllegalArgumentException(argName + " must be greater than " + bound + "; was: " + arg);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not greater than {@code bound}.
	 */
	public static void checkGreaterThan(long arg, long bound, String argName) {
		if (!(arg > bound)) {
			throw new IllegalArgumentException(argName + " must be greater than " + bound + "; was: " + arg);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not greater than {@code bound}.
	 */
	public static void checkGreaterThan(float arg, float bound, String argName) {
		if (!(arg > bound)) {
			throw new IllegalArgumentException(argName + " must be greater than " + bound + "; was: " + arg);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not greater than {@code bound}.
	 */
	public static void checkGreaterThan(double arg, double bound, String argName) {
		if (!(arg > bound)) {
			throw new IllegalArgumentException(argName + " must be greater than " + bound + "; was: " + arg);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not less than or equal to {@code max}.
	 */
	public static void checkAtMost(int arg, int max, String argName) {
		if (!(arg <= max)) {
			throw new IllegalArgumentException(argName + " must not exceed " + max + "; was: " + arg);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not less than or equal to {@code max}.
	 */
	public static void checkAtMost(long arg, long max, String argName) {
		if (!(arg <= max)) {
			throw new IllegalArgumentException(argName + " must not exceed " + max + "; was: " + arg);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not less than or equal to {@code max}.
	 */
	public static void checkAtMost(float arg, float max, String argName) {
		if (!(arg <= max)) {
			throw new IllegalArgumentException(argName + " must not exceed " + max + "; was: " + arg);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not less than or equal to {@code max}.
	 */
	public static void checkAtMost(double arg, double max, String argName) {
		if (!(arg <= max)) {
			throw new IllegalArgumentException(argName + " must not exceed " + max + "; was: " + arg);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is {@linkplain Float#isNaN(float) is NaN}.
	 */
	public static void checkNotNaN(float arg, String argName) {
		if (Float.isNaN(arg)) {
			throw new IllegalArgumentException(argName + " must not be NaN");
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is {@linkplain Double#isNaN(double) is NaN}.
	 */
	public static void checkNotNaN(double arg, String argName) {
		if (Double.isNaN(arg)) {
			throw new IllegalArgumentException(argName + " must not be NaN");
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} {@linkplain Float#isNaN(float) is NaN} or
	 * {@linkplain Float#isInfinite(float) is an infinity}.
	 */
	public static void checkFinite(float arg, String argName) {
		if (Float.isNaN(arg) || Float.isInfinite(arg)) {
			throw new IllegalArgumentException(argName + " must be finite; was: " + arg);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is {@linkplain Double#isNaN(double) NaN} or an
	 * {@linkplain Double#isInfinite(double) infinity}.
	 */
	public static void checkFinite(double arg, String argName) {
		if (Double.isNaN(arg) || Double.isInfinite(arg)) {
			throw new IllegalArgumentException(argName + " must be finite; was: " + arg);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not between {@code min} and {@code max}, inclusive.
	 */
	public static void checkInRange(int arg, int min, int max, String argName) {
		if (!(arg >= min && arg <= max)) {
			throw new IllegalArgumentException(argName + " must be in range [" + min + ',' + max + "]; was: " + arg);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not between {@code min} and {@code max}, inclusive.
	 */
	public static void checkInRange(long arg, long min, long max, String argName) {
		if (!(arg >= min && arg <= max)) {
			throw new IllegalArgumentException(argName + " must be in range [" + min + ',' + max + "]; was: " + arg);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not between {@code min} and {@code max}, inclusive.
	 */
	public static void checkInRange(float arg, float min, float max, String argName) {
		if (!(arg >= min && arg <= max)) {
			throw new IllegalArgumentException(argName + " must be in range [" + min + ',' + max + "]; was: " + arg);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not between {@code min} and {@code max}, inclusive.
	 */
	public static void checkInRange(double arg, double min, double max, String argName) {
		if (!(arg >= min && arg <= max)) {
			throw new IllegalArgumentException(argName + " must be in range [" + min + ',' + max + "]; was: " + arg);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code min} is not less than or equal to {@code max}.
	 */
	public static void checkRangeInclusive(int min, int max, String minName, String maxName) {
		if (!(min <= max)) {
			throw new IllegalArgumentException(minName + " must not exceed " + maxName);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code min} is not less than or equal to {@code max}.
	 */
	public static void checkRangeInclusive(long min, long max, String minName, String maxName) {
		if (!(min <= max)) {
			throw new IllegalArgumentException(minName + " must not exceed " + maxName);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code min} is not less than or equal to {@code max}.
	 */
	public static void checkRangeInclusive(float min, float max, String minName, String maxName) {
		if (!(min <= max)) {
			throw new IllegalArgumentException(minName + " must not exceed " + maxName);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code min} is not less than or equal to {@code max}.
	 */
	public static void checkRangeInclusive(double min, double max, String minName, String maxName) {
		if (!(min <= max)) {
			throw new IllegalArgumentException(minName + " must not exceed " + maxName);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code min} is not less than {@code max}.
	 */
	public static void checkRangeExclusive(int min, int max, String minName, String maxName) {
		if (!(min < max)) {
			throw new IllegalArgumentException(minName + " must be less than " + maxName);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code min} is not less than {@code max}.
	 */
	public static void checkRangeExclusive(long min, long max, String minName, String maxName) {
		if (!(min < max)) {
			throw new IllegalArgumentException(minName + " must be less than " + maxName);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code min} is not less than {@code max}.
	 */
	public static void checkRangeExclusive(float min, float max, String minName, String maxName) {
		if (!(min < max)) {
			throw new IllegalArgumentException(minName + " must be less than " + maxName);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code min} is not less than {@code max}.
	 */
	public static void checkRangeExclusive(double min, double max, String minName, String maxName) {
		if (!(min < max)) {
			throw new IllegalArgumentException(minName + " must be less than " + maxName);
		}
	}

	/**
	 * Throws a {@link NullPointerException} if {@code array} is {@code null} or an {@link IndexOutOfBoundsException} if
	 * {@code offset} and {@code length} specify an array slice outside the bounds of {@code array}.
	 */
	public static void checkSlice(boolean[] array, int offset, int length, String argName) {
		if (array == null) {
			throw new NullPointerException(argName);
		}
		checkSlice(array.length, offset, length, argName);
	}

	/**
	 * Throws a {@link NullPointerException} if {@code array} is {@code null} or an {@link IndexOutOfBoundsException} if
	 * {@code offset} and {@code length} specify an array slice outside the bounds of {@code array}.
	 */
	public static void checkSlice(byte[] array, int offset, int length, String argName) {
		if (array == null) {
			throw new NullPointerException(argName);
		}
		checkSlice(array.length, offset, length, argName);
	}

	/**
	 * Throws a {@link NullPointerException} if {@code array} is {@code null} or an {@link IndexOutOfBoundsException} if
	 * {@code offset} and {@code length} specify an array slice outside the bounds of {@code array}.
	 */
	public static void checkSlice(short[] array, int offset, int length, String argName) {
		if (array == null) {
			throw new NullPointerException(argName);
		}
		checkSlice(array.length, offset, length, argName);
	}

	/**
	 * Throws a {@link NullPointerException} if {@code array} is {@code null} or an {@link IndexOutOfBoundsException} if
	 * {@code offset} and {@code length} specify an array slice outside the bounds of {@code array}.
	 */
	public static void checkSlice(int[] array, int offset, int length, String argName) {
		if (array == null) {
			throw new NullPointerException(argName);
		}
		checkSlice(array.length, offset, length, argName);
	}

	/**
	 * Throws a {@link NullPointerException} if {@code array} is {@code null} or an {@link IndexOutOfBoundsException} if
	 * {@code offset} and {@code length} specify an array slice outside the bounds of {@code array}.
	 */
	public static void checkSlice(long[] array, int offset, int length, String argName) {
		if (array == null) {
			throw new NullPointerException(argName);
		}
		checkSlice(array.length, offset, length, argName);
	}

	/**
	 * Throws a {@link NullPointerException} if {@code array} is {@code null} or an {@link IndexOutOfBoundsException} if
	 * {@code offset} and {@code length} specify an array slice outside the bounds of {@code array}.
	 */
	public static void checkSlice(float[] array, int offset, int length, String argName) {
		if (array == null) {
			throw new NullPointerException(argName);
		}
		checkSlice(array.length, offset, length, argName);
	}

	/**
	 * Throws a {@link NullPointerException} if {@code array} is {@code null} or an {@link IndexOutOfBoundsException} if
	 * {@code offset} and {@code length} specify an array slice outside the bounds of {@code array}.
	 */
	public static void checkSlice(double[] array, int offset, int length, String argName) {
		if (array == null) {
			throw new NullPointerException(argName);
		}
		checkSlice(array.length, offset, length, argName);
	}

	/**
	 * Throws a {@link NullPointerException} if {@code array} is {@code null} or an {@link IndexOutOfBoundsException} if
	 * {@code offset} and {@code length} specify an array slice outside the bounds of {@code array}.
	 */
	public static void checkSlice(char[] array, int offset, int length, String argName) {
		if (array == null) {
			throw new NullPointerException(argName);
		}
		checkSlice(array.length, offset, length, argName);
	}

	/**
	 * Throws a {@link NullPointerException} if {@code array} is {@code null} or an {@link IndexOutOfBoundsException} if
	 * {@code offset} and {@code length} specify an array slice outside the bounds of {@code array}.
	 */
	public static void checkSlice(Object[] array, int offset, int length, String argName) {
		if (array == null) {
			throw new NullPointerException(argName);
		}
		checkSlice(array.length, offset, length, argName);
	}

	private static void checkSlice(int arrayLen, int offset, int length, String argName) {
		if (offset < 0) {
			throw new IndexOutOfBoundsException(argName + " slice offset must be non-negative; was: " + offset);
		}
		if (length < 0) {
			throw new IndexOutOfBoundsException(argName + " slice length must be non-negative; was: " + length);
		}
		if (length > arrayLen - offset) {
			throw new IndexOutOfBoundsException(argName + " slice [offset=" + offset + ", length=" + length + "] exceeds array bounds [0," + arrayLen + ')');
		}
	}

}
