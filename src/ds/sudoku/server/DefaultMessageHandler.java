package ds.sudoku.server;

import static ds.sudoku.server.ServerFrontend.userManagement;

import ds.sudoku.communication.ACKMessage;
import ds.sudoku.communication.Client;
import ds.sudoku.communication.ClientMessageHandler;
import ds.sudoku.communication.DeregisterMessage;
import ds.sudoku.communication.ErrorMessage;
import ds.sudoku.communication.JoinMessage;
import ds.sudoku.communication.LeaveMessage;
import ds.sudoku.communication.NACKMessage;
import ds.sudoku.communication.RegisterMessage;
import ds.sudoku.communication.SetFieldMessage;
import ds.sudoku.exceptions.server.AlreadyExistingUsername;

public class DefaultMessageHandler implements ClientMessageHandler {
	
	private Client client;
	
	public DefaultMessageHandler(Client client) {
		this.client = client;
	}

	@Override
	public void onRegisterMessageReceived(RegisterMessage message) {
		String username = message.getName();
		
		try {
			User user = userManagement.register(username, client);
			client.setMessageHandler(new UserMessageHandler(user));
		} catch (AlreadyExistingUsername e) {
			// TODO: message.reject()
		}
		
		// TODO: message.accept
	}

	@Override
	public void onDeregisterMessageReceived(DeregisterMessage message) {		
		// TODO: message.reject()
	}

	@Override
	public void onJoinMessageReceived(JoinMessage message) {
		// TODO: message.reject()
	}

	@Override
	public void onLeaveMessageReceived(LeaveMessage message) {
		// TODO: message.reject()
	}

	@Override
	public void onSetFieldMessageReceived(SetFieldMessage message) {
		// TODO: message.reject()
	}

	@Override
	public void onErrorMessageReceived(ErrorMessage message) {
		// TODO: what to do?
	}

	@Override
	public void onACKReceived(ACKMessage message) {
		// TODO: what to do?
	}

	@Override
	public void onNACKMessageReceived(NACKMessage message) {
		// TODO: what to do?
	}

}
