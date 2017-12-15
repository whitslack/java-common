/*
 * Created on Dec 12, 2017
 */
package com.mattwhitlock.common;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/**
 * @author Matt Whitlock
 */
public final class NumberUtil {

	public static int checkUnsignedShort(int value) throws ArithmeticException {
		if (Short.toUnsignedInt((short) value) != value) {
			throw new ArithmeticException("overflow");
		}
		return value;
	}

	public static long checkUnsignedInt(long value) throws ArithmeticException {
		if (Integer.toUnsignedLong((int) value) != value) {
			throw new ArithmeticException("overflow");
		}
		return value;
	}

	@Deprecated
	public static byte byteValueExact(byte value) throws ArithmeticException {
		return value;
	}

	public static byte byteValueExact(short value) throws ArithmeticException {
		byte ret = (byte) value;
		if (ret != value) {
			throw new ArithmeticException("overflow");
		}
		return ret;
	}

	public static byte byteValueExact(int value) throws ArithmeticException {
		byte ret = (byte) value;
		if (ret != value) {
			throw new ArithmeticException("overflow");
		}
		return ret;
	}

	public static byte byteValueExact(long value) throws ArithmeticException {
		byte ret = (byte) value;
		if (ret != value) {
			throw new ArithmeticException("overflow");
		}
		return ret;
	}

	public static byte byteValueExact(float value) throws ArithmeticException {
		byte ret = (byte) value;
		if (ret != value) {
			throw new ArithmeticException("overflow");
		}
		return ret;
	}

	public static byte byteValueExact(double value) throws ArithmeticException {
		byte ret = (byte) value;
		if (ret != value) {
			throw new ArithmeticException("overflow");
		}
		return ret;
	}

	public static byte byteValueExact(BigInteger value) throws ArithmeticException {
		return value.byteValueExact();
	}

	public static byte byteValueExact(BigDecimal value) throws ArithmeticException {
		return value.byteValueExact();
	}

	public static byte byteValueExact(Number value) throws ArithmeticException {
		Objects.requireNonNull(value);
		PrimitiveType primitiveType = PrimitiveType.of(value.getClass());
		if (primitiveType == null) {
			if (value instanceof BigInteger) {
				return byteValueExact((BigInteger) value);
			}
			if (value instanceof BigDecimal) {
				return byteValueExact((BigDecimal) value);
			}
		}
		else {
			switch (primitiveType) {
				case BYTE:
					return byteValueExact(value.byteValue());
				case SHORT:
					return byteValueExact(value.shortValue());
				case INT:
					return byteValueExact(value.intValue());
				case LONG:
					return byteValueExact(value.longValue());
				case FLOAT:
					return byteValueExact(value.floatValue());
				case DOUBLE:
					return byteValueExact(value.doubleValue());
				default:
					break;
			}
		}
		return ((Byte) value).byteValue();
	}

	@Deprecated
	public static short shortValueExact(byte value) throws ArithmeticException {
		return value;
	}

	@Deprecated
	public static short shortValueExact(short value) throws ArithmeticException {
		return value;
	}

	public static short shortValueExact(int value) throws ArithmeticException {
		short ret = (short) value;
		if (ret != value) {
			throw new ArithmeticException("overflow");
		}
		return ret;
	}

	public static short shortValueExact(long value) throws ArithmeticException {
		short ret = (short) value;
		if (ret != value) {
			throw new ArithmeticException("overflow");
		}
		return ret;
	}

	public static short shortValueExact(float value) throws ArithmeticException {
		short ret = (short) value;
		if (ret != value) {
			throw new ArithmeticException("overflow");
		}
		return ret;
	}

	public static short shortValueExact(double value) throws ArithmeticException {
		short ret = (short) value;
		if (ret != value) {
			throw new ArithmeticException("overflow");
		}
		return ret;
	}

	public static short shortValueExact(BigInteger value) throws ArithmeticException {
		return value.shortValueExact();
	}

