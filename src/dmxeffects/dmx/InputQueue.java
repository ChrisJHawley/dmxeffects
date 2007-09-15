/*
 * $Id$
 * 
 * Copyright (C) 2007 Christopher Hawley
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package dmxeffects.dmx;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

/**
 * Data structure to queue up input values of DMX for storage into the Universe.
 * 
 * @author chris
 */
public class InputQueue {

    // TODO Possibly remove singletonness due to potential thread unsafeness

    /**
     * The current singleton instance of this InputQueue
     */
    private final static InputQueue singletonQueue = new InputQueue();

    private final transient Queue<Integer> queue;

    private final transient Semaphore queueLock = new Semaphore(1, true);

    /**
     * Creates a new instance of InputQueue
     */
    private InputQueue() {
	// Create a new LinkedList to store as the queue.
	queue = new LinkedList<Integer>();
    }

    /**
     * Retrieve the current instance of InputQueue, creating one if
     * necessary.
     * 
     * @return The singleton instance of InputQueue.
     */
    public static InputQueue getInstance() {
	return singletonQueue;
    }

    /**
     * Method to append an element to the end of the queue.
     * 
     * @param value
     *                The value to append to the end of the queue.
     */
    public void add(final int value) {
	try {
	    queueLock.acquire();
	    queue.add(Integer.valueOf(value));
	    queueLock.release();
	} catch (java.lang.InterruptedException e) {
	    e.printStackTrace(System.err);
	}
    }

    /**
     * Method to append an element to the end of the queue.
     * 
     * @param intValue
     *                The Integer to append to the end of the queue.
     */
    public void add(final Integer intValue) {
	try {
	    queueLock.acquire();
	    queue.add(intValue);
	    queueLock.release();
	} catch (java.lang.InterruptedException e) {
	    e.printStackTrace(System.err);
	}
    }

    /**
     * Method to "poll" the first element from the queue, removing it.
     * 
     * @return The value of the first element in the queue.
     */
    public Integer poll() {
	Integer returnedInteger = null;
	try {
	    queueLock.acquire();
	    try {
		returnedInteger = queue.poll();
	    } catch (java.lang.NullPointerException e) {
		returnedInteger = Integer.valueOf(-1);
	    }
	    queueLock.release();
	} catch (java.lang.InterruptedException e) {
	    e.printStackTrace(System.err);
	}
	return returnedInteger;
    }

    /**
     * Method to "peek" at the first element in the queue, without removing
     * it.
     * 
     * @return The value of the first element in the queue.
     */
    public Integer peek() {
	Integer returnedInteger = null;
	try {
	    queueLock.acquire();
	    try {
		returnedInteger = queue.peek();
	    } catch (java.lang.NullPointerException e) {
		returnedInteger = Integer.valueOf(-1);
	    }
	    queueLock.release();
	} catch (java.lang.InterruptedException e) {
	    e.printStackTrace(System.err);
	}
	return returnedInteger;
    }
}
