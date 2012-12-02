package ds.sudoku.communication;

public interface ServerMessageHandler {
	public void onDeregisterMessageReceived(DeregisterMessage message);
	public void onLeaveMessageReceived(LeaveMessage message);
	public void onSetFieldMessageReceived(SetFieldMessage message);
	public void onErrorMesssageReceived(ErrorMessage message);
	public void onInviteMessageReceived(InviteMessage message);
	public void onACKReceived(ACKMessage message);
	public void onNACKReceived(NACKMessage message);
	public void onGameOverMessageReceived(GameOverMessage message);
	public void onScoreMessageReceived(ScoreMessage message);
}