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
	 * This String will be used as the key for the list of custom
	 * values inside the {@link Message} class.
	 */
	public static final String CUSTOM_VALUES_KEY = "CustomValues";

	/**
	 * This String will be used as the key for the desired name
	 * of a client registering via a {@link RegisterMessage}.
	 */
	public static final String NAME_KEY = "Name";

	/**
	 * This String will be used as the key for the token sent
	 * to a client upon registration in a {@link TokenizedMessage}.
	 */
	public static final String TOKEN_KEY = "Token";

	/**
	 * This String will be used as the key for the reason sent
	 * to a client upon an event that requires a reason.
	 */
	public static final String REASON_KEY = "Reason";

	/**
	 * This String will be used as the key for the index sent to
	 * a client in a {@link SetFieldMessage}.
	 */
    public static final String INDEX_KEY = "Index";

    /**
     * This String will be used as the key for the width of the sudoku
     * field sent to a client in a {@link SetFieldMessage} or a 
     * {@link SudokuMessage}.
     */
    public static final String SUDOKU_WIDTH_KEY = "SudokuWidth";

    /**
     * This String will be used as the key for the height of the sudoku
     * field sent to a client in a {@link SetFieldMessage} or a 
     * {@link SudokuMessage}.
     */
    public static final String SUDOKU_HEIGHT_KEY = "SudokuHeight";

    /**
     * This String will be used as they key for a property describing if
     * a sudoku field index is zero based or not.
     */
    public static final String ZERO_BASED_KEY = "ZeroBased";

    /**
     * This String will be used as the key for a property describing
     * a value.
     */
    public static final String VALUE_KEY = "Value";

    /**
     * This String will be used as the key for a property describing
     * the originator of a message.
     */
    public static final String SENDER_KEY = "Sender";

    /**
     * This string will be used as the key for a property describing
     * if the receiver is winning.
     */
    public static final String IS_WINNING_KEY = "Winning";

    /**
     * This String will be used as the key for a property describing
     * the receiver of a message.
     */
	public static final String RECEIVER_KEY = "Receiver";

	/**
	 * This String will be used as the key for a property describing
	 * the sudoku field sent with a message. 
	 */
	public static final String SUDOKU_FIELD = "Field";
}
