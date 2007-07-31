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
	private transient boolean isProgramMode = true;

	private transient boolean modifiedSinceSave = false;

	private transient final DMXDisplay dmxDisplay;

	private transient final SoundModule soundModule;

	// -- Signals offered by this class -- //
	public transient Signal0 programModeSignal = new Signal0();

	public transient Signal0 runModeSignal = new Signal0();

	// -- Action declarations -- //
	private transient QAction newAction;

	private transient QAction openAction;

	private transient QAction saveAction;

	private transient QAction saveAsAction;

	private transient QAction quitAction;

	private transient QAction programModeAction;

	private transient QAction runModeAction;

	private transient QAction aboutAction;

	/**
	 * Method run when starting application.
	 * 
	 * @param args
	 *            Commandline arguments.
	 */
	public static void main(final String[] args) {
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
		super();
		final QMenuBar menuBar = new QMenuBar();
		setMenuBar(menuBar);

		// TODO: Application icon

		try {
			createActions();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}

		/*
		 * TODO: Somehow dynamically gather the modules to load. Also set them
		 * listening to the various signals that are required.
		 * 
		 * This gathering could be done based on the last configuration, or if
		 * no such configuration exists through the defaults (i.e. all of the
		 * available ones as specified in the default config). Users could then
		 * {en,dis}able modules as required. The DMX module would always be on.
		 */

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
		programModeAction.setEnabled(!isProgramMode);

		// Run mode action
		runModeAction = new QAction(tr("&Run Mode"), this);
		runModeAction.setStatusTip(tr("Enable run mode"));
		runModeAction.triggered.connect(this, "runMode()");
		runModeAction.setEnabled(isProgramMode);

		// About action
		aboutAction = new QAction(tr("&About"), this);
		aboutAction.setStatusTip(tr("About DMXEffects"));
		aboutAction.triggered.connect(this, "about()");
	}

	private void createMenus() {
		// TODO: Icons for appropriate items (new/open/save/quit etc)

		// File menu
		QMenu fileMenu;
		fileMenu = menuBar().addMenu(tr("&File"));
		fileMenu.addAction(newAction);
		fileMenu.addAction(openAction);
		fileMenu.addAction(saveAction);
		fileMenu.addAction(saveAsAction);
		fileMenu.addSeparator();
		fileMenu.addAction(quitAction);

		// Options menu
		QMenu optionsMenu;
		optionsMenu = menuBar().addMenu(tr("&Options"));
		optionsMenu.addAction(programModeAction);
		optionsMenu.addAction(runModeAction);

		// Modules menu
		QMenu modulesMenu;
		modulesMenu = menuBar().addMenu(tr("&Modules"));
		modulesMenu.addMenu(dmxDisplay.getMenu());
		modulesMenu.addMenu(soundModule.getMenu());

		// Help menu
		QMenu helpMenu;
		helpMenu = menuBar().addMenu(tr("&Help"));
		helpMenu.addAction(aboutAction);

	}

	private void createStatusBar() {
		statusBar().showMessage(tr("Ready"), 5000);
	}

	// -- Action handlers -- //

	/**
	 * Create a new show. If appropriate prompt the user to save first, and then
	 * open a blank show
	 */
	public void newShow() {
		if (modifiedSinceSave) {
			// Prompt to save
			String confirmMessage;
			confirmMessage = "There are unsaved changes in this show";
			final QMessageBox.StandardButtons options = new QMessageBox.StandardButtons(
					QMessageBox.StandardButton.SaveAll,
					QMessageBox.StandardButton.Discard,
					QMessageBox.StandardButton.Cancel);
			final QMessageBox.StandardButton response = QMessageBox.question(
					this, "Unsaved changes", confirmMessage, options,
					QMessageBox.StandardButton.SaveAll);
			if (response.equals(QMessageBox.StandardButton.SaveAll)) {
				// Save the show
				saveShow();
				// Then return here
				newShow();
			} else if (response.equals(QMessageBox.StandardButton.Discard)) {
				// Changes... I don't need no stinkin' changes!
				// TODO: Create new show
			} else {
				// Abort
				statusBar().showMessage(tr("Cancelled"), 5000);
			}
		} else {
			// TODO Create new Show
		}
	}

	/**
	 * Open an existing show. If appropriate prompt the user to save first, then
	 * open an existing show
	 */
	public void openShow() {
		if (modifiedSinceSave) {
			// Prompt to save
			String confirmMessage;
			confirmMessage = "There are unsaved changes in this show";
			final QMessageBox.StandardButtons options = new QMessageBox.StandardButtons(
					QMessageBox.StandardButton.SaveAll,
					QMessageBox.StandardButton.Discard,
					QMessageBox.StandardButton.Cancel);
			final QMessageBox.StandardButton response = QMessageBox.question(
					this, "Unsaved changes", confirmMessage, options,
					QMessageBox.StandardButton.SaveAll);
			if (response.equals(QMessageBox.StandardButton.SaveAll)) {
				// Save the show
				saveShow();
				// Then return here
				openShow();
			} else if (response.equals(QMessageBox.StandardButton.Discard)) {
				// Changes... I don't need no stinkin' changes!
				// TODO: Open show
			} else {
				// Abort
				statusBar().showMessage(tr("Cancelled"), 5000);
			}
		} else {
			// TODO: Open Show
		}
	}

	/**
	 * Save the show to existing location. If the show has already been saved,
	 * save to this location, otherwise prompt the user for a location.
	 */
	public void saveShow() {
		// TODO Save to existing path or prompt if none set
		modifiedSinceSave = false;
	}

	/**
	 * Save the show to a new location. Prompt the user for a location to save
	 * to.
	 */
	public void saveShowAs() {
		// TODO Prompt for path to save to
		modifiedSinceSave = false;
	}

	/**
	 * Enable program mode.
	 */
	public void programMode() {
		isProgramMode = true;
		programModeAction.setEnabled(!isProgramMode);
		runModeAction.setEnabled(isProgramMode);
		programModeSignal.emit();
	}

	/**
	 * Enable run mode.
	 */
	public void runMode() {
		isProgramMode = false;
		programModeAction.setEnabled(!isProgramMode);
		runModeAction.setEnabled(isProgramMode);
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
	 * @return Application's DMXDisplay object.
	 */
	public DMXDisplay getDMX() {
		return dmxDisplay;
	}

	/**
	 * Get the object containing Sound info.
	 * 
	 * @return Application's SoundModule object.
	 */
	public SoundModule getSound() {
		return soundModule;
	}

	/**
	 * Get the current mode of the application.
	 * 
	 * @return True if the application is in Program Mode, false otherwise.
	 */
	public boolean getProgramMode() {
		return isProgramMode;
	}

	/**
	 * Get the modified status of this show.
	 * 
	 * @return True if the show has been modifed, false otherwise.
	 */
	public boolean getModified() {
		return modifiedSinceSave;
	}

}
