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
package com.flowpowered.events.object;

/**
 * Base interface for an object that can accept listeners for a specified event
 *
 * @param <T> The type of event allowed
 */
public interface Eventable<T> {
	/**
	 * Add a listener for the type of event this {@link Eventable} covers.
	 *
	 * @param listener The listener to register.
	 */
	public void registerListener(EventableListener<T> listener);

	/**
	 * Remove all listeners for the event
	 */
	public void unregisterAllListeners();

	/**
	 * Unregister a specific listener
	 *
	 * @param listener The listener to unregister
	 */
	public void unregisterListener(EventableListener<T> listener);

	/**
	 * Call the event with all current listeners.
	 *
	 * @param event The event instance to call
	 */
	public void callEvent(T event);
}
