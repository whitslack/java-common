package com.mattwhitlock.common;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Matt Whitlock
 * @author Adam Leko
 */
public final class StringUtil {

	private static final Pattern integerPattern = Pattern.compile("-?\\d+");
	private static final Pattern floatPattern = Pattern.compile("\\s*[-+]?(?:NaN|Infinity|(?:\\d+\\.\\d*|\\.?\\d+)(?:e[-+]?\\d+)?[fd]?|0x(?:\\p{XDigit}+\\.\\p{XDigit}*|\\.?\\p{XDigit}+)(?:p[-+]?\\d+)?[fd]?)\\s*", Pattern.CASE_INSENSITIVE);

	/**
	 * Not instantiable.
	 */
	private StringUtil() {
	}

	/**
	 * Returns {@code true} iff the given string consists only of {@linkplain Character#isDigit(int) digit} code points.
	 * Returns {@code false} if the given string is empty.
	 */
	public static boolean isDigits(String str) {
		int length = str.length();
		if (length == 0) {
			return false;
		}
		for (int index = 0; index < length;) {
			int cp = str.codePointAt(index);
			if (!Character.isDigit(cp)) {
				return false;
			}
			index += Character.charCount(cp);
		}
		return true;
	}

	/**
	 * Returns whether the given string consists solely of one or more decimal digits, optionally preceded by a
	 * hyphen-minus (U+002D).
	 */
	public static boolean isInteger(String str) {
		return integerPattern.matcher(str).matches();
	}

	/**
	 * Returns whether the given string could be parsed by {@link Double#parseDouble(String)} without causing an
	 * exception to be thrown.
	 */
	public static boolean isFloat(String str) {
		return floatPattern.matcher(str).matches();
	}

	/**
	 * Returns {@code true} iff the given string consists only of {@linkplain Character#isWhitespace(int) whitespace}
	 * code points. Returns {@code false} if the given string is empty.
	 */
	public static boolean isWhitespace(String str) {
		int length = str.length();
		if (length == 0) {
			return false;
		}
		for (int index = 0; index < length;) {
			int cp = str.codePointAt(index);
			if (!Character.isWhitespace(cp)) {
				return false;
			}
			index += Character.charCount(cp);
		}
		return true;
	}

	/**
	 * Returns a copy of the given string with its first code point (after any initial opening punctuation) converted to
	 * title case. Returns the same string instance if its first code point is already in title case.
	 */
	public static String capitalize(String string) {
		int length = string.length(), codePoint, start = 0;
		for (;;) {
			if (start >= length) {
				return string;
			}
			if (Character.getType(codePoint = string.codePointAt(start)) != Character.START_PUNCTUATION) {
				break;
			}
			start += Character.charCount(codePoint);
		}
		int newCodePoint = Character.toTitleCase(codePoint);
		if (newCodePoint == codePoint) {
			return string;
		}
		// assumes toTitleCase(codePoint) is same number of chars as codePoint
		char[] chars = string.toCharArray();
		Character.toChars(newCodePoint, chars, start);
		return new String(chars);
	}

	/**
	 * Returns a copy of the given string with the specified code point appended at the end, but only if the specified
	 * code point is not already at a position prior to any trailing punctuation. Otherwise, returns the same string
	 * instance.
	 */
	public static String terminate(String string, int cp) {
		int length = string.length(), codePoint, type;
		for (int end = length; end > 0; end -= Character.charCount(codePoint)) {
			if ((codePoint = string.codePointBefore(end)) == cp) {
				return string;
			}
			if ((type = Character.getType(codePoint)) != Character.END_PUNCTUATION && type != Character.OTHER_PUNCTUATION && type != Character.FINAL_QUOTE_PUNCTUATION) {
				break;
			}
		}
		char[] chars = new char[length + Character.charCount(cp)];
		string.getChars(0, length, chars, 0);
		Character.toChars(cp, chars, length);
		return new String(chars);
	}

	/**
	 * Combines the {@link #capitalize(String)} and {@link #terminate(String, int)} functions efficiently.
	 */
	public static String capitalizeAndTerminate(String string, int cp) {
		int length = string.length(), codePoint, type;
		for (int end = length; end > 0; end -= Character.charCount(codePoint)) {
			if ((codePoint = string.codePointBefore(end)) == cp) {
				return capitalize(string);
			}
			if ((type = Character.getType(codePoint)) != Character.END_PUNCTUATION && type != Character.OTHER_PUNCTUATION && type != Character.FINAL_QUOTE_PUNCTUATION) {
				break;
			}
		}
		char[] chars = new char[length + Character.charCount(cp)];
		string.getChars(0, length, chars, 0);
		Character.toChars(cp, chars, length);
		// capitalize
		int start = 0;
		for (;;) {
			if (start >= length) {
				return new String(chars);
			}
			if (Character.getType(codePoint = string.codePointAt(start)) != Character.START_PUNCTUATION) {
				break;
			}
			start += Character.charCount(codePoint);
		}
		Character.toChars(Character.toTitleCase(codePoint), chars, start);
		return new String(chars);
	}

	/**
	 * Limits a string to a maximum number of code points. If the string is longer, it will be truncated, and the last
	 * code point will be replaced with an ellipsis (U+2026).
	 */
	public static String limit(String string, int maxCount) {
		return limit(string, maxCount, '\u2026');
	}

	/**
	 * Limits a string to a maximum number of code points. If the string is longer, it will be truncated, and the last
	 * code point will be replaced with the given code point.
	 */
	public static String limit(String string, int maxCount, int limitCodePoint) {
		ArgUtil.checkNonNegative(maxCount, "maxCount");
		if (string == null) {
			return null;
		}
		int count = string.codePointCount(0, string.length());
		if (count <= maxCount) {
			return string;
		}
		switch (maxCount) {
			case 0:
				return "";
			case 1:
				return new String(Character.toChars(limitCodePoint));
			default:
				int offset = Character.offsetByCodePoints(string, 0, maxCount - 1);
				return new StringBuilder(offset + Character.charCount(limitCodePoint)).append(string.substring(0, offset)).appendCodePoint(limitCodePoint).toString();
		}
	}

	/**
	 * Pads a string on the left, if necessary, to expand it to the specified minimum number of code points.
	 */
	public static String padLeft(String string, int minCount, int padCodePoint) {
		int pad = minCount - string.codePointCount(0, string.length());
		if (pad <= 0) {
			return string;
		}
		StringBuilder sb = new StringBuilder(pad * Character.charCount(padCodePoint) + string.length());
		do {
			sb.appendCodePoint(padCodePoint);
		} while (--pad > 0);
		return sb.append(string).toString();
	}

	/**
	 * Pads a string on the right, if necessary, to expand it to the specified minimum number of code points.
	 */
	public static String padRight(String string, int minCount, int padCodePoint) {
		int pad = minCount - string.codePointCount(0, string.length());
		if (pad <= 0) {
			return string;
		}
		StringBuilder sb = new StringBuilder(string.length() + pad * Character.charCount(padCodePoint)).append(string);
		do {
			sb.appendCodePoint(padCodePoint);
		} while (--pad > 0);
		return sb.toString();
	}

	public static StringBuilder debugToString(StringBuilder sb, CharSequence string) {
		return string == null ? sb.append((String) null) : escapeJavaString(sb.append('"'), string).append('"');
	}

