/*
 * $Id$
 */
package dmxeffects.dmx;

import java.util.Random;
import java.util.concurrent.Semaphore;

import dmxeffects.Main;
import dmxeffects.OperationCancelledException;
import dmxeffects.OperationFailedException;

/**
 * Class to generate DMX values for test purposes.
 *
 * @author chris
 */
public class Generator {
	
	private static Generator singletonGenerator = null;
	
	private static Semaphore singletonLock = new Semaphore(1, true);
	
	/** Creates a new instance of Generator */
	private Generator() {
	}
	
	/**
	 * Method to return the current instance of Generator, creating a new one if necessary.
	 * @return The current singleton instance of Generator.
	 */
	public static Generator getInstance() {
		try {
			singletonLock.acquire();
			if (singletonGenerator == null) {
				singletonGenerator = new Generator();
			}
			singletonLock.release();
		} catch (java.lang.InterruptedException e) {
			System.err.println("Thread interruption detected.");
			e.printStackTrace(System.err);
		}
		return singletonGenerator;
	}
	
	/**
	 * Method to destroy the current instance of Generator.
	 */
	public static void destroyInstance() {
		try {
			singletonLock.acquire();
			singletonGenerator = null;
			singletonLock.release();
		} catch (java.lang.InterruptedException e) {
			System.err.println("Thread interruption detected.");
			e.printStackTrace(System.err);
		}
	}
	
	/**
	 * Method to generate 512 random values and insert them into the queue.
	 */
	public void generateAll() {
		for (int i=1;i<513;i++) {
			InputQueue.getInstance().add(generateValue());
		}
		Main.getInstance().statusBar().showMessage("DMX values generated", 2000);
	}
	
	/**
	 * Method to generate a single random value upon a specified channel number, leaving the other values unchanged.
	 * @throws InvalidChannelNumberException Exception thrown indicating the channelNumber does not follow the specification.
	 * @throws OperationFailedException Getting user input failed.
	 * @throws OperationCancelledException User cancelled input gathering.
	 */
	public void generate(int channelNumber) throws InvalidChannelNumberException {
		if (Validator.validate(channelNumber, Validator.CHANNEL_NUMBER_VALIDATION)) {
			for (int i=1;i<513;i++) {
				if (i == channelNumber) {
					InputQueue.getInstance().add(generateValue());
				} else {
					InputQueue.getInstance().add(Universe.getInstance().getValue(i));
				}
			}
		} else {
			String exceptionMessage = "Specified channel number, " + String.valueOf(channelNumber) + " was not within the permissible range of 1 to 512 inclusive.";
			throw new InvalidChannelNumberException(exceptionMessage);
		}
		Main.getInstance().statusBar().showMessage("DMX value generated", 2000);
	}
	
	/**
	 * Method to insert a specific value upon a specified channel number, leaving the other values unchanged.
	 * @param channelNumber The number of the channel to insert the data upon. This should be between 1 and 512 inclusive.
	 * @param channelValue The value to insert onto the specified channel. This should be between 0 and 255 inclusive.
	 * @throws InvalidChannelNumberException Exception thrown indicating the channelNumber does not follow the specification.
	 * @throws InvalidChannelValueException Exception thrown indicating the channelValue does not follow the specification.
	 */
	public void inject(int channelNumber, int channelValue) throws InvalidChannelNumberException,InvalidChannelValueException {
		if (Validator.validate(channelNumber, Validator.CHANNEL_NUMBER_VALIDATION)) {
			if (Validator.validate(channelValue, Validator.CHANNEL_VALUE_VALIDATION)) {
				for (int i=1;i<513;i++) {
					if (i == channelNumber) {
					InputQueue.getInstance().add(channelValue);
					} else {
					InputQueue.getInstance().add(Universe.getInstance().getValue(i));
					}
				}
			} else {
				String exceptionMessage = "Specified value, " + String.valueOf(channelValue) + " was not within the permissible range of 0 to 255 inclusive.";
				throw new InvalidChannelValueException(exceptionMessage);
			}
		} else {
			String exceptionMessage = "Specified channel number, " + String.valueOf(channelNumber) + " was not within the permissible range of 1 to 512 inclusive.";
			throw new InvalidChannelNumberException(exceptionMessage);
		}
		Main.getInstance().statusBar().showMessage("DMX value inserted", 2000);
	}
	
	/**
	 * Method to generate a single channel value. This should be from 0 to 255 inclusive.
	 * @return The value generated.
	 */
	public int generateValue() {
		return new Random().nextInt(256);
	}
}
