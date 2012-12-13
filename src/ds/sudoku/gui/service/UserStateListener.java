package ds.sudoku.gui.service;

import ds.sudoku.communication.InviteMessage;
import ds.sudoku.logic.SudokuTemplate;

public interface UserStateListener {
	/**
	 * Called when registration was successful.
	 * 
	 * @param username
	 */
	public void onRegistered(String username);
	
	/**
	 * Called when the user successfully deregistered.
	 */
	public void onDeregistered();
	
	/**
	 * Called when an invite request is received.
	 * Acknowledge invite with Server.ACK(msg).
	 * 
	 * @param msg InviteMessage containing the sender
	 */
	public void onInviteRequest(InviteMessage msg);
	
	/**
	 * Called when a user rejected an invite request.
	 * 
	 * @param msg The rejected InviteMessage
	 */
	public void onInviteRequestRejected(InviteMessage msg);
	
	/**
	 * Called when a new game was started. This method is
	 * called after the SudokuHandler was created.
	 * 
	 * @param template 
	 */
	public void onGameStarted(SudokuTemplate template);
	
	/**
	 * Called if the connection to the server got interrupted.
	 * After this was called, SudokuService.getServer() will return null.
	 */
	public void onDeath(String message);
	
	/**
	 * Called if the server sent an error message.
	 */
	public void onError(String message);
}