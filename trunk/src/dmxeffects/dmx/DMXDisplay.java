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
package dmxeffects.dmx;

import com.trolltech.qt.gui.*;

import dmxeffects.*;

/**
 * Class for providing the GUI elements of the DMX modules.
 * 
 * @author chris
 * 
 */
public class DMXDisplay extends QWidget implements Module {

	/**
	 * Signal for informing other modules that the listener has started
	 */
	public Signal0 listenerEnabled = new Signal0();

	// -- Internal variables for this Module -- //
	private final String MODULE_NAME = tr("DMX Module");

	private final String WIDGET_TITLE = tr("DMX Association Table");

	private Universe universe;

	private DMXInput input;

	private boolean dmxListener = false;

	// -- GUI Elements -- //
	private QMenu menu;

	private QMenu generateMenu;

	private QAction startListenerAction;

	private QAction generateRandomAction;

	private QAction generateChannelAction;

	private QAction injectAction;

	private QAction setAssocAction;

	/**
	 * Instantiate a new instance of this class.
	 */
	public DMXDisplay() {
		// Create input object
		input = new DMXInput();

		// Initialise Universe
		universe = new Universe();

		// Prepare actions
		startListenerAction = new QAction(tr("&Start DMX Listener"), this);
		startListenerAction.setStatusTip(tr("Begin listening for DMX input"));
		startListenerAction.triggered.connect(this, "startListener()");
		startListenerAction.setEnabled(!dmxListener);

		generateRandomAction = new QAction(tr("Generate on &all channels"),
				this);
		generateRandomAction
				.setStatusTip(tr("Generate random DMX data for all channels"));
		generateRandomAction.triggered.connect(Generator.getInstance(),
				"generateAll()");
		generateRandomAction.setEnabled(dmxListener);

		generateChannelAction = new QAction(
				tr("Generate on &specific channel"), this);
		generateChannelAction
				.setStatusTip(tr("Generate random DMX for a specified channel"));
		generateChannelAction.triggered.connect(this, "generate()");
		generateChannelAction.setEnabled(dmxListener);

		injectAction = new QAction(tr("&Inject DMX"), this);
		injectAction.setStatusTip(tr("Inject specific DMX data"));
		injectAction.triggered.connect(this, "inject()");
		injectAction.setEnabled(dmxListener);

		setAssocAction = new QAction(tr("&Set Association"), this);
		setAssocAction.setStatusTip(tr("Set module-channel associations"));
		setAssocAction.triggered.connect(this, "setAssoc()");
		setAssocAction.setEnabled(dmxListener);

		// Prepare menus
		menu = new QMenu(tr("&DMX"));
		menu.addAction(startListenerAction);

		generateMenu = new QMenu("&Generate DMX");
		generateMenu.setEnabled(dmxListener);
		generateMenu.addAction(generateRandomAction);
		generateMenu.addAction(generateChannelAction);

		menu.addMenu(generateMenu);
		menu.addAction(injectAction);
		menu.addAction(setAssocAction);

		// Connect to external signals
		universe.dmxValueUpdater.connect(this,
				"updateTableVal(Integer, Integer)");
		universe.assocRemUpdater.connect(this,
				"displayRemove(Integer, Integer)");
		universe.associationUpdater.connect(this,
				"updateTableAssoc(Integer, String)");

	}

	public QMenu getMenu() {
		return menu;
	}

	public void runMode() {
		setAssocAction.setEnabled(false);
	}

	public void programMode() {
		if (dmxListener) {
			setAssocAction.setEnabled(true);
		}
	}

	public void dmxListenerEnabled() {
		dmxListener = true;
		setAssocAction.setEnabled(true);
		startListenerAction.setEnabled(false);
		generateMenu.setEnabled(true);
		generateRandomAction.setEnabled(true);
		generateChannelAction.setEnabled(true);
		injectAction.setEnabled(true);

		// Transmit the signal
		listenerEnabled.emit();
	}

	/**
	 * Set the DMXInput Thread running, listening to the appropriate
	 * signal.
	 */
	public void startListener() {
		Thread listenerThread = new Thread(input);
		input.inputValue.connect(universe, "setValue(Integer, Integer)");
		input.listenerStarted.connect(this, "dmxListenerEnabled()");
		input.moveToThread(listenerThread);
		listenerThread.setDaemon(true);

		listenerThread.start();
	}

	public void setAssoc() {
		// TODO Auto-generated method block
	}

	public void updateTableVal(Integer channelNumber, Integer channelValue) {
		// TODO Auto-generated method block
	}

	public void updateTableAssoc(Integer channelNumber, String association) {
		// TODO Auto-generated method block
	}

	/**
	 * Handle action and allow the user to specify a channel and value to insert
	 * data for into the system.
	 */
	public void inject() {
		try {
			int channelNumber = new DMXUserInput()
					.getInput(DMXUserInput.CHANNEL_NUMBER_INPUT);
			int channelValue = new DMXUserInput()
					.getInput(DMXUserInput.CHANNEL_VALUE_INPUT);
			Generator.getInstance().inject(channelNumber, channelValue);
		} catch (OperationFailedException OFE) {
			// This should not occur
		} catch (OperationCancelledException OCE) {
			// User cancelled operation. Nevermind
		} catch (InvalidChannelValueException ICVE) {
			// This should not occur
		} catch (InvalidChannelNumberException ICNE) {
			// This should not occur
		}

	}

	/**
	 * Handle action and allow the user to specify a channel to insert a random
	 * value on.
	 */
	public void generate() {
		try {
			int channelNumber = new DMXUserInput()
					.getInput(DMXUserInput.CHANNEL_NUMBER_INPUT);
			Generator.getInstance().generate(channelNumber);
		} catch (OperationFailedException OFE) {
			// This should not occur
		} catch (OperationCancelledException OCE) {
			// User cancelled operation. Nevermind
		} catch (InvalidChannelNumberException ICNE) {
			// This should not occur
		}
	}

	/**
	 * Display the range of channels that have associations removed.
	 * @param channelNumber
	 * 					Integer representation of the first channel removed.
	 * @param numToDelete
	 * 					Integer representation of the size of removed range.
	 */
	public void displayRemove(Integer channelNumber, Integer numToDelete) {
		// TODO Remove stuff
	}

	public String getName() {
		return MODULE_NAME;
	}

	public QWidget getWidget() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getWidgetTitle() {
		return WIDGET_TITLE;
	}

	public Universe getUniverse() {
		return universe;
	}

	public boolean getListenerStatus() {
		return dmxListener;
	}
}
