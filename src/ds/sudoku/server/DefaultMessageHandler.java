package ds.sudoku.server;

import static ds.sudoku.server.ServerFrontend.userManagement;
import ds.sudoku.communication.ACKMessage;
import ds.sudoku.communication.Client;
import ds.sudoku.communication.ClientMessageHandler;
import ds.sudoku.communication.DeregisterMessage;
import ds.sudoku.communication.ErrorMessage;
import ds.sudoku.communication.InviteMessage;
import ds.sudoku.communication.LeaveMessage;
import ds.sudoku.communication.Message;
import ds.sudoku.communication.NACKMessage;
import ds.sudoku.communication.NamedSetFieldMessage;
import ds.sudoku.communication.NewGameMessage;
import ds.sudoku.communication.RegisterMessage;
import ds.sudoku.communication.SetFieldMessage;
import ds.sudoku.exceptions.SudokuError;
import ds.sudoku.exceptions.server.AlreadyExistingUsername;

public class DefaultMessageHandler implements ClientMessageHandler {

	@Override
	public void onRegisterMessageReceived(Client client, RegisterMessage message) {
	
		String username = message.getName();
		
		try {
			User user = userManagement.register(username, client);
			
			UserMessageHandler umh = new UserMessageHandler(user);
			client.setMessageHandler(umh);
			client.setDeathHandler(umh);
			
			client.ACK(message);
		} catch (AlreadyExistingUsername e) {
			client.sendError(
					SudokuError.USERNAME_ALREADY_EXISTS, 
					"Username " + username + " does already exist.");
		}
	}

	@Override
	public void onDeregisterMessageReceived(Client client,
			DeregisterMessage message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInviteMessageReceived(Client client, InviteMessage message) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onLeaveMessageReceived(Client client, LeaveMessage message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSetFieldMessageReceived(Client client, SetFieldMessage message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onErrorMessageReceived(Client client, ErrorMessage message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onACKMessageReceived(Client client, ACKMessage message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNACKMessageReceived(Client client, NACKMessage message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNamedSetFieldMessageReceived(Client client,
			NamedSetFieldMessage message) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onRawMessageReceived(Client client, Message message) {
		// TODO
	}
	
	
}