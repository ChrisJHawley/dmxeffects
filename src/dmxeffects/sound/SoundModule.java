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

import java.util.concurrent.Semaphore;

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
	
	// Module configuration information
	private static final int CHANNELS_REQUIRED = 2;
	private static final String MODULE_NAME = "Sound Module";
	private int firstControlChannel = -1;
	
	// Variables for singleton methods
	private static SoundModule singletonModule = null;
	private static Semaphore singletonLock =  new Semaphore(1, true);
	
	// Data storage for the tracks
	private SoundTrack[] trackArray;
	
	// Variables for indicating if play is allowed
	private boolean playAllowed;
	private static Semaphore playLock = new Semaphore(1, true);
	
	/*
	 * Static variables used for the configuration of the values to be used by
	 * the DMX controller when dealing with this module. These should be properly
	 * documented such that they can be properly implemented.
	 * 
	 * TODO Perhaps they could be shown on a GUI tab for this module.
	 */
	private static final int SOUND_MODULE_DMX_START_PLAYBACK = 10;
	private static final int SOUND_MODULE_DMX_STOP_PLAYBACK = 20;
	
	private SoundModule() {
		trackArray = new SoundTrack[256];
	}
	
	/**
	 * Get the current instance of the SoundModule
	 * @return The current instance, or a new one if one didn't exist.
	 */
	public static SoundModule getInstance() {
		try {
			singletonLock.acquire();
			if (singletonModule == null) {
				singletonModule = new SoundModule();
			}
			singletonLock.release();
		} catch (InterruptedException IE) {
			System.err.println("Thread interruption detected");
			IE.printStackTrace(System.err);
		}
		return singletonModule;
	}

	/**
	 * Destroy the current singleton instance.
	 *
	 */
	public static void destroyInstance() {
		try {
			singletonLock.acquire();
			// XXX Potentially thread unsafe if perfectly interleaved with getInstance()
			singletonModule = null;
			singletonLock.release();
		} catch (InterruptedException IE) {
			System.err.println("Thread interruption detected");
			IE.printStackTrace(System.err);
		}
		
	}
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
		return SoundGUI.getInstance().getMenu();
	}

	/* (non-Javadoc)
	 * @see dmxeffects.Module#getName()
	 */
	public String getName() {
		return MODULE_NAME;
	}

	/* (non-Javadoc)
	 * @see dmxeffects.Module#getPanelTitle()
	 */
	public String getPanelTitle() {
		return SoundGUI.getInstance().getPanelTitle();
	}

	/* (non-Javadoc)
	 * @see dmxeffects.Module#programModeEnabled()
	 */
	public void programModeEnabled() {
		SoundGUI.getInstance().programModeEnabled();
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
		SoundGUI.getInstance().runModeEnabled();
	}

	/* (non-Javadoc)
	 * @see dmxeffects.Module#setModuleChannel()
	 */
	public void setModuleChannel() throws OperationCancelledException {
		// TODO Auto-generated method stub

	}

}
