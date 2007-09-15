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

import com.trolltech.qt.gui.QInputDialog;
import com.trolltech.qt.gui.QWidget;

import dmxeffects.Main;
import dmxeffects.OperationCancelledException;
import dmxeffects.OperationFailedException;

/**
 * Class to get and validate input from a user.
 * 
 * @author chris
 */
public class DMXUserInput extends QWidget { // NOPMD by chris on 07/06/07
    // 00:21

    /**
     * Value used to indicate that a DMX Channel Number is to be input.
     */
    public static final int CHANNEL_NUMBER_INPUT = Validator.CHANNEL_NUMBER_VALIDATION;

    /**
     * Value used to indicate that a DMX Channel Value is to be input.
     */
    public static final int CHANNEL_VALUE_INPUT = Validator.CHANNEL_VALUE_VALIDATION;

    /**
     * Class to get and validate input from a user.
     */
    public DMXUserInput() {
    }

    /**
     * Method used to gather input from a user that meets the required
     * validation criteral for the specified input type.
     * 
     * @param type
     *                The type of input to be taken.
     * @throws dmxeffects.OperationCancelledException
     *                 Indication that the operation was cancelled by the
     *                 user.
     * @throws dmxeffects.OperationFailedException
     *                 Indication that the operation failed at some stage.
     * @return The validated input provided by the user.
     */
    public int getInput(final int type) throws OperationCancelledException,
	    OperationFailedException {
	String label;
	switch (type) {
	case CHANNEL_NUMBER_INPUT:
	    label = tr("Please provide a value within the range 1 to 512 inclusive.");
	    break;
	case CHANNEL_VALUE_INPUT:
	    label = tr("Please provide a value within the range 0 to 255 inclusive.");
	    break;
	default:
	    throw new OperationFailedException("Invalid input type request.");
	}
	return getInput(label, type);
    }

    /**
     * Method used to gather input from a user that meets the required
     * validation criteria for the specified input type.
     * 
     * @param label
     *                The message to display within the box.
     * @param type
     *                The type of input to be taken.
     * @throws dmxeffects.OperationCancelledException
     *                 Indication that the operation was cancelled by the
     *                 user.
     * @throws dmxeffects.OperationFailedException
     *                 Indication that the operation failed at some stage.
     * @return The validated input provided by the user.
     */
    public int getInput(final String label, final int type)
	    throws OperationCancelledException, OperationFailedException {
	String title;
	int minVal, maxVal;
	switch (type) {
	case CHANNEL_NUMBER_INPUT:
	    title = tr("Enter DMX Channel Number");
	    minVal = 1;
	    maxVal = 512;
	    break;
	case CHANNEL_VALUE_INPUT:
	    title = tr("Enter DMX Channel Value");
	    minVal = 0;
	    maxVal = 255;
	    break;
	default:
	    throw new OperationFailedException("Invalid input type requested.");
	}
	final Integer returned = QInputDialog.getInteger(Main.getInstance(),
		title, label, minVal, minVal, maxVal);
	try {
	    if (Validator.validate(returned.intValue(), type)) {
		return returned.intValue();
	    } else {
		throw new OperationCancelledException(
			"Operation cancelled by user");
	    }
	} catch (NullPointerException npe) { 
	    // No value returned
	    throw new OperationCancelledException("Operation cancelled by user");
	}
    }
}
