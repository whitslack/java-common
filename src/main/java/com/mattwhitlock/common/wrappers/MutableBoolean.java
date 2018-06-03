/*
 * Created on May 26, 2018
 */
package com.mattwhitlock.common.wrappers;

import java.io.Serializable;
import java.util.function.Predicate;

/**
 * @author Matt Whitlock
 */
public final class MutableBoolean implements Serializable, Comparable<MutableBoolean> {

	private static final long serialVersionUID = 1L;

	public static final Predicate<MutableBoolean> valueMapper = wrapper -> wrapper.value;

	public boolean value;

	public MutableBoolean() {
	}

	public MutableBoolean(boolean value) {
		this.value = value;
	}

	@Override
	public int compareTo(MutableBoolean o) {
		return Boolean.compare(value, o.value);
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || obj instanceof MutableBoolean && value == ((MutableBoolean) obj).value;
	}

	@Override
	public int hashCode() {
		return Boolean.hashCode(value);
	}

	@Override
	public String toString() {
		return Boolean.toString(value);
	}

}
