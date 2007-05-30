/*
 * $Id$
 */
package dmxeffects.dmx;

/**
 * Class to validate DMX channel numbers and values.
 *
 * @author Chris Hawley
 */
public class Validator {

	/**
	 * Value used to indicate the validation of a DMX Channel Number.
	 */
	public static final int CHANNEL_NUMBER_VALIDATION = 20001;
	/**
	 * Value used to indicate the validation of a DMX Channel Value.
	 */
	public static final int CHANNEL_VALUE_VALIDATION = 20002;

	/**
	 * Method to run the appropriate validation.
	 * @param value The value to perform validation upon.
	 * @param validationType The type of validation to perform. 
	 * This should either be a CHANNEL_NUMBER_VALIDATION or CHANNEL_VALUE_VALIDATION.
	 * @return A boolean indicating whether the data is valid or not.
	 */
	public static boolean validate(int value, int validationType) {
		boolean valid;
		switch (validationType) {
			case CHANNEL_NUMBER_VALIDATION:
				valid = validateChannelNumber(value);
				break;
			case CHANNEL_VALUE_VALIDATION:
				valid = validateChannelValue(value);
				break;
			default:
				valid = false; //Failsafe, should never be reached.
		}
		return valid;
	}
	
	/**
	 * Method to validate a channel number, which should be between 1 and 512 inclusive.
	 * @return A boolean indicating the validity of the data.
	 */
	private static boolean validateChannelNumber(int validationValue) {
		boolean valid = false;
		if ((validationValue < 1) || (validationValue > 512)) {
			valid = false;
		} else {
			valid = true;
		}
		return valid;
	}
	
	/**
	 * Method to validate a channel value, which should be between 0 and 255 inclusive.
	 * @return A boolean indicating the validity of the data.
	 */
	private static boolean validateChannelValue(int validationValue) {
		boolean valid = false;
		if ((validationValue < 0) || (validationValue > 255)) {
			valid = false;
		} else {
			valid = true;
		}
		return valid;
	}
	
}
