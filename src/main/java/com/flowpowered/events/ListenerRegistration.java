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

import java.util.Objects;

/**
 * Represents an {@link EventExecutor}'s registration.
 */
public class ListenerRegistration {
    private final EventExecutor executor;
    private final Order orderSlot;
    private final Object owner;

    /**
     * @param executor Listener this registration represents
     * @param orderSlot Order position this registration is in
     * @param owner object that created this registration
     */
    public ListenerRegistration(final EventExecutor executor, final Order orderSlot, final Object owner) {
        this.executor = executor;
        this.orderSlot = orderSlot;
        this.owner = owner;
    }

    /**
     * Gets the listener for this registration
     *
     * @return Registered Listener
     */
    public EventExecutor getExecutor() {
        return executor;
    }

    /**
     * Gets the {@link Object} for this registration
     *
     * @return Registered owner
     */
    public Object getOwner() {
        return owner;
    }

    /**
     * Gets the order slot for this registration
     *
     * @return Registered order
     */
    public Order getOrder() {
        return orderSlot;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.executor);
        hash = 97 * hash + Objects.hashCode(this.orderSlot);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ListenerRegistration other = (ListenerRegistration) obj;
        if (!Objects.equals(this.executor, other.executor)) {
            return false;
        }
        if (this.orderSlot != other.orderSlot) {
            return false;
        }
        return true;
    }

}
