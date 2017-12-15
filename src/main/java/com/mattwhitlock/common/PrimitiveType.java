/*
 * Created on Jun 3, 2017
 */
package com.mattwhitlock.common;

import java.util.IdentityHashMap;

public enum PrimitiveType {

	BOOLEAN(Boolean.TYPE, Boolean.class, 0, 0), //
	CHAR(Character.TYPE, Character.class, 1, 1), //
	BYTE(Byte.TYPE, Byte.class, 2, 2), //
	SHORT(Short.TYPE, Short.class, 2, 3), //
	INT(Integer.TYPE, Integer.class, 2, 4), //
	LONG(Long.TYPE, Long.class, 2, 5), //
	FLOAT(Float.TYPE, Float.class, 2, 6), //
	DOUBLE(Double.TYPE, Double.class, 2, 7);

	public final Class<?> primitiveType, wrapperClass;

	private final int minAssignableOrdinal, maxAssignableOrdinal;

	private static final IdentityHashMap<Class<?>, PrimitiveType> map;

	static {
		map = new IdentityHashMap<>(18);
		map.put(Boolean.TYPE, BOOLEAN);
		map.put(Boolean.class, BOOLEAN);
		map.put(Character.TYPE, CHAR);
		map.put(Character.class, CHAR);
		map.put(Byte.TYPE, BYTE);
		map.put(Byte.class, BYTE);
		map.put(Short.TYPE, SHORT);
		map.put(Short.class, SHORT);
		map.put(Integer.TYPE, INT);
		map.put(Integer.class, INT);
		map.put(Long.TYPE, LONG);
		map.put(Long.class, LONG);
		map.put(Float.TYPE, FLOAT);
		map.put(Float.class, FLOAT);
		map.put(Double.TYPE, DOUBLE);
		map.put(Double.class, DOUBLE);
		assert map.size() == 16;
	}

	private PrimitiveType(Class<?> primitiveType, Class<?> wrapperClass, int minAssignableOrdinal, int maxAssignableOrdinal) {
		assert primitiveType.isPrimitive() && !wrapperClass.isPrimitive();
		this.primitiveType = primitiveType;
		this.wrapperClass = wrapperClass;
		this.minAssignableOrdinal = minAssignableOrdinal;
		this.maxAssignableOrdinal = maxAssignableOrdinal;
	}

	public boolean isAssignableFrom(PrimitiveType other) {
		int otherOrdinal = other.ordinal();
		return otherOrdinal >= minAssignableOrdinal && otherOrdinal <= maxAssignableOrdinal;
	}

	public static PrimitiveType of(Class<?> clazz) {
		return map.get(clazz);
	}

	public static Class<?> box(Class<?> clazz) {
		PrimitiveType primitiveType = map.get(clazz);
		return primitiveType == null ? clazz : primitiveType.wrapperClass;
	}

	public static Class<?> unbox(Class<?> clazz) {
		PrimitiveType primitiveType = map.get(clazz);
		return primitiveType == null ? clazz : primitiveType.primitiveType;
	}

}
