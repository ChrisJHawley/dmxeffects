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

	private File TrackFile = null;

	private String TrackTitle = null;

	private int TrackStatus;

	public Signal0 dataUpdated = new Signal0();

	/**
	 * Value used to indicate that the track has a "ready" status.
	 */
	public static final int TRACK_STATUS_READY = 30001;

	/**
	 * Value used to indicate that the track has a "cued" status.
	 */
	public static final int TRACK_STATUS_CUED = 30002;

	/**
	 * Value used to indicate that the track has a "playing" status.
	 */
	public static final int TRACK_STATUS_PLAYING = 30003;

	/**
	 * Value used to indicate that the track has a "paused" status.
	 */
	public static final int TRACK_STATUS_PAUSED = 30004;

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
	public SoundTrack(File track, String title) throws OperationFailedException {
		if (track == null) {
			throw new OperationFailedException("No file was provided.");
		}
		if (title.length() == 0) {
			throw new OperationFailedException("No title was provided.");
		} else {
			TrackFile = track;
			TrackTitle = title;
			TrackStatus = TRACK_STATUS_READY;
		}
	}

	/**
	 * Get the current File object stored within this soundTrack
	 * 
	 * @return The File represening the audio track.
	 */
	public File getFile() {
		return TrackFile;
	}

	/**
	 * Get the title of this soundTrack.
	 * 
	 * @return The String title of this soundTrack.
	 */
	public String getTitle() {
		return TrackTitle;
	}

	/**
	 * Get the status of this soundTrack.
	 * 
	 * @return The int representation of the status.
	 */
	public int getStatus() {
		return TrackStatus;
	}

	/**
	 * Set the title of this soundTrack.
	 * 
	 * @param title
	 *            The title to set for this soundTrack.
	 * @throws operationFailedException
	 *             Indication that the title did not pass validation.
	 */
	public void setTitle(String title) throws OperationFailedException {
		if (title.length() == 0) {
			throw new OperationFailedException("No title was provided.");
		} else {
			TrackTitle = title;
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
	public void setStatus(int status) throws OperationFailedException {
		switch (status) {
		case TRACK_STATUS_READY:
		case TRACK_STATUS_CUED:
		case TRACK_STATUS_PLAYING:
		case TRACK_STATUS_PAUSED:
			break;
		default:
			throw new OperationFailedException("Status: "
					+ String.valueOf(status) + " is not valid.");
		}
		TrackStatus = status;
		dataUpdated.emit();
	}
}