	public static short shortValueExact(BigDecimal value) throws ArithmeticException {
		return value.shortValueExact();
	}

	public static short shortValueExact(Number value) throws ArithmeticException {
		Objects.requireNonNull(value);
		PrimitiveType primitiveType = PrimitiveType.of(value.getClass());
		if (primitiveType == null) {
			if (value instanceof BigInteger) {
				return shortValueExact((BigInteger) value);
			}
			if (value instanceof BigDecimal) {
				return shortValueExact((BigDecimal) value);
			}
		}
		else {
			switch (primitiveType) {
				case BYTE:
					return shortValueExact(value.byteValue());
				case SHORT:
					return shortValueExact(value.shortValue());
				case INT:
					return shortValueExact(value.intValue());
				case LONG:
					return shortValueExact(value.longValue());
				case FLOAT:
					return shortValueExact(value.floatValue());
				case DOUBLE:
					return shortValueExact(value.doubleValue());
				default:
					break;
			}
		}
		return ((Short) value).shortValue();
	}

	@Deprecated
	public static int intValueExact(byte value) throws ArithmeticException {
		return value;
	}

	@Deprecated
	public static int intValueExact(short value) throws ArithmeticException {
		return value;
	}

	@Deprecated
	public static int intValueExact(int value) throws ArithmeticException {
		return value;
	}

	public static int intValueExact(long value) throws ArithmeticException {
		int ret = (int) value;
		if (ret != value) {
			throw new ArithmeticException("overflow");
		}
		return ret;
	}

	public static int intValueExact(float value) throws ArithmeticException {
		int ret = (int) value;
		if (ret != value) {
			throw new ArithmeticException("overflow");
		}
		return ret;
	}

	public static int intValueExact(double value) throws ArithmeticException {
		int ret = (int) value;
		if (ret != value) {
			throw new ArithmeticException("overflow");
		}
		return ret;
	}

	public static int intValueExact(BigInteger value) throws ArithmeticException {
		return value.intValueExact();
	}

	public static int intValueExact(BigDecimal value) throws ArithmeticException {
		return value.intValueExact();
	}

	public static int intValueExact(Number value) throws ArithmeticException {
		Objects.requireNonNull(value);
		PrimitiveType primitiveType = PrimitiveType.of(value.getClass());
		if (primitiveType == null) {
			if (value instanceof BigInteger) {
				return intValueExact((BigInteger) value);
			}
			if (value instanceof BigDecimal) {
				return intValueExact((BigDecimal) value);
			}
		}
		else {
			switch (primitiveType) {
				case BYTE:
					return intValueExact(value.byteValue());
				case SHORT:
					return intValueExact(value.shortValue());
				case INT:
					return intValueExact(value.intValue());
				case LONG:
					return intValueExact(value.longValue());
				case FLOAT:
					return intValueExact(value.floatValue());
				case DOUBLE:
					return intValueExact(value.doubleValue());
				default:
					break;
			}
		}
		return ((Integer) value).intValue();
	}

	@Deprecated
	public static long longValueExact(byte value) throws ArithmeticException {
		return value;
	}

	@Deprecated
	public static long longValueExact(short value) throws ArithmeticException {
		return value;
	}

	@Deprecated
	public static long longValueExact(int value) throws ArithmeticException {
		return value;
	}

	@Deprecated
	public static long longValueExact(long value) throws ArithmeticException {
		return value;
	}

	public static long longValueExact(float value) throws ArithmeticException {
		long ret = (long) value;
		if (ret != value) {
			throw new ArithmeticException("overflow");
		}
		return ret;
	}

	public static long longValueExact(double value) throws ArithmeticException {
		long ret = (long) value;
		if (ret != value) {
			throw new ArithmeticException("overflow");
		}
		return ret;
	}

	public static long longValueExact(BigInteger value) throws ArithmeticException {
		return value.longValueExact();
	}

	public static long longValueExact(BigDecimal value) throws ArithmeticException {
		return value.longValueExact();
	}

