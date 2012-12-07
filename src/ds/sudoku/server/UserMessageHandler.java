package ds.sudoku.server;

import ds.sudoku.communication.ACKMessage;
import ds.sudoku.communication.Client;
import ds.sudoku.communication.DeathHandler;
import ds.sudoku.communication.DeregisterMessage;
import ds.sudoku.communication.InviteMessage;
import ds.sudoku.communication.LeaveMessage;
import ds.sudoku.communication.Message;
import ds.sudoku.communication.NACKMessage;
import ds.sudoku.communication.SetFieldMessage;
import ds.sudoku.exceptions.server.NonExistingUsername;
import static ds.sudoku.server.ServerFrontend.userManagement;
import static ds.sudoku.server.ServerFrontend.gamesManager;

public class UserMessageHandler extends DefaultMessageHandler implements DeathHandler<Client> {

	private User user;

	public UserMessageHandler(User user) {
		this.user = user;
	}

	@Override
	public void onDeregisterMessageReceived(Client client, DeregisterMessage message) {
		try {
			userManagement.deregister(user);
			client.setMessageHandler(ServerFrontend.messageHandler);
			client.ACK(message);
		} catch (NonExistingUsername e) {
			// this should never happen!
			e.printStackTrace();
		}
	}

	@Override
	public void onInviteMessageReceived(Client client, InviteMessage message) {
		String receiver = message.getReciever();
		String sender = message.getSender();
		
		if(receiver != null) {
			Client receiverClient = userManagement.getUser(receiver).getClient();
			receiverClient.invite(sender);
		} else {
			// random request
			User other = gamesManager.matchWithOtherRandomUser(user);
			if(other != null) {
				gamesManager.startNewGame(user, other);
			}
		}
	}

	@Override
	public void onLeaveMessageReceived(Client client, LeaveMessage message) {
		GameHandler handler = user.getGame().getHandler();
		handler.playerLeft(user);
	}

	@Override
	public void onSetFieldMessageReceived(Client client, SetFieldMessage message) {
		assert !message.isZeroBased() : "zero based index not supported!";
		
		GameHandler handler = user.getGame().getHandler();		
		handler.setField(user, message.getRow(), message.getColumn(), message.getValue());
	}

	@Override
	public void onACKMessageReceived(Client client, ACKMessage message) {
		Message ackMsg = message.getConfirmedMessage();
		
		if(ackMsg instanceof InviteMessage) {
			InviteMessage invMsg = (InviteMessage) ackMsg;
			User other = userManagement.getUser(invMsg.getSender());
			gamesManager.startNewGame(user, other);
		}
	}

	@Override
	public void onNACKMessageReceived(Client client, NACKMessage message) {
		Message nackMsg = message.getConfirmedMessage();
		
		if(nackMsg instanceof InviteMessage) {
			InviteMessage invMsg = (InviteMessage) nackMsg;
			User other = userManagement.getUser(invMsg.getSender());
			other.getClient().NACK(invMsg);
		}
	}

	@Override
	public void onDeath(Client instance, String message) {
		try {
			userManagement.deregister(user);
			Game game = user.getGame();
			if(game != null) {
				GameHandler handler = game.getHandler();
				handler.playerLeft(user);
			}
		} catch (NonExistingUsername e) {
			e.printStackTrace();
		}
	}
	
}