package ds.sudoku.communication;

import java.util.List;
import java.util.Map;

/**
 * This message is sent whenever a player participating a sudoku game leaves the
 * game.
 * 
 * @author dalhai
 * 
 */
public class LeaveMessage extends Message {

    /**
     * Creates a new LeaveMessage. The LeaveMessage is sent from the client to
     * the server to sign, that it wants to leave the current game.
     */
    public LeaveMessage() {
    }

    /**
     * Creates a new LeaveMessage. The LeaveMessage is sent from the client to
     * the server to sign, that it wants to leave the current game. Additionally
     * the message can store custom values and properties.
     * 
     * @param customValues
     *            The custom values stored in this message.
     * @param customProperties
     *            The custom properties stored in this message.
     */
    public LeaveMessage(List<String> customValues,
            Map<String, String> customProperties) {
        super(customValues, customProperties);
    }
}