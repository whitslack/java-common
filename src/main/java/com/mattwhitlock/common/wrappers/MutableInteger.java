/*
 * Created on May 26, 2018
 */
package com.mattwhitlock.common.wrappers;

import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;
import java.util.function.ToIntFunction;

import com.mattwhitlock.common.NumberUtil;

/**
 * @author Matt Whitlock
 */
public final class MutableInteger extends Number implements Comparable<MutableInteger> {

	private static final long serialVersionUID = 1L;

	public static final ToIntFunction<MutableInteger> valueMapper = wrapper -> wrapper.value;

	public int value;

	public MutableInteger() {
	}

	public MutableInteger(int value) {
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
		return value;
	}

	@Override
	public long longValue() {
		return value;
	}

	@Override
	public float floatValue() {
		return value;
	}

	public float floatValueExact() throws ArithmeticException {
		return NumberUtil.floatValueExact(value);
	}

	@Override
	public double doubleValue() {
		return value;
	}

	@Override
	public int compareTo(MutableInteger o) {
		return Integer.compare(value, o.value);
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || obj instanceof MutableInteger && value == ((MutableInteger) obj).value;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(value);
	}

	@Override
	public String toString() {
		return Integer.toString(value);
	}

	public MutableInteger increase(MutableInteger augend) {
		value += augend.value;
		return this;
	}

	public MutableInteger decrease(MutableInteger subtrahend) {
		value -= subtrahend.value;
		return this;
	}

	public int update(IntUnaryOperator updatingFunction) {
		return value = updatingFunction.applyAsInt(value);
	}

	public int merge(int value, IntBinaryOperator mergingFunction) {
		return this.value = mergingFunction.applyAsInt(this.value, value);
	}

}
