package ds.sudoku.communication;

import ds.sudoku.exceptions.SudokuError;

/**
 * This interface specifies a connection in a client - server application.
 * 
 * <p>
 * The most simple methods that you can invoke on any connection are
 * <ul>
 * <li>giving a positive response (ACKing) a received message.</li>
 * <li>giving a negative response (NACKing) a received message.</li>
 * <li>sending an error message when NACKing is not appropriate.</li>
 */
public interface Connection {
    /**
     * Sends an error with the given message over the connection. The equivalent
     * in natural language would be "Something is wrong.".
     * 
     * @param error
     *            The error to be sent.
     * @param message
     *            the message sent alongisde with the error.
     */
    public void sendError(SudokuError error, String message);

    /**
     * Sends an arbitrary message over the connection.
     * 
     * @param message
     *            the message sent alongside with the error.
     */
    public void sendMessage(Message message);

    /**
     * Sends a positive confirmation over the connection. The equivalent in
     * natural language would be "Everything went well".
     * 
     * @param confirmedMessage
     *            The message that is to be confirmed.
     */
    public void ACK(Message confirmedMessage);

    /**
     * Sends a negative confirmation over the connection. The equivalent in
     * natural language would be "Something with what you're saying is wrong".
     * 
     * @param confirmedMessage
     *            The message that is to be confirmed.
     */
    public void NACK(Message confirmedMessage);
    
    /**
     * Start the connection and listen for incomming messages.
     * Also start sending queued messages.
     */
    public void start();
    
    /**
     * Stop the connection. <b>CLOSES THE SOCKET!</b>
     */
    public void stop();
}
