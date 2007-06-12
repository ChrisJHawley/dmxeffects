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
package dmxeffects.dmx; // NOPMD by chris on 12/06/07 20:33

/**
 * Class to validate DMX channel numbers and values.
 * 
 * @author Chris Hawley
 */
public class Validator { // NOPMD by chris on 12/06/07 20:33

	/**
	 * Value used to indicate the validation of a DMX Channel Number.
	 */
	public static final int NUM_VALIDATION = 20001;

	/**
	 * Value used to indicate the validation of a DMX Channel Value.
	 */
	public static final int VAL_VALIDATION = 20002;

	/**
	 * Method to run the appropriate validation.
	 * 
	 * @param value
	 *            The value to perform validation upon.
	 * @param validationType
	 *            The type of validation to perform. This should either be a
	 *            NUM_VALIDATION or VAL_VALIDATION.
	 * @return A boolean indicating whether the data is valid or not.
	 */
	public static boolean validate(final int value, final int validationType) {
		boolean valid;
		switch (validationType) {
		case NUM_VALIDATION:
			valid = validateChannelNumber(value);
			break;
		case VAL_VALIDATION:
			valid = validateChannelValue(value);
			break;
		default:
			valid = false; // Failsafe, should never be reached.
		}
		return valid;
	}

	/**
	 * Method to validate a channel number, which should be between 1 and 512
	 * inclusive.
	 * 
	 * @return A boolean indicating the validity of the data.
	 */
	private static boolean validateChannelNumber(final int validationValue) {
		boolean valid = false;
		if ((validationValue < 1) || (validationValue > 512)) {
			valid = false;
		} else {
			valid = true;
		}
		return valid;
	}

	/**
	 * Method to validate a channel value, which should be between 0 and 255
	 * inclusive.
	 * 
	 * @return A boolean indicating the validity of the data.
	 */
	private static boolean validateChannelValue(final int validationValue) {
		boolean valid = false;
		if ((validationValue < 0) || (validationValue > 255)) {
			valid = false;
		} else {
			valid = true;
		}
		return valid;
	}

}