	public static StringBuilder debugToString(StringBuilder sb, Iterable<?> iterable) {
		if (iterable == null) {
			return sb.append((String) null);
		}
		sb.append('[');
		Iterator<?> it = iterable.iterator();
		if (it.hasNext()) {
			debugToString(sb, it.next());
			while (it.hasNext()) {
				debugToString(sb.append(", "), it.next());
			}
		}
		return sb.append(']');
	}

	public static StringBuilder debugToString(StringBuilder sb, Map<?, ?> map) {
		if (map == null) {
			return sb.append((String) null);
		}
		sb.append('{');
		Iterator<? extends Map.Entry<?, ?>> it = map.entrySet().iterator();
		if (it.hasNext()) {
			Map.Entry<?, ?> entry = it.next();
			debugToString(debugToString(sb, entry.getKey()).append('='), entry.getValue());
			while (it.hasNext()) {
				entry = it.next();
				debugToString(debugToString(sb.append(", "), entry.getKey()).append('='), entry.getValue());
			}
		}
		return sb.append('}');
	}

	public static StringBuilder debugToString(StringBuilder sb, boolean[] array) {
		return array == null ? sb.append((String) null) : debugToString(sb, array, 0, array.length);
	}

	public static StringBuilder debugToString(StringBuilder sb, byte[] array) {
		return array == null ? sb.append((String) null) : debugToString(sb, array, 0, array.length);
	}

	public static StringBuilder debugToString(StringBuilder sb, short[] array) {
		return array == null ? sb.append((String) null) : debugToString(sb, array, 0, array.length);
	}

	public static StringBuilder debugToString(StringBuilder sb, int[] array) {
		return array == null ? sb.append((String) null) : debugToString(sb, array, 0, array.length);
	}

	public static StringBuilder debugToString(StringBuilder sb, long[] array) {
		return array == null ? sb.append((String) null) : debugToString(sb, array, 0, array.length);
	}

	public static StringBuilder debugToString(StringBuilder sb, float[] array) {
		return array == null ? sb.append((String) null) : debugToString(sb, array, 0, array.length);
	}

	public static StringBuilder debugToString(StringBuilder sb, double[] array) {
		return array == null ? sb.append((String) null) : debugToString(sb, array, 0, array.length);
	}

	public static StringBuilder debugToString(StringBuilder sb, char[] array) {
		return array == null ? sb.append((String) null) : debugToString(sb, array, 0, array.length);
	}

	public static StringBuilder debugToString(StringBuilder sb, CharSequence[] array) {
		return array == null ? sb.append((String) null) : debugToString(sb, array, 0, array.length);
	}

	public static StringBuilder debugToString(StringBuilder sb, Object[] array) {
		return array == null ? sb.append((String) null) : debugToString(sb, array, 0, array.length);
	}

	public static StringBuilder debugToString(StringBuilder sb, boolean[] array, int offset, int length) {
		if (array == null) {
			return sb.append((String) null);
		}
		sb.append('[');
		if (length > 0) {
			length += offset;
			sb.append(array[offset]);
			while (++offset < length) {
				sb.append(", ").append(array[offset]);
			}
		}
		return sb.append(']');
	}

	public static StringBuilder debugToString(StringBuilder sb, byte[] array, int offset, int length) {
		if (array == null) {
			return sb.append((String) null);
		}
		return toHex(sb.append("0x"), array, offset, length);
	}

	public static StringBuilder debugToString(StringBuilder sb, short[] array, int offset, int length) {
		if (array == null) {
			return sb.append((String) null);
		}
		sb.append('[');
		if (length > 0) {
			length += offset;
			sb.append(array[offset]);
			while (++offset < length) {
				sb.append(", ").append(array[offset]);
			}
		}
		return sb.append(']');
	}

	public static StringBuilder debugToString(StringBuilder sb, int[] array, int offset, int length) {
		if (array == null) {
			return sb.append((String) null);
		}
		sb.append('[');
		if (length > 0) {
			length += offset;
			sb.append(array[offset]);
			while (++offset < length) {
				sb.append(", ").append(array[offset]);
			}
		}
		return sb.append(']');
	}

	public static StringBuilder debugToString(StringBuilder sb, long[] array, int offset, int length) {
		if (array == null) {
			return sb.append((String) null);
		}
		sb.append('[');
		if (length > 0) {
			length += offset;
			sb.append(array[offset]).append('L');
			while (++offset < length) {
				sb.append(", ").append(array[offset]).append('L');
			}
		}
		return sb.append(']');
	}

	public static StringBuilder debugToString(StringBuilder sb, float[] array, int offset, int length) {
		if (array == null) {
			return sb.append((String) null);
		}
		sb.append('[');
		if (length > 0) {
			length += offset;
			sb.append(array[offset]).append('f');
			while (++offset < length) {
				sb.append(", ").append(array[offset]).append('f');
			}
		}
		return sb.append(']');
	}

	public static StringBuilder debugToString(StringBuilder sb, double[] array, int offset, int length) {
		if (array == null) {
			return sb.append((String) null);
		}
		sb.append('[');
		if (length > 0) {
			length += offset;
			sb.append(array[offset]);
			while (++offset < length) {
				sb.append(", ").append(array[offset]);
			}
		}
		return sb.append(']');
	}

	public static StringBuilder debugToString(StringBuilder sb, char[] array, int offset, int length) {
		if (array == null) {
			return sb.append((String) null);
		}
		return escapeJavaString(sb.append('"'), array, offset, length).append('"');
	}

	public static StringBuilder debugToString(StringBuilder sb, CharSequence[] array, int offset, int length) {
		if (array == null) {
			return sb.append((String) null);
		}
		sb.append('[');
		if (length > 0) {
			length += offset;
			debugToString(sb, array[offset]);
			while (++offset < length) {
				debugToString(sb.append(", "), array[offset]);
			}
		}
		return sb.append(']');
	}

	public static StringBuilder debugToString(StringBuilder sb, Object[] array, int offset, int length) {
		if (array == null) {
			return sb.append((String) null);
		}
		if (array instanceof String[]) {
			return debugToString(sb, (String[]) array, offset, length);
		}
		sb.append('[');
		if (length > 0) {
			length += offset;
			debugToString(sb, array[offset]);
			while (++offset < length) {
				debugToString(sb.append(", "), array[offset]);
			}
		}
		return sb.append(']');
	}

	public static StringBuilder debugToString(StringBuilder sb, Object o) {
		if (o == null) {
			return sb.append((String) null);
		}
		if (o instanceof CharSequence) {
			return debugToString(sb, (CharSequence) o);
		}
		if (o instanceof Iterable<?>) {
			return debugToString(sb, (Iterable<?>) o);
		}
		if (o instanceof Map<?, ?>) {
			return debugToString(sb, (Map<?, ?>) o);
		}
		Class<? extends Object> clazz = o.getClass();
		if (clazz.isArray()) {
			if (o instanceof Object[]) {
				return debugToString(sb, (Object[]) o);
			}
			if (o instanceof boolean[]) {
				return debugToString(sb, (boolean[]) o);
			}
			if (o instanceof byte[]) {
				return debugToString(sb, (byte[]) o);
			}
			if (o instanceof short[]) {
				return debugToString(sb, (short[]) o);
			}
			if (o instanceof int[]) {
				return debugToString(sb, (int[]) o);
			}
			if (o instanceof long[]) {
				return debugToString(sb, (long[]) o);
			}
			if (o instanceof float[]) {
				return debugToString(sb, (float[]) o);
			}
			if (o instanceof double[]) {
				return debugToString(sb, (double[]) o);
			}
			if (o instanceof char[]) {
				return debugToString(sb, (char[]) o);
			}
		}
		if (PrimitiveType.of(clazz) != null) {
			if (o instanceof Long) {
				return sb.append(o).append('L');
			}
			if (o instanceof Float) {
				return sb.append(o).append('f');
			}
			if (o instanceof Character) {
				return escapeJavaChar(sb.append('\''), ((Character) o).charValue()).append('\'');
			}
			return sb.append(o);
		}
		String str = o.toString();
		String className = clazz.getName(), shortClassName = ClassUtil.getShortClassName(clazz);
		if (str.startsWith(className)) {
			return sb.append(shortClassName).append(str.substring(className.length()));
		}
		if (str.startsWith(shortClassName)) {
			return sb.append(str);
		}
		return sb.append('(').append(shortClassName).append(") ").append(str);
	}

