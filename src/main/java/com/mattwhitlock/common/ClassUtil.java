/*
 * Created on Apr 20, 2005
 */
package com.mattwhitlock.common;

import static com.mattwhitlock.common.NumberUtil.byteValueExact;
import static com.mattwhitlock.common.NumberUtil.doubleValueExact;
import static com.mattwhitlock.common.NumberUtil.floatValueExact;
import static com.mattwhitlock.common.NumberUtil.intValueExact;
import static com.mattwhitlock.common.NumberUtil.longValueExact;
import static com.mattwhitlock.common.NumberUtil.shortValueExact;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Matt Whitlock
 */
public final class ClassUtil {

	public static final Class<?>[] emptyClassArray = {};

	/**
	 * Not instantiable.
	 */
	private ClassUtil() {
	}

	/**
	 * Returns the name of the given class, excluding the package name, but including the names of any outer classes.
	 * Array classes are represented by a recursive application of this method on the
	 * {@linkplain Class#getComponentType() component type}, with a pair of brackets appended.
	 */
	public static String getShortClassName(Class<?> clazz) {
		if (clazz.isArray()) {
			return getShortClassName(clazz.getComponentType()) + "[]";
		}
		String name = clazz.getName();
		return name.substring(name.lastIndexOf('.') + 1);
	}

	/**
	 * Returns all superclasses of the given class. If {@code clazz} does not represent a class, the returned
	 * {@link Stream} will be empty.
	 */
	public static <T> Stream<Class<? super T>> getSuperclasses(Class<T> clazz) {
		return getSuperclasses(clazz, false, null);
	}

	/**
	 * Returns all superclasses of the given class, up to but not including {@code stopClass}. If {@code clazz} does not
	 * represent a class, the returned {@link Stream} will be empty.
	 */
	public static <T> Stream<Class<? super T>> getSuperclasses(Class<T> clazz, Class<? super T> stopClass) {
		return getSuperclasses(clazz, false, stopClass);
	}

	/**
	 * Returns all superclasses of the given class, including the class itself if {@code includeClazz} is {@code true}.
	 * If {@code clazz} does not represent a class and {@code includeClazz} is {@code false}, the returned
	 * {@link Stream} will be empty.
	 */
	public static <T> Stream<Class<? super T>> getSuperclasses(Class<T> clazz, boolean includeClazz) {
		return getSuperclasses(clazz, includeClazz, null);
	}

	/**
	 * Returns all superclasses of the given class, including the class itself if {@code includeClazz} is {@code true},
	 * up to but not including {@code stopClass}. If {@code clazz} does not represent a class and {@code includeClazz}
	 * is {@code false}, the returned {@link Iterable} will have no elements.
	 */
	public static <T> Stream<Class<? super T>> getSuperclasses(Class<T> clazz, boolean includeClazz, Class<? super T> stopClass) {
		assert stopClass == null || stopClass.isAssignableFrom(clazz) : "stopClass (" + stopClass.getCanonicalName() + ") is not assignable from clazz (" + clazz.getCanonicalName() + ')';
		Class<? super T> startClass = includeClazz ? clazz : clazz.getSuperclass();
		return StreamSupport.stream(new Spliterator<Class<? super T>>() {

			private Class<? super T> clazz = startClass;

			@Override
			public boolean tryAdvance(Consumer<? super Class<? super T>> action) {
				if (clazz == stopClass) {
					return false;
				}
				action.accept(clazz);
				clazz = clazz.getSuperclass();
				return true;
			}

			@Override
			public Spliterator<Class<? super T>> trySplit() {
				return null;
			}

			@Override
			public long estimateSize() {
				return Long.MAX_VALUE;
			}

			@Override
			public int characteristics() {
				return ORDERED | DISTINCT | NONNULL | IMMUTABLE;
			}

		}, false);
	}

	/**
	 * Returns all interfaces implemented by the given class or interface, including interfaces implemented by its
	 * superclasses and all transitive superinterfaces. If {@code clazz} represents an interface, it is included in the
	 * returned {@link Set}.
	 */
	public static <T> Set<Class<? super T>> getInterfaces(Class<T> clazz) {
		LinkedHashSet<Class<? super T>> interfaces = new LinkedHashSet<>();
		if (clazz.isInterface()) {
			interfaces.add(clazz);
		}
		getSuperclasses(clazz, true, null).forEach(superclass -> getInterfaces(superclass, interfaces));
		return interfaces;
	}

