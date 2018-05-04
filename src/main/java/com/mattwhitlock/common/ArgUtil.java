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
	 * 
	 * @return {@code arg}
	 */
	public static <T> T[] checkNoneNull(T[] arg, String argName) throws NullPointerException {
		if (arg == null) {
			throw new NullPointerException(argName);
		}
		for (int i = 0; i < arg.length; ++i) {
			if (arg[i] == null) {
				throw new NullPointerException(argName + '[' + i + ']');
			}
		}
		return arg;
	}

	/**
	 * Throws a {@link NullPointerException} if {@code arg} or any element of the specified slice of {@code arg} is
	 * {@code null}. Throws an {@link IndexOutOfBoundsException} if {@code offset} and {@code length} specify an array
	 * slice outside the bounds of {@code arg}.
	 * 
	 * @return {@code arg}
	 */
	public static <T> T[] checkNoneNull(T[] arg, int offset, int length, String argName) throws NullPointerException {
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
		return arg;
	}

	/**
	 * Throws a {@link NullPointerException} if {@code arg} or any element of it is {@code null}.
	 * 
	 * @return {@code arg}
	 */
	public static <T> Iterable<T> checkNoneNull(Iterable<T> arg, String argName) throws NullPointerException {
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
		return arg;
	}

	/**
	 * Throws a {@link NullPointerException} if {@code arg} is {@code null} or an {@link IllegalArgumentException} if
	 * {@code arg} is empty.
	 * 
	 * @return {@code arg}
	 */
	public static boolean[] checkNotEmpty(boolean[] arg, String argName) {
		if (arg == null) {
			throw new NullPointerException(argName);
		}
		if (arg.length == 0) {
			throw new IllegalArgumentException(argName + " must not be empty");
		}
		return arg;
	}

	/**
	 * Throws a {@link NullPointerException} if {@code arg} is {@code null} or an {@link IllegalArgumentException} if
	 * {@code arg} is empty.
	 * 
	 * @return {@code arg}
	 */
	public static byte[] checkNotEmpty(byte[] arg, String argName) {
		if (arg == null) {
			throw new NullPointerException(argName);
		}
		if (arg.length == 0) {
			throw new IllegalArgumentException(argName + " must not be empty");
		}
		return arg;
	}

	/**
	 * Throws a {@link NullPointerException} if {@code arg} is {@code null} or an {@link IllegalArgumentException} if
	 * {@code arg} is empty.
	 * 
	 * @return {@code arg}
	 */
	public static short[] checkNotEmpty(short[] arg, String argName) {
		if (arg == null) {
			throw new NullPointerException(argName);
		}
		if (arg.length == 0) {
			throw new IllegalArgumentException(argName + " must not be empty");
		}
		return arg;
	}

	/**
	 * Throws a {@link NullPointerException} if {@code arg} is {@code null} or an {@link IllegalArgumentException} if
	 * {@code arg} is empty.
	 * 
	 * @return {@code arg}
	 */
	public static int[] checkNotEmpty(int[] arg, String argName) {
		if (arg == null) {
			throw new NullPointerException(argName);
		}
		if (arg.length == 0) {
			throw new IllegalArgumentException(argName + " must not be empty");
		}
		return arg;
	}

	/**
	 * Throws a {@link NullPointerException} if {@code arg} is {@code null} or an {@link IllegalArgumentException} if
	 * {@code arg} is empty.
	 * 
	 * @return {@code arg}
	 */
	public static long[] checkNotEmpty(long[] arg, String argName) {
		if (arg == null) {
			throw new NullPointerException(argName);
		}
		if (arg.length == 0) {
			throw new IllegalArgumentException(argName + " must not be empty");
		}
		return arg;
	}

	/**
	 * Throws a {@link NullPointerException} if {@code arg} is {@code null} or an {@link IllegalArgumentException} if
	 * {@code arg} is empty.
	 * 
	 * @return {@code arg}
	 */
	public static float[] checkNotEmpty(float[] arg, String argName) {
		if (arg == null) {
			throw new NullPointerException(argName);
		}
		if (arg.length == 0) {
			throw new IllegalArgumentException(argName + " must not be empty");
		}
		return arg;
	}

	/**
	 * Throws a {@link NullPointerException} if {@code arg} is {@code null} or an {@link IllegalArgumentException} if
	 * {@code arg} is empty.
	 * 
	 * @return {@code arg}
	 */
	public static double[] checkNotEmpty(double[] arg, String argName) {
		if (arg == null) {
			throw new NullPointerException(argName);
		}
		if (arg.length == 0) {
			throw new IllegalArgumentException(argName + " must not be empty");
		}
		return arg;
	}

	/**
	 * Throws a {@link NullPointerException} if {@code arg} is {@code null} or an {@link IllegalArgumentException} if
	 * {@code arg} is empty.
	 * 
	 * @return {@code arg}
	 */
	public static char[] checkNotEmpty(char[] arg, String argName) {
		if (arg == null) {
			throw new NullPointerException(argName);
		}
		if (arg.length == 0) {
			throw new IllegalArgumentException(argName + " must not be empty");
		}
		return arg;
	}

	/**
	 * Throws a {@link NullPointerException} if {@code arg} is {@code null} or an {@link IllegalArgumentException} if
	 * {@code arg} is empty.
	 * 
	 * @return {@code arg}
	 */
	public static <T> T[] checkNotEmpty(T[] arg, String argName) {
		if (arg == null) {
			throw new NullPointerException(argName);
		}
		if (arg.length == 0) {
			throw new IllegalArgumentException(argName + " must not be empty");
		}
		return arg;
	}

	/**
	 * Throws a {@link NullPointerException} if {@code arg} is {@code null} or an {@link IllegalArgumentException} if
	 * {@code arg} {@linkplain Collection#isEmpty() is empty}.
	 * 
	 * @return {@code arg}
	 */
	public static <T> Collection<T> checkNotEmpty(Collection<T> arg, String argName) {
		if (arg == null) {
			throw new NullPointerException(argName);
		}
		if (arg.isEmpty()) {
			throw new IllegalArgumentException(argName + " must not be empty");
		}
		return arg;
	}

	/**
	 * Throws a {@link NullPointerException} if {@code arg} is {@code null} or an {@link IllegalArgumentException} if
	 * {@code arg} {@linkplain Map#isEmpty() is empty}.
	 * 
	 * @return {@code arg}
	 */
	public static <K, V> Map<K, V> checkNotEmpty(Map<K, V> arg, String argName) {
		if (arg == null) {
			throw new NullPointerException(argName);
		}
		if (arg.isEmpty()) {
			throw new IllegalArgumentException(argName + " must not be empty");
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not negative.
	 * 
	 * @return {@code arg}
	 */
	public static byte checkNegative(byte arg, String argName) throws IllegalArgumentException {
		if (!(arg < 0)) {
			throw new IllegalArgumentException(argName + " must be negative; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not negative.
	 * 
	 * @return {@code arg}
	 */
	public static short checkNegative(short arg, String argName) throws IllegalArgumentException {
		if (!(arg < 0)) {
			throw new IllegalArgumentException(argName + " must be negative; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not negative.
	 * 
	 * @return {@code arg}
	 */
	public static int checkNegative(int arg, String argName) throws IllegalArgumentException {
		if (!(arg < 0)) {
			throw new IllegalArgumentException(argName + " must be negative; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not negative.
	 * 
	 * @return {@code arg}
	 */
	public static long checkNegative(long arg, String argName) throws IllegalArgumentException {
		if (!(arg < 0)) {
			throw new IllegalArgumentException(argName + " must be negative; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not negative.
	 * 
	 * @return {@code arg}
	 */
	public static float checkNegative(float arg, String argName) throws IllegalArgumentException {
		if (!(arg < 0)) {
			throw new IllegalArgumentException(argName + " must be negative; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not negative.
	 * 
	 * @return {@code arg}
	 */
	public static double checkNegative(double arg, String argName) throws IllegalArgumentException {
		if (!(arg < 0)) {
			throw new IllegalArgumentException(argName + " must be negative; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException}.
	 * 
	 * @deprecated A {@code char} can never be negative.
	 */
	@Deprecated
	public static char checkNegative(char arg, String argName) throws IllegalArgumentException {
		throw new IllegalArgumentException(StringUtil.escapeJavaChar(new StringBuilder(argName).append(" must be negative; was: ").append('\''), arg).append('\'').toString());
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is negative.
	 * 
	 * @return {@code arg}
	 */
	public static byte checkNonNegative(byte arg, String argName) throws IllegalArgumentException {
		if (!(arg >= 0)) {
			throw new IllegalArgumentException(argName + " must be non-negative; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is negative.
	 * 
	 * @return {@code arg}
	 */
	public static short checkNonNegative(short arg, String argName) throws IllegalArgumentException {
		if (!(arg >= 0)) {
			throw new IllegalArgumentException(argName + " must be non-negative; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is negative.
	 * 
	 * @return {@code arg}
	 */
	public static int checkNonNegative(int arg, String argName) throws IllegalArgumentException {
		if (!(arg >= 0)) {
			throw new IllegalArgumentException(argName + " must be non-negative; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is negative.
	 * 
	 * @return {@code arg}
	 */
	public static long checkNonNegative(long arg, String argName) throws IllegalArgumentException {
		if (!(arg >= 0)) {
			throw new IllegalArgumentException(argName + " must be non-negative; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is negative or not a number.
	 * 
	 * @return {@code arg}
	 */
	public static float checkNonNegative(float arg, String argName) throws IllegalArgumentException {
		if (!(arg >= 0)) {
			throw new IllegalArgumentException(argName + " must be non-negative; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is negative or not a number.
	 * 
	 * @return {@code arg}
	 */
	public static double checkNonNegative(double arg, String argName) throws IllegalArgumentException {
		if (!(arg >= 0)) {
			throw new IllegalArgumentException(argName + " must be non-negative; was: " + arg);
		}
		return arg;
	}

	/**
	 * Returns {@code arg}.
	 * 
	 * @deprecated A {@code char} is always zero or positive.
	 */
	@Deprecated
	public static char checkNonNegative(char arg, String argName) throws IllegalArgumentException {
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not positive.
	 * 
	 * @return {@code arg}
	 */
	public static byte checkPositive(byte arg, String argName) throws IllegalArgumentException {
		if (!(arg > 0)) {
			throw new IllegalArgumentException(argName + " must be positive; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not positive.
	 * 
	 * @return {@code arg}
	 */
	public static short checkPositive(short arg, String argName) throws IllegalArgumentException {
		if (!(arg > 0)) {
			throw new IllegalArgumentException(argName + " must be positive; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not positive.
	 * 
	 * @return {@code arg}
	 */
	public static int checkPositive(int arg, String argName) throws IllegalArgumentException {
		if (!(arg > 0)) {
			throw new IllegalArgumentException(argName + " must be positive; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not positive.
	 * 
	 * @return {@code arg}
	 */
	public static long checkPositive(long arg, String argName) throws IllegalArgumentException {
		if (!(arg > 0)) {
			throw new IllegalArgumentException(argName + " must be positive; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not positive.
	 * 
	 * @return {@code arg}
	 */
	public static float checkPositive(float arg, String argName) throws IllegalArgumentException {
		if (!(arg > 0)) {
			throw new IllegalArgumentException(argName + " must be positive; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not positive.
	 * 
	 * @return {@code arg}
	 */
	public static double checkPositive(double arg, String argName) throws IllegalArgumentException {
		if (!(arg > 0)) {
			throw new IllegalArgumentException(argName + " must be positive; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not positive.
	 * 
	 * @return {@code arg}
	 * @deprecated Call {@link #checkNonPositive(int, String)} instead.
	 */
	@Deprecated
	public static char checkPositive(char arg, String argName) throws IllegalArgumentException {
		if (!(arg > 0)) {
			throw new IllegalArgumentException(StringUtil.escapeJavaChar(new StringBuilder(argName).append(" must be positive; was: ").append('\''), arg).append('\'').toString());
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is positive.
	 * 
	 * @return {@code arg}
	 */
	public static byte checkNonPositive(byte arg, String argName) throws IllegalArgumentException {
		if (!(arg <= 0)) {
			throw new IllegalArgumentException(argName + " must be non-positive; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is positive.
	 * 
	 * @return {@code arg}
	 */
	public static short checkNonPositive(short arg, String argName) throws IllegalArgumentException {
		if (!(arg <= 0)) {
			throw new IllegalArgumentException(argName + " must be non-positive; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is positive.
	 * 
	 * @return {@code arg}
	 */
	public static int checkNonPositive(int arg, String argName) throws IllegalArgumentException {
		if (!(arg <= 0)) {
			throw new IllegalArgumentException(argName + " must be non-positive; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is positive.
	 * 
	 * @return {@code arg}
	 */
	public static long checkNonPositive(long arg, String argName) throws IllegalArgumentException {
		if (!(arg <= 0)) {
			throw new IllegalArgumentException(argName + " must be non-positive; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is positive or not a number.
	 * 
	 * @return {@code arg}
	 */
	public static float checkNonPositive(float arg, String argName) throws IllegalArgumentException {
		if (!(arg <= 0)) {
			throw new IllegalArgumentException(argName + " must be non-positive; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is positive or not a number.
	 * 
	 * @return {@code arg}
	 */
	public static double checkNonPositive(double arg, String argName) throws IllegalArgumentException {
		if (!(arg <= 0)) {
			throw new IllegalArgumentException(argName + " must be non-positive; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is positive.
	 * 
	 * @return {@code arg}
	 * @deprecated Call {@link #checkZero(char, String)} instead.
	 */
	@Deprecated
	public static char checkNonPositive(char arg, String argName) throws IllegalArgumentException {
		if (!(arg <= 0)) {
			throw new IllegalArgumentException(StringUtil.escapeJavaChar(new StringBuilder(argName).append(" must be non-positive; was: ").append('\''), arg).append('\'').toString());
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not zero.
	 * 
	 * @return {@code arg}
	 */
	public static byte checkZero(byte arg, String argName) throws IllegalArgumentException {
		if (arg != 0) {
			throw new IllegalArgumentException(argName + " must be zero; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not zero.
	 * 
	 * @return {@code arg}
	 */
	public static short checkZero(short arg, String argName) throws IllegalArgumentException {
		if (arg != 0) {
			throw new IllegalArgumentException(argName + " must be zero; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not zero.
	 * 
	 * @return {@code arg}
	 */
	public static int checkZero(int arg, String argName) throws IllegalArgumentException {
		if (arg != 0) {
			throw new IllegalArgumentException(argName + " must be zero; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not zero.
	 * 
	 * @return {@code arg}
	 */
	public static long checkZero(long arg, String argName) throws IllegalArgumentException {
		if (arg != 0) {
			throw new IllegalArgumentException(argName + " must be zero; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not zero.
	 * 
	 * @return {@code arg}
	 */
	public static float checkZero(float arg, String argName) throws IllegalArgumentException {
		if (arg != 0) {
			throw new IllegalArgumentException(argName + " must be zero; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not zero.
	 * 
	 * @return {@code arg}
	 */
	public static double checkZero(double arg, String argName) throws IllegalArgumentException {
		if (arg != 0) {
			throw new IllegalArgumentException(argName + " must be zero; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not zero.
	 * 
	 * @return {@code arg}
	 */
	public static char checkZero(char arg, String argName) throws IllegalArgumentException {
		if (arg != 0) {
			throw new IllegalArgumentException(StringUtil.escapeJavaChar(new StringBuilder(argName).append(" must be zero; was: ").append('\''), arg).append('\'').toString());
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is zero.
	 * 
	 * @return {@code arg}
	 */
	public static byte checkNonZero(byte arg, String argName) throws IllegalArgumentException {
		if (arg == 0) {
			throw new IllegalArgumentException(argName + " must be non-zero");
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is zero.
	 * 
	 * @return {@code arg}
	 */
	public static short checkNonZero(short arg, String argName) throws IllegalArgumentException {
		if (arg == 0) {
			throw new IllegalArgumentException(argName + " must be non-zero");
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is zero.
	 * 
	 * @return {@code arg}
	 */
	public static int checkNonZero(int arg, String argName) throws IllegalArgumentException {
		if (arg == 0) {
			throw new IllegalArgumentException(argName + " must be non-zero");
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is zero.
	 * 
	 * @return {@code arg}
	 */
	public static long checkNonZero(long arg, String argName) throws IllegalArgumentException {
		if (arg == 0) {
			throw new IllegalArgumentException(argName + " must be non-zero");
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is zero or not a number.
	 * 
	 * @return {@code arg}
	 */
	public static float checkNonZero(float arg, String argName) throws IllegalArgumentException {
		if (arg == 0 || Float.isNaN(arg)) {
			throw new IllegalArgumentException(argName + " must be non-zero");
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is zero or not a number.
	 * 
	 * @return {@code arg}
	 */
	public static double checkNonZero(double arg, String argName) throws IllegalArgumentException {
		if (arg == 0 || Double.isNaN(arg)) {
			throw new IllegalArgumentException(argName + " must be non-zero");
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is zero.
	 * 
	 * @return {@code arg}
	 */
	public static char checkNonZero(char arg, String argName) throws IllegalArgumentException {
		if (arg == 0) {
			throw new IllegalArgumentException(argName + " must be non-zero");
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not less than {@code bound}.
	 * 
	 * @return {@code arg}
	 */
	public static byte checkLessThan(byte arg, byte bound, String argName) {
		if (!(arg < bound)) {
			throw new IllegalArgumentException(argName + " must be less than " + bound + "; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not less than {@code bound}.
	 * 
	 * @return {@code arg}
	 */
	public static short checkLessThan(short arg, short bound, String argName) {
		if (!(arg < bound)) {
			throw new IllegalArgumentException(argName + " must be less than " + bound + "; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not less than {@code bound}.
	 * 
	 * @return {@code arg}
	 */
	public static int checkLessThan(int arg, int bound, String argName) {
		if (!(arg < bound)) {
			throw new IllegalArgumentException(argName + " must be less than " + bound + "; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not less than {@code bound}.
	 * 
	 * @return {@code arg}
	 */
	public static long checkLessThan(long arg, long bound, String argName) {
		if (!(arg < bound)) {
			throw new IllegalArgumentException(argName + " must be less than " + bound + "; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not less than {@code bound}.
	 * 
	 * @return {@code arg}
	 */
	public static float checkLessThan(float arg, float bound, String argName) {
		if (!(arg < bound)) {
			throw new IllegalArgumentException(argName + " must be less than " + bound + "; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not less than {@code bound}.
	 * 
	 * @return {@code arg}
	 */
	public static double checkLessThan(double arg, double bound, String argName) {
		if (!(arg < bound)) {
			throw new IllegalArgumentException(argName + " must be less than " + bound + "; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not less than {@code bound}.
	 * 
	 * @return {@code arg}
	 */
	public static char checkLessThan(char arg, char bound, String argName) {
		if (!(arg < bound)) {
			throw new IllegalArgumentException(StringUtil.escapeJavaChar(StringUtil.escapeJavaChar(new StringBuilder(argName).append(" must be less than ").append('\''), bound).append('\'').append("; was: ").append('\''), arg).append('\'').toString());
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not greater than or equal to {@code min}.
	 * 
	 * @return {@code arg}
	 */
	public static byte checkAtLeast(byte arg, byte min, String argName) {
		if (!(arg >= min)) {
			throw new IllegalArgumentException(argName + " must be at least " + min + "; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not greater than or equal to {@code min}.
	 * 
	 * @return {@code arg}
	 */
	public static short checkAtLeast(short arg, short min, String argName) {
		if (!(arg >= min)) {
			throw new IllegalArgumentException(argName + " must be at least " + min + "; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not greater than or equal to {@code min}.
	 * 
	 * @return {@code arg}
	 */
	public static int checkAtLeast(int arg, int min, String argName) {
		if (!(arg >= min)) {
			throw new IllegalArgumentException(argName + " must be at least " + min + "; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not greater than or equal to {@code min}.
	 * 
	 * @return {@code arg}
	 */
	public static long checkAtLeast(long arg, long min, String argName) {
		if (!(arg >= min)) {
			throw new IllegalArgumentException(argName + " must be at least " + min + "; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not greater than or equal to {@code min}.
	 * 
	 * @return {@code arg}
	 */
	public static float checkAtLeast(float arg, float min, String argName) {
		if (!(arg >= min)) {
			throw new IllegalArgumentException(argName + " must be at least " + min + "; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not greater than or equal to {@code min}.
	 * 
	 * @return {@code arg}
	 */
	public static double checkAtLeast(double arg, double min, String argName) {
		if (!(arg >= min)) {
			throw new IllegalArgumentException(argName + " must be at least " + min + "; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not greater than or equal to {@code min}.
	 * 
	 * @return {@code arg}
	 */
	public static char checkAtLeast(char arg, char min, String argName) {
		if (!(arg >= min)) {
			throw new IllegalArgumentException(StringUtil.escapeJavaChar(StringUtil.escapeJavaChar(new StringBuilder(argName).append(" must be at least ").append('\''), min).append('\'').append("; was: ").append('\''), arg).append('\'').toString());
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not greater than {@code bound}.
	 * 
	 * @return {@code arg}
	 */
	public static byte checkGreaterThan(byte arg, byte bound, String argName) {
		if (!(arg > bound)) {
			throw new IllegalArgumentException(argName + " must be greater than " + bound + "; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not greater than {@code bound}.
	 * 
	 * @return {@code arg}
	 */
	public static short checkGreaterThan(short arg, short bound, String argName) {
		if (!(arg > bound)) {
			throw new IllegalArgumentException(argName + " must be greater than " + bound + "; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not greater than {@code bound}.
	 * 
	 * @return {@code arg}
	 */
	public static int checkGreaterThan(int arg, int bound, String argName) {
		if (!(arg > bound)) {
			throw new IllegalArgumentException(argName + " must be greater than " + bound + "; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not greater than {@code bound}.
	 * 
	 * @return {@code arg}
	 */
	public static long checkGreaterThan(long arg, long bound, String argName) {
		if (!(arg > bound)) {
			throw new IllegalArgumentException(argName + " must be greater than " + bound + "; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not greater than {@code bound}.
	 * 
	 * @return {@code arg}
	 */
	public static float checkGreaterThan(float arg, float bound, String argName) {
		if (!(arg > bound)) {
			throw new IllegalArgumentException(argName + " must be greater than " + bound + "; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not greater than {@code bound}.
	 * 
	 * @return {@code arg}
	 */
	public static double checkGreaterThan(double arg, double bound, String argName) {
		if (!(arg > bound)) {
			throw new IllegalArgumentException(argName + " must be greater than " + bound + "; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not greater than {@code bound}.
	 * 
	 * @return {@code arg}
	 */
	public static char checkGreaterThan(char arg, char bound, String argName) {
		if (!(arg > bound)) {
			throw new IllegalArgumentException(StringUtil.escapeJavaChar(StringUtil.escapeJavaChar(new StringBuilder(argName).append(" must be greater than ").append('\''), bound).append('\'').append("; was: ").append('\''), arg).append('\'').toString());
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not less than or equal to {@code max}.
	 * 
	 * @return {@code arg}
	 */
	public static byte checkAtMost(byte arg, byte max, String argName) {
		if (!(arg <= max)) {
			throw new IllegalArgumentException(argName + " must not exceed " + max + "; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not less than or equal to {@code max}.
	 * 
	 * @return {@code arg}
	 */
	public static short checkAtMost(short arg, short max, String argName) {
		if (!(arg <= max)) {
			throw new IllegalArgumentException(argName + " must not exceed " + max + "; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not less than or equal to {@code max}.
	 * 
	 * @return {@code arg}
	 */
	public static int checkAtMost(int arg, int max, String argName) {
		if (!(arg <= max)) {
			throw new IllegalArgumentException(argName + " must not exceed " + max + "; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not less than or equal to {@code max}.
	 * 
	 * @return {@code arg}
	 */
	public static long checkAtMost(long arg, long max, String argName) {
		if (!(arg <= max)) {
			throw new IllegalArgumentException(argName + " must not exceed " + max + "; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not less than or equal to {@code max}.
	 * 
	 * @return {@code arg}
	 */
	public static float checkAtMost(float arg, float max, String argName) {
		if (!(arg <= max)) {
			throw new IllegalArgumentException(argName + " must not exceed " + max + "; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not less than or equal to {@code max}.
	 * 
	 * @return {@code arg}
	 */
	public static double checkAtMost(double arg, double max, String argName) {
		if (!(arg <= max)) {
			throw new IllegalArgumentException(argName + " must not exceed " + max + "; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not less than or equal to {@code max}.
	 * 
	 * @return {@code arg}
	 */
	public static char checkAtMost(char arg, char max, String argName) {
		if (!(arg <= max)) {
			throw new IllegalArgumentException(StringUtil.escapeJavaChar(StringUtil.escapeJavaChar(new StringBuilder(argName).append(" must not exceed ").append('\''), max).append('\'').append("; was: ").append('\''), arg).append('\'').toString());
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is {@linkplain Float#isNaN(float) is NaN}.
	 * 
	 * @return {@code arg}
	 */
	public static float checkNotNaN(float arg, String argName) {
		if (Float.isNaN(arg)) {
			throw new IllegalArgumentException(argName + " must not be NaN");
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is {@linkplain Double#isNaN(double) is NaN}.
	 * 
	 * @return {@code arg}
	 */
	public static double checkNotNaN(double arg, String argName) {
		if (Double.isNaN(arg)) {
			throw new IllegalArgumentException(argName + " must not be NaN");
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not {@linkplain Float#isFinite(float) finite}.
	 * 
	 * @return {@code arg}
	 */
	public static float checkFinite(float arg, String argName) {
		if (!Float.isFinite(arg)) {
			throw new IllegalArgumentException(argName + " must be finite; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not {@linkplain Double#isFinite(double) finite}.
	 * 
	 * @return {@code arg}
	 */
	public static double checkFinite(double arg, String argName) {
		if (!Double.isFinite(arg)) {
			throw new IllegalArgumentException(argName + " must be finite; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not between {@code min} and {@code max}, inclusive.
	 * 
	 * @return {@code arg}
	 */
	public static byte checkInRange(byte arg, byte min, byte max, String argName) {
		if (!(arg >= min && arg <= max)) {
			throw new IllegalArgumentException(argName + " must be in range [" + min + ',' + max + "]; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not between {@code min} and {@code max}, inclusive.
	 * 
	 * @return {@code arg}
	 */
	public static short checkInRange(short arg, short min, short max, String argName) {
		if (!(arg >= min && arg <= max)) {
			throw new IllegalArgumentException(argName + " must be in range [" + min + ',' + max + "]; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not between {@code min} and {@code max}, inclusive.
	 * 
	 * @return {@code arg}
	 */
	public static int checkInRange(int arg, int min, int max, String argName) {
		if (!(arg >= min && arg <= max)) {
			throw new IllegalArgumentException(argName + " must be in range [" + min + ',' + max + "]; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not between {@code min} and {@code max}, inclusive.
	 * 
	 * @return {@code arg}
	 */
	public static long checkInRange(long arg, long min, long max, String argName) {
		if (!(arg >= min && arg <= max)) {
			throw new IllegalArgumentException(argName + " must be in range [" + min + ',' + max + "]; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not between {@code min} and {@code max}, inclusive.
	 * 
	 * @return {@code arg}
	 */
	public static float checkInRange(float arg, float min, float max, String argName) {
		if (!(arg >= min && arg <= max)) {
			throw new IllegalArgumentException(argName + " must be in range [" + min + ',' + max + "]; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not between {@code min} and {@code max}, inclusive.
	 * 
	 * @return {@code arg}
	 */
	public static double checkInRange(double arg, double min, double max, String argName) {
		if (!(arg >= min && arg <= max)) {
			throw new IllegalArgumentException(argName + " must be in range [" + min + ',' + max + "]; was: " + arg);
		}
		return arg;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if {@code arg} is not between {@code min} and {@code max}, inclusive.
	 * 
	 * @return {@code arg}
	 */
	public static char checkInRange(char arg, char min, char max, String argName) {
		if (!(arg >= min && arg <= max)) {
			throw new IllegalArgumentException(StringUtil.escapeJavaChar(StringUtil.escapeJavaChar(StringUtil.escapeJavaChar(new StringBuilder(argName).append(" must be in range [").append('\''), min).append('\'').append(',').append('\''), max).append('\'').append("]; was: ").append('\''), arg).append('\'').toString());
		}
		return arg;
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
