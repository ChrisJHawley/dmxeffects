/*
 * Module.java
 *
 * Created on 28 June 2006, 12:14
 *
 * $Rev: 52 $$Date: 2006-12-15 21:37:56 +0000 (Fri, 15 Dec 2006) $
 */

package dmxeffects;

import com.trolltech.qt.gui.QMenu;

import dmxeffects.dmx.InvalidChannelNumberException;
import dmxeffects.dmx.InvalidChannelValueException;

/**
 * Interface defining the methods that each module must provide for interaction with the Main
 * application. This allows for a standard set of controls to be used.
 *
 * @author Chris Hawley
 */
public interface Module {

	/**
	 * Method to inform the Module that dmxInput has been recieved.
	 * It is expected that each module will validate this data and throw exceptions as appropriate.
	 * @param channelNumber The channel number upon which the input was recieved.
	 * @param channelValue The value recieved upon this channel.
	 * @throws InvalidChannelNumberException The channelNumber does not fall within the valid range of 1 to 512.
	 * @throws InvalidChannelValueException The channelValue does not fall within the valid range of 0 to 255.
	 */
	void dmxInput(int channelNumber, int channelValue) throws InvalidChannelNumberException, InvalidChannelValueException;
	
	/**
	 * Method to get the panel elements to be displayed for this module in the Main application window.
	 * @return The displayable panel elements.
	 */
	//JComponent getPanelElements();
	
	/**
	 * Get the text to be used as the title for this Module's panel elements.
	 * @return The title.
	 */
	String getPanelTitle();
	
	/**
	 * Method to get the menu to be displayed for this module.
	 * @return The displayable menu element.
	 */
	QMenu getMenu();
		
	/**
	 * Method to inform the Module that the application is now in 'Run' mode.
	 */
	void runModeEnabled();
	
	/**
	 * Method to inform the Module that the application is now in 'Program' mode.
	 */
	void programModeEnabled();
	
	/**
	 * Method to get the module to set it's control channel information.
	 * @throws OperationCancelledException The user cancelled the deletion of existing data when changing the channel's settings.
	 */
	void setModuleChannel() throws OperationCancelledException;
	
	/**
	 * Method to change functions based on the DMX listener thread being active.
	 */
	void dmxListenerEnabled();
	
	/**
	 * Method to set that the module's channel associations have been edited, and that it should recreate them.
	 */
	void removeChannelAssociations();
	
	/**
	 * Extract the name from the module, that is used in the associations.
	 * @return The name of the module.
	 */
	String getName();
}