	public static String debugToString(CharSequence string) {
		return debugToString(new StringBuilder(), string).toString();
	}

	public static String debugToString(Iterable<?> iterable) {
		return debugToString(new StringBuilder(), iterable).toString();
	}

	public static String debugToString(Map<?, ?> map) {
		return debugToString(new StringBuilder(), map).toString();
	}

	public static String debugToString(boolean[] array) {
		return debugToString(new StringBuilder(), array).toString();
	}

	public static String debugToString(byte[] array) {
		return debugToString(new StringBuilder(), array).toString();
	}

	public static String debugToString(short[] array) {
		return debugToString(new StringBuilder(), array).toString();
	}

	public static String debugToString(int[] array) {
		return debugToString(new StringBuilder(), array).toString();
	}

	public static String debugToString(long[] array) {
		return debugToString(new StringBuilder(), array).toString();
	}

	public static String debugToString(float[] array) {
		return debugToString(new StringBuilder(), array).toString();
	}

	public static String debugToString(double[] array) {
		return debugToString(new StringBuilder(), array).toString();
	}

	public static String debugToString(char[] array) {
		return debugToString(new StringBuilder(), array).toString();
	}

	public static String debugToString(CharSequence[] array) {
		return debugToString(new StringBuilder(), array).toString();
	}

	public static String debugToString(Object[] array) {
		return debugToString(new StringBuilder(), array).toString();
	}

	public static String debugToString(boolean[] array, int offset, int length) {
		return debugToString(new StringBuilder(), array, offset, length).toString();
	}

	public static String debugToString(byte[] array, int offset, int length) {
		return debugToString(new StringBuilder(), array, offset, length).toString();
	}

	public static String debugToString(short[] array, int offset, int length) {
		return debugToString(new StringBuilder(), array, offset, length).toString();
	}

	public static String debugToString(int[] array, int offset, int length) {
		return debugToString(new StringBuilder(), array, offset, length).toString();
	}

	public static String debugToString(long[] array, int offset, int length) {
		return debugToString(new StringBuilder(), array, offset, length).toString();
	}

	public static String debugToString(float[] array, int offset, int length) {
		return debugToString(new StringBuilder(), array, offset, length).toString();
	}

	public static String debugToString(double[] array, int offset, int length) {
		return debugToString(new StringBuilder(), array, offset, length).toString();
	}

	public static String debugToString(char[] array, int offset, int length) {
		return debugToString(new StringBuilder(), array, offset, length).toString();
	}

	public static String debugToString(CharSequence[] array, int offset, int length) {
		return debugToString(new StringBuilder(), array, offset, length).toString();
	}

	public static String debugToString(Object[] array, int offset, int length) {
		return debugToString(new StringBuilder(), array, offset, length).toString();
	}

	public static String debugToString(Object o) {
		return debugToString(new StringBuilder(), o).toString();
	}

	public static StringBuilder join(StringBuilder sb, Object[] array, int offset, int length, char glue) {
		if (length > 0) {
			length += offset;
			sb.append(array[offset++]);
			while (offset < length) {
				sb.append(glue).append(array[offset++]);
			}
		}
		return sb;
	}

	public static StringBuilder join(StringBuilder sb, Object[] array, int offset, int length, Object glue) {
		if (glue == null) {
			length += offset;
			while (offset < length) {
				sb.append(array[offset++]);
			}
		}
		else if (length > 0) {
			length += offset;
			sb.append(array[offset++]);
			while (offset < length) {
				sb.append(glue).append(array[offset++]);
			}
		}
		return sb;
	}

	public static StringBuilder join(StringBuilder sb, Object[] array, char glue) {
		return join(sb, array, 0, array.length, glue);
	}

	public static StringBuilder join(StringBuilder sb, Object[] array, Object glue) {
		return join(sb, array, 0, array.length, glue);
	}

	public static StringBuilder join(StringBuilder sb, Iterator<?> iterator, char glue) {
		if (iterator.hasNext()) {
			sb.append(iterator.next());
			while (iterator.hasNext()) {
				sb.append(glue).append(iterator.next());
			}
		}
		return sb;
	}

	public static StringBuilder join(StringBuilder sb, Iterator<?> iterator, Object glue) {
		if (glue == null) {
			while (iterator.hasNext()) {
				sb.append(iterator.next());
			}
		}
		else {
			if (iterator.hasNext()) {
				sb.append(iterator.next());
				while (iterator.hasNext()) {
					sb.append(glue).append(iterator.next());
				}
			}
		}
		return sb;
	}

	public static StringBuilder join(StringBuilder sb, Iterable<?> iterable, char glue) {
		return join(sb, iterable.iterator(), glue);
	}

	public static StringBuilder join(StringBuilder sb, Iterable<?> iterable, Object glue) {
		return join(sb, iterable.iterator(), glue);
	}

	public static String join(Object[] array, int offset, int length, char glue) {
		return join(new StringBuilder(), array, offset, length, glue).toString();
	}

	public static String join(Object[] array, int offset, int length, Object glue) {
		return join(new StringBuilder(), array, offset, length, glue).toString();
	}

	public static String join(Object[] array, char glue) {
		return join(new StringBuilder(), array, 0, array.length, glue).toString();
	}

	public static String join(Object[] array, Object glue) {
		return join(new StringBuilder(), array, 0, array.length, glue).toString();
	}

	public static String join(Iterator<?> iterator, char glue) {
		return join(new StringBuilder(), iterator, glue).toString();
	}

	public static String join(Iterator<?> iterator, Object glue) {
		return join(new StringBuilder(), iterator, glue).toString();
	}

	public static String join(Iterable<?> iterable, char glue) {
		return join(new StringBuilder(), iterable.iterator(), glue).toString();
	}

	public static String join(Iterable<?> iterable, Object glue) {
		return join(new StringBuilder(), iterable.iterator(), glue).toString();
	}

	public static StringBuilder replace(StringBuilder sb, String string, int replace, Object replacement) {
		int replaceCharCount = Character.charCount(replace);
		for (int i = 0, j = string.indexOf(replace);; j = string.indexOf(replace, i = j + replaceCharCount)) {
			/*
			 * Sadly, there is no StringBuilder.append(String, int, int), and StringBuilder.append(CharSequence, int,
			 * int) isn't smart enough to use System.arraycopy(..) in the case where the CharSequence is a String, so to
			 * get the performance of arraycopy, we have to allow a new String to be allocated here.
			 */
			if (j < 0) {
				return sb.append(string.substring(i));
			}
			sb.append(string.substring(i, j)).append(replacement);
		}
	}

