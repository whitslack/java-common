/*
 * Created on Aug 26, 2006
 */
package com.mattwhitlock.common;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EventListener;

/**
 * @author Matt Whitlock
 */
public final class Beans {

	/**
	 * Not instantiable.
	 */
	private Beans() {
	}

	public static BeanInfo getBeanInfo(Class<?> beanClass) {
		try {
			return Introspector.getBeanInfo(beanClass);
		}
		catch (IntrospectionException e) {
			throw new RuntimeException(e);
		}
	}

	public static BeanInfo getBeanInfo(Class<?> beanClass, Class<?> stopClass) {
		try {
			return Introspector.getBeanInfo(beanClass, stopClass);
		}
		catch (IntrospectionException e) {
			throw new RuntimeException(e);
		}
	}

	public static BeanDescriptor getBeanDescriptor(Class<?> beanClass) {
		return getBeanInfo(beanClass).getBeanDescriptor();
	}

	public static EventSetDescriptor[] getEventSetDescriptors(Class<?> beanClass) {
		return getBeanInfo(beanClass).getEventSetDescriptors();
	}

	public static PropertyDescriptor[] getPropertyDescriptors(Class<?> beanClass) {
		return getBeanInfo(beanClass).getPropertyDescriptors();
	}

	public static MethodDescriptor[] getMethodDescriptors(Class<?> beanClass) {
		return getBeanInfo(beanClass).getMethodDescriptors();
	}

	public static BeanDescriptor getBeanDescriptor(Class<?> beanClass, Class<?> stopClass) {
		return getBeanInfo(beanClass, stopClass).getBeanDescriptor();
	}

	public static EventSetDescriptor[] getEventSetDescriptors(Class<?> beanClass, Class<?> stopClass) {
		return getBeanInfo(beanClass, stopClass).getEventSetDescriptors();
	}

	public static PropertyDescriptor[] getPropertyDescriptors(Class<?> beanClass, Class<?> stopClass) {
		return getBeanInfo(beanClass, stopClass).getPropertyDescriptors();
	}

	public static MethodDescriptor[] getMethodDescriptors(Class<?> beanClass, Class<?> stopClass) {
		return getBeanInfo(beanClass, stopClass).getMethodDescriptors();
	}

	public static EventSetDescriptor getEventSetDescriptor(Class<?> beanClass, String eventSetName) {
		for (EventSetDescriptor eventSetDescriptor : getEventSetDescriptors(beanClass)) {
			if (eventSetName.equals(eventSetDescriptor.getName())) {
				return eventSetDescriptor;
			}
		}
		return null;
	}

	public static PropertyDescriptor getPropertyDescriptor(Class<?> beanClass, String propertyName) {
		for (PropertyDescriptor propertyDescriptor : getPropertyDescriptors(beanClass)) {
			if (propertyName.equals(propertyDescriptor.getName())) {
				return propertyDescriptor;
			}
		}
		return null;
	}

	public static MethodDescriptor getMethodDescriptor(Class<?> beanClass, Method method) {
		for (MethodDescriptor methodDescriptor : getMethodDescriptors(beanClass)) {
			if (method.equals(methodDescriptor.getMethod())) {
				return methodDescriptor;
			}
		}
		return null;
	}

	public static boolean addListener(Object bean, String eventSetName, EventListener listener) {
		EventSetDescriptor eventSetDescriptor = getEventSetDescriptor(bean.getClass(), eventSetName);
		if (eventSetDescriptor == null) {
			return false;
		}
		Method addListenerMethod = eventSetDescriptor.getAddListenerMethod();
		if (addListenerMethod == null) {
			return false;
		}
		try {
			addListenerMethod.invoke(bean, listener);
			return true;
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			throw cause instanceof RuntimeException ? (RuntimeException) cause : new RuntimeException(cause);
		}
	}

