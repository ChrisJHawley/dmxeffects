package dmxeffects.dmx;

/**
 * Exception to be thrown when a DMX Channel Number that is provided, is not valid.
 * @author chris
 */
public class InvalidChannelNumberException extends java.lang.Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6629294444106256390L;

	/**
	 * Creates a new instance of <code>InvalidChannelNumberException</code> without detail message.
	 */
	public InvalidChannelNumberException() {
	}
	
	
	/**
	 * Constructs an instance of <code>InvalidChannelNumberException</code> with the specified detail message.
	 * @param msg the detail message.
	 */
	public InvalidChannelNumberException(String msg) {
		super(msg);
	}
	
	/**
	 * Constructs an instance of <code>InvalidChannelNumberException</code> with the standard error message using the value provided.
	 * @param chanNo The value that was out of the range.
	 */
	public InvalidChannelNumberException(int chanNo) {
		super("Specified value " + chanNo + " was not within the permissible range of 1 to 512 inclusive.");
	}
}
