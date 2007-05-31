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

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.*;

import dmxeffects.Main;
import dmxeffects.Module;
import dmxeffects.dmx.ControlChannel;
import dmxeffects.dmx.InvalidChannelValueException;

/**
 * Sound module main file. Provides all interfaces from external classes to the
 * functionality provided by this module.
 * 
 * @author chris
 * 
 */
public class SoundModule extends QObject implements Module {

	// -- Module configuration information -- //
	private static final int CHANNELS_REQUIRED = 2;

	private static final String MODULE_NAME = "Sound Module";

	private int firstControlChannel = -1;

	// -- Data storage for the tracks -- //
	private SoundTrack[] trackArray;

	private ControlChannel[] controls = new ControlChannel[CHANNELS_REQUIRED];

	public Signal1<Integer> trackQueueSignal;

	public Signal1<Integer> startPlaybackSignal;

	public Signal1<Integer> stopPlaybackSignal;

	// -- GUI Elements -- //
	private QMenu soundMenu;

	private QAction changeAssociationAction;

	private QAction testSoundAction;

	private QAction addTrackAction;

	private QAction editTrackAction;

	private QAction deleteTrackAction;

	private QAction clearTracksAction;

	/**
	 * Create new SoundModule.
	 * 
	 */
	public SoundModule() {
		// Initialise data storage
		trackArray = new SoundTrack[256];

		// Initialise controls
		// Control Channel 1 is used for queing tracks
		controls[0] = new ControlChannel(1, MODULE_NAME);
		trackQueueSignal = new Signal1<Integer>();
		trackQueueSignal.connect(this, "queueTrack(Integer)");
		for (int i = 0; i < 256; i++) {
			try {
				controls[0].setSignal(i, trackQueueSignal);
			} catch (InvalidChannelValueException e) {
				e.printStackTrace(System.err);
			}
		}

		// Control Channel 2 is used for play controls
		controls[1] = new ControlChannel(2, MODULE_NAME);
		startPlaybackSignal = new Signal1<Integer>();
		startPlaybackSignal.connect(this, "startPlayback(Integer)");
		stopPlaybackSignal = new Signal1<Integer>();
		stopPlaybackSignal.connect(this, "stopPlayback(Integer)");
		try {
			controls[1].setSignal(10, startPlaybackSignal);
			controls[1].setSignal(20, stopPlaybackSignal);
		} catch (InvalidChannelValueException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace(System.err);
		}

		// Initialise GUI
		try {
			createActions();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		createMenus();

	}

	public void createActions() {
		// If actions have conditional enable they are created disabled
		changeAssociationAction = new QAction(tr("&Change DMX Association"),
				this);
		changeAssociationAction
				.setStatusTip(tr("Change DMX Channel Association for the sound module"));
		changeAssociationAction.triggered.connect(this, "setAssoc()");
		changeAssociationAction.setEnabled(false);

		testSoundAction = new QAction(tr("&Test Sound Output"), this);
		testSoundAction
				.setStatusTip(tr("Perform a test of the sound setup on this system"));
		testSoundAction.triggered.connect(this, "testSound()");
		testSoundAction.setEnabled(false);

		addTrackAction = new QAction(tr("&Add Track"), this);
		addTrackAction.setStatusTip(tr("Add a new track the show"));
		addTrackAction.triggered.connect(this, "addTrack()");
		addTrackAction.setEnabled(false);

		editTrackAction = new QAction(tr("&Edit Track"), this);
		editTrackAction
				.setStatusTip(tr("Edit an existing track within the show"));
		editTrackAction.triggered.connect(this, "editTrack()");
		editTrackAction.setEnabled(false);

		deleteTrackAction = new QAction(tr("&Delete Track"), this);
		deleteTrackAction.setStatusTip(tr("Remove a track from the show"));
		deleteTrackAction.triggered.connect(this, "deleteTrack()");
		deleteTrackAction.setEnabled(false);

		clearTracksAction = new QAction(tr("&Clear Tracks"), this);
		clearTracksAction.setStatusTip(tr("Remove all tracks from the show"));
		clearTracksAction.triggered.connect(this, "clearTracks()");
		clearTracksAction.setEnabled(false);
	}

	public void createMenus() {
		soundMenu = new QMenu(tr("&Sound"));
		soundMenu.addAction(changeAssociationAction);
		soundMenu.addAction(testSoundAction);
		soundMenu.addSeparator();
		soundMenu.addAction(addTrackAction);
		soundMenu.addAction(editTrackAction);
		soundMenu.addAction(deleteTrackAction);
		soundMenu.addAction(clearTracksAction);
	}

	// -- Implement Module classes -- //

	/*
	 * (non-Javadoc)
	 * 
	 * @see dmxeffects.Module#getMenu()
	 */
	public QMenu getMenu() {
		return soundMenu;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dmxeffects.Module#getName()
	 */
	public String getName() {
		return MODULE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dmxeffects.Module#getWidget()
	 */
	public QWidget getWidget() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dmxeffects.Module#getWidgetTitle()
	 */
	public String getWidgetTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	// -- Other public methods -- //

	/**
	 * Provide the number of channels required for this module.
	 * 
	 * @return The requisite number of channels.
	 */
	public int getChannelsRequired() {
		return CHANNELS_REQUIRED;
	}

	// -- Action handlers -- //

	public void dmxListenerEnabled() {
		// Run association method as we will want to be able to act on suitable
		// input
		setAssoc();

		// Start listening for input
		Main.getInstance().getDMX().getUniverse().dmxValueUpdater.connect(this,
				"dmxInput(Integer, Integer)");

		// Listen for channel assignments being revoked
		Main.getInstance().getDMX().getUniverse().assocRemUpdater.connect(this,
				"assocUpdate(Integer, Integer)");

		// Allow associations to be changed if in program mode
		if (Main.getInstance().getProgramMode()) {
			changeAssociationAction.setEnabled(true);
		}

	}

	/**
	 * Handle DMX Input signals sent by the Universe. All input is assumed to
	 * have been validated due to the signal being sent by the Universe class.
	 * 
	 * @param channelNumber
	 *            Number of the channel for which input has been receieved
	 * @param channelValue
	 *            Value this channel now holds.
	 */
	public void dmxInput(Integer channelNumber, Integer channelValue) {
		// TODO Auto-generated method stub
		// will confirm the channelNumber is one we're listening to, and then
		// check if the channelValue is appropriate to perform an action.
		int chanNum = channelNumber.intValue();
		if ((firstControlChannel != -1) && (chanNum >= firstControlChannel)
				&& (chanNum < firstControlChannel + CHANNELS_REQUIRED)) {
			int chanVal = channelValue.intValue();
			try {
				controls[chanNum].trigger(chanVal);
			} catch (ArrayIndexOutOfBoundsException AOB) {
				// No controls on this channel
			} catch (NullPointerException NPE) {
				// No controls on this channel
			} catch (InvalidChannelValueException ICVE) {
				// Should not occur
			}
		}

	}

	/**
	 * Handle channel association removal signals sent by Universe. This allows
	 * the module to remove its own if appropriate.
	 * 
	 * @param firstChannel
	 *            The first of the channels being revoked.
	 * @param range
	 *            The range of channels being revoked.
	 */
	public void assocUpdate(Integer firstChannel, Integer range) {
		// TODO Auto-generated method stub
	}

	public void programMode() {
		// Enable actions that are only available in program mode
		try {
			if (Main.getInstance().getDMX().getListenerStatus()) {
				changeAssociationAction.setEnabled(true);
			}
		} catch (NullPointerException npe) {
			// This is only thrown if Main is presently creating itself
		}
		addTrackAction.setEnabled(true);
		editTrackAction.setEnabled(true);
		deleteTrackAction.setEnabled(true);
		clearTracksAction.setEnabled(true);
	}

	public void runMode() {
		// Disable actions that could be detrimental if in run mode.
		changeAssociationAction.setEnabled(false);
		addTrackAction.setEnabled(false);
		editTrackAction.setEnabled(false);
		deleteTrackAction.setEnabled(false);
		clearTracksAction.setEnabled(false);
	}

	/**
	 * Set the channel association for this module
	 * 
	 */
	public void setAssoc() {
		// TODO Auto-generated method stub
	}

	/**
	 * Test the sound system.
	 * 
	 */
	public void testSound() {
		// TODO Auto-generated method stub
	}

	/**
	 * Add a new track into the show.
	 * 
	 */
	public void addTrack() {
		// TODO Auto-generated method stub
	}

	/**
	 * Edit an existing track within the show.
	 * 
	 */
	public void editTrack() {
		// TODO Auto-generated method stub
	}

	/**
	 * Remove a track from the show.
	 * 
	 */
	public void deleteTrack() {
		// TODO Auto-generated method stub
	}

	/**
	 * Remove all tracks from the show.
	 * 
	 */
	public void clearTracks() {
		// TODO Auto-generated method stub
	}

	/**
	 * Queue a track for playback.
	 * 
	 * @param val
	 *            DMX Value of the track to be queued.
	 */
	public void queueTrack(Integer val) {
		// TODO Auto-generated method stub
	}

	/**
	 * Start playback.
	 * 
	 * @param val
	 *            Not used.
	 */
	public void startPlayback(Integer val) {
		// TODO Auto-generated method stub
	}

	/**
	 * Stop playback
	 * 
	 * @param val
	 *            Not used.
	 */
	public void stopPlayback(Integer val) {
		// TODO Auto-generated method stub
	}
}
