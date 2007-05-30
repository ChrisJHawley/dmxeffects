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
	 * Extract the name from the module, which is used in the associations.
	 * @return The name of the module.
	 */
	String getName();
}
