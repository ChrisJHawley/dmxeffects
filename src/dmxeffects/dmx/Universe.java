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
import com.trolltech.qt.gui.QMessageBox;

import dmxeffects.Main;
import dmxeffects.OperationCancelledException;

/**
 * Class to store the values within a DMX Universe.
 * 
 * @author chris
 */
public class Universe extends QObject {

	// -- Internal data stores -- //
	private int[] dmxValues;

	private String[] dmxAssociations;

	// -- Signals sent by this object -- //
	public Signal2<Integer, Integer> dmxValueUpdater = new Signal2<Integer, Integer>();

	public Signal2<Integer, Integer> assocRemUpdater = new Signal2<Integer, Integer>();

	public Signal2<Integer, String> associationUpdater = new Signal2<Integer, String>();

	/** Creates a new instance of Universe */
	public Universe() {
		dmxValues = new int[512];
		/*
		 * In theory a nicer way to do the associations would to be to store an
		 * array containing objects with the following info: String - the name
		 * of the association. int - the first channel of the associations. int -
		 * the number of channels
		 * 
		 * This information is then taken and interpreted for any operations
		 * that are performed on associations, ensuring that all the channels
		 * for a particular association are removed if you remove one.
		 */
		dmxAssociations = new String[512];
	}

	public void setValue(Integer channelNum, Integer channelVal) {
		int channelNumber = channelNum.intValue();
		int channelValue = channelVal.intValue();
		try {
			setValue(channelNumber, channelValue);
		} catch (InvalidChannelNumberException ICNE) {
			System.err.println(ICNE.getMessage());
			ICNE.printStackTrace(System.err);
		} catch (InvalidChannelValueException ICVE) {
			System.err.println(ICVE.getMessage());
			ICVE.printStackTrace(System.err);
		}

	}

	/**
	 * Method to set the value of a channel in the Universe to a specific value
	 * based upon some input.
	 * 
	 * @param channelNumber
	 *            The channel number that the value applies to. This ranges from
	 *            1 to 512.
	 * @param channelValue
	 *            The value that the channel is set to. This ranges from 0 to
	 *            255.
	 * @throws InvalidChannelNumberException
	 *             Exception for when the channelNumber does not follow the
	 *             specification.
	 * @throws InvalidChannelValueException
	 *             Exception for when the channelValue does not follow the
	 *             specification.
	 */
	public void setValue(int channelNumber, int channelValue)
			throws InvalidChannelNumberException, InvalidChannelValueException {
		// Perform validation upon the information
		if (Validator.validate(channelNumber,
				Validator.CHANNEL_NUMBER_VALIDATION) == false) {
			throw new InvalidChannelNumberException(channelNumber);
		} else if (Validator.validate(channelValue,
				Validator.CHANNEL_VALUE_VALIDATION) == false) {
			throw new InvalidChannelValueException(channelValue);
		}
		// Perform the appropriate conversion to zero-based indexing and store
		// the data.
		dmxValues[channelNumber - 1] = channelValue;

		// Inform listening objects that there has been a new value added.
		dmxValueUpdater.emit(new Integer(channelNumber), new Integer(
				channelValue));

	}

	/**
	 * Method to return a given value
	 * 
	 * @param channelNumber
	 *            The channel number for which the value should be returned.
	 *            This ranges from 1 to 512.
	 * @return The value of the channel requested.
	 * @throws InvalidChannelNumberException
	 *             Exception for when the channelNumber does not follow the
	 *             specification.
	 */
	public int getValue(int channelNumber) throws InvalidChannelNumberException {
		if (Validator.validate(channelNumber,
				Validator.CHANNEL_NUMBER_VALIDATION) == false) {
			throw new InvalidChannelNumberException(channelNumber);
		} else {
			int returnValue = 0;
			try {
				// Perform the appropriate conversion to zero-based indexing and
				// return the data.
				returnValue = dmxValues[channelNumber - 1];
			} catch (java.lang.NullPointerException e) {
				// No value set at present, return 0.
				returnValue = 0;
			}
			return returnValue;
		}
	}

