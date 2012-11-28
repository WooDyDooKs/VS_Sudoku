package ds.sudoku.communication.serialization;

import ds.sudoku.exceptions.communication.DeserializationException;

/**
 * Implementing this interface allows objects to be serialized to
 * and deserialized from a string.
 * @author dalhai
 *
 */
public interface Serializable {
	
	/**
	 * Serialize this object to a string.
	 * @return
	 */
	public String serialize();
	
	/**
	 * Deserialize this object from a string. 
	 * @param source The source string.
	 * @throws DeserializationException 
	 * 				Thrown when the input string does not
	 * 				resemble to a reasonable object.
	 */
	public void deserialize(String source) 
			throws DeserializationException;
}
