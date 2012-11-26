package ds.sudoku.communication;

/**
 * This interface specifies a connection in a client - server application. 
 *
 * <p>The most simple methods that you can invoke on any connection are 
 * <ul>
 * <li> giving a positive response (ACKing) a received message. </li>
 * <li> giving a negative response (NACKing) a received message. </li>
 * <li> sending an error message when NACKing is not appropriate. </li>
 */
public interface Connection {	
        /**
         * Sends an error with the given message over the connection.
         * The equivalent in natural language would be "Something is wrong.".
         * @param message the message sent alongisde with the error.
         */
	public void sendError(String message);

        /**
         * Sends a positive confirmation over the connection.
         * The equivalent in natural language would be "Everything went well".
         */
	public void ACK();

        /**
         * Sends a negative confirmation over the connection.
         * The equivalent in natural language would be 
         * "Something with what you're saying is wrong".
         */
	public void NACK();
}
