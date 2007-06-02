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

import com.trolltech.qt.gui.*;

import dmxeffects.dmx.DMXDisplay;
import dmxeffects.sound.SoundModule;

public class Main extends QMainWindow {

	// Singleton variable.
	private static Main singletonApp;

	// -- Internal data storage -- //
	private boolean programMode = true;

	private boolean modifiedSinceSave = false;

	private DMXDisplay dmxDisplay;

	private SoundModule soundModule;

	// -- Signals offered by this class -- //
	public Signal0 programModeSignal = new Signal0();

	public Signal0 runModeSignal = new Signal0();

	// -- GUI Element declarations -- //
	private QMenu fileMenu;

	private QAction newAction;

	private QAction openAction;

	private QAction saveAction;

	private QAction saveAsAction;

	private QAction quitAction;

	private QMenu optionsMenu;

	private QAction programModeAction;

	private QAction runModeAction;

	private QMenu modulesMenu;

	private QMenu helpMenu;

	private QAction aboutAction;

	/**
	 * Method run when starting application.
	 * 
	 * @param args
	 *            Commandline arguments.
	 */
	public static void main(String[] args) {
		// Main application turn on!
		QApplication.initialize(args);

		// Initialising in this manner ensures thread safety.
		singletonApp = new Main();

		Main.getInstance().show();

		QApplication.exec();
	}

	/**
	 * Get the instance of this application.
	 * 
	 * @return Application instance.
	 */
	public static final Main getInstance() {
		return singletonApp;
	}

	/**
	 * Constructor for the main application.
	 */
	private Main() {
		QMenuBar menuBar = new QMenuBar();
		setMenuBar(menuBar);

		// TODO: Application icon

		try {
			createActions();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}

		// TODO: Somehow dynamically gather the modules to load.
		// Also set them listening to the various signals that are required
		dmxDisplay = new DMXDisplay();
		programModeSignal.connect(dmxDisplay, "programMode()");
		runModeSignal.connect(dmxDisplay, "runMode()");

		soundModule = new SoundModule();
		dmxDisplay.listenerEnabled.connect(soundModule, "dmxListenerEnabled()");
		programModeSignal.connect(soundModule, "programMode()");
		runModeSignal.connect(soundModule, "runMode()");

		createMenus();
		createStatusBar();

		// Start in program mode
		programMode();

		setWindowTitle(tr("DMXEffects"));
	}

	private void createActions() {
		// New action
		newAction = new QAction(tr("&New"), this);
		newAction.setShortcut(new QKeySequence(tr("Ctrl+N")));
		newAction.setStatusTip(tr("Begin new show"));
		newAction.triggered.connect(this, "newShow()");

		// Open action
		openAction = new QAction(tr("&Open"), this);
		openAction.setShortcut(new QKeySequence(tr("Ctrl+O")));
		openAction.setStatusTip(tr("Open existing show"));
		openAction.triggered.connect(this, "openShow()");

		// Save action
		saveAction = new QAction(tr("&Save"), this);
		saveAction.setShortcut(new QKeySequence(tr("Ctrl+S")));
		saveAction.setStatusTip(tr("Save show"));
		saveAction.triggered.connect(this, "saveShow()");

		// Save as action
		saveAsAction = new QAction(tr("Save &as"), this);
		saveAsAction.setStatusTip(tr("Save show with different file name"));
		saveAsAction.triggered.connect(this, "saveShowAs()");

		// Quit action
		quitAction = new QAction(tr("&Quit"), this);
		quitAction.setShortcut(new QKeySequence(tr("Ctrl+Q")));
		quitAction.setStatusTip(tr("Quit DMXEffects"));
		quitAction.triggered.connect(this, "close()");

		// Program mode action
		programModeAction = new QAction(tr("&Program Mode"), this);
		programModeAction.setStatusTip(tr("Enable program mode"));
		programModeAction.triggered.connect(this, "programMode()");
		programModeAction.setEnabled(!programMode);

		// Run mode action
		runModeAction = new QAction(tr("&Run Mode"), this);
		runModeAction.setStatusTip(tr("Enable run mode"));
		runModeAction.triggered.connect(this, "runMode()");
		runModeAction.setEnabled(programMode);

		// About action
		aboutAction = new QAction(tr("&About"), this);
		aboutAction.setStatusTip(tr("About DMXEffects"));
		aboutAction.triggered.connect(this, "about()");
	}

