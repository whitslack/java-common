/*
 * Created on May 26, 2018
 */
package com.mattwhitlock.common.wrappers;

import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;
import java.util.function.ToLongFunction;

import com.mattwhitlock.common.NumberUtil;

/**
 * @author Matt Whitlock
 */
public final class MutableLong extends Number implements Comparable<MutableLong> {

	private static final long serialVersionUID = 1L;

	public static final ToLongFunction<MutableLong> valueMapper = wrapper -> wrapper.value;

	public long value;

	public MutableLong() {
	}

	public MutableLong(long value) {
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

	public double doubleValueExact() throws ArithmeticException {
		return NumberUtil.doubleValueExact(value);
	}

	@Override
	public int compareTo(MutableLong o) {
		return Long.compare(value, o.value);
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || obj instanceof MutableLong && value == ((MutableLong) obj).value;
	}

	@Override
	public int hashCode() {
		return Long.hashCode(value);
	}

	@Override
	public String toString() {
		return Long.toString(value);
	}

	public MutableLong increase(MutableLong augend) {
		value += augend.value;
		return this;
	}

	public MutableLong decrease(MutableLong subtrahend) {
		value -= subtrahend.value;
		return this;
	}

	public long update(LongUnaryOperator updatingFunction) {
		return value = updatingFunction.applyAsLong(value);
	}

	public long merge(long value, LongBinaryOperator mergingFunction) {
		return this.value = mergingFunction.applyAsLong(this.value, value);
	}

}
