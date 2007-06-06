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
	public transient Signal0 listenerEnabled = new Signal0();

	// -- Internal variables for this Module -- //
	private final String MODULE_NAME = tr("DMX Module"); // NOPMD by chris on 07/06/07 00:11

	private final String WIDGET_TITLE = tr("DMX Association Table"); // NOPMD by chris on 07/06/07 00:11

	private final transient Universe universe;

	private final transient DMXInput input;

	private transient boolean dmxListener = false;

	// -- GUI Elements -- //
	private final transient QMenu menu;

	private final transient QMenu generateMenu;

	private final transient QAction listenerAction;

	private final transient QAction randomAction;

	private final transient QAction channelAction;

	private final transient QAction injectAction;

	private final transient QAction setAssocAction;

	/**
	 * Instantiate a new instance of this class.
	 */
	public DMXDisplay() {
		super();
		// Create input object
		input = new DMXInput();

		// Initialise Universe
		universe = new Universe();

		// Prepare actions
		listenerAction = new QAction(tr("&Start DMX Listener"), this);
		listenerAction.setStatusTip(tr("Begin listening for DMX input"));
		listenerAction.triggered.connect(this, "startListener()");
		listenerAction.setEnabled(!dmxListener);

		randomAction = new QAction(tr("Generate on &all channels"),
				this);
		randomAction
				.setStatusTip(tr("Generate random DMX data for all channels"));
		randomAction.triggered.connect(Generator.getInstance(),
				"generateAll()");
		randomAction.setEnabled(dmxListener);

		channelAction = new QAction(
				tr("Generate on &specific channel"), this);
		channelAction
				.setStatusTip(tr("Generate random DMX for a specified channel"));
		channelAction.triggered.connect(this, "generate()");
		channelAction.setEnabled(dmxListener);

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
		menu.addAction(listenerAction);

		generateMenu = new QMenu("&Generate DMX");
		generateMenu.setEnabled(dmxListener);
		generateMenu.addAction(randomAction);
		generateMenu.addAction(channelAction);

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
		listenerAction.setEnabled(false);
		generateMenu.setEnabled(true);
		randomAction.setEnabled(true);
		channelAction.setEnabled(true);
		injectAction.setEnabled(true);

		// Transmit the signal
		listenerEnabled.emit();
	}

	/**
	 * Set the DMXInput Thread running, listening to the appropriate signal.
	 */
	public void startListener() {
		final Thread listenerThread = new Thread(input);
		input.inputValue.connect(universe, "setValue(Integer, Integer)");
		input.listenerStarted.connect(this, "dmxListenerEnabled()");
		input.moveToThread(listenerThread);
		listenerThread.setDaemon(true);

		listenerThread.start();
	}

	public void setAssoc() {
		// TODO Auto-generated method block
	}

	public void updateTableVal(final Integer channelNumber,
			final Integer channelValue) {
		// TODO Auto-generated method block
	}

	public void updateTableAssoc(final Integer channelNumber, 
			final String association) {
		// TODO Auto-generated method block
	}

	/**
	 * Handle action and allow the user to specify a channel and value to insert
	 * data for into the system.
	 */
	public void inject() {
		try {
			final int channelNumber = new DMXUserInput()
					.getInput(DMXUserInput.CHANNEL_NUMBER_INPUT);
			final int channelValue = new DMXUserInput()
					.getInput(DMXUserInput.CHANNEL_VALUE_INPUT);
			Generator.getInstance().inject(channelNumber, channelValue);
		} catch (OperationFailedException OFE) {
			// This should not occur
			OFE.printStackTrace(System.err);
		} catch (OperationCancelledException OCE) { // NOPMD by chris on 07/06/07 00:16
			// User cancelled operation. Nevermind
		} catch (InvalidChannelValueException ICVE) {
			// This should not occur
			ICVE.printStackTrace(System.err);
		} catch (InvalidChannelNumberException ICNE) {
			// This should not occur
			ICNE.printStackTrace(System.err);
		}

	}

	/**
	 * Handle action and allow the user to specify a channel to insert a random
	 * value on.
	 */
	public void generate() {
		try {
			final int channelNumber = new DMXUserInput()
					.getInput(DMXUserInput.CHANNEL_NUMBER_INPUT);
			Generator.getInstance().generate(channelNumber);
		} catch (OperationFailedException OFE) {
			// This should not occur
			OFE.printStackTrace(System.err);
		} catch (OperationCancelledException OCE) { // NOPMD by chris on 07/06/07 00:17
			// User cancelled operation. Nevermind
		} catch (InvalidChannelNumberException ICNE) {
			// This should not occur
			ICNE.printStackTrace(System.err);
		}
	}

	/**
	 * Display the range of channels that have associations removed.
	 * 
	 * @param channelNumber
	 *            Integer representation of the first channel removed.
	 * @param numToDelete
	 *            Integer representation of the size of removed range.
	 */
	public void displayRemove(final Integer channelNumber,
			final Integer numToDelete) {
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
