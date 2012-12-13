package ds.sudoku.communication;

import java.util.List;
import java.util.Map;

public class InviteRandomMessage extends Message {

    private final String difficulty;
    
    /**
     * Creates a new {@link InviteRandomMessage} which represents a request for
     * a random match with the given difficulty.
     * 
     * @param difficulty
     *            The desired difficulty of the random game.
     */
    public InviteRandomMessage(String difficulty) {
        super();
        this.difficulty = difficulty;
    }

    /**
     * Creates a new {@link InviteRandomMessage} which represents a request for
     * a random match with the given difficulty. Additionally, store
     * custom values and properties and send them with this message.
     * 
     * @param difficulty
     *            The desired difficulty of the random game.
     * @param customValues
     *            The custom values stored in this message.
     * @param customProperties
     *            The custom properties stored in this message.
     */
    public InviteRandomMessage( String difficulty,
            final List<String> customValues,
            final Map<String, String> customProperties) {
        super(customValues, customProperties);
        this.difficulty = difficulty;
    }   
    
    /**
     * Get the difficulty associated with this invitation.
     * @return The requested difficulty of the match.
     */
    public String getDifficulty() {
        return difficulty;
    }
}
