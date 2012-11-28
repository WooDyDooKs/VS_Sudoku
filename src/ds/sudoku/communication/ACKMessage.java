package ds.sudoku.communication;

/**
 * Represents a positive confirmation.
 *
 * <p>Should be used in a clearly defined protocol only.
 */
public class ACKMessage extends Message {

	@Override
	public String getMessageType() {
		return "ACKMessage";
	}
}
