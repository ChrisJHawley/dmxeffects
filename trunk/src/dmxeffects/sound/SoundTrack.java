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

import java.io.File;

import com.trolltech.qt.core.QObject;

import dmxeffects.OperationFailedException;

/**
 * Data storage class for storing data for sound track elements.
 * 
 * @author chris
 */
public class SoundTrack extends QObject {

	private final transient File trackFile;

	private transient String trackTitle = null;

	private transient int trackStatus;

	public transient Signal0 dataUpdated = new Signal0();

	/**
	 * Value used to indicate that the track has a "ready" status.
	 */
	public static final int READY_STATUS = 30001;

	/**
	 * Value used to indicate that the track has a "cued" status.
	 */
	public static final int CUED_STATUS = 30002;

	/**
	 * Value used to indicate that the track has a "playing" status.
	 */
	public static final int PLAYING_STATUS = 30003;

	/**
	 * Value used to indicate that the track has a "paused" status.
	 */
	public static final int PAUSED_STATUS = 30004;

	/**
	 * Creates a new instance of soundTrack
	 * 
	 * @param track
	 *            The File object representing the audio track.
	 * @param title
	 *            The String object representing the title of this track in the
	 *            system.
	 * @throws operationFailedException
	 *             Indication that one of the provided values did not validate.
	 */
	public SoundTrack(File trackFile, String trackTitle)
			throws OperationFailedException {
		super();
		if (trackFile == null) {
			throw new OperationFailedException("No file was provided.");
		}
		if (trackTitle.length() == 0) {
			throw new OperationFailedException("No title was provided.");
		} else {
			this.trackFile = trackFile;
			this.trackTitle = trackTitle;
			this.trackStatus = READY_STATUS;
		}
	}

	/**
	 * Get the current File object stored within this soundTrack
	 * 
	 * @return The File represening the audio track.
	 */
	public File getFile() {
		return trackFile;
	}

	/**
	 * Get the title of this soundTrack.
	 * 
	 * @return The String title of this soundTrack.
	 */
	public String getTitle() {
		return trackTitle;
	}

	/**
	 * Get the status of this soundTrack.
	 * 
	 * @return The int representation of the status.
	 */
	public int getStatus() {
		return trackStatus;
	}

	/**
	 * Set the title of this soundTrack.
	 * 
	 * @param title
	 *            The title to set for this soundTrack.
	 * @throws operationFailedException
	 *             Indication that the title did not pass validation.
	 */
	public void setTitle(final String trackTitle)
			throws OperationFailedException {
		if (trackTitle.length() == 0) {
			throw new OperationFailedException("No title was provided.");
		} else {
			this.trackTitle = trackTitle;
			dataUpdated.emit();
		}
	}

	/**
	 * Set the status of this soundTrack.
	 * 
	 * @param status
	 *            The status to set this soundTrack to have.
	 * @throws operationFailedException
	 *             Exception indicating that the status that was attempted to be
	 *             set was not valid.
	 */
	public void setStatus(final int trackStatus)
			throws OperationFailedException {
		switch (trackStatus) {
		case READY_STATUS:
		case CUED_STATUS:
		case PLAYING_STATUS:
		case PAUSED_STATUS:
			break;
		default:
			throw new OperationFailedException("Status: " + trackStatus
					+ " is not valid.");
		}
		this.trackStatus = trackStatus;
		dataUpdated.emit();
	}
}