	/**
	 * Method to insert data to the user indicating what the a channel is
	 * associated with. This will include other modules, or perhaps just a light
	 * that the user wishes to label.
	 * 
	 * @param channelNumber
	 *            The number of the channel for which the association exists.
	 * @param associatedElement
	 *            A String naming the element for which the association end.
	 * @throws InvalidChannelNumberException
	 *             The channelNumber doesn't meet the specification.
	 * @throws OperationCancelledException
	 */
	public void setAssociation(int channelNumber, String associatedElement)
			throws InvalidChannelNumberException, OperationCancelledException {
		// Perform validation upon the channelNumber information
		if (Validator.validate(channelNumber,
				Validator.CHANNEL_NUMBER_VALIDATION) == false) {
			throw new InvalidChannelNumberException(channelNumber);
		}

		// Check if this is already associated
		if (dmxAssociations[channelNumber - 1] != null) {
			// Confirm the deletion of the pre-existing association
			removeAssociation(channelNumber, 1);
		}
		// Change to zero-based indexing, and store the data.
		dmxAssociations[channelNumber - 1] = associatedElement;

		// Update the display
		associationUpdater.emit(new Integer(channelNumber), associatedElement);
	}

	/**
	 * Get the name of the element associated with the specified channel number.
	 * 
	 * @param channelNumber
	 *            The number of the channel for which the association exsists.
	 * @return The String naming the element for which the association exists.
	 * @throws invalidChannelNumberExecption
	 *             The channelNumber doesn't meet the specification.
	 */
	public String getAssociation(int channelNumber)
			throws InvalidChannelNumberException {
		if (Validator.validate(channelNumber,
				Validator.CHANNEL_NUMBER_VALIDATION) == false) {
			throw new InvalidChannelNumberException(channelNumber);
		} else {
			String returnString = null;
			try {
				returnString = dmxAssociations[channelNumber - 1];
			} catch (java.lang.NullPointerException e) {
				// No data, return the null string.
				returnString = null;
			}
			return returnString;
		}
	}

	public void removeAssociation(int channelNumber, int numToDelete)
			throws InvalidChannelNumberException, OperationCancelledException {
		if (Validator.validate(channelNumber,
				Validator.CHANNEL_NUMBER_VALIDATION) == false) {
			throw new InvalidChannelNumberException(channelNumber);
		} else {
			// Confirm that they wish to delete all the elements.
			String confirmMessage;
			if (numToDelete > 1) {
				confirmMessage = "Please confirm that you wish to delete the "
						+ "associations for channels between "
						+ String.valueOf(channelNumber) + " and "
						+ String.valueOf(channelNumber + numToDelete) + ".";
			} else {
				confirmMessage = "Please confirm that you wish to delete the "
						+ "association for channel "
						+ String.valueOf(channelNumber) + ".";
			}
			QMessageBox.StandardButtons options = new QMessageBox.StandardButtons(
					QMessageBox.StandardButton.Yes,
					QMessageBox.StandardButton.No);
			QMessageBox.StandardButton response = QMessageBox.question(Main
					.getInstance().getDMX(), "Confirm deletion",
					confirmMessage, options, QMessageBox.StandardButton.Yes);
			if (response.equals(QMessageBox.StandardButton.Yes)) {
				// Confirmed
				// Do remove
				assocRemUpdater.emit(new Integer(channelNumber), new Integer(
						numToDelete));
				for (int i = 0; i < numToDelete; i++) {
					// Send out a signal indicating which range of channels has
					// been removed.
					dmxAssociations[channelNumber + i - 1] = null;
					associationUpdater.emit(new Integer(channelNumber), " ");
				}
			} else {
				// Abort
				throw new OperationCancelledException(
						"The user aborted the operation.");
			}
		}
	}
}
