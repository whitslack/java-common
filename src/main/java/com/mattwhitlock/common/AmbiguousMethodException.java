/*
 * Created on Dec 2, 2017
 */
package com.mattwhitlock.common;

import java.lang.reflect.Executable;
import java.util.Collection;

/**
 * @author Matt Whitlock
 */
public class AmbiguousMethodException extends ReflectiveOperationException {

	private static final long serialVersionUID = 1L;

	public final Collection<? extends Executable> candidates;

	public AmbiguousMethodException(String s, Collection<? extends Executable> candidates) {
		super(s);
		this.candidates = candidates;
	}

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder(super.getMessage()).append("\ncandidates are:");
		for (Executable candidate : candidates) {
			sb.append("\n- ").append(candidate);
		}
		return sb.toString();
	}

}
