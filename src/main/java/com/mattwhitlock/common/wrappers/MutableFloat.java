/*
 * Created on May 26, 2018
 */
package com.mattwhitlock.common.wrappers;

import com.mattwhitlock.common.NumberUtil;

/**
 * @author Matt Whitlock
 */
public final class MutableFloat extends Number implements Comparable<MutableFloat> {

	private static final long serialVersionUID = 1L;

	public float value;

	public MutableFloat() {
	}

	public MutableFloat(float value) {
		this.value = value;
	}

	public byte byteValueExact() throws ArithmeticException {
		return NumberUtil.byteValueExact(value);
	}

	public short shortValueExact() throws ArithmeticException {
		return NumberUtil.shortValueExact(value);
	}

	@Override
	public int intValue() {
		return (int) value;
	}

	public int intValueExact() throws ArithmeticException {
		return NumberUtil.intValueExact(value);
	}

	@Override
	public long longValue() {
		return (long) value;
	}

	public long longValueExact() throws ArithmeticException {
		return NumberUtil.longValueExact(value);
	}

	@Override
	public float floatValue() {
		return value;
	}

	@Override
	public double doubleValue() {
		return value;
	}

	@Override
	public int compareTo(MutableFloat o) {
		return Float.compare(value, o.value);
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || obj instanceof MutableFloat && value == ((MutableFloat) obj).value;
	}

	@Override
	public int hashCode() {
		return Float.hashCode(value);
	}

	@Override
	public String toString() {
		return Float.toString(value);
	}

	public MutableFloat increase(MutableFloat augend) {
		value += augend.value;
		return this;
	}

	public MutableFloat decrease(MutableFloat subtrahend) {
		value -= subtrahend.value;
		return this;
	}

}
