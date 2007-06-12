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
	private transient int[] dmxValues;

	private transient String[] dmxAssociations;

	// -- Signals sent by this object -- //
	/**
	 * Signal indicating a channel-value pair has updated. First Integer is the
	 * channel number. Second Integer is the channel value.
	 */
	public transient Signal2<Integer, Integer> dmxValueUpdater =
		new Signal2<Integer, Integer>();

	/**
	 * Signal indicating the removal of an association range. First Integer is
	 * the first channel number. Second Integer is the size of the range.
	 */
	public transient Signal2<Integer, Integer> assocRemUpdater =
		new Signal2<Integer, Integer>();

	/**
	 * Signal indicating the update of an association. Integer is the channel
	 * number. String is the module name or an empty string if no module.
	 */
	public transient Signal2<Integer, String> assocUpdater =
		new Signal2<Integer, String>();

	/** Creates a new instance of Universe */
	public Universe() {
		super();
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

	public void setValue(final Integer channelNum, final Integer channelVal) {
		final int channelNumber = channelNum.intValue();
		final int channelValue = channelVal.intValue();
		try {
			setValue(channelNumber, channelValue);
		} catch (InvalidChannelNumberException ICNE) {
			ICNE.printStackTrace(System.err);
		} catch (InvalidChannelValueException ICVE) {
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
	public void setValue(final int channelNumber, final int channelValue)
			throws InvalidChannelNumberException, InvalidChannelValueException {
		// Perform validation upon the information
		if (!Validator.validate(channelNumber,
				Validator.NUM_VALIDATION)) {
			throw new InvalidChannelNumberException(channelNumber);
		}
		if (!Validator.validate(channelValue,
				Validator.VAL_VALIDATION)) {
			throw new InvalidChannelValueException(channelValue);
		}
		// Perform the appropriate conversion to zero-based indexing and store
		// the data.
		dmxValues[channelNumber - 1] = channelValue;

		// Inform listening objects that there has been a new value added.
		dmxValueUpdater.emit(Integer.valueOf(channelNumber), 
				Integer.valueOf(channelValue));

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
	public int getValue(final int channelNumber) 
		throws InvalidChannelNumberException {
		if (Validator.validate(channelNumber,
				Validator.NUM_VALIDATION)) {
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
		} else {
			throw new InvalidChannelNumberException(channelNumber);
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
	public void setAssociation(final int channelNumber, 
			final String associatedElement)
			throws InvalidChannelNumberException, OperationCancelledException {
		// Perform validation upon the channelNumber information
		if (!Validator.validate(channelNumber,
				Validator.NUM_VALIDATION)) {
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
		assocUpdater.emit(Integer.valueOf(channelNumber), associatedElement);
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
	public String getAssociation(final int channelNumber)
			throws InvalidChannelNumberException {
		if (Validator.validate(channelNumber,
				Validator.NUM_VALIDATION)) {
			String returnString = null;
			try {
				returnString = dmxAssociations[channelNumber - 1];
			} catch (java.lang.NullPointerException e) {
				// No data, return an empty String.
				returnString = "";
			}
			return returnString;
		} else {
			throw new InvalidChannelNumberException(channelNumber);
		}
	}

	public void removeAssociation(final int channelNumber,
			final int numToDelete)
			throws InvalidChannelNumberException, OperationCancelledException {
		if (Validator.validate(channelNumber,
				Validator.NUM_VALIDATION)) {
			// Confirm that they wish to delete all the elements.
			String confirmMessage;
			if (numToDelete > 1) {
				confirmMessage = "Please confirm that you wish to delete the " +
						"associations for channels between " +
						channelNumber + " and " + (channelNumber + numToDelete)
						+ ".";
			} else {
				confirmMessage = "Please confirm that you wish to delete the "
						+ "association for channel " + channelNumber + ".";
			}
			final QMessageBox.StandardButtons options =
				new QMessageBox.StandardButtons(QMessageBox.StandardButton.Yes,
					QMessageBox.StandardButton.No);
			final QMessageBox.StandardButton response = QMessageBox.question(
					Main.getInstance().getDMX(), "Confirm deletion",
					confirmMessage, options, QMessageBox.StandardButton.Yes);
			if (response.equals(QMessageBox.StandardButton.Yes)) {
				// Confirmed
				// Do remove
				assocRemUpdater.emit(Integer.valueOf(channelNumber), 
						Integer.valueOf(numToDelete));
				for (int i = 0; i < numToDelete; i++) {
					// Send out a signal indicating which range of channels has
					// been removed.
					dmxAssociations[channelNumber + i - 1] = null; // NOPMD by chris on 12/06/07 20:29
					assocUpdater.emit(Integer.valueOf(channelNumber), "");
				}
			} else {
				// Abort
				throw new OperationCancelledException(
						"The user aborted the operation.");
			}
		} else {
			throw new InvalidChannelNumberException(channelNumber);
		}
	}
}
