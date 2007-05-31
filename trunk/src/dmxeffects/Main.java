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

	public void newShow() {

	}

	public void openShow() {

	}

	public void saveShow() {

	}

	public void saveShowAs() {

	}

	public void programMode() {
		programMode = true;
		programModeAction.setEnabled(!programMode);
		runModeAction.setEnabled(programMode);
		programModeSignal.emit();
	}

	public void runMode() {
		programMode = false;
		programModeAction.setEnabled(!programMode);
		runModeAction.setEnabled(programMode);
		runModeSignal.emit();
	}

	public void about() {

	}

	// -- Getters -- //

	public DMXDisplay getDMX() {
		return dmxDisplay;
	}

	public SoundModule getSound() {
		return soundModule;
	}

	public boolean getProgramMode() {
		return programMode;
	}
}
