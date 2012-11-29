package ds.sudoku.communication.serialization;

import ds.sudoku.communication.*;

/**
 * Provides all constants used for serialization.
 * @author dalhai
 *
 */
public final class SerializationKeys {
	
	//	Private constructor: Avoid instantiation.
	private SerializationKeys() {}
	
	/**
	 * This String will be used as the key for the message type
	 * property inside the {@linkds. Message} class.
	 */
	public static final String MESSAGE_TYPE_KEY = "MessageType";
	
	/**
	 * This String will be used as the key for the list of custom
	 * properties inside the {@link Message} class.
	 */
	public static final String CUSTOM_PROPERTIES_KEY = "CustomProperties";
	
	/**
	 * This String will be used as the key for hte list of custom
	 * values inside the {@link Message} class.
	 */
	public static final String CUSTOM_VALUES_KEY = "CustomValues";
}
