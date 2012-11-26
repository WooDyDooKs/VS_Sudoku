package ds.sudoku.communication;

/**
 * The message handler interface for clients on server side which
 * specifies all necessary methods which need to be implemented to
 * handle incomming messages. 
 *
 * <p>Note that the handler methods will be invoked in a separate thread
 * and must therefore be secured against race conditions and parallel inconsistency.
 */
public interface ClientMessageHandler {

        /**
         * Will be invoked when a new register message from target client is received.
         *
         * @param message The {@link RegisterMessage} generated from the incomming data.
         */
	public void onRegisterMessageReceived(RegisterMessage message);

        /**
         * Will be invoked when a new deregistration message from target client is received.
         *
         * @param message The {@link DeregisterMessage} generated from the incomming data.
         */
	public void onDeregisterMessageReceived(DeregisterMessage message);

        /**
         * Will be invoked when a join message is received.
         *
         * @param message The {@link JoinMessage} generated from the incomming data.
         */
	public void onJoinMessageReceived(JoinMessage message);

        /**
         * Will be invoked when a leave message is received.
         *
         * @param message the {@link LeaveMessage} generated from the incomming data.
         */
	public void onLeaveMessageReceived(LeaveMessage message);

        /**
         * Will be invoked when a message representing a request to set a field is received.
         *
         * @param message The {@link SetFieldMessage} generated from the incomming data.
         */
	public void onSetFieldMessageReceived(SetFieldMessage message);

        /**
         * Will be invoked when a message representing an error is received.
         *
         * @param message The {@link ErrorMessage} generated from the incomming data.
         */
	public void onErrorMessageReceived(ErrorMessage message);

        /**
         * Will be invoked when a positive confirmation is received.
         *
         * @param message The {@link ACKMessage} generated from the incomming data.
         */
	public void onACKReceived(ACKMessage message);

        /**
         * Will be invoked when a negative confirmation is received.
         *
         * @param message The {@link NACKMessage} generated from the incomming data.
         */
	public void onNACKMessageReceived(NACKMessage message);
}
