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
 * Order of an event listener may be registered at. <p> Odd-numbered (IGNORE_CANCELLED) slots are called even when events are marked "not propagating".<br/> If an event stops propagating part way
 * through an even slot, that slot will not cease execution, but future even slots will not be called.
 */
public enum Order {
    /**
     * Called before all other handlers. Should be used for high-priority event canceling.
     */
    EARLIEST(0, false),
    /**
     * Called after "Earliest" handlers and before "Early" handlers.<br/> Is called even when event has been canceled.<br/> Should generally be used to uncancel events canceled in Earliest.<br/>
     */
    EARLY_IGNORE_CANCELLED(1, true),
    /**
     * Called after "Earliest" handlers. Should generally be used for low priority event canceling.
     */
    EARLY(2, false),
    /**
     * Called after "Early" handlers and before "Default" handlers.<br/> Is called even when event has been canceled. <br/> This is for general-purpose always-run events.<br/>
     */
    DEFAULT_IGNORE_CANCELLED(3, true),
    /**
     * Default call, for general purpose handlers
     */
    DEFAULT(4, false),
    /**
     * Called after "Default" handlers and before "Late" handlers.<br/> Is called even when event has been canceled.<br/>
     */
    LATE_IGNORE_CANCELLED(5, true),
    /**
     * Called after "Default" handlers.
     */
    LATE(6, false),
    /**
     * Called after "Late" handlers and before "Latest" handlers. <br/> Is called even when event has been canceled.<br/>
     */
    LATEST_IGNORE_CANCELLED(7, true),
    /**
     * Called after "Late" handlers.
     */
    LATEST(8, false),
    /**
     * Called after "Latest" handlers. <br/> No changes to the event should be made in this order slot (though it is not enforced).<br/> This is called even when event has been cancelled.</br>
     */
    MONITOR_IGNORE_CANCELLED(9, true),
    /**
     * Called after "Latest" handlers. <br/> No changes to the event should be made in this order slot (though it is not enforced).<br/>
     */
    MONITOR(10, false);

    private final int index;
    private final boolean ignoreCancelled;

    Order(int index, boolean ignoreCancelled) {
        this.index = index;
        this.ignoreCancelled = ignoreCancelled;
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return whether this Order ignores cancellation status
     */
    public boolean ignoresCancelled() {
        return ignoreCancelled;
    }
}
