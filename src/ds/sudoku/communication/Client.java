package ds.sudoku.communication;

import ds.sudoku.logic.SudokuTemplate;

/**
 * This interface specifies the methods which can be invoked on a client.
 * 
 * <p>
 * The {@link Client} interface is a server side representation of a client and
 * should therefore only be used on the server side. The client is internally
 * threaded and all calls to a handler will be triggered asynchronously, which
 * means that users of this interface must take care of locking datastructures
 * shared between clients.
 * 
 * <p>
 * In the current design, the communication layer and therefore also the client
 * interface do not take care of implementing protocols, they just provide the
 * raw methods which can be invoked to communicate with the physical client
 * sitting at the other end of a connection.
 */
public interface Client extends Connection, SudokuParticipant {

    /**
     * Sends the client a deregistration message. After calling this method, the
     * client will not be available any longer. The internal connection will be
     * closed and the client will consider itself as not registered any longer.
     */
    public void deregister();

    /**
     * Sends the client an invitation to a sudoku from the {@code otherPlayer}.
     * This message should only be invoked when a game is started by inviting
     * another player by name. It should not be used when matching players to a
     * random (quick) match.
     * 
     * @param otherPlayer
     *            name of the player who sent the invite.
     */
    public void invite(String otherPlayer);

    /**
     * Sends the client a message about the player with name {@code otherPlayer}
     * having left the game.
     * 
     * @param otherPlayer
     *            Name of the player who left the game.
     */
    public void playerLeft(String otherPlayer);

    /**
     * Sends the client a game over message indicating that someone won the
     * game.
     * 
     * @param won
     *            Indicates wether the client has won the game or not.
     */
    public void gameOver(boolean won);

    /**
     * Sends the client a game over message indicating that someone won the
     * game.
     * 
     * @param winner
     *            the name of the player who won the game.
     */
    public void gameOver(String winner);

    /**
     * Sends the client an update on the score. Since we do not want to reveal
     * the score to avoid making trial - and - error strategies viable, we only
     * tell the client if he is currently winning or not.
     * 
     * @param winning
     *            Indicates wether the client is winning or not.
     */
    public void score(boolean winning);

    /**
     * Sends the client a new game stored in a sudoku template.
     * 
     * @param sudoku
     *            The sudoku sent to the client.
     */
    public void newGame(SudokuTemplate sudoku);

    /**
     * Set a message handler which will be asynchronously invoked whenever a new
     * message from this client arrives.
     * 
     * @param handler
     *            The message handler used to handle the incomming messages.
     */
    public void setMessageHandler(ClientMessageHandler handler);
}
