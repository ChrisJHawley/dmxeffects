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

/**
 * @author chris
 *
 */
public class Player extends QObject implements Runnable {

	// -- Signals for playback information -- //
	
	Signal0 playbackStartedSignal = new Signal0();

	Signal0 playbackStoppedSignal = new Signal0();

	/**
	 * Create a new instance of this class
	 */
	public Player() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Run when the containing Thread starts
	 */
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	// -- Action handlers -- //
	/**
	 * Handle a track beinq cued
	 * @param track
	 * 			Track to cue.
	 */
	public void cueTrack(SoundTrack track) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Handle play signal.
	 */
	public void play() {
		// TODO Auto-generated method stub
		
		// Emit signal indicating start
		playbackStartedSignal.emit();
	}
	
	/**
	 * Handle stop signal
	 */
	public void stop() {
		// TODO Auto-generated method stub

		// Emit signal indicating stoppage
		playbackStoppedSignal.emit();
	}
}
