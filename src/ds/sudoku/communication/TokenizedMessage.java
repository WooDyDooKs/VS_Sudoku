package ds.sudoku.communication;

import java.util.List;
import java.util.Map;

import ds.sudoku.communication.serialization.Serializable;
import ds.sudoku.exceptions.communication.DeserializationException;

/**
 * Represents a message holding a token. 
 * A token may be any serializable object.
 * @author dalhai
 *
 */
public abstract class TokenizedMessage extends Message {
	
	private final String token;
	
	/**
	 * Create a new tokenized message. Attach the given token
	 * to this message.
	 * @param token The token to send with the message.
	 */
	public TokenizedMessage(Serializable token) {
		super();
		this.token = token != null ? token.serialize() : null;
	}
	
	/**
	 * Create a new tokenized message. Attach the given token
	 * to this message and store the given custom values and 
	 * properties.
	 * @param token The token to send with the message.
	 * @param customValues The custom values stored in this message.
	 * @param customProperties The custom properties stored in this message.
	 */
	public TokenizedMessage(Serializable token, List<String> customValues,
			Map<String, String> customProperties) {
		this(	token != null ? token.serialize() : null,
				customValues,
				customProperties
			);
	}
	
	/**
	 * Create a new tokenized message. Attach the given token
	 * to this message and store the given custom values and 
	 * properties.
	 * @param token The token to send with the message.
	 * @param customValues The custom values stored in this message.
	 * @param customProperties The custom properties stored in this message.
	 */
	public TokenizedMessage(String token, List<String> customValues,
			Map<String, String> customProperties) {
		super(customValues, customProperties);
		this.token = token;
	}
	
	/**
	 * Get the token stored in the message as string.
	 * @return The token as string.
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Fill the given object with the token. 
	 * @param target The target to be filled with the token string.
	 * @throws DeserializationException
	 * 				Thrown when the deserialization of the token
	 * 				into the target object fails.
	 */
	public void fillWithToken(Serializable target) 
		throws DeserializationException{
		if(target == null)
			throw new DeserializationException();
		target.deserialize(token);
	}

	/**
	 * Check if a token is available.
	 * @return {@code true}, if a token is available, {@code false}, else.
	 */
	public boolean hasToken() {
		return token != null;
	}
	
}
