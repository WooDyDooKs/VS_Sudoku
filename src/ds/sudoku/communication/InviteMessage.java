package ds.sudoku.communication;

import java.util.List;
import java.util.Map;

/**
 * This message is sent when a client wants to
 * invite another player into a sudoku game or when
 * a client wants to join a random game. 
 * 
 * @author dalhai
 *
 */
public class InviteMessage extends Message {
	
	private final String invited;
	private final String sender;
	
	/**
	 * Creates a new, empty {@link InviteMessage}. This message will
	 * stand for a request for a random matchup.
	 * @param sender Who sent this invitation?
	 */
	public InviteMessage(String sender) {
		this(sender, null);
	}
	
	/**
	 * Create a new, empty {@link InviteMessage} from sender {@code from} to
	 * recipient {@code invited}.
	 * @param sender Who sent this invitation?
	 * @param invited Who is this invitation for?
	 */
	public InviteMessage(String sender, String invited) {
		super();
		this.invited = invited;
		this.sender = sender;
	}

	/**
	 * Create a new, empty {@link InviteMessage} from sender {@code from} to
	 * recipient {@code invited}. Additionally, allows storing custom values and
	 * properties in this message.
	 * @param sender Who sent this invitation?
	 * @param invited Who is this invitation for?
	 * @param customValues The custom values stored in this message.
	 * @param customProperties The custom properties stored in this message.
	 */
	public InviteMessage(String sender, String invited,
			List<String> customValues, Map<String, String> customProperties) {
		super(customValues, customProperties);
		this.invited = invited;
		this.sender = sender;
	}
	
	/**
	 * Get the receiver of this message.
	 * @return The receiver.
	 */
	public String getReciever() {
		return invited;
	}
	
	/**
	 * Get the sender of this message.
	 * @return The sender.
	 */
	public String getSender() {
	    return sender;
	}
}
