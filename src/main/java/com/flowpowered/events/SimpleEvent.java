/*
 * This file is part of Flow Events, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2013 Spout LLC <https://spout.org/>
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

import com.flowpowered.events.Event.VoidParameter;

/**
 * Represents a callable event.
 */
public abstract class SimpleEvent extends Event<VoidParameter> {
    public SimpleEvent() {
        parameter = new VoidParameter();
    }

    /**
     * Set cancelled status. Events which wish to be cancellable should implement Cancellable and implement setCancelled as: <p>
     * <pre>
     * public void setCancelled(boolean cancelled) {
     *     super.setCancelled(cancelled);
     * }
     * </pre>
     *
     * @param cancelled True to cancel event
     */
    protected void setCancelled(boolean cancelled) {
         parameter.setCancelled(cancelled);
    }

    /**
     * Returning true will prevent calling any even {@link Order}ed slots.
     *
     * @return false if the event is propogating; events which do not implement Cancellable should never return true here.
     * @see Order
     */
    public boolean isCancelled() {
        return parameter.isCancelled();
    }
}