	public static boolean removeListener(Object bean, String eventSetName, EventListener listener) {
		EventSetDescriptor eventSetDescriptor = getEventSetDescriptor(bean.getClass(), eventSetName);
		if (eventSetDescriptor == null) {
			return false;
		}
		Method removeListenerMethod = eventSetDescriptor.getRemoveListenerMethod();
		if (removeListenerMethod == null) {
			return false;
		}
		try {
			removeListenerMethod.invoke(bean, listener);
			return true;
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			throw cause instanceof RuntimeException ? (RuntimeException) cause : new RuntimeException(cause);
		}
	}

	public static EventListener[] getListeners(Object bean, String eventSetName) {
		EventSetDescriptor eventSetDescriptor = getEventSetDescriptor(bean.getClass(), eventSetName);
		if (eventSetDescriptor == null) {
			return null;
		}
		Method getListenerMethod = eventSetDescriptor.getGetListenerMethod();
		if (getListenerMethod == null) {
			return null;
		}
		try {
			return (EventListener[]) getListenerMethod.invoke(bean, (Object[]) null);
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			throw cause instanceof RuntimeException ? (RuntimeException) cause : new RuntimeException(cause);
		}
	}

	public static boolean addPropertyChangeListener(Object bean, PropertyChangeListener propertyChangeListener) {
		try {
			bean.getClass().getMethod("addPropertyChangeListener", PropertyChangeListener.class).invoke(bean, propertyChangeListener);
			return true;
		}
		catch (NoSuchMethodException e) {
			return false;
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			throw cause instanceof RuntimeException ? (RuntimeException) cause : new RuntimeException(cause);
		}
	}

	public static boolean removePropertyChangeListener(Object bean, PropertyChangeListener propertyChangeListener) {
		try {
			bean.getClass().getMethod("removePropertyChangeListener", PropertyChangeListener.class).invoke(bean, propertyChangeListener);
			return true;
		}
		catch (NoSuchMethodException e) {
			return false;
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			throw cause instanceof RuntimeException ? (RuntimeException) cause : new RuntimeException(cause);
		}
	}

	public static PropertyChangeListener[] getPropertyChangeListeners(Object bean) {
		try {
			Method method = bean.getClass().getMethod("getPropertyChangeListeners", (Class<?>[]) null);
			Class<?> returnType = method.getReturnType();
			if (!returnType.isArray() || returnType.getComponentType() != PropertyChangeListener.class) {
				return null;
			}
			return (PropertyChangeListener[]) method.invoke(bean, (Object[]) null);
		}
		catch (NoSuchMethodException e) {
			return null;
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			throw cause instanceof RuntimeException ? (RuntimeException) cause : new RuntimeException(cause);
		}
	}

	public static boolean addPropertyChangeListener(Object bean, String propertyName, PropertyChangeListener propertyChangeListener) {
		try {
			bean.getClass().getMethod("addPropertyChangeListener", String.class, PropertyChangeListener.class).invoke(bean, propertyName, propertyChangeListener);
			return true;
		}
		catch (NoSuchMethodException e) {
			return false;
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			throw cause instanceof RuntimeException ? (RuntimeException) cause : new RuntimeException(cause);
		}
	}

	public static boolean removePropertyChangeListener(Object bean, String propertyName, PropertyChangeListener propertyChangeListener) {
		try {
			bean.getClass().getMethod("removePropertyChangeListener", String.class, PropertyChangeListener.class).invoke(bean, propertyName, propertyChangeListener);
			return true;
		}
		catch (NoSuchMethodException e) {
			return false;
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			throw cause instanceof RuntimeException ? (RuntimeException) cause : new RuntimeException(cause);
		}
	}

	public static PropertyChangeListener[] getPropertyChangeListeners(Object bean, String propertyName) {
		try {
			Method method = bean.getClass().getMethod("getPropertyChangeListeners", String.class);
			Class<?> returnType = method.getReturnType();
			if (!returnType.isArray() || returnType.getComponentType() != PropertyChangeListener.class) {
				return null;
			}
			return (PropertyChangeListener[]) method.invoke(bean, propertyName);
		}
		catch (NoSuchMethodException e) {
			return null;
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			throw cause instanceof RuntimeException ? (RuntimeException) cause : new RuntimeException(cause);
		}
	}

}
