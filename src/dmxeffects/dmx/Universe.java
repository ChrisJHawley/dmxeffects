package dmxeffects.dmx;

import dmxeffects.OperationCancelledException;
//import dmxeffects.sound.soundModule;
import java.util.concurrent.Semaphore;

import com.trolltech.qt.gui.QMessageBox;

/**
 * Class to store the values within a DMX Universe.
 *
 * @author chris
 */
public class Universe {
	
	private static Universe singletonUniverse = null;
	
	private static Semaphore singletonLock = new Semaphore(1,true);
	
	private int[] dmxValues;
	private String[] dmxAssociations;
	
	/** Creates a new instance of Universe */
	private Universe() {
		dmxValues = new int[512];
		/* In theory a nicer way to do the associations would to be to store an 
		 * array containing objects with the following info:
		 * String - the name of the association.
		 * int - the first channel of the associations.
		 * int - the number of channels
		 *
		 * This information is then taken and interpreted for any operations
		 * that are performed on associations, ensuring that all the channels for
		 * a particular association are removed if you remove one.
		 */
		dmxAssociations = new String[512];
	}
	
	/**
	 * Method to return the singleton instance of the Universe.
	 * @return The current instance of the Universe.
	 */
	public static Universe getInstance() {
		try {
			singletonLock.acquire();
			if(singletonUniverse == null) {
				singletonUniverse = new Universe();
			}
			singletonLock.release();
		} catch (java.lang.InterruptedException e) {
			System.err.println("Thread interruption detected.");
			e.printStackTrace(System.err);
		}
		return singletonUniverse;
	}

	/**
	 * Method to destroy the current instance of the Universe.
	 */
	public static void destroyInstance() {
		try {
			singletonLock.acquire();
			singletonUniverse = null;
			singletonLock.release();
		} catch (java.lang.InterruptedException e) {
			System.err.println("Thread interruption detected.");
			e.printStackTrace(System.err);
		}
	}
	
	/**
	 * Method to set the value of a channel in the Universe to a specific value based upon some input.
	 * @param channelNumber The channel number that the value applies to. This ranges from 1 to 512.
	 * @param channelValue The value that the channel is set to. This ranges from 0 to 255.
	 * @throws InvalidChannelNumberException Exception for when the channelNumber does not follow the specification.
	 * @throws InvalidChannelValueException Exception for when the channelValue does not follow the specification.
	 */
	public void setValue(int channelNumber, int channelValue) throws InvalidChannelNumberException, InvalidChannelValueException {
		//Perform validation upon the information
		if(Validator.validate(channelNumber, Validator.CHANNEL_NUMBER_VALIDATION) == false) {
			throw new InvalidChannelNumberException(channelNumber);
		} else if (Validator.validate(channelValue, Validator.CHANNEL_VALUE_VALIDATION) == false) {
			throw new InvalidChannelValueException(channelValue);
		}

		//Perform the appropriate conversion to zero-based indexing and store the data.
		dmxValues[channelNumber-1] = channelValue;

		//Inform the dmxDisplay that there has been a change and instruct it to update
		DMXDisplay.getInstance().updateTable();
		
		//Call the modules to tell them.
		//soundModule.getInstance().dmxInput(channelNumber, channelValue);
	}
	
	/**
	 * Method to return a given value
	 * @param channelNumber The channel number for which the value should be returned. This ranges from 1 to 512.
	 * @return The value of the channel requested.
	 * @throws InvalidChannelNumberException Exception for when the channelNumber does not follow the specification.
	 */
	public int getValue(int channelNumber) throws InvalidChannelNumberException {
		if(Validator.validate(channelNumber, Validator.CHANNEL_NUMBER_VALIDATION) == false) {
			throw new InvalidChannelNumberException(channelNumber);
		} else {
			int returnValue = 0;
			try {
				//Perform the appropriate conversion to zero-based indexing and return the data.
				returnValue = dmxValues[channelNumber-1];
			} catch (java.lang.NullPointerException e) {
				//No value set at present, return 0.
				returnValue = 0;
			}
			return returnValue;
		}
	}
	
