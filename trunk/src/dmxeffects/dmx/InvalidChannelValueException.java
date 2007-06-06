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

/**
 * Exception to be thrown when a DMX Channel Value that is provided, is not
 * valid.
 * 
 * @author chris
 */
public class InvalidChannelValueException extends java.lang.Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8999225398251554354L;

	/**
	 * Creates a new instance of <code>InvalidChannelValueException</code>
	 * without detail message.
	 */
	public InvalidChannelValueException() {
		super();
	}

	/**
	 * Constructs an instance of <code>InvalidChannelValueException</code>
	 * with the specified detail message.
	 * 
	 * @param msg
	 *            the detail message.
	 */
	public InvalidChannelValueException(String msg) {
		super(msg);
	}

	/**
	 * Constructs an instance of <code>InvalidChannelValueException</code>
	 * with the standard error message using the value provided.
	 * 
	 * @param chanVal
	 *            The value that was out of the range.
	 */
	public InvalidChannelValueException(int chanVal) {
		super("Specified value " + chanVal
				+ "was not within the permissible range of 0 to 255 inclusive.");
	}
}
