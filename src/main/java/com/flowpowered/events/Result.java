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

/**
 * Represents an event's result.
 */
public enum Result {
    /**
     * Deny the event. Depending on the event, the action indicated by the event will either not take place or will be reverted. Some actions may not be denied.
     */
    DENY(false),
    /**
     * Neither deny nor allow the event. The server will proceed with its normal handling.
     */
    DEFAULT(false),
    /**
     * Allow / Force the event. The action indicated by the event will take place if possible, even if the server would not normally allow the action. Some actions may not be allowed.
     */
    ALLOW(true);

    private boolean result;

    private Result(boolean result) {
        this.result = result;
    }

    /**
     * True if the event is allowed, and is taking normal operation. False if the event is denied. Null if neither allowed, nor denied. The server will continue to proceed with its normal handling.
     *
     * @return the event's resolution.
     */
    public boolean getResult() {
        return result;
    }
}
