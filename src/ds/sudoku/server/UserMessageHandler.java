package ds.sudoku.server;

import ds.sudoku.communication.ACKMessage;
import ds.sudoku.communication.Client;
import ds.sudoku.communication.DeathHandler;
import ds.sudoku.communication.DeregisterMessage;
import ds.sudoku.communication.InviteMessage;
import ds.sudoku.communication.InviteRandomMessage;
import ds.sudoku.communication.LeaveMessage;
import ds.sudoku.communication.Message;
import ds.sudoku.communication.NACKMessage;
import ds.sudoku.communication.SetFieldMessage;
import ds.sudoku.exceptions.SudokuError;
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
			
			ServerLog.l("User %s deregistered.", user.getUsername());
		} catch (NonExistingUsername e) {
			// this should never happen!
			e.printStackTrace();
		}
	}

	@Override
	public void onInviteMessageReceived(Client client, InviteMessage message) {
		User invitedUser = userManagement.getUser(message.getName());
		String invitedName = invitedUser.getUsername();
		String invitingName = user.getUsername();
		
		if(invitingName.equals(invitedName)) {
			user.getClient().sendError(
					SudokuError.INVALID_SELF_INVITE, 
					"You cannot invite yourself");
			return;
		}
		
		invitedUser.getClient().invite(invitingName, message.getDifficulty());
		ServerLog.l("Got InviteRequest from %s to %s.", invitingName, invitedName);
	}
	
	@Override
	public void onInviteMessageReceived(Client client, InviteRandomMessage message) {
		User other = gamesManager.matchWithOtherRandomUser(user);
		ServerLog.l("User %s requested random match.", user.getUsername());

		if(other != null) {
			// TODO: search by difficulty
			gamesManager.startNewGame(user, other, message.getDifficulty());
		}
	}

	@Override
	public void onLeaveMessageReceived(Client client, LeaveMessage message) {
		if(user.getGame() == null) return;
		
		GameHandler handler = user.getGame().getHandler();
		handler.playerLeft(user);
		ServerLog.l("User %s has left the game.", user.getUsername());
	}

	@Override
	public void onSetFieldMessageReceived(Client client, SetFieldMessage message) {
		assert !message.isZeroBased() : "zero based index not supported!";
		if(user.getGame() == null) return;
		
		GameHandler handler = user.getGame().getHandler();
		
		int row = message.getRow();
		int column = message.getColumn();
		int value = message.getValue();
		
		handler.setField(user, row, column, value);
		
		ServerLog.l("User %s set field (%d, %d) to %d.", user.getUsername(), row, column, value);

	}

	@Override
	public void onACKMessageReceived(Client client, ACKMessage message) {
		Message ackMsg = message.getConfirmedMessage();
		
		if(ackMsg instanceof InviteMessage) {
			InviteMessage invMsg = (InviteMessage) ackMsg;
			User other = userManagement.getUser(invMsg.getName());
			gamesManager.startNewGame(user, other, invMsg.getDifficulty());
			
			ServerLog.l("Starting new game with players %s and %s", user.getUsername(), other.getUsername());
		}
	}

	@Override
	public void onNACKMessageReceived(Client client, NACKMessage message) {
		Message nackMsg = message.getConfirmedMessage();
		
		if(nackMsg instanceof InviteMessage) {
			InviteMessage invMsg = (InviteMessage) nackMsg;
			User other = userManagement.getUser(invMsg.getName());
			other.getClient().NACK(invMsg);
			
			ServerLog.l("User %s requested invite from", user.getUsername(), other.getUsername());
		}
	}

	@Override
	public void onDeath(Client instance, String message) {
		try {
			ServerLog.l("User %s disconnected.", user.getUsername());

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