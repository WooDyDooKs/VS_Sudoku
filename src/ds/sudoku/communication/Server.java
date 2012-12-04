package ds.sudoku.communication;

import ds.sudoku.communication.serialization.Serializable;

/**
 * This interface specifies the methods which can be invoked on a server.
 * 
 * <p>
 * The {@link Server} interface is a client side representation of the server.
 * It should therefore only be used on the client side. The Server is internally
 * threaded and all calls to a handler will be triggered asynchronously, which
 * means that users of this interface must take care of locking datastructures
 * used in the registered handler.
 * </p>
 * 
 * <p>
 * In the current design, the communication layer and therefore also the server
 * interface to not take care of implementing protocols, they just provide the
 * raw methods which can be invoked to communicate with the physical server
 * sitting at the other end of the connection.
 * 
 * @author dalhai
 * 
 */
public interface Server extends Connection, SudokuParticipant {

    /**
     * Sends the server a registration message. After calling this method, the
     * client should wait for an ACK message from the server, confirming the
     * registration. The answer can also be an error message if the registration
     * failed.
     * 
     * @param name
     *            The name used for registration.
     */
    public void register(String name);

    /**
     * Sends the server a registration alongside with a token. The token should
     * have been received from the server with the first successful registration
     * attempt. After calling this method, the client should wait for an ACK
     * message from the server, confirming the registration. The answer can also
     * be an error message if the registration failed.
     * 
     * @param name
     *            The name used for registration.
     * @param token
     *            The token sent alongside the message.
     */
    public void register(String name, Serializable token);

    /**
     * Sends the server a dereigstration message alongside with the token
     * received from the server upon first successful registration. After
     * calling this method, the client should wait for an ACK message from the
     * server, confirming the deregistration. The answer can be an error message
     * if the registration failed.
     */
    public void deregister(Serializable token);

    /**
     * Sends the server an invitation for another player. After calling this
     * method, the client should wait for an ACK message confirming the
     * invitation, i.e. the other player accepts the invitation. Right after the
     * ACK, the client should wait for a NewGame message containing information
     * about the game. Instead of an ACK message, you can receive a NACK
     * message, indicating that the challenged player did not accept the
     * invitation.
     * 
     * @param invited
     *            The player being invited.
     */
    public void invite(String invited);

    /**
     * Sends the server a request for a random match. After calling this method,
     * the client should wait for an ACK message confirming the request. Right
     * after the ACK, the client shouldw ait for a NewGame message containing
     * information about the game. Instead of an ACK message, you can receive a
     * NACK message, indicating that no player was found for you request.
     */
    public void requestRandomMatch();

    /**
     * Sends the server a leave message, indicating that you left the current
     * sudoku game. After calling this method, you can assume the server to be
     * aware of you having left the game.
     */
    public void leave();

    /**
     * Set a message handler which will be asynchronously invoked whenever a
     * message from the server arrives.
     * 
     * @param handler
     *            The message handler used to handle the incoming messages.
     */
    public void setMessageHandler(ServerMessageHandler handler);

    /**
     * Set a death handler which will be asynchronously invoked whenever a the
     * server dies.
     * 
     * @param handler
     *            The death handler used to handle server death.
     */
    public void setDeathHandler(DeathHandler<Server> handler);
}