	/**
	 * Returns all constructors of the given class.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Stream<Constructor<T>> getConstructors(Class<T> clazz) {
		return Arrays.stream((Constructor<T>[]) clazz.getDeclaredConstructors());
	}

	/**
	 * Returns all methods of the given class, including static methods and inherited methods.
	 */
	public static final Stream<Method> getMethods(Class<?> clazz) {
		return getMethods(clazz, null);
	}

	/**
	 * Returns all methods of the given class, including static methods and methods inherited from classes derived from
	 * the given stop class.
	 */
	public static <T> Stream<Method> getMethods(Class<T> clazz, Class<? super T> stopClass) {
		return getSuperclasses(clazz, true, stopClass).flatMap(c -> Arrays.stream(c.getDeclaredMethods()));
	}

	/**
	 * Returns all fields of the given class, including static fields and inherited fields.
	 */
	public static final Stream<Field> getFields(Class<?> clazz) {
		return getFields(clazz, null);
	}

	/**
	 * Returns all fields of the given class, including static fields and fields inherited from classes derived from the
	 * given stop class.
	 */
	public static <T> Stream<Field> getFields(Class<T> clazz, Class<? super T> stopClass) {
		return getSuperclasses(clazz, true, stopClass).flatMap(c -> Arrays.stream(c.getDeclaredFields()));
	}

	/**
	 * Returns all constructors of the given class that have the given modifiers under the given mask.
	 */
	public static <T> Stream<Constructor<T>> getConstructors(Class<T> clazz, int modifierMask, int modifiers) {
		return filterByModifiers(getConstructors(clazz), modifierMask, modifiers);
	}

	/**
	 * Returns all methods of the given class, including static methods and inherited methods, that have the given
	 * modifiers under the given mask.
	 */
	public static final Stream<Method> getMethods(Class<?> clazz, int modifierMask, int modifiers) {
		return getMethods(clazz, null, modifierMask, modifiers);
	}

	/**
	 * Returns all methods of the given class, including static methods and methods inherited from classes derived from
	 * the given stop class, that have the given modifiers under the given mask.
	 */
	public static <T> Stream<Method> getMethods(Class<T> clazz, Class<? super T> stopClass, int modifierMask, int modifiers) {
		return filterByModifiers(getMethods(clazz, stopClass), modifierMask, modifiers);
	}

	/**
	 * Returns all fields of the given class, including static fields and inherited fields, that have the given
	 * modifiers under the given mask.
	 */
	public static final Stream<Field> getFields(Class<?> clazz, int modifierMask, int modifiers) {
		return getFields(clazz, null, modifierMask, modifiers);
	}

	/**
	 * Returns all fields of the given class, including static fields and fields inherited from classes derived from the
	 * given stop class, that have the given modifiers under the given mask.
	 */
	public static <T> Stream<Field> getFields(Class<T> clazz, Class<? super T> stopClass, int modifierMask, int modifiers) {
		return filterByModifiers(getFields(clazz, stopClass), modifierMask, modifiers);
	}

	/**
	 * Returns all public constructors of the given class.
	 */
	public static final <T> Stream<Constructor<T>> getPublicConstructors(Class<T> clazz) {
		return getConstructors(clazz, Modifier.PUBLIC, Modifier.PUBLIC);
	}

	/**
	 * Returns all public methods of the given class, including static methods and inherited methods.
	 */
	public static final Stream<Method> getPublicMethods(Class<?> clazz) {
		return getPublicMethods(clazz, null);
	}

	/**
	 * Returns all public methods of the given class, including static methods and methods inherited from classes
	 * derived from the given stop class.
	 */
	public static final <T> Stream<Method> getPublicMethods(Class<T> clazz, Class<? super T> stopClass) {
		return getMethods(clazz, stopClass, Modifier.PUBLIC, Modifier.PUBLIC);
	}

	/**
	 * Returns all public fields of the given class, including static fields and inherited fields.
	 */
	public static final Stream<Field> getPublicFields(Class<?> clazz) {
		return getPublicFields(clazz, null);
	}

	/**
	 * Returns all public fields of the given class, including static fields and fields inherited from classes derived
	 * from the given stop class.
	 */
	public static final <T> Stream<Field> getPublicFields(Class<T> clazz, Class<? super T> stopClass) {
		return getFields(clazz, stopClass, Modifier.PUBLIC, Modifier.PUBLIC);
	}

