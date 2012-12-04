package ds.sudoku.communication;

/**
 * The message handler interface for servers on client side which specifies all
 * necessary methods which need to be implemented to handle incoming messages.
 * 
 * <p>
 * Note that the handler methods will be invoked in a separate thread an must
 * therefore be secured against race conditions and parallel inconsistency.
 * </p>
 * 
 * @author dalhai
 * 
 */
public interface ServerMessageHandler {

    /**
     * Will be invoked when a new deregistration message from the server is
     * received.
     * 
     * @param server
     *          The {@link Server} who sent the message.
     * @param message
     *          The {@link DeregisterMessage} generated from the incoming data.
     */
    public void onDeregisterMessageReceived(Server server,
            DeregisterMessage message);

    /**
     * Will be invoked when a new leave message from the server is
     * received.
     * 
     * @param server
     *          The {@link Server} who sent the message.
     * @param message
     *          The {@link LeftMessage} generated from the incoming data.
     */
    public void onLeftMessageReceived(Server server, LeftMessage message);

    /**
     * Will be invoked when a new set field message from the server is
     * received.
     * 
     * @param server
     *          The {@link Server} who sent the message.
     * @param message
     *          The {@link SetFieldMessage} generated from the incoming data.
     */
    public void onSetFieldMessageReceived(Server server, SetFieldMessage message);
    
    /**
     * Will be invoked when a new named set field message from the server is
     * received.
     * 
     * @param server
     *          The {@link Server} who sent the message.
     * @param message
     *          The {@link NamedSetFieldMessage} generated from the incoming data.
     */
    public void onNamedSetFieldMessageReceived(Server server, NamedSetFieldMessage message);
    
    /**
     * Will be invoked when a new error message from the server is
     * received.
     * 
     * @param server
     *          The {@link Server} who sent the message.
     * @param message
     *          The {@link ErrorMessage} generated from the incoming data.
     */
    public void onErrorMesssageReceived(Server server, ErrorMessage message);

    /**
     * Will be invoked when a new invite message from the server is
     * received.
     * 
     * @param server
     *          The {@link Server} who sent the message.
     * @param message
     *          The {@link InviteMessage} generated from the incoming data.
     */
    public void onInviteMessageReceived(Server server, InviteMessage message);

    /**
     * Will be invoked when a new ACK message from the server is
     * received.
     * 
     * @param server
     *          The {@link Server} who sent the message.
     * @param message
     *          The {@link ACKMessage} generated from the incoming data.
     */
    public void onACKReceived(Server server, ACKMessage message);

    /**
     * Will be invoked when a new NACK message from the server is
     * received.
     * 
     * @param server
     *          The {@link Server} who sent the message.
     * @param message
     *          The {@link NACKMessage} generated from the incoming data.
     */
    public void onNACKReceived(Server server, NACKMessage message);

    /**
     * Will be invoked when a new game over message from the server is
     * received.
     * 
     * @param server
     *          The {@link Server} who sent the message.
     * @param message
     *          The {@link GameOverMessage} generated from the incoming data.
     */
    public void onGameOverMessageReceived(Server server, GameOverMessage message);

    /**
     * Will be invoked when a new score message from the server is
     * received.
     * 
     * @param server
     *          The {@link Server} who sent the message.
     * @param message
     *          The {@link score Message} generated from the incoming data.
     */
    public void onScoreMessageReceived(Server server, ScoreMessage message);

    /**
     * Will be invoked when a new game message from target client is received.
     * 
     * @param message
     *            The {@link NewGameMessage} generated from the incoming data.
     * @param client
     *            The {@link Server} who sent the message.
     */
    public void onNewGameMessageReceived(Server client, NewGameMessage message);
}