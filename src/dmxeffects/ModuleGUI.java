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

public interface ModuleGUI {
	
	/**
	 * Gather a menu for display within the application
	 * @return The menu provided by this class.
	 */
	QMenu getMenu();
	
	/**
	 * Performs actions relevant when the system moves into run mode.
	 */
	void runModeEnabled();
	
	/**
	 * Performs actions relevant when the system moves into program mode
	 */
	void programModeEnabled();
	
	/**
	 * Performs actions relevant when the DMX listener starts
	 */
	void dmxListenerEnabled();
	
	/**
	 * Gather the title for any panel elements for display within the application
	 * @return The title for the panel elements provided by this class.
	 */
	String getPanelTitle();
}
