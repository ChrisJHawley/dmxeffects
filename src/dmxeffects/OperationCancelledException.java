/*
 * $Id$
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
