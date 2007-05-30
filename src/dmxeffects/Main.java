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

import java.util.concurrent.Semaphore;
import com.trolltech.qt.gui.*;
import dmxeffects.dmx.*;
import dmxeffects.sound.*;

public class Main extends QMainWindow{

	private static Main singletonMain = null;
	private static Semaphore singletonLock = new Semaphore(1, true);
	
	private boolean programMode = true;
	
	private int notificationType = PASSIVE_NOTIFICATION;
	public static final int PASSIVE_NOTIFICATION = 100001;
	public static final int ACTIVE_NOTIFICATION = 100002;
	
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
	private QAction passiveNotificationsAction;
	private QAction activeNotificationsAction;
	
	private QMenu modulesMenu;
	
	private QMenu helpMenu;
	private QAction aboutAction;
	private QAction aboutQtAction;
    
	/**
	 * Method run when starting application.
	 * @param args Commandline arguments.
	 */
    public static void main(String[] args) {
    	//Main application turn on!
    	QApplication.initialize(args);
    	
    	Main.getInstance().show();
    	
    	QApplication.exec();
    }
    
    /**
     * Constructor for the main application.
     */
    private Main(){
        QMenuBar menuBar = new QMenuBar();
        setMenuBar(menuBar);
        
        // TODO: Make this portable
        setWindowIcon(new QIcon("/usr/share/icons/crystalsvg/128x128/apps/kcmdevices.png"));
        
        try {
        	createActions();
        } catch (Exception e) {
        	e.printStackTrace(System.err);
        }
        
        // TODO: Somehow dynamically gather the modules to load. This will require some thought.
        createMenus();
        createStatusBar();
        
        // Inform all the modules of the current system mode
        modeCheckSet();
        
        setWindowTitle(tr("DMXEffects"));
    }
    
    public static Main getInstance() {
    	try {
    		singletonLock.acquire();
    		if (singletonMain == null) {
    			singletonMain = new Main();
    		}
    		singletonLock.release();
    	} catch (InterruptedException IE) {
    		System.err.println("Thread interruption detected");
    		IE.printStackTrace(System.err);
    	}
    	return singletonMain;
    }
    
    /**
     * Displays a message box with information about this application.
     */
    public void aboutBox() {
    	QMessageBox.about(this, tr("About DMXEffects"), tr("DMXEffects © Chris Hawley"));
    }
    
    public void newShow() {
    	/* Create a new show. This is done by first confirming that any changes
    	 * are wanted to be discarded, and then resetting all the data stores.
    	 */ 
    	// TODO: Method for new show
    }
    
    public void openFile() {
    	// TODO: Method for opening show
    }
    
    public void saveFile() {
    	// TODO: Method for saving show
    }
        
    public void saveFileAs() {
    	// TODO: Method for saving show under different name
    }
    
    public void passiveNotify() {
    	//TODO: Enable passive notifications only
    }
    
    public void activeNotify() {
    	//TODO: Enable active notifications 
    }
    
    public void programMode() {
    	programMode = true;
    	programModeAction.setEnabled(false);
    	runModeAction.setEnabled(true);
    	
       	DMXDisplay.getInstance().programModeEnabled();
        SoundModule.getInstance().programModeEnabled();
    }
    
    public void runMode() {
    	programMode = false;
    	programModeAction.setEnabled(true);
    	runModeAction.setEnabled(false);
    	
    	
    	DMXDisplay.getInstance().runModeEnabled();
        SoundModule.getInstance().runModeEnabled();
    }
    
    /**
     * Check the current mode and set things as appropriate
     */
    public void modeCheckSet() {
    	if (programMode) {
    		programMode();
    	} else {
    		runMode();
    	}
    }
    
    /**
     * Provide information about the current state of program/run mode
     * @return Boolean indicating if the system is in program mode.
     */
    public boolean isProgramMode() {
    	return programMode;
    }
    
    // -- Private methods -- //
    
