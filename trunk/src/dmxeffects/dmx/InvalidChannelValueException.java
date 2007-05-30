package dmxeffects.dmx;

/**
 * Exception to be thrown when a DMX Channel Value that is provided, is not valid.
 * @author chris
 */
public class InvalidChannelValueException extends java.lang.Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8999225398251554354L;

	/**
	 * Creates a new instance of <code>InvalidChannelValueException</code> without detail message.
	 */
	public InvalidChannelValueException() {
	}
	
	
	/**
	 * Constructs an instance of <code>InvalidChannelValueException</code> with the specified detail message.
	 * @param msg the detail message.
	 */
	public InvalidChannelValueException(String msg) {
		super(msg);
	}
	
	/**
	 * Constructs an instance of <code>InvalidChannelValueException</code> with the standard error message using the value provided.
	 * @param chanVal The value that was out of the range.
	 */
	public InvalidChannelValueException(int chanVal) {
		super("Specified value " + chanVal + "was not within the permissible range of 0 to 255 inclusive.");
	}
}
