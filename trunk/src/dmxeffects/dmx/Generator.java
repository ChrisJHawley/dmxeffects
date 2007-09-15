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

import java.util.Random;

import dmxeffects.Main;

/**
 * Class to generate DMX values for test purposes.
 * 
 * @author chris
 */
public class Generator {

    private static Generator singletonGen = new Generator();

    /** Creates a new instance of Generator */
    private Generator() {
    }

    /**
     * Method to return the current instance of Generator, creating a new
     * one if necessary.
     * 
     * @return The current singleton instance of Generator.
     */
    public static Generator getInstance() {
	return singletonGen;
    }

    /**
     * Method to generate 512 random values and insert them into the queue.
     */
    public void generateAll() {
	for (int i = 1; i < 513; i++) {
	    InputQueue.getInstance().add(generateValue());
	}
	Main.getInstance().statusBar()
		.showMessage("DMX values generated", 2000);
    }

    /**
     * Method to generate a single random value upon a specified channel
     * number, leaving the other values unchanged.
     * 
     * @throws InvalidChannelNumberException
     *                 Exception thrown indicating the channelNumber does
     *                 not follow the specification.
     * @throws OperationFailedException
     *                 Getting user input failed.
     * @throws OperationCancelledException
     *                 User cancelled input gathering.
     */
    public void generate(final int channelNumber)
	    throws InvalidChannelNumberException {
	if (Validator.validate(channelNumber,
		Validator.CHANNEL_NUMBER_VALIDATION)) {
	    for (int i = 1; i < 513; i++) {
		if (i == channelNumber) {
		    InputQueue.getInstance().add(generateValue());
		} else {
		    InputQueue.getInstance().add(
			    Main.getInstance().getDMX().getUniverse().getValue(
				    i));
		}
	    }
	} else {
	    final String exceptionMessage = "Specified channel number, "
		    + channelNumber
		    + " was not within the permissible range of "
		    + "1 to 512 inclusive.";
	    throw new InvalidChannelNumberException(exceptionMessage);
	}
	Main.getInstance().statusBar().showMessage("DMX value generated", 2000);
    }

    /**
     * Method to insert a specific value upon a specified channel number,
     * leaving the other values unchanged.
     * 
     * @param channelNumber
     *                The number of the channel to insert the data upon.
     *                This should be between 1 and 512 inclusive.
     * @param channelValue
     *                The value to insert onto the specified channel. This
     *                should be between 0 and 255 inclusive.
     * @throws InvalidChannelNumberException
     *                 Exception thrown indicating the channelNumber does
     *                 not follow the specification.
     * @throws InvalidChannelValueException
     *                 Exception thrown indicating the channelValue does not
     *                 follow the specification.
     */
    public void inject(final int channelNumber, final int channelValue)
	    throws InvalidChannelNumberException, InvalidChannelValueException {
	if (Validator.validate(channelNumber,
		Validator.CHANNEL_NUMBER_VALIDATION)) {
	    if (Validator.validate(channelValue,
		    Validator.CHANNEL_VALUE_VALIDATION)) {
		for (int i = 1; i < 513; i++) {
		    if (i == channelNumber) {
			InputQueue.getInstance().add(channelValue);
		    } else {
			InputQueue.getInstance().add(
				Main.getInstance().getDMX().getUniverse()
					.getValue(i));
		    }
		}
	    } else {
		final String exceptionMessage = "Specified value, "
			+ channelValue
			+ " was not within the permissible range"
			+ " of 0 to 255 inclusive.";
		throw new InvalidChannelValueException(exceptionMessage);
	    }
	} else {
	    final String exceptionMessage = "Specified channel number, "
		    + channelNumber
		    + " was not within the permissible range of"
		    + " 1 to 512 inclusive.";
	    throw new InvalidChannelNumberException(exceptionMessage);
	}
	Main.getInstance().statusBar().showMessage("DMX value inserted", 2000);
    }

    /**
     * Method to generate a single channel value. This should be from 0 to
     * 255 inclusive.
     * 
     * @return The value generated.
     */
    public int generateValue() {
	return new Random().nextInt(256);
    }
}
