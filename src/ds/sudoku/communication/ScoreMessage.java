package ds.sudoku.communication;

import java.util.List;
import java.util.Map;

/**
 * This message is sent whenever there is a score update available for a client.
 * The score message contains information about the client only. There is not an
 * actual score inside of this message but instead, the message contains
 * information about the player having the advantage in the game or not.
 * 
 * @author dalhai
 * 
 */
public class ScoreMessage extends Message {

    private final boolean winning;

    /**
     * Creates a new ScoreMessage which carries a boolean. If {@code winning} is
     * set to true the receiving player is in lead.
     * 
     * @param winning
     *            States if the receiver is in lead.
     */
    public ScoreMessage(boolean winning) {
        this.winning = winning;
    }

    /**
     * Creates a new ScoreMessage which carries a boolean {@code winning}. If
     * {@code winning} is set to true the receiving player is in lead.
     * Additionally the message can store custom values and properties.
     * 
     * @param winning
     *            States if the receiver is in lead.
     * @param customValues
     *            The custom values stored in this message.
     * @param customProperties
     *            The custom properties stored in this message.
     */
    public ScoreMessage(boolean winning, List<String> customValues,
            Map<String, String> customProperties) {
        super(customValues, customProperties);
        this.winning = winning;
    }

    /**
     * Check if the client is winning.
     * 
     * @return Returns {@code true} if the client is winning, else {@code false}
     */
    public boolean isWinning() {
        return winning;
    }
}