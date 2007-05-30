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
