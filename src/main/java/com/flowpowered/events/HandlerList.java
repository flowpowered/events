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

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A list of event handlers, stored per-event.
 */
public final class HandlerList {
    /**
     * Handler array. This field being an array is the key to this system's speed.
     */
    private ListenerRegistration[] handlers = null;
    /**
     * Returns the Dynamic handler lists.<br> These are changed using register() and unregister()<br> Changes automatically baked to the handlers array any time they have changed..
     */
    private final EnumMap<Order, List<ListenerRegistration>> handlerSlots;
    private final CopyOnWriteArrayList<HandlerList> children = new CopyOnWriteArrayList<>(); // Not modified that much, it's fine
    private final HandlerList parent;

    /**
     * Create a new handler list and initialize using EventPriority The HandlerList is then added to meta-list for use in bakeAll()
     */
    public HandlerList() {
        this(null);
    }

    public HandlerList(HandlerList parent) {
        handlerSlots = new EnumMap<>(Order.class);
        for (Order o : Order.values()) {
            handlerSlots.put(o, new ArrayList<ListenerRegistration>());
        }

        this.parent = parent;
        if (parent != null) {
            parent.addChild(this);
        }
    }

    /**
     * Register a new listener in this handler list
     *
     * @param listener listener to register
     */
    public void register(ListenerRegistration listener) {
        if (handlerSlots.get(listener.getOrder()).contains(listener)) {
            throw new IllegalStateException("This listener is already registered to priority " + listener.getOrder().toString());
        }
        dirty();
        handlerSlots.get(listener.getOrder()).add(listener);
    }

    public void registerAll(Collection<ListenerRegistration> listeners) {
        for (ListenerRegistration listener : listeners) {
            register(listener);
        }
    }

    /**
     * Remove a listener from a specific order slot
     *
     * @param listener listener to remove
     */
    public void unregister(ListenerRegistration listener) {
        if (handlerSlots.get(listener.getOrder()).remove(listener)) {
            dirty();
        }
    }

    public void unregister(Object owner) {
        boolean changed = false;
        for (List<ListenerRegistration> list : handlerSlots.values()) {
            for (ListIterator<ListenerRegistration> i = list.listIterator(); i.hasNext(); ) {
                if (i.next().getOwner().equals(owner)) {
                    i.remove();
                    changed = true;
                }
            }
        }
        if (changed) {
            dirty();
        }
    }

    public void unregisterAll() {
        for (List<ListenerRegistration> regs : handlerSlots.values()) {
            regs.clear();
        }
        handlers = null;
    }

    /**
     * Bake HashMap and ArrayLists to 2d array - does nothing if not necessary
     *
     * @return The baked array of ListenerRegistrations
     */
    public ListenerRegistration[] bake() {
        if (handlers != null) {
            return handlers; // don't re-bake when still valid
        }
        List<ListenerRegistration> entries = new ArrayList<>();
        for (Order order : Order.values()) {
            addAllListeners(entries, order);
        }
        handlers = entries.toArray(new ListenerRegistration[entries.size()]);
        return handlers;
    }

    private void addAllListeners(List<ListenerRegistration> entries, Order order) {
        List<ListenerRegistration> entry = handlerSlots.get(order);
        if (entry != null) {
            entries.addAll(entry);
        }

        if (parent != null) {
            parent.addAllListeners(entries, order);
        }
    }

    private void dirty() {
        this.handlers = null;
        for (HandlerList child : children) {
            child.dirty();
        }
    }

    /**
     * Gets an array of all currently ListenerRegistration, if the handlers list is currently null, it will attempt to bake new listeners prior to returning.
     *
     * @return array of ListenerRegistrations
     */
    public ListenerRegistration[] getRegisteredListeners() {
        return bake();
    }

    protected void addChild(HandlerList handlerList) {
        children.add(handlerList);
    }

    public boolean hasChildren() {
        return children.size() > 0;
    }
}
