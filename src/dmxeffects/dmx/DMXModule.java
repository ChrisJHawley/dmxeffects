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

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.Qt.ItemFlag;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QTableWidgetItem;
import com.trolltech.qt.gui.QWidget;

import dmxeffects.Module;
import dmxeffects.OperationCancelledException;
import dmxeffects.OperationFailedException;

/**
 * Class for providing the GUI elements of the DMX modules.
 * 
 * @author chris
 * 
 */
public class DMXModule extends QObject implements Module {

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

	private QAction listenerAction;

	private QAction randomAction;

	private QAction channelAction;

	private QAction injectAction;

	private QAction setAssocAction;

	private QTableWidget dmxTable;

	/**
	 * Instantiate a new instance of this class.
	 */
	public DMXModule() {
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

		randomAction = new QAction(tr("Generate on &all channels"), this);
		randomAction
				.setStatusTip(tr("Generate random DMX data for all channels"));
		randomAction.triggered
				.connect(Generator.getInstance(), "generateAll()");
		randomAction.setEnabled(dmxListener);

		channelAction = new QAction(tr("Generate on &specific channel"), this);
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

		// Initialise Table
		dmxTable = new QTableWidget(512, 3);
		dmxTable.setAcceptDrops(false);
		dmxTable.setAlternatingRowColors(true);
		dmxTable.setDragEnabled(false);
		dmxTable.setVisible(true);
		dmxTable.setHorizontalHeaderItem(0, new QTableWidgetItem(
				tr("DMX Channel")));
		dmxTable.setHorizontalHeaderItem(1, new QTableWidgetItem(
				tr("Current Value")));
		dmxTable.setHorizontalHeaderItem(2, new QTableWidgetItem(
				tr("Associated Element")));
		for (int i = 0; i < 512; i++) {
			// Number the rows
			dmxTable.setItem(i, 0, new QTableWidgetItem(tr(String
					.valueOf(i + 1))));

			// Initialise the DMX values to 0
			dmxTable.setItem(i, 1, new QTableWidgetItem(tr(String.valueOf(0))));

			// Cannot do anything other than select these cells.
			dmxTable.item(i, 0).setFlags(ItemFlag.ItemIsSelectable);
			dmxTable.item(i, 1).setFlags(ItemFlag.ItemIsSelectable);
		}
		// Listen for edits to cells
		dmxTable.cellChanged.connect(this, "assocEdited(Integer, Integer)");

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

	/**
	 * Update a value for a specific channel within the DMX table.
	 * 
	 * @param channelNumber
	 *            The channel to update.
	 * @param channelValue
	 *            The new value.
	 */
	public void updateTableVal(final Integer channelNumber,
			final Integer channelValue) {
		synchronized (dmxTable) {
			dmxTable.setItem(channelNumber.intValue() -1 , 1, new QTableWidgetItem(
					tr(channelValue.toString())));
			dmxTable.item(channelNumber.intValue() -1, 1).setFlags(ItemFlag.ItemIsSelectable);
		}
	}

	/**
	 * Update an association for a specific channel within the DMX table.
	 * 
	 * @param channelNumber
	 *            The channel to update.
	 * @param association
	 *            The new association.
	 */
	public void updateTableAssoc(final Integer channelNumber,
			final String association) {
		synchronized (dmxTable) {
			dmxTable.setItem(channelNumber.intValue(), 2, new QTableWidgetItem(
					tr(association)));
			dmxTable.item(channelNumber.intValue(), 2).setFlags(ItemFlag.ItemIsSelectable);
		}
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
		} catch (OperationCancelledException OCE) {
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
		} catch (OperationCancelledException OCE) {
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
		synchronized (dmxTable) {
			int start = channelNumber.intValue();
			int finish = start + numToDelete.intValue();
			for (int i = start; i < finish; i++) {
				dmxTable.setItem(i, 2, new QTableWidgetItem(""));
				dmxTable.item(i, 2).setFlags(new ItemFlag[] {ItemFlag.ItemIsEditable, ItemFlag.ItemIsSelectable});
			}
		}
	}
	
	/**
	 * Handle edits to specific cells within the table, which should only be of
	 * associations.
	 * @param row Row changed.
	 * @param column Column changed, this should always be 2.
	 */
	public void assocEdited(Integer row, Integer column) {
		if (column.intValue() == 0) {
			// Reset to the correct value and lock out edits
			synchronized(dmxTable) {
				dmxTable.setItem(row.intValue(), 0, new QTableWidgetItem(tr(String.valueOf(row.intValue() +1))));
				dmxTable.item(row.intValue(), 0).setFlags(ItemFlag.ItemIsSelectable);
			}
		}
		if (column.intValue() == 1) {
			// Reset to the correct value and lock out edits
			synchronized(dmxTable) {
				dmxTable.setItem(row.intValue(), 1, new QTableWidgetItem());
				dmxTable.item(row.intValue(), 1).setFlags(ItemFlag.ItemIsSelectable);
			}
		}
		if (column.intValue() == 2) {
			// Only handle updates to associations.
			synchronized(dmxTable) {
				// Extract the new data, store it in the universe, and lock the cell
				try {
					universe.setAssociation(row.intValue() +1, dmxTable.item(row.intValue(), 1).text());
				} catch (OperationCancelledException OCE) {
					// Nevermind.
				} catch (InvalidChannelNumberException ICNE) {
					// Hmm, strange.
					ICNE.printStackTrace(System.err);
				}
				dmxTable.item(row.intValue(), 1).setFlags(ItemFlag.ItemIsSelectable);
			}
		}
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
	
	/**
	 * Handle actions where a user wishes to set some association.
	 *
	 */
	public void setAssoc() {
		// TODO Auto-generated method stub
	}
}