	/**
	 * Returns all public static methods of the given class, including inherited static methods.
	 */
	public static final Stream<Method> getPublicStaticMethods(Class<?> clazz) {
		return getPublicStaticMethods(clazz, null);
	}

	/**
	 * Returns all public static methods of the given class, including static methods inherited from classes derived
	 * from the given stop class.
	 */
	public static final <T> Stream<Method> getPublicStaticMethods(Class<T> clazz, Class<? super T> stopClass) {
		return getMethods(clazz, stopClass, Modifier.PUBLIC | Modifier.STATIC, Modifier.PUBLIC | Modifier.STATIC);
	}

	/**
	 * Returns all public static fields of the given class, including inherited static fields.
	 */
	public static final Stream<Field> getPublicStaticFields(Class<?> clazz) {
		return getPublicStaticFields(clazz, null);
	}

	/**
	 * Returns all public static fields of the given class, including static fields inherited from classes derived from
	 * the given stop class.
	 */
	public static final <T> Stream<Field> getPublicStaticFields(Class<T> clazz, Class<? super T> stopClass) {
		return getFields(clazz, stopClass, Modifier.PUBLIC | Modifier.STATIC, Modifier.PUBLIC | Modifier.STATIC);
	}

	/**
	 * Returns all public instance methods of the given class, including inherited instance methods.
	 */
	public static final Stream<Method> getPublicInstanceMethods(Class<?> clazz) {
		return getPublicInstanceMethods(clazz, null);
	}

	/**
	 * Returns all public instance methods of the given class, including instance methods inherited from classes derived
	 * from the given stop class.
	 */
	public static final <T> Stream<Method> getPublicInstanceMethods(Class<T> clazz, Class<? super T> stopClass) {
		return getMethods(clazz, stopClass, Modifier.PUBLIC | Modifier.STATIC, Modifier.PUBLIC);
	}

	/**
	 * Returns all public instance fields of the given class, including inherited instance fields.
	 */
	public static final Stream<Field> getPublicInstanceFields(Class<?> clazz) {
		return getPublicInstanceFields(clazz, null);
	}

	/**
	 * Returns all public instance fields of the given class, including instance fields inherited from classes derived
	 * from the given stop class.
	 */
	public static final <T> Stream<Field> getPublicInstanceFields(Class<T> clazz, Class<? super T> stopClass) {
		return getFields(clazz, stopClass, Modifier.PUBLIC | Modifier.STATIC, Modifier.PUBLIC);
	}

	/**
	 * Returns all {@linkplain #getPublicConstructors(Class) public constructors} of the given class that are
	 * {@linkplain #isApplicable(Executable, Class...) applicable} for the given argument types. A {@code null} element
	 * in {@code argTypes} matches a formal parameter of any reference type.
	 */
	public static <T> Stream<Constructor<T>> findApplicableConstructors(Class<T> clazz, Class<?>... argTypes) {
		return filterByArgTypes(getPublicConstructors(clazz), argTypes);
	}

	/**
	 * Returns all {@linkplain #getPublicStaticMethods(Class) public static methods} of the given class, including
	 * inherited static methods, that have the specified name and are {@linkplain #isApplicable(Executable, Class...)
	 * applicable} for the given argument types. A {@code null} element in {@code argTypes} matches a formal parameter
	 * of any reference type.
	 */
	public static Stream<Method> findApplicableStaticMethods(Class<?> clazz, String methodName, Class<?>... argTypes) {
		return filterByArgTypes(filterByName(getPublicStaticMethods(clazz), methodName), argTypes);
	}

	/**
	 * Returns all {@linkplain #getPublicInstanceMethods(Class) public instance methods} of the given class, including
	 * inherited instance methods, that have the specified name and are {@linkplain #isApplicable(Executable, Class...)
	 * applicable} for the given argument types. A {@code null} element in {@code argTypes} matches a formal parameter
	 * of any reference type.
	 */
	public static Stream<Method> findApplicableInstanceMethods(Class<?> clazz, String methodName, Class<?>... argTypes) {
		return filterByArgTypes(filterByName(getPublicInstanceMethods(clazz), methodName), argTypes);
	}

