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

/**
 * Class linking specific channel values to specific actions.
 * 
 * @author chris
 * 
 */
public class ControlChannel extends QObject {

	private int moduleChanNumber;

	private String moduleName;

	/**
	 * Signal generated from a control, which includes the channel value in case
	 * this is significant.
	 */
	public Signal1<Integer>[] controlSignal;

	/**
	 * Create a new channel for controls.
	 * 
	 * @param number
	 *            This ControlChannel's number for the module.
	 * @param module
	 *            The module this ControlChannel relates to
	 */
	// Bah, sodding lack of generic arrays means this will always warn
	// there may be some workaround required to fix this, until then suppress
	@SuppressWarnings("unchecked")
	public ControlChannel(int number, String module) {
		controlSignal = new Signal1[256];
		moduleChanNumber = number;
		moduleName = module;
	}

	/**
	 * Add a new value-signal pair to the ControlChannel.
	 * 
	 * @param val
	 *            The DMX value to associate with the action
	 * @param signal
	 *            The signal to be generated when triggered
	 * @throws InvalidChannelValueException
	 *             Indication that the provided value was not valid.
	 */
	public void setSignal(int val, Signal1<Integer> signal)
			throws InvalidChannelValueException {
		if (Validator.validate(val, Validator.CHANNEL_VALUE_VALIDATION)) {
			controlSignal[val] = signal;
		} else {
			throw new InvalidChannelValueException(val);
		}
	}

	/**
	 * Get a specific Signal object.
	 * 
	 * @param val
	 *            The signal to get
	 * @return The signal for this value of the channel.
	 * @throws InvalidChannelValueException
	 *             Indication that the provided value was not valid.
	 */
	public Signal1<Integer> getSignal(int val)
			throws InvalidChannelValueException {
		if (Validator.validate(val, Validator.CHANNEL_VALUE_VALIDATION)) {
			return controlSignal[val];
		} else {
			throw new InvalidChannelValueException(val);
		}
	}

	/**
	 * Trigger a specific Signal.
	 * 
	 * @param val
	 *            The value of Signal to trigger.
	 * @throws InvalidChannelValueException
	 *             Indication that the provided value was not valid.
	 */
	public void trigger(int val) throws InvalidChannelValueException {
		if (Validator.validate(val, Validator.CHANNEL_VALUE_VALIDATION)) {
			try {
				controlSignal[val].emit(new Integer(val));
			} catch (NullPointerException e) {
				// Nothing to signal
			}
		} else {
			throw new InvalidChannelValueException(val);
		}
	}

	/**
	 * Get this channel's number within those for the module
	 * 
	 * @return This ControlChannel's identifier number for the module.
	 */
	public int getModuleChanNumber() {
		return moduleChanNumber;
	}

	/**
	 * Get the name of the owning module for this ControlChannel
	 * 
	 * @return The name of the module to which this ControlChannel belongs.
	 */
	public String getModuleName() {
		return moduleName;
	}
}