    /**
     * Set up the actions for use within this application
     */
    private void createActions() {
    	// New action
    	newAction = new QAction(tr("&New"), this);
    	newAction.setShortcut(new QKeySequence(tr("Ctrl+N")));
    	newAction.setStatusTip(tr("New DMXEffects Show"));
    	newAction.triggered.connect(this, "newShow()");
    	
    	// Open action
    	openAction = new QAction(tr("&Open"), this);
    	openAction.setShortcut(new QKeySequence(tr("Ctrl+O")));
    	openAction.setStatusTip(tr("Open existing DMXEffects Show file"));
    	openAction.triggered.connect(this, "openFile()");
    	
    	// Save action
    	saveAction = new QAction(tr("&Save"), this);
    	saveAction.setShortcut(new QKeySequence(tr("Ctrl+S")));
    	saveAction.setStatusTip(tr("Save to DMXEffects Show file"));
    	saveAction.triggered.connect(this, "saveFile()");
    	
    	// Save as action
    	saveAsAction = new QAction(tr("Save &As..."), this);
    	saveAsAction.setStatusTip(tr("Save to DMXEffects Show file with new name"));
    	saveAsAction.triggered.connect(this, "saveFileAs()");
    	
    	// Quit action
    	quitAction = new QAction(tr("&Quit"), this);
    	quitAction.setShortcut(new QKeySequence(tr("Ctrl+Q")));
    	quitAction.setStatusTip(tr("Exit this application"));
    	quitAction.triggered.connect(this, "close()");
    	
    	// Passive notifications action
    	passiveNotificationsAction = new QAction(tr("Passive notifications"), this);
    	passiveNotificationsAction.setStatusTip(tr("Set notifications to be passive"));
    	passiveNotificationsAction.triggered.connect(this, "passiveNotify()");
    	
    	// Active notifications action
    	activeNotificationsAction = new QAction(tr("Active notifications"), this);
    	activeNotificationsAction.setStatusTip(tr("Set notifications to be active"));
    	activeNotificationsAction.triggered.connect(this, "activeNotify()");
    	
    	if (notificationType == PASSIVE_NOTIFICATION) {
    		passiveNotificationsAction.setEnabled(false);
    		activeNotificationsAction.setEnabled(true);
    	} else {
    		passiveNotificationsAction.setEnabled(true);
    		activeNotificationsAction.setEnabled(false);
    	}
    	
    	// Program mode action
    	programModeAction = new QAction(tr("Program Mode"), this);
    	programModeAction.setStatusTip(tr("Enable program mode"));
    	programModeAction.triggered.connect(this, "programMode()");
    	programModeAction.setEnabled(!programMode);
    	
    	// Run mode action
    	runModeAction = new QAction(tr("Run Mode"), this);
    	runModeAction.setStatusTip(tr("Enable run mode"));
    	runModeAction.triggered.connect(this, "runMode()");
    	runModeAction.setEnabled(programMode);
    	
    	// About action
    	aboutAction = new QAction(tr("&About"), this);
    	aboutAction.setStatusTip(tr("About this application"));
    	aboutAction.triggered.connect(this, "aboutBox()");
    	
    	// About Qt action
    	aboutQtAction = new QAction(tr("About &Qt"), this);
    	aboutQtAction.setStatusTip(tr("About Qt"));
    	aboutQtAction.triggered.connect(QApplication.instance(), "aboutQt()");
    }
    
    /**
     * Set up the menus for use within this application
     */
    private void createMenus() {
    	fileMenu = menuBar().addMenu(tr("&File"));
    	fileMenu.addAction(newAction);
    	fileMenu.addAction(openAction);
    	fileMenu.addAction(saveAction);
    	fileMenu.addAction(saveAsAction);
    	fileMenu.addSeparator();
    	fileMenu.addAction(quitAction);
    	
    	optionsMenu = menuBar().addMenu(tr("&Options"));
    	optionsMenu.addAction(passiveNotificationsAction);
    	optionsMenu.addAction(activeNotificationsAction);
    	optionsMenu.addSeparator();
    	optionsMenu.addAction(programModeAction);
    	optionsMenu.addAction(runModeAction);
    	
    	modulesMenu = menuBar().addMenu(tr("&Modules"));
    	modulesMenu.addMenu(DMXDisplay.getInstance().getMenu());
    	//modulesMenu.addMenu(SoundModule.getInstance().getMenu());
    	
    	helpMenu = menuBar().addMenu(tr("&Help"));
    	helpMenu.addAction(aboutAction);
    	helpMenu.addAction(aboutQtAction);
    }
    
    private void createStatusBar() {
    	statusBar().showMessage(tr("Ready"));
    }

	public void dmxListenerEnabled() {
		statusBar().showMessage(tr("DMX Listener started"), 2000);
		
		SoundModule.getInstance().dmxListenerEnabled();
	}
}
