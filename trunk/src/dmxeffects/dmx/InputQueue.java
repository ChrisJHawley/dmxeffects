/*
 * $Id$
 */
package dmxeffects.dmx;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

/**
 * Data structure to queue up input values of DMX for storage into the Universe.
 *
 * @author chris
 */
public class InputQueue {
	 
	/**
	 * The current singleton instance of this InputQueue
	 */
	private static InputQueue singletonInputQueue = null;
	
	private LinkedList<Integer> queue;
	private Semaphore queueLock = null;
	private static Semaphore singletonLock = new Semaphore(1, true);
	
	/**
	 * Creates a new instance of InputQueue
	 */
	private InputQueue() {
		//Create a new LinkedList to store as the queue.
		queue = new LinkedList<Integer>();
		queueLock = new Semaphore(1, true);
	}
	
	/**
	 * Retrieve the current instance of InputQueue, creating one if necessary.
	 * @return The singleton instance of InputQueue.
	 */
	public static InputQueue getInstance() {
		try {
			singletonLock.acquire();
			if (singletonInputQueue == null) {
			singletonInputQueue = new InputQueue();
			}  
			singletonLock.release();
		} catch (java.lang.InterruptedException e) {
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
		}
		return singletonInputQueue;
	}
	
	/**
	 * Method to destroy the current instance of InputQueue.
	 */
	public static void destroyInstance() {
		try {
			singletonLock.acquire();
			singletonInputQueue = null;
			singletonLock.release();
		} catch (java.lang.InterruptedException e) {
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
		}
	}
	
	/**
	 * Method to append an element to the end of the queue.
	 * @param value The value to append to the end of the queue.
	 */
	public void add(int value) {
		try {
			queueLock.acquire();
			queue.add(new Integer(value));
			queueLock.release();
		} catch (java.lang.InterruptedException e) {
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
		}
	}
	
	/**
	 * Method to append an element to the end of the queue.
	 * @param intValue The Integer to append to the end of the queue.
	 */
	public void add(Integer intValue) {
		try {
			queueLock.acquire();
			queue.add(intValue);
			queueLock.release();
		} catch (java.lang.InterruptedException e) {
			System.err.println("Thread interruption detected");
			e.printStackTrace(System.err);
		}
	}
   
	/**
	 * Method to "poll" the first element from the queue, removing it.
	 * @return The value of the first element in the queue.
	 */
	public Integer poll() {
		Integer returnedInteger = null;
		try {
			queueLock.acquire();
			try {
			returnedInteger = queue.poll();
			} catch (java.lang.NullPointerException e) {
			returnedInteger = null;
			}
			queueLock.release();
		} catch (java.lang.InterruptedException e) {
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
		}
		return returnedInteger;
	}
	
	/**
	 * Method to "peek" at the first element in the queue, without removing it.
	 * @return The value of the first element in the queue.
	 */
	public Integer peek() {
		Integer returnedInteger = null;
		try {
			queueLock.acquire();
			try {
			returnedInteger = queue.peek();
			} catch (java.lang.NullPointerException e) {
			returnedInteger = null;
			}
			queueLock.release();
		} catch (java.lang.InterruptedException e) {
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
		}
		return returnedInteger;
	}
}
