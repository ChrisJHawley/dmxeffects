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

import dmxeffects.*;
import dmxeffects.dmx.*;

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
	public void dmxInput(Integer channelNumber, Integer channelValue) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see dmxeffects.Module#dmxListenerEnabled()
	 */
	public void dmxListenerEnabled() {
		// Listener in operation indicates that input may be incoming, start
		// listening for this
		Universe.getInstance().dmxValueUpdater.connect(this, "dmxInput(Integer, Integer)");
		
		// This should only be called when the listener first starts, which is
		// when we want to set channel associations
		try {
			setModuleChannel();
		} catch (OperationCancelledException OCE) {
			// This means that the user does not want to properly enable the 
			// module. DO NOT ALLOW ENABLING.
			// TODO: Ensure enabling not performed.
		}

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
		// This may be run to re-assign channels, so we may want to remove assocs
		if (firstControlChannel != -1) {
			try {
				Universe.getInstance().removeAssociation(firstControlChannel, CHANNELS_REQUIRED);
			} catch (InvalidChannelNumberException e) {
				// Should not occur as any existing control channel will have been
				// validated when it was initialised.
				e.printStackTrace(System.err);
			}
		}
		int firstChan = SoundGUI.getInstance().getFirstChannel();
		// Confirm if any of the required channels are in use - asking for overwrite
		int[] overWriteChans = new int[CHANNELS_REQUIRED];
		int overWrites = 0;
		for (int i=0;i<CHANNELS_REQUIRED;i++) {
			try {
				String association = Universe.getInstance().getAssociation(firstChan+i);
				if (association != null) {
					overWriteChans[overWrites] = firstChan+i;
				}
			} catch (InvalidChannelNumberException e) {
				e.printStackTrace(System.err);
			}
		}
		if (overWrites != 0) {
			// Prompt the user for the range requiring overwriting, which may already
			// have empty associations, it's just nicer.
			try {
				Universe.getInstance().removeAssociation(overWriteChans[0], 
						overWriteChans[overWrites] - overWriteChans[0]);
			} catch (InvalidChannelNumberException e) {
				e.printStackTrace(System.err);
			}
		}
		
		// Set the associations
		for (int i=0;i<CHANNELS_REQUIRED;i++) {
		
			try {
				Universe.getInstance().setAssociation(firstChan+i, MODULE_NAME);
			} catch (InvalidChannelNumberException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setModuleChannel(int channel) throws InvalidChannelNumberException, OperationCancelledException {
		if (channel == -1) {
			// Module has had control revoked
			firstControlChannel = -1;
			setModuleChannel();
		} else {
			if (Validator.validate(channel, Validator.CHANNEL_NUMBER_VALIDATION)) {
				firstControlChannel = channel;
			} else {
				throw new InvalidChannelNumberException("Invalid channel number provided");
			}
		}
	}

	/**
	 * Provide the number of channels required for this module.
	 * @return The requisite number of channels.
	 */
	public int getChannelsRequired() {
		return CHANNELS_REQUIRED;
	}

}
