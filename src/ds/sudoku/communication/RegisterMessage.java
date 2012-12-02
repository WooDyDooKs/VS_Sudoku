package ds.sudoku.communication;

import java.util.List;
import java.util.Map;

import ds.sudoku.communication.serialization.Serializable;

/**
 * This message is sent right after a client has set up
 * a connection with a server. It is used to provide the
 * server with Information needed to identify the client.
 * @author dalhai
 *
 */
public class RegisterMessage extends TokenizedMessage {

	private final String name;
	
	/**
	 * Create a new register message for the given name.
	 * @param name The name of the client to be registered.
	 */
	public RegisterMessage(String name) {
		this(name, null);
	}
	
	/**
	 * Create a new register message for the given name and
	 * attach the given token to the message.
	 * @param name The name of the client to be registered.
	 * @param token The token to send with the message.
	 */
	public RegisterMessage(String name, Serializable token) {
		super(token);
		this.name = name;
	}
	
	/**
	 * Create a new register message for the given name and
	 * attach the given token to the message. Additionally,
	 * store the given custom values and properties in the message.
	 * @param name The name of the client to be registered.
	 * @param token The token to send with the client.
	 * @param customValues The custom values stored in this message.
	 * @param customProperties The custom properties stored in this message.
	 */
	public RegisterMessage(String name, String token,
			List<String> customValues, Map<String, String> customProperties) { 
		super(token, customValues, customProperties);
		this.name = name;
	}
	
	/**
	 * Get the name stored in the message.
	 * @return The name sent with this message.
	 */
	public String getName() {
		return name;
	}
}