	public static long longValueExact(Number value) throws ArithmeticException {
		Objects.requireNonNull(value);
		PrimitiveType primitiveType = PrimitiveType.of(value.getClass());
		if (primitiveType == null) {
			if (value instanceof BigInteger) {
				return longValueExact((BigInteger) value);
			}
			if (value instanceof BigDecimal) {
				return longValueExact((BigDecimal) value);
			}
		}
		else {
			switch (primitiveType) {
				case BYTE:
					return longValueExact(value.byteValue());
				case SHORT:
					return longValueExact(value.shortValue());
				case INT:
					return longValueExact(value.intValue());
				case LONG:
					return longValueExact(value.longValue());
				case FLOAT:
					return longValueExact(value.floatValue());
				case DOUBLE:
					return longValueExact(value.doubleValue());
				default:
					break;
			}
		}
		return ((Long) value).longValue();
	}

	@Deprecated
	public static float floatValueExact(byte value) throws ArithmeticException {
		return value;
	}

	@Deprecated
	public static float floatValueExact(short value) throws ArithmeticException {
		return value;
	}

	public static float floatValueExact(int value) throws ArithmeticException {
		float ret = value;
		if ((int) ret != value) {
			throw new ArithmeticException("overflow");
		}
		return ret;
	}

	public static float floatValueExact(long value) throws ArithmeticException {
		float ret = value;
		if ((long) ret != value) {
			throw new ArithmeticException("overflow");
		}
		return ret;
	}

	@Deprecated
	public static float floatValueExact(float value) throws ArithmeticException {
		return value;
	}

	public static float floatValueExact(double value) throws ArithmeticException {
		float ret = (float) value;
		if (ret != value) {
			throw new ArithmeticException("overflow");
		}
		return ret;
	}

	public static float floatValueExact(Number value) throws ArithmeticException {
		Objects.requireNonNull(value);
		PrimitiveType primitiveType = PrimitiveType.of(value.getClass());
		if (primitiveType != null) {
			switch (primitiveType) {
				case BYTE:
					return floatValueExact(value.byteValue());
				case SHORT:
					return floatValueExact(value.shortValue());
				case INT:
					return floatValueExact(value.intValue());
				case LONG:
					return floatValueExact(value.longValue());
				case FLOAT:
					return floatValueExact(value.floatValue());
				case DOUBLE:
					return floatValueExact(value.doubleValue());
				default:
					break;
			}
		}
		return ((Float) value).floatValue();
	}

	@Deprecated
	public static double doubleValueExact(byte value) throws ArithmeticException {
		return value;
	}

	@Deprecated
	public static double doubleValueExact(short value) throws ArithmeticException {
		return value;
	}

	@Deprecated
	public static double doubleValueExact(int value) throws ArithmeticException {
		return value;
	}

	public static double doubleValueExact(long value) throws ArithmeticException {
		double ret = value;
		if ((long) ret != value) {
			throw new ArithmeticException("overflow");
		}
		return ret;
	}

	@Deprecated
	public static double doubleValueExact(float value) throws ArithmeticException {
		return value;
	}

	@Deprecated
	public static double doubleValueExact(double value) throws ArithmeticException {
		return value;
	}

	public static double doubleValueExact(Number value) throws ArithmeticException {
		Objects.requireNonNull(value);
		PrimitiveType primitiveType = PrimitiveType.of(value.getClass());
		if (primitiveType != null) {
			switch (primitiveType) {
				case BYTE:
					return doubleValueExact(value.byteValue());
				case SHORT:
					return doubleValueExact(value.shortValue());
				case INT:
					return doubleValueExact(value.intValue());
				case LONG:
					return doubleValueExact(value.longValue());
				case FLOAT:
					return doubleValueExact(value.floatValue());
				case DOUBLE:
					return doubleValueExact(value.doubleValue());
				default:
					break;
			}
		}
		return ((Double) value).doubleValue();
	}

}
