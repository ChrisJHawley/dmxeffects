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

import com.trolltech.qt.gui.QInputDialog;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QWidget;

import dmxeffects.Main;
import dmxeffects.ModuleGUI;
import dmxeffects.OperationCancelledException;
import dmxeffects.dmx.Validator;

/**
 * @author chris
 *
 */
public class SoundGUI extends QWidget implements ModuleGUI {

	private static SoundGUI singletonGUI = null;
	private static Semaphore singletonLock = new Semaphore(1, true);
	
	private static final String PANEL_TITLE = "Sound Module";
	
	private SoundGUI() {
		// TODO Init
	}
	
	/**
	 * Gather the current instance of this GUI class.
	 * @return The present instance.
	 */
	public static SoundGUI getInstance() {
		try {
			singletonLock.acquire();
			if (singletonGUI == null) {
				singletonGUI = new SoundGUI();
			}
			singletonLock.release();
		} catch (InterruptedException IE) {
			System.err.println("Thread interruption detected");
			IE.printStackTrace(System.err);
		}
		return singletonGUI;
	}
	
	/* (non-Javadoc)
	 * @see dmxeffects.ModuleGUI#dmxListenerEnabled()
	 */
	public void dmxListenerEnabled() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see dmxeffects.ModuleGUI#getMenu()
	 */
	public QMenu getMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see dmxeffects.ModuleGUI#programModeEnabled()
	 */
	public void programModeEnabled() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see dmxeffects.ModuleGUI#runModeEnabled()
	 */
	public void runModeEnabled() {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Update the Track-listing table
	 */
	public void updateTable() {
		// TODO Implement
	}
	
	/*
	 * (non-Javadoc)
	 * @see dmxeffects.ModuleGUI#getPanelTitle()
	 */
	public String getPanelTitle() {
		return tr(PANEL_TITLE);
	}

	public int getFirstChannel() throws OperationCancelledException {
		String title = tr("Specify channels for " + SoundModule.getInstance().getName());
		int chansRequired = SoundModule.getInstance().getChannelsRequired();
		int maxVal = 513-chansRequired;
		int minVal = 1;
		String label = tr("Please provide the first channel of the ") + 
			String.valueOf(chansRequired) +
			tr(" required for this module. This value must be between 1 and ") +
			String.valueOf(maxVal) + ".";
		Integer returned = QInputDialog.getInteger(Main.getInstance(), title, label, minVal, minVal, maxVal);
		try {
			if (Validator.validate(returned.intValue(), Validator.CHANNEL_NUMBER_VALIDATION)) {
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
