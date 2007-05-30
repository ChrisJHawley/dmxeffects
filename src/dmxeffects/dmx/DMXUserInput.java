package dmxeffects.dmx;

import com.trolltech.qt.gui.QInputDialog;
import com.trolltech.qt.gui.QWidget;

import dmxeffects.*;

/**
 * Class to get and validate input from a user.
 *
 * @author chris
 */
public class DMXUserInput extends QWidget {
	
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
	 * Method used to gather input from a user that meets the required validation criteria for the specified input type.
	 * @param type The type of input to be taken.
	 * @throws dmxeffects.OperationCancelledException Indication that the operation was cancelled by the user.
	 * @throws dmxeffects.OperationFailedException Indication that the operation failed at some stage.
	 * @return The validated input provided by the user.
	 */
	public int getInput(int type) throws OperationCancelledException, OperationFailedException {
		String title,label;
		int minVal,maxVal;
		switch (type) {
			case CHANNEL_NUMBER_INPUT:
				title = tr("Enter DMX Channel Number");
				label = tr("Please provide a value within the range 1 to 512 inclusive.");
				minVal = 1;
				maxVal = 512;
				break;
			case CHANNEL_VALUE_INPUT:
				title = tr("Enter DMX Channel Value");
				label = tr("Please provide a value within the range 0 to 255 inclusive.");
				minVal = 0;
				maxVal = 255;
				break;
			default:
				throw new OperationFailedException("Invalid input type requested.");
		}
		Integer returned = QInputDialog.getInteger(Main.getInstance(), title, label, 0, minVal, maxVal);
		try {
			if (Validator.validate(returned.intValue(), type)) {
				return returned.intValue();
			} else {
				throw new OperationCancelledException("Operation cancelled by user");
			}
		} catch (NullPointerException npe) {
			// No value returned
			throw new OperationCancelledException("Operation cancelled by user");
		}
	}
}