	private void createMenus() {
		// TODO: Icons for appropriate items (new/open/save/quit etc)

		// File menu
		fileMenu = menuBar().addMenu(tr("&File"));
		fileMenu.addAction(newAction);
		fileMenu.addAction(openAction);
		fileMenu.addAction(saveAction);
		fileMenu.addAction(saveAsAction);
		fileMenu.addSeparator();
		fileMenu.addAction(quitAction);

		// Options menu
		optionsMenu = menuBar().addMenu(tr("&Options"));
		optionsMenu.addAction(programModeAction);
		optionsMenu.addAction(runModeAction);

		// Modules menu
		modulesMenu = menuBar().addMenu(tr("&Modules"));
		modulesMenu.addMenu(dmxDisplay.getMenu());
		modulesMenu.addMenu(soundModule.getMenu());

		// Help menu
		helpMenu = menuBar().addMenu(tr("&Help"));
		helpMenu.addAction(aboutAction);

	}

	private void createStatusBar() {
		statusBar().showMessage(tr("Ready"), 5000);
	}

	// -- Action handlers -- //

	/**
	 * Create a new show. 
	 * If appropriate prompt the user to save first, and then open a blank show
	 */
	public void newShow() {
		if (modifiedSinceSave) {
			// TODO Prompt to save
		}
		// TODO Create new Show
	}

	/**
	 * Open an existing show.
	 * If appropriate prompt the user to save first, then open an existing show
	 */
	public void openShow() {
		if (modifiedSinceSave) {
			// TODO Prompt to save
		}
		// TODO Open file
	}

	/**
	 * Save the show to existing location.
	 * If the show has already been saved, save to this location, otherwise
	 * prompt the user for a location.
	 */
	public void saveShow() {
		// TODO Save to existing path or prompt if none set
		modifiedSinceSave = false;
	}

	/**
	 * Save the show to a new location.
	 * Prompt the user for a location to save to.
	 */
	public void saveShowAs() {
		// TODO Prompt for path to save to
		modifiedSinceSave = false;
	}

	/**
	 * Enable program mode.
	 */
	public void programMode() {
		programMode = true;
		programModeAction.setEnabled(!programMode);
		runModeAction.setEnabled(programMode);
		programModeSignal.emit();
	}

	/**
	 * Enable run mode.
	 */
	public void runMode() {
		programMode = false;
		programModeAction.setEnabled(!programMode);
		runModeAction.setEnabled(programMode);
		runModeSignal.emit();
	}

	/**
	 * Display an "About" box.
	 */
	public void about() {
		// TODO Display an "About" box for this application
	}

	/**
	 * Inform the application that some of the data has been modified
	 */
	public void modified() {
		// TODO Change title to display this change
		modifiedSinceSave = true;
	}

	// -- Getters -- //

	/**
	 * Get the object containing DMX info.
	 * 
	 * @return
	 * 			Application's DMXDisplay object.
	 */
	public DMXDisplay getDMX() {
		return dmxDisplay;
	}

	/**
	 * Get the object containing Sound info.
	 *
	 * @return
	 * 			Application's SoundModule object.
	 */
	public SoundModule getSound() {
		return soundModule;
	}

	/**
	 * Get the current mode of the application.
	 * @return
	 * 			True if the application is in Program Mode, false otherwise.
	 */
	public boolean getProgramMode() {
		return programMode;
	}

	/**
	 * Get the modified status of this show.
	 * @return
	 * 			True if the show has been modifed, false otherwise.
	 */
	public boolean getModified() {
		return modifiedSinceSave;
	}

}