	/**
	 * Returns the most specific public constructor of the given class that is
	 * {@linkplain #isApplicable(Executable, Class...) applicable} for the given argument types. A {@code null} element
	 * in {@code argTypes} matches a formal parameter of any reference type.
	 * 
	 * @throws NoSuchMethodException
	 *         if no public constructor is applicable.
	 * @throws AmbiguousMethodException
	 *         if multiple public constructors are applicable and maximally specific.
	 */
	public static <T> Constructor<T> findMostSpecificConstructor(Class<T> clazz, Class<?>... argTypes) throws NoSuchMethodException, AmbiguousMethodException {
		Constructor<T> constructor = findMostSpecificExecutable(findApplicableConstructors(clazz, argTypes), argTypes);
		if (constructor == null) {
			throw new NoSuchMethodException(buildNoSuchMethodMessage(clazz, clazz.getSimpleName(), argTypes));
		}
		return constructor;
	}

	/**
	 * Returns the most specific public static method of the given class that has the specified name and is
	 * {@linkplain #isApplicable(Executable, Class...) applicable} for the given argument types. A {@code null} element
	 * in {@code argTypes} matches a formal parameter of any reference type.
	 * 
	 * @throws NoSuchMethodException
	 *         if no public static method with the specified name is applicable.
	 * @throws AmbiguousMethodException
	 *         if multiple public static methods with the specified name are applicable and maximally specific.
	 */
	public static Method findMostSpecificStaticMethod(Class<?> clazz, String methodName, Class<?>... argTypes) throws NoSuchMethodException, AmbiguousMethodException {
		Method method = findMostSpecificExecutable(findApplicableStaticMethods(clazz, methodName, argTypes), argTypes);
		if (method == null) {
			throw new NoSuchMethodException(buildNoSuchMethodMessage(clazz, methodName, argTypes));
		}
		return method;
	}

	/**
	 * Returns the most specific public instance method of the given class that has the specified name and is
	 * {@linkplain #isApplicable(Executable, Class...) applicable} for the given argument types. A {@code null} element
	 * in {@code argTypes} matches a formal parameter of any reference type.
	 * 
	 * @throws NoSuchMethodException
	 *         if no public instance method with the specified name is applicable.
	 * @throws AmbiguousMethodException
	 *         if multiple public instance methods with the specified name are applicable and maximally specific.
	 */
	public static Method findMostSpecificInstanceMethod(Class<?> clazz, String methodName, Class<?>... argTypes) throws NoSuchMethodException, AmbiguousMethodException {
		Method method = findMostSpecificExecutable(findApplicableInstanceMethods(clazz, methodName, argTypes), argTypes);
		if (method == null) {
			throw new NoSuchMethodException(buildNoSuchMethodMessage(clazz, methodName, argTypes));
		}
		return method;
	}