	public static String replace(String string, int replace, Object replacement) {
		int replaceCharCount = Character.charCount(replace);
		int count = 0;
		for (int i = string.indexOf(replace); i >= 0; i = string.indexOf(replace, i + replaceCharCount)) {
			++count;
		}
		if (count == 0) {
			return string;
		}
		String replacementStr = String.valueOf(replacement);
		return replace(new StringBuilder(string.length() + (replacementStr.length() - replaceCharCount) * count), string, replace, replacementStr).toString();
	}

	public static CharSequence asCharSequence(char[] array) {
		return asCharSequence(array, 0, array.length);
	}

	public static CharSequence asCharSequence(final char[] array, final int offset, final int length) {
		ArgUtil.checkSlice(array, offset, length, "array");
		return new CharSequence() {

			private String string;

			@Override
			public char charAt(int index) {
				if (index < 0 || index >= length) {
					throw new IndexOutOfBoundsException();
				}
				return array[offset + index];
			}

			@Override
			public int length() {
				return length;
			}

			@Override
			public CharSequence subSequence(int start, int end) {
				if (start < 0 || end > length) {
					throw new IndexOutOfBoundsException();
				}
				return asCharSequence(array, offset + start, end - start);
			}

			@Override
			public String toString() {
				return string == null ? (string = new String(array, offset, length)) : string;
			}

		};
	}

	/**
	 * Returns a {@code CharSequence} containing the specified number of spaces.
	 */
	public static CharSequence spaces(int length) {
		return repeat(' ', length);
	}

	/**
	 * Returns a {@code CharSequence} containing the specified number of repetitions of the specified character.
	 */
	public static CharSequence repeat(final char c, final int n) {
		return new CharSequence() {

			private String string;

			@Override
			public char charAt(int index) {
				return c;
			}

			@Override
			public int length() {
				return n;
			}

			@Override
			public CharSequence subSequence(int start, int end) {
				return repeat(c, end - start);
			}

			@Override
			public String toString() {
				if (string == null) {
					char[] chars = new char[n];
					Arrays.fill(chars, c);
					string = new String(chars);
				}
				return string;
			}

		};
	}

	/**
	 * Returns a {@link CharSequence} containing the specified number of repetitions of the specified
	 * {@link CharSequence}.
	 */
	public static CharSequence repeat(final CharSequence cs, int n) {
		class Repeater implements CharSequence {

			private final int phase;
			private final int length;

			private String string;

			Repeater(int phase, int length) {
				this.phase = phase;
				this.length = length;
			}

			@Override
			public int length() {
				return length;
			}

			@Override
			public char charAt(int index) {
				return cs.charAt((index + phase) % cs.length());
			}

			@Override
			public CharSequence subSequence(int start, int end) {
				return new Repeater(phase + start, end - start);
			}

			@Override
			public String toString() {
				if (string == null) {
					int length = this.length;
					char[] chars = new char[length];
					for (int index = 0; index < length; ++index) {
						chars[index] = charAt(index);
					}
					string = new String(chars);
				}
				return string;
			}

		}
		return new Repeater(0, cs.length() * n);
	}

	public static StringBuilder hexDump(StringBuilder sb, int bytesPerLine, byte[] bytes) {
		return hexDump(sb, bytesPerLine, bytes, 0, bytes.length);
	}

	public static StringBuilder hexDump(StringBuilder sb, int bytesPerLine, byte[] bytes, int offset, int length) {
		sb.append('\n');
		while (length > 0) {
			int n = Math.min(length, bytesPerLine);
			for (int i = 0; i < n; ++i) {
				toHex(sb, bytes[offset + i]).append(' ');
				if ((i & 3) == 3) {
					sb.append(' ');
				}
			}
			for (int i = n; i < bytesPerLine; ++i) {
				sb.append("   ");
				if ((i & 3) == 3) {
					sb.append(' ');
				}
			}
			sb.append("   ");
			for (int i = 0; i < n; ++i) {
				byte b = bytes[offset + i];
				sb.append(b >= 0x20 && b < 0x7F ? (char) b : '\u00B7');
			}
			sb.append('\n');
			offset += n;
			length -= n;
		}
		return sb;
	}

	public static String hexDump(int bytesPerLine, byte[] bytes) {
		return hexDump(bytesPerLine, bytes, 0, bytes.length);
	}

	public static String hexDump(int bytesPerLine, byte[] bytes, int offset, int length) {
		return hexDump(new StringBuilder(2 + (length + bytesPerLine - 1) / bytesPerLine * (4 * bytesPerLine + bytesPerLine / 4 + 5)), bytesPerLine, bytes, offset, length).toString();
	}

	/**
	 * Efficiently parses a {@link String} into an array of substrings by splitting it at occurrences of a separator
	 * code point. The separator code point is not included in the returned substrings.
	 * <p>
	 * If {@code stripWhitespace} is {@code true}, leading and trailing {@linkplain Character#isWhitespace(int)
	 * whitespace} will not be included in the returned substrings. If stripping whitespace would cause a substring to
	 * contain no characters, the corresponding element of the returned array will be {@code null}. If
	 * {@code stripWhitespace} is {@code false}, the returned array is allowed to contain zero-length substrings, and no
	 * elements will be {@code null}.
	 * </p>
	 * 
	 * @param string
	 *        the {@link String} to parse.
	 * @param separator
	 *        the separator code point.
	 * @param stripWhitespace
	 *        whether to strip leading and trailing whitespace from the returned substrings.
	 * @return the array of substrings.
	 */
	public static String[] split(String string, int separator, boolean stripWhitespace) {
		int scc = Character.charCount(separator), count = 1;
		for (int sep = string.indexOf(separator); sep >= 0; sep = string.indexOf(separator, sep + scc)) {
			++count;
		}
		String[] substrings = new String[count];
		int index = 0, start = 0, sep = string.indexOf(separator);
		if (stripWhitespace) {
			int codePoint;
			for (;;) {
				int end = sep < 0 ? string.length() : sep;
				while (end > start && Character.isWhitespace(codePoint = string.codePointBefore(end))) {
					end -= Character.charCount(codePoint);
				}
				while (start < end && Character.isWhitespace(codePoint = string.codePointAt(start))) {
					start += Character.charCount(codePoint);
				}
				if (start < end) {
					substrings[index] = string.substring(start, end);
				}
				if (sep < 0) {
					return substrings;
				}
				sep = string.indexOf(separator, start = sep + scc);
				++index;
			}
		}
		while (sep >= 0) {
			substrings[index++] = string.substring(start, sep);
			sep = string.indexOf(separator, start = sep + scc);
		}
		substrings[index] = string.substring(start);
		return substrings;
	}

	/**
	 * Efficiently parses a bracketed, comma-separated list (e.g., {@code "[ 42, foo ]"}) into an array (e.g.,
	 * <code>{ "42", "foo" }</code>). Effectively calls
	 * <code>{@link #split(String, int, boolean) split}(list.{@link String#substring(int, int) substring}(1, list.{@link String#length() length()} - 1), ',', true)</code>
	 * after first checking that the first and last characters of {@code list} are {@code '['} and {@code ']'},
	 * respectively.
	 * 
	 * @return the array.
	 * @throws IllegalArgumentException
	 *         if {@code list} does not begin with an opening square bracket ({@code '['}) or does not end with a
	 *         closing square bracket ({@code ']'}).
	 */
	public static String[] parseBracketedList(String list) {
		int lastIndex = list.length() - 1;
		if (list.charAt(0) != '[' || list.charAt(lastIndex) != ']') {
			throw new IllegalArgumentException("missing bracket");
		}
		return split(list.substring(1, lastIndex), ',', true);
	}

