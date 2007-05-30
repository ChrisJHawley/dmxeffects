/*
 * $Id$
 */
package dmxeffects.dmx;

import java.util.concurrent.Semaphore;
import com.trolltech.qt.gui.*;
import dmxeffects.*;

/**
 * Class for providing the GUI elements of the DMX modules.
 * @author chris
 *
 */
public class DMXDisplay extends QWidget implements ModuleGUI {

	private static DMXDisplay singletonDisplay = null;
	private static Semaphore singletonLock = new Semaphore(1, true);
	
	private boolean dmxListener = false;
	
	private QMenu menu;
	private QMenu generateMenu;
	private QAction startListenerAction;
	private QAction generateRandomAction;
	private QAction generateChannelAction;
	private QAction injectAction;
	private QAction setAssocAction;
	
	private DMXDisplay() {
		// Prepare actions
		startListenerAction = new QAction(tr("&Start DMX Listener"), this);
		startListenerAction.setStatusTip(tr("Begin listening for DMX input"));
		startListenerAction.triggered.connect(this, "startListener()");
		startListenerAction.setEnabled(!dmxListener);
		
		generateRandomAction = new QAction(tr("Generate on &all channels"), this);
		generateRandomAction.setStatusTip(tr("Generate random DMX data for all channels"));
		generateRandomAction.triggered.connect(Generator.getInstance(), "generateAll()");
		generateRandomAction.setEnabled(dmxListener);
		
		generateChannelAction = new QAction(tr("Generate on &specific channel"), this);
		generateChannelAction.setStatusTip(tr("Generate random DMX for a specified channel"));
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
		
	}
	
	/**
	 * Retrieve a current instance, creating one if required.
	 * @return The current singleton instance of this class.
	 */
	public static DMXDisplay getInstance() {
		try {
			singletonLock.acquire();
			if (singletonDisplay == null) {
				singletonDisplay = new DMXDisplay();
			}
			singletonLock.release();
		} catch (InterruptedException IE) {
			System.err.println("Thread interruption detected");
			IE.printStackTrace(System.err);
		}
		return singletonDisplay;
	}
	
	
	public QMenu getMenu() {
		return menu;
	}
	
	public void runModeEnabled() {
		setAssocAction.setEnabled(false);
	}
	
	public void programModeEnabled() {
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
		
		Main.getInstance().statusBar().showMessage("DMX Listener started", 2000);
	}
	
	public void startListener() {
		Thread listenerThread = new Thread(DMXInput.getInstance());
		DMXInput.getInstance().moveToThread(listenerThread);
		listenerThread.setDaemon(true);
		DMXInput.getInstance().updateTable.connect(this, "updateTable()");
		
		listenerThread.start();
		
		// Inform the modules as appropriate.
		if (listenerThread.getState() == Thread.State.RUNNABLE) {
			dmxListenerEnabled();
		} else {
			// Um... not working! This isn't a good thing
		}
	}
	
	public void setAssoc() {
		
	}
	
	public void updateTable() {
		
	}
	
	public void inject() {
		try {
			int channelNumber = new DMXUserInput().getInput(DMXUserInput.CHANNEL_NUMBER_INPUT);
			int channelValue = new DMXUserInput().getInput(DMXUserInput.CHANNEL_VALUE_INPUT);
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
	
	public void generate() {
		try {
			int channelNumber = new DMXUserInput().getInput(DMXUserInput.CHANNEL_NUMBER_INPUT);
			Generator.getInstance().generate(channelNumber);
		} catch (OperationFailedException OFE) {
			// This should not occur
		} catch (OperationCancelledException OCE) {
			// User cancelled operation. Nevermind			
		} catch (InvalidChannelNumberException ICNE) {
			// This should not occur
		}
	}
}
