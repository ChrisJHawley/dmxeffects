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

/**
 * Interface defining the methods that each module must provide for interaction with the Main
 * application. This allows for a standard set of controls to be used.
 *
 * @author Chris Hawley
 */
public interface Module {

	/**
	 * Method to inform the Module that dmxInput has been recieved.
	 * This method is called as a slot to a signal from the Universe class, so
	 * all data has previosuly been validated.
	 * @param channelNumber The channel number upon which the input was recieved.
	 * @param channelValue The value recieved upon this channel.
	 */
	void dmxInput(Integer channelNumber, Integer channelValue)
;
	
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