	/**
	 * Parses the given {@link String} into an array of substrings by splitting it at instances of characters that are
	 * members of the specified Unicode general categories. The separating characters are not included in the returned
	 * substrings.
	 * 
	 * @param string
	 *        the {@link String} to parse.
	 * @param separatorCategories
	 *        a bitmask indicating the Unicode general categories of characters that are to act as separators. Each bit
	 *        in the bitmask corresponds to one of the Unicode general category constants defined in {@link Character}.
	 *        For example, to split at all horizontal white space and control characters, this argument would be
	 *        <code>1 << {@link Character#SPACE_SEPARATOR} | 1 << {@link Character#CONTROL}</code>.
	 * @return the array of substrings.
	 */
	public static String[] splitByCharacterCategory(String string, int separatorCategories) {
		int length = string.length(), count = 1, codePoint = 0;
		for (int index = 0; index < length; index += Character.charCount(codePoint)) {
			if ((separatorCategories & 1 << Character.getType(codePoint = string.codePointAt(index))) != 0) {
				++count;
			}
		}
		String[] substrings = new String[count];
		for (int index = 0, start = 0; index < count; ++index) {
			int end = start;
			while (end < length && (separatorCategories & 1 << Character.getType(codePoint = string.codePointAt(end))) == 0) {
				end += Character.charCount(codePoint);
			}
			substrings[index] = string.substring(start, end);
			start = end + Character.charCount(codePoint);
		}
		return substrings;
	}

	/**
	 * Parses the given {@link String} into an array of substrings by splitting it at runs of characters that are
	 * members of the specified Unicode general categories. The runs of separating characters are not included in the
	 * returned substrings. Runs of the specified characters at the beginning or end of the specified string are
	 * stripped off prior to splitting.
	 * 
	 * @param string
	 *        the {@link String} to parse.
	 * @param separatorCategories
	 *        a bitmask indicating the Unicode general categories of characters that are to act as separators. Each bit
	 *        in the bitmask corresponds to one of the Unicode general category constants defined in {@link Character}.
	 *        For example, to split at all runs of horizontal white space and control characters, this argument would be
	 *        <code>1 << {@link Character#SPACE_SEPARATOR} | 1 << {@link Character#CONTROL}</code>.
	 * @return the array of substrings.
	 */
	public static String[] splitByCharacterCategoryRun(String string, int separatorCategories) {
		int length = string.length(), count = 0, codePoint = 0;
		for (int index = 0; index < length; index += Character.charCount(codePoint)) {
			if ((separatorCategories & 1 << Character.getType(codePoint = string.codePointAt(index))) == 0) {
				++count;
				do {
					index += Character.charCount(codePoint);
				} while (index < length && (separatorCategories & 1 << Character.getType(codePoint = string.codePointAt(index))) == 0);
			}
		}
		String[] substrings = new String[count];
		for (int index = 0, start = 0; index < count; ++index) {
			while (start < length && (separatorCategories & 1 << Character.getType(codePoint = string.codePointAt(start))) != 0) {
				start += Character.charCount(codePoint);
			}
			int end = start;
			do {
				end += Character.charCount(codePoint);
			} while (end < length && (separatorCategories & 1 << Character.getType(codePoint = string.codePointAt(end))) == 0);
			substrings[index] = string.substring(start, end);
			start = end;
		}
		return substrings;
	}

	/**
	 * Appends the 8-bit binary representation of the specified {@code byte} to the given {@link StringBuilder}.
	 * 
	 * @see #toBinary(byte)
	 */
	public static StringBuilder toBinary(StringBuilder sb, byte b) {
		int v = b;
		for (int i = 0; i < 8; ++i) {
			sb.append((char) ('0' + (v >> 7 - i & 1)));
		}
		return sb;
	}

	/**
	 * Appends the 16-bit binary representation of the specified {@code short} to the given {@link StringBuilder}.
	 * 
	 * @see #toBinary(short)
	 */
	public static StringBuilder toBinary(StringBuilder sb, short i) {
		return toBinary(toBinary(sb, (byte) (i >> 8)), (byte) i);
	}

	/**
	 * Appends the 32-bit binary representation of the specified {@code int} to the given {@link StringBuilder}.
	 * 
	 * @see #toBinary(int)
	 */
	public static StringBuilder toBinary(StringBuilder sb, int i) {
		return toBinary(toBinary(sb, (short) (i >> 16)), (short) i);
	}

	/**
	 * Appends the 64-bit binary representation of the specified {@code long} to the given {@link StringBuilder}.
	 * 
	 * @see #toBinary(long)
	 */
	public static StringBuilder toBinary(StringBuilder sb, long i) {
		return toBinary(toBinary(sb, (int) (i >> 32)), (int) i);
	}

	/**
	 * Appends the binary representation of the given bytes to the given {@link StringBuilder}.
	 * 
	 * @see #toBinary(byte[], int, int)
	 */
	public static StringBuilder toBinary(StringBuilder sb, byte[] b, int offset, int length) {
		length += offset;
		while (offset < length) {
			toBinary(sb, b[offset++]);
		}
		return sb;
	}

	/**
	 * Appends the binary representation of the given bytes to the given {@link StringBuilder}.
	 * 
	 * @see #toBinary(byte[])
	 */
	public static StringBuilder toBinary(StringBuilder sb, byte[] b) {
		return toBinary(sb, b, 0, b.length);
	}

	/**
	 * Returns a {@link String} containing {@code "0b"} followed by the 8-bit binary representation of the specified
	 * {@code byte}.
	 * 
	 * @see #toBinary(StringBuilder, byte)
	 */
	public static String toBinary(byte b) {
		return toBinary(new StringBuilder(10).append("0b"), b).toString();
	}

	/**
	 * Returns a {@link String} containing {@code "0b"} followed by the 16-bit binary representation of the specified
	 * {@code short}.
	 * 
	 * @see #toBinary(StringBuilder, short)
	 */
	public static String toBinary(short i) {
		return toBinary(new StringBuilder(18).append("0b"), i).toString();
	}

	/**
	 * Returns a {@link String} containing {@code "0b"} followed by the 32-bit binary representation of the specified
	 * {@code int}.
	 * 
	 * @see #toBinary(StringBuilder, int)
	 */
	public static String toBinary(int i) {
		return toBinary(new StringBuilder(34).append("0b"), i).toString();
	}

	/**
	 * Returns a {@link String} containing {@code "0b"} followed by the 64-bit binary representation of the specified
	 * {@code long}.
	 * 
	 * @see #toBinary(StringBuilder, long)
	 */
	public static String toBinary(long i) {
		return toBinary(new StringBuilder(66).append("0b"), i).toString();
	}

	/**
	 * Returns a {@link String} containing {@code "0b"} followed by the binary representation of the given bytes.
	 * 
	 * @see #toBinary(StringBuilder, byte[], int, int)
	 */
	public static String toBinary(byte[] b, int offset, int length) {
		return toBinary(new StringBuilder(2 + length * 8).append("0b"), b, offset, length).toString();
	}

	/**
	 * Returns a {@link String} containing {@code "0b"} followed by the binary representation of the given bytes.
	 * 
	 * @see #toBinary(StringBuilder, byte[])
	 */
	public static String toBinary(byte[] b) {
		return toBinary(b, 0, b.length);
	}

