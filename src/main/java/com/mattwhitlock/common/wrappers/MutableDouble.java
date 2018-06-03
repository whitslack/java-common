/*
 * Created on May 26, 2018
 */
package com.mattwhitlock.common.wrappers;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;
import java.util.function.ToDoubleFunction;

import com.mattwhitlock.common.NumberUtil;

/**
 * @author Matt Whitlock
 */
public final class MutableDouble extends Number implements Comparable<MutableDouble> {

	private static final long serialVersionUID = 1L;

	public static final ToDoubleFunction<MutableDouble> valueMapper = wrapper -> wrapper.value;

	public double value;

	public MutableDouble() {
	}

	public MutableDouble(double value) {
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
		return (float) value;
	}

	public float floatValueExact() throws ArithmeticException {
		return NumberUtil.floatValueExact(value);
	}

	@Override
	public double doubleValue() {
		return value;
	}

	@Override
	public int compareTo(MutableDouble o) {
		return Double.compare(value, o.value);
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || obj instanceof MutableDouble && value == ((MutableDouble) obj).value;
	}

	@Override
	public int hashCode() {
		return Double.hashCode(value);
	}

	@Override
	public String toString() {
		return Double.toString(value);
	}

	public MutableDouble increase(MutableDouble augend) {
		value += augend.value;
		return this;
	}

	public MutableDouble decrease(MutableDouble subtrahend) {
		value -= subtrahend.value;
		return this;
	}

	public double update(DoubleUnaryOperator updatingFunction) {
		return value = updatingFunction.applyAsDouble(value);
	}

	public double merge(double value, DoubleBinaryOperator mergingFunction) {
		return this.value = mergingFunction.applyAsDouble(this.value, value);
	}

}
