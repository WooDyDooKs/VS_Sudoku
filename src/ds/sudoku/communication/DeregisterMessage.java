package ds.sudoku.communication;

import java.util.List;
import java.util.Map;

import ds.sudoku.communication.serialization.Serializable;

/**
 * This message is sent in different contexts, depending on the sender.
 * <ul>
 * <li><b>Sender is server:</b>
 * <p>
 * In this case the deregister message is equivalent to a kick. </p>
 * </li>
 * <li><b>Sender is client:</b>
 * <p>
 * In this case, the deregister message tells the server that a
 * player will cut the connection. </p>
 * </li>
 * </ul>
 * @author dalhai
 *
 */
public class DeregisterMessage extends TokenizedMessage {
	
	private final String reason;
	
	/**
	 * Create a new deregister message with the given reason.
	 * @param reason The reason for the deregistration.
	 */
	public DeregisterMessage(String reason) {
		this(reason, null);
	}
	
	/**
	 * Create a new deregister message with the given reason and
	 * attach the given token to the message.
	 * @param reason The reason for the deregistration.
	 * @param token The token to send with with the message.
	 */
	public DeregisterMessage(String reason, Serializable token) {
		super(token);
		this.reason = reason;
	}
	
	/**
	 * Create a new register message for the given name and
	 * attach the given token to the message. Additionally,
	 * store the given custom values and properties in the message.
	 * @param reason The reason for the deregistration.
	 * @param token The token to send with the message.
	 * @param customValues The custom values stored in this message.
	 * @param customProperties The custom properties stored in this message.
	 */
	public DeregisterMessage(String reason, String token,
			List<String> customValues, Map<String, String> customProperties) {
		super(token, customValues, customProperties);
		this.reason = reason;
	}

	/**
	 * Get the reason for the deregistration.
	 * @return The reason sent with this message.
	 */
	public String getReason() {
		return reason;
	}
}