	private static char nibbleToHexLower(int nibble) {
		assert (nibble & ~0xF) == 0;
		return (char) ((nibble < 10 ? '0' : 'a' - 10) + nibble);
	}

	private static char nibbleToHexUpper(int nibble) {
		assert (nibble & ~0xF) == 0;
		return (char) ((nibble < 10 ? '0' : 'A' - 10) + nibble);
	}

	/**
	 * Appends the two-digit lowercase hexadecimal representation of the specified {@code byte} to the given
	 * {@link StringBuilder}.
	 * 
	 * @see #toHex(byte)
	 * @see #toHexUpper(StringBuilder, byte)
	 */
	public static StringBuilder toHex(StringBuilder sb, byte b) {
		return sb.append(nibbleToHexLower(b >> 4 & 0xF)).append(nibbleToHexLower(b & 0xF));
	}

	/**
	 * Appends the two-digit uppercase hexadecimal representation of the specified {@code byte} to the given
	 * {@link StringBuilder}.
	 * 
	 * @see #toHex(StringBuilder, byte)
	 */
	public static StringBuilder toHexUpper(StringBuilder sb, byte b) {
		return sb.append(nibbleToHexUpper(b >> 4 & 0xF)).append(nibbleToHexUpper(b & 0xF));
	}

	/**
	 * Appends the four-digit lowercase hexadecimal representation of the specified {@code short} to the given
	 * {@link StringBuilder}.
	 * 
	 * @see #toHex(short)
	 * @see #toHexUpper(StringBuilder, short)
	 */
	public static StringBuilder toHex(StringBuilder sb, short i) {
		return toHex(toHex(sb, (byte) (i >> 8)), (byte) i);
	}

	/**
	 * Appends the four-digit uppercase hexadecimal representation of the specified {@code short} to the given
	 * {@link StringBuilder}.
	 * 
	 * @see #toHex(StringBuilder, short)
	 */
	public static StringBuilder toHexUpper(StringBuilder sb, short i) {
		return toHexUpper(toHexUpper(sb, (byte) (i >> 8)), (byte) i);
	}

	/**
	 * Appends the eight-digit lowercase hexadecimal representation of the specified {@code int} to the given
	 * {@link StringBuilder}.
	 * 
	 * @see #toHex(int)
	 * @see #toHexUpper(StringBuilder, int)
	 */
	public static StringBuilder toHex(StringBuilder sb, int i) {
		return toHex(toHex(sb, (short) (i >> 16)), (short) i);
	}

	/**
	 * Appends the eight-digit uppercase hexadecimal representation of the specified {@code int} to the given
	 * {@link StringBuilder}.
	 * 
	 * @see #toHex(StringBuilder, int)
	 */
	public static StringBuilder toHexUpper(StringBuilder sb, int i) {
		return toHexUpper(toHexUpper(sb, (short) (i >> 16)), (short) i);
	}

	/**
	 * Appends the sixteen-digit lowercase hexadecimal representation of the specified {@code long} to the given
	 * {@link StringBuilder}.
	 * 
	 * @see #toHex(long)
	 * @see #toHexUpper(StringBuilder, long)
	 */
	public static StringBuilder toHex(StringBuilder sb, long i) {
		return toHex(toHex(sb, (int) (i >> 32)), (int) i);
	}

	/**
	 * Appends the sixteen-digit uppercase hexadecimal representation of the specified {@code long} to the given
	 * {@link StringBuilder}.
	 * 
	 * @see #toHex(StringBuilder, long)
	 */
	public static StringBuilder toHexUpper(StringBuilder sb, long i) {
		return toHexUpper(toHexUpper(sb, (int) (i >> 32)), (int) i);
	}

	/**
	 * Appends the lowercase hexadecimal representation of the given bytes to the given {@link StringBuilder}.
	 * 
	 * @see #toHex(byte[], int, int)
	 * @see #toHexUpper(StringBuilder, byte[], int, int)
	 */
	public static StringBuilder toHex(StringBuilder sb, byte[] b, int offset, int length) {
		length += offset;
		while (offset < length) {
			toHex(sb, b[offset++]);
		}
		return sb;
	}

	/**
	 * Appends the uppercase hexadecimal representation of the given bytes to the given {@link StringBuilder}.
	 * 
	 * @see #toHex(StringBuilder, byte[], int, int)
	 */
	public static StringBuilder toHexUpper(StringBuilder sb, byte[] b, int offset, int length) {
		length += offset;
		while (offset < length) {
			toHexUpper(sb, b[offset++]);
		}
		return sb;
	}

	/**
	 * Appends the lowercase hexadecimal representation of the given bytes to the given {@link StringBuilder}.
	 * 
	 * @see #toHex(byte[])
	 * @see #toHexUpper(StringBuilder, byte[])
	 */
	public static StringBuilder toHex(StringBuilder sb, byte[] b) {
		return toHex(sb, b, 0, b.length);
	}

	/**
	 * Appends the uppercase hexadecimal representation of the given bytes to the given {@link StringBuilder}.
	 * 
	 * @see #toHex(StringBuilder, byte[])
	 */
	public static StringBuilder toHexUpper(StringBuilder sb, byte[] b) {
		return toHexUpper(sb, b, 0, b.length);
	}

	/**
	 * Returns a {@link String} containing {@code "0x"} followed by the two-digit lowercase hexadecimal representation
	 * of the specified {@code byte}.
	 * 
	 * @see #toHex(StringBuilder, byte)
	 */
	public static String toHex(byte b) {
		return toHex(new StringBuilder(4).append("0x"), b).toString();
	}

	/**
	 * Returns a {@link String} containing {@code "0x"} followed by the four-digit lowercase hexadecimal representation
	 * of the specified {@code short}.
	 * 
	 * @see #toHex(StringBuilder, short)
	 */
	public static String toHex(short i) {
		return toHex(new StringBuilder(6).append("0x"), i).toString();
	}

	/**
	 * Returns a {@link String} containing {@code "0x"} followed by the eight-digit lowercase hexadecimal representation
	 * of the specified {@code int}.
	 * 
	 * @see #toHex(StringBuilder, int)
	 */
	public static String toHex(int i) {
		return toHex(new StringBuilder(10).append("0x"), i).toString();
	}

	/**
	 * Returns a {@link String} containing {@code "0x"} followed by the sixteen-digit lowercase hexadecimal
	 * representation of the specified {@code long}.
	 * 
	 * @see #toHex(StringBuilder, long)
	 */
	public static String toHex(long i) {
		return toHex(new StringBuilder(18).append("0x"), i).toString();
	}

	/**
	 * Returns a {@link String} containing {@code "0x"} followed by the lowercase hexadecimal representation of the
	 * given bytes.
	 * 
	 * @see #toHex(StringBuilder, byte[], int, int)
	 */
	public static String toHex(byte[] b, int offset, int length) {
		return toHex(new StringBuilder(2 + b.length * 2).append("0x"), b, offset, length).toString();
	}

	/**
	 * Returns a {@link String} containing {@code "0x"} followed by the lowercase hexadecimal representation of the
	 * given bytes.
	 * 
	 * @see #toHex(StringBuilder, byte[])
	 */
	public static String toHex(byte[] b) {
		return toHex(b, 0, b.length);
	}

