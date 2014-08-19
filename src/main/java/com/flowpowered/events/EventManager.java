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

import java.util.List;

/**
 * Manages event registration through {@link Listener}s and {@link EventExecutor}s,  It also handles calling of events, and delayed events.
 */
public interface EventManager {
    /**
     * Calls an event with the given details
     *
     * @param <T> the type of event
     * @param event SimpleEvent details
     * @return Called event
     */
    public <T extends Event> T callEvent(T event);

    /**
     * Calls an event for each parameter in {@code parameters}
     *
     * @param <U> the type of parameter the event takes
     * @param <T> the type of event
     * @param event SimpleEvent details
     * @param parameters the parameters to use
     * @return the parameters after they've been called
     */
    public <U extends EventParameter, T extends Event<U>> List<U> callEventWithParameters(T event, List<U> parameters);

    /**
     * Unregisters all the events in the given listener class
     *
     * @param listener Listener to unregister
     */
    public void unRegisterEventsByListener(Object listener);

    /**
     * Unregisters all the events in the given listener class
     *
     * @param owner the Owner to register all events from
     */
    public void unRegisterEventsByOwner(Object owner);

    /**
     * Registers all the events in the given listener class
     *
     * @param listener Listener to register
     * @param owner Owner to register the event for
     */
    public void registerEvents(Object listener, Object owner);

    /**
     * Registers the specified executor to the given event class
     *
     * @param event SimpleEvent type to register
     * @param priority Priority to register this event at
     * @param executor EventExecutor to register
     * @param owner Plugin to register
     */
    public void registerEvent(Class<? extends Event> event, Order priority, EventExecutor executor, Object owner);
}
