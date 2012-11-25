package ds.sudoku.communication;

public interface ClientMessageHandler {
	public void onRegisterMessageReceived(RegisterMessage message);
	public void onDeregisterMessageReceived(DeregisterMessage message);
	public void onJoinMessageReceived(JoinMessage message);
	public void onLeaveMessageReceived(LeaveMessage message);
	public void onSetFieldMessageReceived(SetFieldMessage message);
	public void onErrorMessageReceived(ErrorMessage message);
	public void onACKReceived(ACKMessage message);
	public void onNACKMessageReceived(NACKMessage message);
}