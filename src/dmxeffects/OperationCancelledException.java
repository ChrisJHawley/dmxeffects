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

/**
 * Exception thrown to indicate that the user cancelled an essential operation.
 *
 * @author chris
 */
public class OperationCancelledException extends java.lang.Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6042068577702888369L;


	/**
	 * Creates a new instance of <code>OperationCancelledException</code> without detail message.
	 */
	public OperationCancelledException() {
	}
	
	
	/**
	 * Constructs an instance of <code>OperationCancelledException</code> with the specified detail message.
	 * @param msg the detail message.
	 */
	public OperationCancelledException(String msg) {
		super(msg);
	}
}
