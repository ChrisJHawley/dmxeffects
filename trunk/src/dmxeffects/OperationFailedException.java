package dmxeffects;

/**
 * Exception thrown when an operation called in order to perform some necessary function fails.
 *
 * @author chris
 */
public class OperationFailedException extends java.lang.Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 572770114145315167L;


	/**
	 * Creates a new instance of <code>OperationFailedException</code> without detail message.
	 */
	public OperationFailedException() {
	}
	
	
	/**
	 * Constructs an instance of <code>OperationFailedException</code> with the specified detail message.
	 * @param msg the detail message.
	 */
	public OperationFailedException(String msg) {
		super(msg);
	}
}