	/**
	 * Method to insert data to the user indicating what the a channel is associated with.
	 * This will include other modules, or perhaps just a light that the user wishes to
	 * label.
	 * @param channelNumber The number of the channel for which the association exists.
	 * @param associatedElement A String naming the element for which the association end.
	 * @throws InvalidChannelNumberException The channelNumber doesn't meet the
	 * specification.
	 */
	public void setAssociation(int channelNumber, String associatedElement) throws InvalidChannelNumberException {
		//Perform validation upon the channelNumber information
		if(Validator.validate(channelNumber, Validator.CHANNEL_NUMBER_VALIDATION) == false) {
			throw new InvalidChannelNumberException(channelNumber);
		}
		
		//Change to zero-based indexing, and store the data.
		dmxAssociations[channelNumber-1] = associatedElement;
		
		//Update the display
		DMXDisplay.getInstance().updateTable();
	}
	
	/**
	 * Get the name of the element associated with the specified channel number.
	 * @param channelNumber The number of the channel for which the association exsists.
	 * @return The String naming the element for which the association exists.
	 * @throws invalidChannelNumberExecption The channelNumber doesn't meet the
	 * specification.
	 */
	public String getAssociation(int channelNumber) throws InvalidChannelNumberException {
		if(Validator.validate(channelNumber, Validator.CHANNEL_NUMBER_VALIDATION) == false) {
			throw new InvalidChannelNumberException(channelNumber);
		} else {
			String returnString = null;
			try {
				returnString = dmxAssociations[channelNumber-1];
			} catch (java.lang.NullPointerException e) {
				//No data, return the null string.
				returnString = null;
			}
			return returnString;
		}
	}
	
	public void removeAssociation(int channelNumber, int numToDelete) throws InvalidChannelNumberException, OperationCancelledException {
		if(Validator.validate(channelNumber, Validator.CHANNEL_NUMBER_VALIDATION) == false) {
			throw new InvalidChannelNumberException(channelNumber);
		} else {
			//Confirm that they wish to delete all the elements.
			String confirmMessage;
			if (numToDelete > 1) {
				confirmMessage = "Please confirm that you wish to delete the " +
						"associations for channels between " 
						+ String.valueOf(channelNumber) + " and " 
						+ String.valueOf(channelNumber + numToDelete) + ".";
			} else {
				confirmMessage = "Please confirm that you wish to delete the " +
						"association for channel "
						+ String.valueOf(channelNumber) + ".";
			}
			QMessageBox.StandardButtons options = new QMessageBox.StandardButtons(QMessageBox.StandardButton.Yes, QMessageBox.StandardButton.No);
			QMessageBox.StandardButton response = QMessageBox.question(DMXDisplay.getInstance(), "Confirm deletion", confirmMessage, options, QMessageBox.StandardButton.Yes);
			if (response.equals(QMessageBox.StandardButton.Yes)) {
				// Confirmed
				// Do remove
				for (int i=0;i<numToDelete;i++) {
					dmxAssociations[channelNumber+i-1] = null;
				}
			} else {
				// Abort
				throw new OperationCancelledException("The user aborted the operation.");
			}
			/*
			if (confirmBox.confirm(confirmMessage)) {
				for (int i=0;i<numToDelete;i++) {
				//	if (dmxAssociations[channelNumber+i-1].equals(soundModule.getInstance().getName())) {
					//	soundModule.getInstance().removeChannelAssociations();
					}
					//dmxAssociations[channelNumber+i-1] = null;
				}
				String[] deleteMessage;
				if (numToDelete > 1) {
					deleteMessage = new String[] {
						"The associations were successfully removed."
					};
				} else {
					deleteMessage = new String[] {
					"The association was successfully removed."
					};
				}
				Notifier.notify(deleteMessage, Notifier.INFORMATION_MESSAGE);
			*/
		}
	}
}
