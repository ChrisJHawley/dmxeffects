package dmxeffects.dmx;

import java.util.concurrent.Semaphore;

import com.trolltech.qt.QSignalEmitter;
import com.trolltech.qt.core.QObject;

public class DMXInput extends QObject implements Runnable {

	private static DMXInput singletonInput = null;
	private static Semaphore singletonLock = new Semaphore(1, true);
	
	// Our signal emitter to tell things to update.
	public QSignalEmitter.Signal0 updateTable = new QSignalEmitter.Signal0();
	
	/**
	 * Creates a new instance of DMXInput
	 *
	 */
	private DMXInput() {	
	}
	
	/**
	 * Get the current instance of DMXInput, creating one if required
	 * @return The current instance
	 */
	public static DMXInput getInstance() {
		try {
			singletonLock.acquire();
			if (singletonInput == null) {
				singletonInput = new DMXInput();
			}
			singletonLock.release();
		} catch (InterruptedException IE) {
			System.err.println("Thread interruption detected");
			IE.printStackTrace(System.err);
		}
		return singletonInput;
	}
	
	/**
	 * Method invoked when this method is run by a QThread
	 */
	public void run() {
		while (true) {
			/*
			 * Here we read in data from the InputQueue, inserting it into the 
			 * data structure as appropriate. This assumes that we recieve all 
			 * 512 channels of data at once, which may not always be true due 
			 * to the protocol specification.
			 * However in the majority of cases it will be.
			 *
			 * We must also consider situations where no data is being recieved.
			 * To allow for this, 88 microseconds should be waited between 
			 * polling for data, due to the specification of the protocol.
			 */
			Integer dataPeek = InputQueue.getInstance().peek();
			while (dataPeek == null) {
				try {
					Thread.sleep((long)0,88000);
				} catch (java.lang.InterruptedException e) {
					System.err.println("Thread interruption detected");
					e.printStackTrace(System.err);
				}
				dataPeek = InputQueue.getInstance().peek();
			}
			for (int i=1;i<513;i++) {
				dataPeek = InputQueue.getInstance().peek();
				while (dataPeek == null) {
					/*
					 * It appears that the break in the data was longer than anticipated.
					 * The protocol indicates that this break could last for up to one second, therefore a sleep for 100 microseconds should be performed.
					 */
					try {
						Thread.sleep((long)0,10000);
					} catch (java.lang.InterruptedException e) {
						System.err.println("Thread interruption detected");
						e.printStackTrace(System.err);
					}
					dataPeek = InputQueue.getInstance().peek();
				}
				//Set the value as appropriate
				try {
					Universe.getInstance().setValue(i,InputQueue.getInstance().poll().intValue());
				} catch (InvalidChannelNumberException e) {
					System.err.println(e.getMessage());
					e.printStackTrace(System.err);
				} catch (InvalidChannelValueException e) {
					System.err.println(e.getMessage());
					e.printStackTrace(System.err);
				}
			}
			updateTable.emit();
		}
	}
}