	/**
	 * Returns the bytes that the given hex {@link String} represents. This method will ignore a leading {@code "0x"} if
	 * it is present.
	 * 
	 * @throws NumberFormatException
	 *         if {@code hex} contains any characters other than hexadecimal digits or a leading {@code "0x"}.
	 */
	public static byte[] fromHex(String hex) throws NumberFormatException {
		char[] chars = hex.toCharArray();
		int i = chars.length >= 2 && chars[0] == '0' && (chars[1] | 0x20) == 'x' ? 1 : -1, j, v;
		byte[] bytes = new byte[(chars.length - i) / 2];
		if ((j = chars.length & 1) != 0) {
			if ((v = Character.digit(chars[++i], 16)) < 0) {
				throw new NumberFormatException("not a hex digit at offset " + i);
			}
			bytes[0] = (byte) v;
		}
		while (++i < chars.length) {
			if ((v = Character.digit(chars[i], 16) << 4) < 0 || (v |= Character.digit(chars[++i], 16)) < 0) {
				throw new NumberFormatException("not a hex digit at offset " + i);
			}
			bytes[j++] = (byte) v;
		}
		assert j == bytes.length;
		return bytes;
	}

	/**
	 * Appends the Java-escaped form of the specified character to the given {@link StringBuilder}.
	 * <p>
	 * See <a href="http://java.sun.com/docs/books/jls/second_edition/html/lexical.doc.html#101089"><cite>Java Language
	 * Specification</cite>, Second Edition, &sect;3.10.6, "Escape Sequences for Character and String Literals"</a>.
	 * </p>
	 * 
	 * @see #escapeJavaString(StringBuilder, CharSequence)
	 * @see #unescapeJavaString(StringBuilder, CharSequence)
	 */
	public static StringBuilder escapeJavaChar(StringBuilder sb, char ch) {
		switch (ch) {
			case '\0':
				return sb.append("\\0");
			case '\b':
				return sb.append("\\b");
			case '\t':
				return sb.append("\\t");
			case '\n':
				return sb.append("\\n");
			case '\f':
				return sb.append("\\f");
			case '\r':
				return sb.append("\\r");
			case '"':
				return sb.append("\\\"");
			case '\'':
				return sb.append("\\'");
			case '\\':
				return sb.append("\\\\");
			default:
				if (ch >= 0x20 && ch < 0x7F) {
					return sb.append(ch);
				}
				return toHex(sb.append("\\u"), (short) ch);
		}
	}

	/**
	 * Appends the Java-escaped form of the characters in the given array to the given {@link StringBuilder}.
	 * <p>
	 * See <a href="http://java.sun.com/docs/books/jls/second_edition/html/lexical.doc.html#101089"><cite>Java Language
	 * Specification</cite>, Second Edition, &sect;3.10.6, "Escape Sequences for Character and String Literals"</a>.
	 * </p>
	 * 
	 * @see #escapeJavaChar(StringBuilder, char)
	 * @see #escapeJavaString(StringBuilder, CharSequence)
	 * @see #unescapeJavaString(StringBuilder, CharSequence)
	 */
	public static StringBuilder escapeJavaString(StringBuilder sb, char[] chars) {
		return escapeJavaString(sb, chars, 0, chars.length);
	}

	/**
	 * Appends the Java-escaped form of the characters in the specified slice of the given array to the given
	 * {@link StringBuilder}.
	 * <p>
	 * See <a href="http://java.sun.com/docs/books/jls/second_edition/html/lexical.doc.html#101089"><cite>Java Language
	 * Specification</cite>, Second Edition, &sect;3.10.6, "Escape Sequences for Character and String Literals"</a>.
	 * </p>
	 * 
	 * @see #escapeJavaChar(StringBuilder, char)
	 * @see #escapeJavaString(StringBuilder, CharSequence)
	 * @see #unescapeJavaString(StringBuilder, CharSequence)
	 */
	public static StringBuilder escapeJavaString(StringBuilder sb, char[] chars, int offset, int length) {
		length += offset;
		while (offset < length) {
			escapeJavaChar(sb, chars[offset++]);
		}
		return sb;
	}

	/**
	 * Appends the Java-escaped form of the given {@link CharSequence} to the given {@link StringBuilder}.
	 * <p>
	 * See <a href="http://java.sun.com/docs/books/jls/second_edition/html/lexical.doc.html#101089"><cite>Java Language
	 * Specification</cite>, Second Edition, &sect;3.10.6, "Escape Sequences for Character and String Literals"</a>.
	 * </p>
	 * 
	 * @see #escapeJavaChar(StringBuilder, char)
	 * @see #escapeJavaString(CharSequence)
	 * @see #unescapeJavaString(StringBuilder, CharSequence)
	 */
	public static StringBuilder escapeJavaString(StringBuilder sb, CharSequence string) {
		for (int i = 0, n = string.length(); i < n; ++i) {
			escapeJavaChar(sb, string.charAt(i));
		}
		return sb;
	}

	/**
	 * Returns a {@link String} containing the Java-escaped form of the characters in the given array.
	 * <p>
	 * See <a href="http://java.sun.com/docs/books/jls/second_edition/html/lexical.doc.html#101089"><cite>Java Language
	 * Specification</cite>, Second Edition, &sect;3.10.6, "Escape Sequences for Character and String Literals"</a>.
	 * </p>
	 * 
	 * @see #escapeJavaString(StringBuilder, CharSequence)
	 * @see #unescapeJavaString(CharSequence)
	 */
	public static String escapeJavaString(char[] chars) {
		return escapeJavaString(chars, 0, chars.length);
	}

	/**
	 * Returns a {@link String} containing the Java-escaped form of the characters in the specified slice of the given
	 * array.
	 * <p>
	 * See <a href="http://java.sun.com/docs/books/jls/second_edition/html/lexical.doc.html#101089"><cite>Java Language
	 * Specification</cite>, Second Edition, &sect;3.10.6, "Escape Sequences for Character and String Literals"</a>.
	 * </p>
	 * 
	 * @see #escapeJavaString(StringBuilder, CharSequence)
	 * @see #unescapeJavaString(CharSequence)
	 */
	public static String escapeJavaString(char[] chars, int offset, int length) {
		return escapeJavaString(new StringBuilder(length * 2), chars, offset, length).toString();
	}

	/**
	 * Returns a {@link String} containing the Java-escaped form of the given {@link CharSequence}.
	 * <p>
	 * See <a href="http://java.sun.com/docs/books/jls/second_edition/html/lexical.doc.html#101089"><cite>Java Language
	 * Specification</cite>, Second Edition, &sect;3.10.6, "Escape Sequences for Character and String Literals"</a>.
	 * </p>
	 * 
	 * @see #escapeJavaString(StringBuilder, CharSequence)
	 * @see #unescapeJavaString(CharSequence)
	 */
	public static String escapeJavaString(CharSequence string) {
		return escapeJavaString(new StringBuilder(string.length() * 2), string).toString();
	}

