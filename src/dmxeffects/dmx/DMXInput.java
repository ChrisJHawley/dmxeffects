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

import com.trolltech.qt.core.QObject;

public class DMXInput extends QObject implements Runnable {

	public Signal2<Integer, Integer> inputValue = new Signal2<Integer, Integer>();

	public Signal0 listenerStarted = new Signal0();

	/**
	 * Creates a new instance of DMXInput
	 * 
	 */
	public DMXInput() {
	}

	/**
	 * Method invoked when this method is run by a QThread
	 */
	public void run() {
		// Inform listening QObjects that this thread has commenced operation
		listenerStarted.emit();
		while (true) {
			/*
			 * Here we read in data from the InputQueue, inserting it into the
			 * data structure as appropriate. This assumes that we recieve all
			 * 512 channels of data at once, which may not always be true due to
			 * the protocol specification. However in the majority of cases it
			 * will be.
			 * 
			 * We must also consider situations where no data is being recieved.
			 * To allow for this, 88 microseconds should be waited between
			 * polling for data, due to the specification of the protocol.
			 */
			Integer dataPeek = InputQueue.getInstance().peek();
			while (dataPeek == null) {
				try {
					Thread.sleep((long) 0, 88000);
				} catch (java.lang.InterruptedException e) {
					System.err.println("Thread interruption detected");
					e.printStackTrace(System.err);
				}
				dataPeek = InputQueue.getInstance().peek();
			}
			for (int i = 1; i < 513; i++) {
				dataPeek = InputQueue.getInstance().peek();
				while (dataPeek == null) {
					/*
					 * It appears that the break in the data was longer than
					 * anticipated. The protocol indicates that this break could
					 * last for up to one second, therefore a sleep for 100
					 * microseconds should be performed.
					 */
					try {
						Thread.sleep((long) 0, 10000);
					} catch (java.lang.InterruptedException e) {
						System.err.println("Thread interruption detected");
						e.printStackTrace(System.err);
					}
					dataPeek = InputQueue.getInstance().peek();
				}
				// Send a signal indicating the new value
				inputValue
						.emit(new Integer(i), InputQueue.getInstance().poll());
			}
		}
	}
}
