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
public class InviteMessage extends NamedMessage {
	
	private final String invited;
	
	/**
	 * Creates a new, empty {@link InviteMessage}. This message will
	 * stand for a request for a random matchup.
	 * @param from Who sent this invitation?
	 */
	public InviteMessage(String from) {
		this(from, null);
	}
	
	/**
	 * Create a new, empty {@link InviteMessage} from sender {@code from} to
	 * recipient {@code invited}.
	 * @param from Who sent this invitation?
	 * @param invited Who is this invitation for?
	 */
	public InviteMessage(String from, String invited) {
		super(from);
		this.invited = invited;
	}

	/**
	 * Create a new, empty {@link InviteMessage} from sender {@code from} to
	 * recipient {@code invited}. Additionally, allows storing custom values and
	 * properties in this message.
	 * @param from Who sent this invitation?
	 * @param invited Who is this invitation for?
	 * @param customValues The custom values stored in this message.
	 * @param customProperties The custom properties stored in this message.
	 */
	public InviteMessage(String from, String invited,
			List<String> customValues, Map<String, String> customProperties) {
		super(from, customValues, customProperties);
		this.invited = invited;
	}
	
	/**
	 * Get the receiver of this message.
	 * @return The receiver.
	 */
	public String getReciever() {
		return invited;
	}
}
