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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A simple implementation of the {@link EventManager} that handles all {@link Event}s for the engine.
 */
public class SimpleEventManager implements EventManager {
    private final Logger logger;
    private final Map<Class<? extends Event>, HandlerList> handlers = new ConcurrentHashMap<>(16, .75f, 4);

    public SimpleEventManager() {
        this.logger = LogManager.getLogger(getClass().getSimpleName());
    }

    public SimpleEventManager(Logger logger) {
        this.logger = logger;
    }

    /**
     * Bake all handler lists. Best used just after all normal event registration is complete.
     */
    public void bakeAll() {
        for (HandlerList h : this.handlers.values()) {
            h.bake();
        }
    }

    public <T> void unregisterAll() {
        for (HandlerList h : this.handlers.values()) {
            h.unregisterAll();
        }
    }

    public void unregisterAll(Object plugin) {
        for (HandlerList h : this.handlers.values()) {
            h.unregister(plugin);
        }
    }

    @Override
    public <T extends Event> T callEvent(T event) {
        HandlerList handlers = this.handlers.get(event.getClass());
        if (handlers == null) {
            return event;
        }
        ListenerRegistration[] listeners = handlers.getRegisteredListeners();

        if (listeners != null) {
            for (ListenerRegistration listener : listeners) {
                try {
                    if (!event.parameter.cancelled || listener.getOrder().ignoresCancelled()) {
                        listener.getExecutor().execute(event);
                    }
                } catch (Throwable ex) {
                    this.logger.log(Level.ERROR, "Could not pass event " + event.getEventName() + " to " + listener.getOwner().getClass().getName(), ex);
                }
            }
            event.parameter.beenCalled = true;
        }
        return event;
    }

    @Override
    public <U extends EventParameter, T extends Event<U>> List<U> callEventWithParameters(T event, List<U> parameters) {
        for (U parameter : parameters) {
            event.parameter = parameter;
            callEvent(event);
        }
        return parameters;
    }

    @Override
    public void unRegisterEvents(Listener listener) {
        List<Method> methods = getAllMethods(listener);
        for (final Method method : methods) {
            final EventHandler eh = method.getAnnotation(EventHandler.class);
            if (eh == null) {
                continue;
            }
            try {
                Class<? extends Event> eventClass = getValidatedClass(method);
                HandlerList get = this.handlers.get(eventClass);
                if (get == null) {
                    continue;
                }
                get.unregister(new ListenerRegistration(new MethodEventExecutor(listener, method), eh.order(), null));
            } catch (IllegalArgumentException e) {
                this.logger.error(e.getMessage());
            }
        }
    }

    @Override
    public void unRegisterEvents(Object owner) {
        for (HandlerList list : this.handlers.values()) {
            list.unregister(owner);
        }
    }

    @Override
    public void registerEvents(Listener listener, Object owner) {
        List<Method> methods = getAllMethods(listener);
        for (final Method method : methods) {
            final EventHandler eh = method.getAnnotation(EventHandler.class);
            if (eh == null) {
                continue;
            }
            try {
                Class<? extends Event> eventClass = getValidatedClass(method);
                newHandlerList(eventClass).register(new ListenerRegistration(new MethodEventExecutor(listener, method), eh.order(), owner));
            } catch (IllegalArgumentException e) {
                this.logger.error(e.getMessage());
            }
        }
    }

    @Override
    public void registerEvent(Class<? extends Event> event, Order priority, EventExecutor executor, Object owner) {
        newHandlerList(event).register(new ListenerRegistration(executor, priority, owner));
    }

    private HandlerList newHandlerList(Class<? extends Event> clazz) {
        HandlerList list = this.handlers.get(clazz);
        if (list == null) {
            HandlerList parent = null;
            if (clazz.getSuperclass() != null && Event.class.isAssignableFrom(clazz.getSuperclass()) && !clazz.getSuperclass().equals(Event.class)) {
                parent = newHandlerList(clazz.getSuperclass().asSubclass(Event.class));
            }
            list = new HandlerList(parent);
            this.handlers.put(clazz, list);
        }
        return list;
    }

    private List<Method> getAllMethods(Listener listener) {
        List<Method> methods = new ArrayList<>();
        Class<?> listenerClass = listener.getClass();
        while (listenerClass != null && !listenerClass.equals(Object.class)) {
            try {
                methods.addAll(Arrays.asList(listenerClass.getDeclaredMethods()));
            } catch (NoClassDefFoundError e) {
                this.logger.log(Level.ERROR, "Listener class " + listenerClass + " does not exist.", e);
                break;
            }
            listenerClass = listenerClass.getSuperclass();
        }
        return methods;
    }

    private Class<? extends Event> getValidatedClass(Method method) throws IllegalArgumentException {
        if (method.getParameterTypes().length < 1) {
            throw new IllegalArgumentException("No method arguments used for event type registered");
        }

        final Class<?> checkClass = method.getParameterTypes()[0];
        if (!Event.class.isAssignableFrom(checkClass) || method.getParameterTypes().length != 1) {
            throw new IllegalArgumentException("Wrong method arguments used for event type registered");
        }
        return checkClass.asSubclass(Event.class);
    }

    private static class MethodEventExecutor implements EventExecutor {
        private final Listener listenerInstance;
        private final Method method;

        public MethodEventExecutor(Listener listener, Method method) {
            this.listenerInstance = listener;
            this.method = method;
        }

        @Override
        public void execute(Event<?> event) throws EventException {
            try {
                this.method.invoke(this.listenerInstance, event);
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof EventException) {
                    throw (EventException) e.getCause();
                }

                throw new EventException(e.getCause());
            } catch (Throwable t) {
                throw new EventException(t);
            }
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 97 * hash + Objects.hashCode(this.listenerInstance);
            hash = 97 * hash + Objects.hashCode(this.method);
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
            final MethodEventExecutor other = (MethodEventExecutor) obj;
            if (!Objects.equals(this.listenerInstance, other.listenerInstance)) {
                return false;
            }
            if (!Objects.equals(this.method, other.method)) {
                return false;
            }
            return true;
        }
    }
}
