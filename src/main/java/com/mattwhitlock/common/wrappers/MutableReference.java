/*
 * Created on May 26, 2018
 */
package com.mattwhitlock.common.wrappers;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Matt Whitlock
 */
public final class MutableReference<T> implements Serializable, Comparable<MutableReference<T>> {

	private static final long serialVersionUID = 1L;

	public T value;

	public MutableReference() {
	}

	public MutableReference(T value) {
		this.value = value;
	}

	public static <T> Comparator<MutableReference<T>> comparator(Comparator<T> delegate) {
		return (o1, o2) -> delegate.compare(o1.value, o2.value);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int compareTo(MutableReference<T> o) {
		return ((Comparable) value).compareTo(o.value);
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || obj instanceof MutableReference<?> && Objects.equals(value, ((MutableReference<?>) obj).value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}

	@Override
	public String toString() {
		return Objects.toString(value);
	}

	public T computeIfUnset(Supplier<? extends T> supplier) {
		return value == null ? value = supplier.get() : value;
	}

	public T computeIfSet(Function<? super T, ? extends T> updatingFunction) {
		return value != null ? value = updatingFunction.apply(value) : null;
	}

	public T compute(Function<? super T, ? extends T> updatingFunction) {
		return value = updatingFunction.apply(value);
	}

	public <U> T merge(U value, BiFunction<? super T, ? super U, ? extends T> mergingFunction) {
		return this.value = mergingFunction.apply(this.value, value);
	}

}