	/**
	 * Appends the unescaped form of the given Java-escaped {@link CharSequence} to the given {@link StringBuilder}.
	 * <p>
	 * See <a href="http://java.sun.com/docs/books/jls/second_edition/html/lexical.doc.html#101089"><cite>Java Language
	 * Specification</cite>, Second Edition, &sect;3.10.6, "Escape Sequences for Character and String Literals"</a>.
	 * </p>
	 * 
	 * @see #unescapeJavaString(CharSequence)
	 * @see #escapeJavaString(StringBuilder, CharSequence)
	 */
	public static StringBuilder unescapeJavaString(StringBuilder sb, CharSequence string) {
		for (int i = 0, n = string.length(); i < n; ++i) {
			char ch = string.charAt(i);
			if (ch == '\\') {
				if (++i >= n) {
					throw new IllegalArgumentException("incomplete escape sequence at end of string: " + string);
				}
				switch (string.charAt(i)) {
					case 'b':
						sb.append('\b');
						break;
					case 't':
						sb.append('\t');
						break;
					case 'n':
						sb.append('\n');
						break;
					case 'f':
						sb.append('\f');
						break;
					case 'r':
						sb.append('\r');
						break;
					case '"':
						sb.append('"');
						break;
					case '\'':
						sb.append('\'');
						break;
					case '\\':
						sb.append('\\');
						break;
					case 'u': {
						if (i + 4 >= n) {
							throw new IllegalArgumentException("incomplete Unicode escape sequence at end of string: " + string);
						}
						int code = Character.digit(string.charAt(++i), 16);
						code = code << 4 | Character.digit(string.charAt(++i), 16);
						code = code << 4 | Character.digit(string.charAt(++i), 16);
						code = code << 4 | Character.digit(string.charAt(++i), 16);
						if (code < 0) {
							throw new IllegalArgumentException("invalid Unicode escape sequence in string: " + string);
						}
						sb.append((char) code);
						break;
					}
					default:
						if (ch >= '0' && ch <= '7') {
							// old school octal escape sequence
							int code = ch - '0';
							if (i + 1 < n) {
								ch = string.charAt(i + 1);
								if (ch >= '0' && ch <= '7') {
									++i;
									code = code << 3 | ch - '0';
									if (code < 0x20 && i + 1 < n) {
										ch = string.charAt(i + 1);
										if (ch >= '0' && ch <= '7') {
											++i;
											code = code << 3 | ch - '0';
										}
									}
								}
							}
							sb.append((char) code);
							break;
						}
						throw new IllegalArgumentException("invalid escape sequence in string: " + string);
				}
			}
			else {
				sb.append(ch);
			}
		}
		return sb;
	}

	/**
	 * Returns a {@link String} containing the unescaped form of the given Java-escaped {@link CharSequence}.
	 * 
	 * @see #unescapeJavaString(StringBuilder, CharSequence)
	 * @see #escapeJavaString(CharSequence)
	 */
	public static String unescapeJavaString(CharSequence string) {
		return unescapeJavaString(new StringBuilder(string.length()), string).toString();
	}

	/**
	 * Appends Java source code for constructing the given {@code byte} array to the given {@link StringBuilder}.
	 */
	public static StringBuilder appendJavaInitializationString(StringBuilder sb, byte[] byteArray) {
		sb.append("new ").append(byte.class.getName()).append("[] {");
		if (byteArray.length > 0) {
			sb.append(' ').append(byteArray[0]);
			for (int i = 1; i < byteArray.length; ++i) {
				sb.append(", ").append(byteArray[i]);
			}
		}
		return sb.append(" }");
	}

	/**
	 * Returns a {@link String} containing Java source code for constructing the given {@code byte} array.
	 */
	public static String getJavaInitializationString(byte[] byteArray) {
		return appendJavaInitializationString(new StringBuilder(14 + byteArray.length * 6), byteArray).toString();
	}

	/**
	 * Appends the specified string to the given {@link StringBuilder}, substituting SGML character entity references
	 * for the SGML special characters: ampersand, less than, greater than, and (optionally) double quotation mark.
	 */
	public static StringBuilder escapeSGMLSpecialChars(StringBuilder sb, CharSequence string, boolean escapeQuotes) {
		for (int i = 0, n = string.length(); i < n; ++i) {
			char c = string.charAt(i);
			String entity;
			switch (c) {
				case '&':
					entity = "&amp;";
					break;
				case '<':
					entity = "&lt;";
					break;
				case '>':
					entity = "&gt;";
					break;
				case '"':
					if (escapeQuotes) {
						entity = "&quot;";
						break;
					}
					// fall through
				default:
					sb.append(c);
					continue;
			}
			sb.append(entity);
		}
		return sb;
	}

	/**
	 * Returns the specified string, with SGML character entity references substituted for the SGML special characters:
	 * ampersand, less than, greater than, and (optionally) double quotation mark.
	 */
	public static String escapeSGMLSpecialChars(CharSequence string, boolean escapeQuotes) {
		return escapeSGMLSpecialChars(new StringBuilder(), string, escapeQuotes).toString();
	}

	/**
	 * Appends to the given {@link StringBuilder} a pretty representation of the given byte count.
	 * 
	 * @see #formatByteCount(long)
	 */
	public static StringBuilder formatByteCount(StringBuilder sb, long count) {
		ArgUtil.checkNonNegative(count, "count");
		if (count < 1000) {
			return sb.append(count).append(" B");
		}
		double d = count;
		String unit;
		if (count < 1000 * 1024) {
			d = Math.scalb(d, -10);
			unit = "KiB";
		}
		else if (count < 1000 * 1024 * 1024) {
			d = Math.scalb(d, -20);
			unit = "MiB";
		}
		else if (count < 1000L * 1024 * 1024 * 1024) {
			d = Math.scalb(d, -30);
			unit = "GiB";
		}
		else if (count < 1000L * 1024 * 1024 * 1024 * 1024) {
			d = Math.scalb(d, -40);
			unit = "TiB";
		}
		else if (count < 1000L * 1024 * 1024 * 1024 * 1024 * 1024) {
			d = Math.scalb(d, -50);
			unit = "PiB";
		}
		else {
			d = Math.scalb(d, -60);
			unit = "EiB";
		}
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setRoundingMode(RoundingMode.HALF_UP);
		format.setMinimumFractionDigits(0);
		if (d < 10) {
			format.setMaximumFractionDigits(2);
		}
		else if (d < 100) {
			format.setMaximumFractionDigits(1);
		}
		else {
			format.setMaximumFractionDigits(0);
		}
		return sb.append(format.format(d)).append(' ').append(unit);
	}

	/**
	 * Returns a pretty representation of the given byte count.
	 * 
	 * @see #formatByteCount(StringBuilder, long)
	 */
	public static String formatByteCount(long count) {
		return formatByteCount(new StringBuilder(8), count).toString();
	}

	/**
	 * Appends to the given {@link StringBuilder} a pretty representation of the specified duration.
	 * 
	 * @see #formatDuration(long)
	 */
	public static StringBuilder formatDuration(StringBuilder sb, long millis) {
		if (millis < 0) {
			sb.append('-');
			millis = -millis;
		}
		long days = millis / (24 * 60 * 60 * 1000);
		long hours = (millis -= 24 * 60 * 60 * 1000 * days) / (60 * 60 * 1000);
		long minutes = (millis -= 60 * 60 * 1000 * hours) / (60 * 1000);
		long seconds = (millis -= 60 * 1000 * minutes) / 1000;
		millis -= 1000 * seconds;
		if (days > 0) {
			sb.append(days).append("d ");
		}
		sb.append(hours).append(':');
		if (minutes < 10) {
			sb.append('0');
		}
		sb.append(minutes).append(':');
		if (seconds < 10) {
			sb.append('0');
		}
		sb.append(seconds);
		if (millis > 0) {
			sb.append('.');
			if (millis < 100) {
				sb.append('0');
				if (millis < 10) {
					sb.append('0');
				}
			}
			sb.append(millis);
		}
		return sb;
	}

	/**
	 * Returns a pretty representation of the specified duration.
	 * 
	 * @see #formatDuration(StringBuilder, long)
	 */
	public static String formatDuration(long millis) {
		return formatDuration(new StringBuilder(26), millis).toString();
	}

}
