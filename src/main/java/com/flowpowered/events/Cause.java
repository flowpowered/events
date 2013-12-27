/*
 * This file is part of Flow Events, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2013 Spout LLC <http://www.spout.org/>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.flowpowered.events;

/**
 * Represents a cause of an event
 *
 * @param <T> source of the cause
 */
public abstract class Cause<T> {
	private static final int MAX_CAUSES = 100;
	private final Cause<?> parent;

	/**
	 * Gets the source of the action
	 */
	public abstract T getSource();

	/**
	 * Constructs a cause with no parent cause
	 */
	public Cause() {
		this(null);
	}

	/**
	 * Constructs a cause with a parent cause that was directly responsible for the action
	 */
	public Cause(Cause<?> parent) {
		this.parent = parent;
	}

	/**
	 * Gets the first cause in the parent-child series of causes that led to this.
	 *
	 * <p>May terminate early to prevent infinite loops</p>
	 *
	 * Note: Can be null if there is no parent
	 *
	 * @return first cause or null if there is no parent
	 */
	public final Cause<?> getFirstCause() {
		int causes = 0;
		Cause<?> main = this;
		while (causes < MAX_CAUSES) {
			if (main.getParent() != null) {
				main = main.getParent();
			} else {
				break;
			}
		}
		return main;
	}

	/**
	 * Gets the parent cause of this cause. <br/><br/> Note: Can be null if there is no parent
	 *
	 * @return parent cause or null if there is no parent
	 */
	public final Cause<?> getParent() {
		return parent;
	}
}