	/**
	 * {@linkplain Constructor#newInstance(Object...) Invokes} on the given class the
	 * {@linkplain #findMostSpecificConstructor(Class, Class...) most specific public constructor} that is
	 * {@linkplain #isApplicable(Executable, Class...) applicable} for the given arguments.
	 * 
	 * @throws NoSuchMethodException
	 *         if no public constructor is applicable.
	 * @throws AmbiguousMethodException
	 *         if multiple public constructors are applicable and maximally specific.
	 * @throws InvocationTargetException
	 *         if the invoked constructor throws an exception.
	 */
	public static <T> T invokeConstructor(Class<T> clazz, Object... args) throws NoSuchMethodException, AmbiguousMethodException, InstantiationException, InvocationTargetException {
		try {
			return invokeConstructor(findMostSpecificConstructor(clazz, getTypes(args)), args);
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * {@linkplain Method#invoke(Object, Object...) Invokes} on the given class the
	 * {@linkplain #findMostSpecificStaticMethod(Class, String, Class...) most specific public static method} that has
	 * the specified name and is {@linkplain #isApplicable(Executable, Class...) applicable} for the given arguments.
	 * 
	 * @throws NoSuchMethodException
	 *         if no public static method with the specified name is applicable.
	 * @throws AmbiguousMethodException
	 *         if multiple public static methods with the specified name are applicable and maximally specific.
	 * @throws InvocationTargetException
	 *         if the invoked method throws an exception.
	 */
	public static Object invokeStaticMethod(Class<?> clazz, String methodName, Object... args) throws NoSuchMethodException, AmbiguousMethodException, InvocationTargetException {
		try {
			return invokeMethod(findMostSpecificStaticMethod(clazz, methodName, getTypes(args)), null, args);
		}
		catch (IllegalAccessException impossible) {
			throw new RuntimeException(impossible);
		}
	}

	/**
	 * {@linkplain Method#invoke(Object, Object...) Invokes} on the given object the
	 * {@linkplain #findMostSpecificInstanceMethod(Class, String, Class...) most specific public instance method} that
	 * has the specified name and is {@linkplain #isApplicable(Executable, Class...) applicable} for the given
	 * arguments.
	 * 
	 * @throws NoSuchMethodException
	 *         if no public instance method with the specified name is applicable.
	 * @throws AmbiguousMethodException
	 *         if multiple public instance methods with the specified name are applicable and maximally specific.
	 * @throws InvocationTargetException
	 *         if the invoked method throws an exception.
	 */
	public static Object invokeInstanceMethod(Object obj, String methodName, Object... args) throws NoSuchMethodException, AmbiguousMethodException, InvocationTargetException {
		try {
			return invokeMethod(findMostSpecificInstanceMethod(obj.getClass(), methodName, getTypes(args)), obj, args);
		}
		catch (IllegalAccessException impossible) {
			throw new RuntimeException(impossible);
		}
	}

	/**
	 * {@linkplain Constructor#newInstance(Object...) Invokes} the given {@link Constructor}, collecting variable arity
	 * arguments as appropriate.
	 */
	public static <T> T invokeConstructor(Constructor<T> constructor, Object... args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return constructor.newInstance(collectVarArgs(constructor, args));
	}

	/**
	 * {@linkplain Method#invoke(Object, Object...) Invokes} the given {@link Method}, collecting variable arity
	 * arguments as appropriate.
	 */
	public static Object invokeMethod(Method method, Object obj, Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return method.invoke(obj, collectVarArgs(method, args));
	}

	/**
	 * Returns whether a variable of type {@code to} may be assigned from a value of type {@code from}, allowing for
	 * boxing/unboxing and widening conversions. This method more faithfully models
	 * <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-5.html#jls-5.2"><em>assignment contexts</em></a>
	 * than {@link Class#isAssignableFrom(Class)} does, as the latter method allows only for reference widening
	 * conversions but not for boxing/unboxing or primitive widening conversions.
	 * 
	 * <table border="1">
	 * <thead>
	 * <tr>
	 * <th>&darr; {@code from \ to} &rarr;</th>
	 * <th>{@link Number}</th>
	 * <th>{@link Long}</th>
	 * <th>{@link Long#TYPE long}</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <th>{@code null}</th>
	 * <td>{@code true}</td>
	 * <td>{@code true}</td>
	 * <td>{@code false}</td>
	 * </tr>
	 * <tr>
	 * <th>{@link Number}</th>
	 * <td>{@code true}</td>
	 * <td>{@code false}</td>
	 * <td>{@code false}</td>
	 * </tr>
	 * <tr>
	 * <th>{@link Integer}</th>
	 * <td>{@code true}</td>
	 * <td>{@code false}</td>
	 * <td>{@code true}</td>
	 * </tr>
	 * <tr>
	 * <th>{@link Integer#TYPE int}</th>
	 * <td>{@code true}</td>
	 * <td>{@code false}</td>
	 * <td>{@code true}</td>
	 * </tr>
	 * <tr>
	 * <th>{@link Long}</th>
	 * <td>{@code true}</td>
	 * <td>{@code true}</td>
	 * <td>{@code true}</td>
	 * </tr>
	 * <tr>
	 * <th>{@link Long#TYPE long}</th>
	 * <td>{@code true}</td>
	 * <td>{@code true}</td>
	 * <td>{@code true}</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 */
	public static boolean isAssignable(Class<?> to, Class<?> from) {
		PrimitiveType toPrimitiveType, fromPrimitiveType;
		return to == from || (to.isPrimitive() ? from != null && (toPrimitiveType = PrimitiveType.of(to)) != null && (fromPrimitiveType = PrimitiveType.of(from)) != null && toPrimitiveType.isAssignableFrom(fromPrimitiveType) : from == null || to.isAssignableFrom(PrimitiveType.box(from)));
	}

	/**
	 * Returns whether the given {@link Executable} is applicable to an invocation with the specified argument types.
	 * 
	 * @see #getTypes(Object...)
	 */
	public static boolean isApplicable(Executable executable, Class<?>... argTypes) {
		Class<?>[] paramTypes = executable.getParameterTypes();
		int n = paramTypes.length, m = argTypes.length;
		if (executable.isVarArgs()) {
			if (--n > m) {
				return false;
			}
			Class<?> componentType = paramTypes[n].getComponentType();
			assert componentType != null;
			for (int i = n; i < m; ++i) {
				if (!isAssignable(componentType, argTypes[i]) && ++n != m) {
					return false;
				}
			}
		}
		else if (n != m) {
			return false;
		}
		for (int i = 0; i < n; ++i) {
			if (!isAssignable(paramTypes[i], argTypes[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns an array containing the types of the given objects. The returned array will have the same length as the
	 * provided array, and a {@code null} element in the provided array will translate to a {@code null} element in the
	 * returned array.
	 */
	public static Class<?>[] getTypes(Object... objects) {
		if (objects == null || objects.length == 0) {
			return emptyClassArray;
		}
		Class<?>[] types = new Class<?>[objects.length];
		for (int i = 0, n = objects.length; i < n; ++i) {
			Object object = objects[i];
			if (object != null) {
				types[i] = object.getClass();
			}
		}
		return types;
	}

	private static <T, S extends T> void getInterfaces(Class<T> clazz, LinkedHashSet<Class<? super S>> interfaces) {
		@SuppressWarnings("unchecked")
		Class<? super T>[] clazzInterfaces = (Class<? super T>[]) clazz.getInterfaces();
		int n = clazzInterfaces.length;
		for (int i = 0; i < n; ++i) {
			if (!interfaces.add(clazzInterfaces[i])) {
				clazzInterfaces[i] = null;
			}
		}
		for (int i = 0; i < n; ++i) {
			Class<? super T> interfaze = clazzInterfaces[i];
			if (interfaze != null) {
				getInterfaces(interfaze, interfaces);
			}
		}
	}

	private static <M extends Member> Stream<M> filterByModifiers(Stream<M> candidates, int modifierMask, int modifiers) {
		return candidates.filter(candidate -> (candidate.getModifiers() & modifierMask) == modifiers);
	}

	private static <M extends Member> Stream<M> filterByName(Stream<M> candidates, String name) {
		return candidates.filter(candidate -> name.equals(candidate.getName()));
	}

	private static <E extends Executable> Stream<E> filterByArgTypes(Stream<E> candidates, Class<?>[] argTypes) {
		return candidates.filter(candidate -> isApplicable(candidate, argTypes));
	}

	/**
	 * @see <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html#jls-15.12.2.5">The Java Language
	 *      Specification, Java SE 8 Edition, &sect;15.12.2.5, Choosing the Most Specific Method</a>
	 */
	public static <E extends Executable> E findMostSpecificExecutable(Stream<E> candidates, Class<?>... argTypes) throws AmbiguousMethodException {
		ArrayList<E> maximallySpecific = new ArrayList<>(1);
		int bestPhase = 3;
		candidates: for (Iterator<E> it = candidates.iterator(); it.hasNext();) {
			E candidate = it.next();
			assert isApplicable(candidate, argTypes);
			int phase;
			Class<?>[] paramTypes = candidate.getParameterTypes();
			int n = paramTypes.length;
			if (n == argTypes.length && (!candidate.isVarArgs() || isAssignable(paramTypes[n - 1], argTypes[n - 1]))) {
				phase = 1;
				for (int i = 0; i < n; ++i) {
					Class<?> argType = argTypes[i];
					if (paramTypes[i].isPrimitive() != (argType != null && argType.isPrimitive())) {
						phase = 2;
						break;
					}
				}
			}
			else {
				assert candidate.isVarArgs();
				phase = 3;
			}
			if (phase <= bestPhase) {
				if (phase < bestPhase) {
					bestPhase = phase;
					maximallySpecific.clear();
				}
				else if (!maximallySpecific.isEmpty()) {
					boolean variableArity = phase == 3;
					int k = maximallySpecific.size();
					for (int i = 0; i < k; ++i) {
						E other = maximallySpecific.get(i);
						if (isStrictlyMoreSpecific(other, candidate, variableArity, argTypes.length)) {
							continue candidates;
						}
					}
					for (int i = 0; i < k;) {
						E other = maximallySpecific.get(i);
						if (isStrictlyMoreSpecific(candidate, other, variableArity, argTypes.length)) {
							swapWithLastAndDelete(maximallySpecific, i);
							--k;
						}
						else {
							++i;
						}
					}
				}
				maximallySpecific.add(candidate);
			}
		}
		int count = maximallySpecific.size();
		if (count > 1) {
			throw new AmbiguousMethodException(appendArgumentTypes(new StringBuilder("with argument types "), argTypes).toString(), maximallySpecific);
		}
		return count == 0 ? null : maximallySpecific.get(0);
	}

	/**
	 * @see <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html#jls-15.12.2.5">The Java Language
	 *      Specification, Java SE 8 Edition, &sect;15.12.2.5, Choosing the Most Specific Method</a>
	 */
	private static boolean isStrictlyMoreSpecific(Executable which, Executable than, boolean variableArity, int argCount) {
		return isMoreSpecific(which, than, variableArity, argCount) && !isMoreSpecific(than, which, variableArity, argCount);
	}

	/**
	 * @see <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html#jls-15.12.2.5">The Java Language
	 *      Specification, Java SE 8 Edition, &sect;15.12.2.5, Choosing the Most Specific Method</a>
	 */
	private static boolean isMoreSpecific(Executable which, Executable than, boolean variableArity, int argCount) {
		Class<?>[] whichParamTypes = which.getParameterTypes(), thanParamTypes = than.getParameterTypes();
		if (variableArity) {
			for (int i = 0; i < argCount; ++i) {
				if (!isAssignable(getVariableArityParameterType(thanParamTypes, i), getVariableArityParameterType(whichParamTypes, i))) {
					return false;
				}
			}
			return thanParamTypes.length != argCount + 1 || isAssignable(getVariableArityParameterType(thanParamTypes, argCount), getVariableArityParameterType(whichParamTypes, argCount));
		}
		assert whichParamTypes.length == thanParamTypes.length;
		for (int i = 0; i < whichParamTypes.length; ++i) {
			if (!isAssignable(thanParamTypes[i], whichParamTypes[i])) {
				return false;
			}
		}
		return than.getDeclaringClass().isAssignableFrom(which.getDeclaringClass()) && (!(which instanceof Method) || !(than instanceof Method) || ((Method) than).getReturnType().isAssignableFrom(((Method) which).getReturnType()));
	}

	/**
	 * @see <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html#jls-15.12.2.4">The Java Language
	 *      Specification, Java SE 8 Edition, &sect;15.12.2.4, Phase 3: Identify Methods Applicable by Variable Arity
	 *      Invocation</a>
	 */
	private static Class<?> getVariableArityParameterType(Class<?>[] paramTypes, int i) {
		int n = paramTypes.length;
		return i < n - 1 ? paramTypes[i] : paramTypes[n - 1].getComponentType();
	}

	private static Object[] collectVarArgs(Executable executable, Object[] args) {
		if (!executable.isVarArgs()) {
			return args;
		}
		Class<?>[] paramTypes = executable.getParameterTypes();
		int n = paramTypes.length;
		assert n > 0;
		Object arg;
		if (n == args.length && ((arg = args[n - 1]) == null || isAssignable(paramTypes[n - 1], arg.getClass()))) {
			return args;
		}
		Object[] converted = Arrays.copyOf(args, n--);
		Class<?>[] argTypes = getTypes(args);
		int k = argTypes.length - n;
		Class<?> componentType = paramTypes[n].getComponentType();
		PrimitiveType componentPrimitiveType = PrimitiveType.of(componentType);
		try {
			if (componentPrimitiveType == null) {
				Object varargs = Array.newInstance(componentType, k);
				System.arraycopy(args, n, varargs, 0, k);
				converted[n] = varargs;
			}
			else {
				if (componentType.isPrimitive()) {
					switch (componentPrimitiveType) {
						case BOOLEAN: {
							boolean[] varargs = new boolean[k];
							for (int i = 0; i < k; ++i) {
								varargs[i] = (Boolean) args[n + i];
							}
							converted[n] = varargs;
							break;
						}
						case CHAR: {
							char[] varargs = new char[k];
							for (int i = 0; i < k; ++i) {
								varargs[i] = (Character) args[n + i];
							}
							converted[n] = varargs;
							break;
						}
						case BYTE: {
							byte[] varargs = new byte[k];
							for (int i = 0; i < k; ++i) {
								varargs[i] = (arg = args[n + i]) instanceof Byte ? (Byte) arg : byteValueExact((Number) arg);
							}
							converted[n] = varargs;
							break;
						}
						case SHORT: {
							short[] varargs = new short[k];
							for (int i = 0; i < k; ++i) {
								varargs[i] = (arg = args[n + i]) instanceof Short ? (Short) arg : shortValueExact((Number) arg);
							}
							converted[n] = varargs;
							break;
						}
						case INT: {
							int[] varargs = new int[k];
							for (int i = 0; i < k; ++i) {
								varargs[i] = (arg = args[n + i]) instanceof Integer ? (Integer) arg : intValueExact((Number) arg);
							}
							converted[n] = varargs;
							break;
						}
						case LONG: {
							long[] varargs = new long[k];
							for (int i = 0; i < k; ++i) {
								varargs[i] = (arg = args[n + i]) instanceof Long ? (Long) arg : longValueExact((Number) arg);
							}
							converted[n] = varargs;
							break;
						}
						case FLOAT: {
							float[] varargs = new float[k];
							for (int i = 0; i < k; ++i) {
								varargs[i] = (arg = args[n + i]) instanceof Float ? (Float) arg : floatValueExact((Number) arg);
							}
							converted[n] = varargs;
							break;
						}
						case DOUBLE: {
							double[] varargs = new double[k];
							for (int i = 0; i < k; ++i) {
								varargs[i] = (arg = args[n + i]) instanceof Double ? (Double) arg : doubleValueExact((Number) arg);
							}
							converted[n] = varargs;
							break;
						}
					}
				}
				else {
					switch (componentPrimitiveType) {
						case BOOLEAN: {
							Boolean[] varargs = new Boolean[k];
							System.arraycopy(args, n, varargs, 0, k);
							converted[n] = varargs;
							break;
						}
						case CHAR: {
							Character[] varargs = new Character[k];
							System.arraycopy(args, n, varargs, 0, k);
							converted[n] = varargs;
							break;
						}
						case BYTE: {
							Byte[] varargs = new Byte[k];
							for (int i = 0; i < k; ++i) {
								varargs[i] = (arg = args[n + i]) instanceof Byte ? (Byte) arg : Byte.valueOf(byteValueExact((Number) arg));
							}
							converted[n] = varargs;
							break;
						}
						case SHORT: {
							Short[] varargs = new Short[k];
							for (int i = 0; i < k; ++i) {
								varargs[i] = (arg = args[n + i]) instanceof Short ? (Short) arg : Short.valueOf(shortValueExact((Number) arg));
							}
							converted[n] = varargs;
							break;
						}
						case INT: {
							Integer[] varargs = new Integer[k];
							for (int i = 0; i < k; ++i) {
								varargs[i] = (arg = args[n + i]) instanceof Integer ? (Integer) arg : Integer.valueOf(intValueExact((Number) arg));
							}
							converted[n] = varargs;
							break;
						}
						case LONG: {
							Long[] varargs = new Long[k];
							for (int i = 0; i < k; ++i) {
								varargs[i] = (arg = args[n + i]) instanceof Long ? (Long) arg : Long.valueOf(longValueExact((Number) arg));
							}
							converted[n] = varargs;
							break;
						}
						case FLOAT: {
							Float[] varargs = new Float[k];
							for (int i = 0; i < k; ++i) {
								varargs[i] = (arg = args[n + i]) instanceof Float ? (Float) arg : Float.valueOf(floatValueExact((Number) arg));
							}
							converted[n] = varargs;
							break;
						}
						case DOUBLE: {
							Double[] varargs = new Double[k];
							for (int i = 0; i < k; ++i) {
								varargs[i] = (arg = args[n + i]) instanceof Double ? (Double) arg : Double.valueOf(doubleValueExact((Number) arg));
							}
							converted[n] = varargs;
							break;
						}
					}
				}
			}
			return converted;
		}
		catch (Exception e) {
			throw new IllegalArgumentException(appendArgumentTypes(new StringBuilder(executable.toString()).append(" is not applicable for arguments "), argTypes).toString(), e);
		}
	}

	private static String buildNoSuchMethodMessage(Class<?> clazz, String methodName, Class<?>[] argTypes) {
		return appendArgumentTypes(new StringBuilder(clazz.getCanonicalName()).append('.').append(methodName), argTypes).toString();
	}

	private static StringBuilder appendArgumentTypes(StringBuilder sb, Class<?>[] argTypes) {
		sb.append('(');
		if (argTypes.length > 0) {
			for (int i = 0;;) {
				Class<?> argType = argTypes[i];
				sb.append(argType == null ? null : argType.getCanonicalName());
				if (++i == argTypes.length) {
					break;
				}
				sb.append(", ");
			}
		}
		sb = sb.append(')');
		return sb;
	}

	private static <T> void swapWithLastAndDelete(ArrayList<T> list, int i) {
		int last = list.size() - 1;
		list.set(i, list.get(last));
		list.remove(last);
	}

}
