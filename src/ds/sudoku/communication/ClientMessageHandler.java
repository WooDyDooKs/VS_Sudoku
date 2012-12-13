package ds.sudoku.communication;

/**
 * The message handler interface for clients on server side which specifies all
 * necessary methods which need to be implemented to handle incoming messages.
 * 
 * <p>
 * Note that the handler methods will be invoked in a separate thread and must
 * therefore be secured against race conditions and parallel inconsistency.
 * </p>
 * 
 * @author dalhai
 */
public interface ClientMessageHandler {

	/**
	 * Will be invoked when a new raw message from target client is received.
	 * 
	 * @param client
	 *            The {@link Client} who sent the message.
	 * @param message
	 *            The {@link RegisterMessage} generated from the incoming data.
	 */
	public void onRawMessageReceived(Client client, Message message);

	/**
	 * Will be invoked when a new register message from target client is
	 * received.
	 * 
	 * @param client
	 *            The {@link Client} who sent the message.
	 * @param message
	 *            The {@link RegisterMessage} generated from the incoming data.
	 */
	public void onRegisterMessageReceived(Client client, RegisterMessage message);

	/**
	 * Will be invoked when a new deregistration message from target client is
	 * received.
	 * 
	 * @param client
	 *            The {@link Client} who sent the message.
	 * @param message
	 *            The {@link DeregisterMessage} generated from the incoming
	 *            data.
	 */
	public void onDeregisterMessageReceived(Client client,
			DeregisterMessage message);

    /**
     * Will be invoked when a new direct invite is received.
     * 
     * @param client
     *            The {@link Client} who sent the message.
     * @param message
     *            The {@link InviteMessage} generated from the incoming
     *            data.
     */
    public void onInviteMessageReceived(Client client,
            InviteMessage message);

    /**
     * Will be invoked when a new random invite is received. This random invite
     * represents a request to the server to match the inviter up with another
     * participant waiting for a random match.
     * 
     * @param client
     *            The {@link Client} who sent the message.
     * @param message
     *            The {@link InviteMessage} generated from the incoming
     *            data.
     */
    public void onInviteMessageReceived(Client client,
            InviteRandomMessage message);

	/**
	 * Will be invoked when a leave message is received.
	 * 
	 * @param client
	 *            The {@link Client} who sent the message
	 * @param message
	 *            The {@link LeaveMessage} generated from the incoming data..
	 */
	public void onLeaveMessageReceived(Client client, LeaveMessage message);

	/**
	 * Will be invoked when a message representing a request to set a field is
	 * received.
	 * 
	 * @param client
	 *            The {@link Client} who sent the message.
	 * @param message
	 *            The {@link SetFieldMessage} generated from the incoming data.
	 */
	public void onSetFieldMessageReceived(Client client, SetFieldMessage message);

	/**
	 * Will be invoked when a message representing a request to set a field with
	 * an attached name is received.
	 * 
	 * @param client
	 *            The {@link Client} who sent the message.
	 * @param message
	 *            The {@link NamedSetFieldMessage} generated from the incoming
	 *            data.
	 */
	public void onNamedSetFieldMessageReceived(Client client,
			NamedSetFieldMessage message);

	/**
	 * Will be invoked when a message representing an error is received.
	 * 
	 * @param client
	 *            The {@link Client} who sent the message.
	 * @param message
	 *            The {@link ErrorMessage} generated from the incoming data.
	 */
	public void onErrorMessageReceived(Client client, ErrorMessage message);

	/**
	 * Will be invoked when a positive confirmation is received.
	 * 
	 * @param client
	 *            The {@link Client} who sent the message.
	 * @param message
	 *            The {@link ACKMessage} generated from the incoming data.
	 */
	public void onACKMessageReceived(Client client, ACKMessage message);

	/**
	 * Will be invoked when a negative confirmation is received.
	 * 
	 * @param client
	 *            The {@link Client} who sent the message.
	 * @param message
	 *            The {@link NACKMessage} generated from the incoming data.
	 */
	public void onNACKMessageReceived(Client client, NACKMessage message);
}
