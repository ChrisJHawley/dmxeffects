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
package dmxeffects.sound;

import com.trolltech.qt.gui.QMenu;

import dmxeffects.Module;
import dmxeffects.OperationCancelledException;
import dmxeffects.dmx.InvalidChannelNumberException;
import dmxeffects.dmx.InvalidChannelValueException;

/**
 * Sound module main file. Provides all interfaces from external classes to the
 * functionality provided by this module.
 * 
 * @author chris
 *
 */
public class SoundModule implements Module {

	/* (non-Javadoc)
	 * @see dmxeffects.Module#dmxInput(int, int)
	 */
	public void dmxInput(int channelNumber, int channelValue)
			throws InvalidChannelNumberException, InvalidChannelValueException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see dmxeffects.Module#dmxListenerEnabled()
	 */
	public void dmxListenerEnabled() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see dmxeffects.Module#getMenu()
	 */
	public QMenu getMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see dmxeffects.Module#getName()
	 */
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see dmxeffects.Module#getPanelTitle()
	 */
	public String getPanelTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see dmxeffects.Module#programModeEnabled()
	 */
	public void programModeEnabled() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see dmxeffects.Module#removeChannelAssociations()
	 */
	public void removeChannelAssociations() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see dmxeffects.Module#runModeEnabled()
	 */
	public void runModeEnabled() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see dmxeffects.Module#setModuleChannel()
	 */
	public void setModuleChannel() throws OperationCancelledException {
		// TODO Auto-generated method stub

	}

}
