package ds.sudoku.communication;

import java.util.List;
import java.util.Map;

public class InviteMessage extends Message {

    private final String name;
    private final String difficulty;

    /**
     * Create a new {@link InviteMessage} which represents an invitation.
     * 
     * @param name
     *            The name associated with the invitation.
     * @param difficulty
     *            The difficulty associated with the invitation.
     */
    public InviteMessage(final String name, final String difficulty) {
        this.name = name;
        this.difficulty = difficulty;
    }

    /**
     * Create a new {@link InviteMessage} which represents an invitation from
     * {@code inviter} to {@code invited}. Additionally, store custom values and
     * properties and send them with this message.
     * 
     * @param name
     *            The name associated with the invitation.
     * @param difficulty
     *            The difficulty associated with the invitation.
     * @param customValues
     *            The custom values stored in this message.
     * @param customProperties
     *            The custom properties stored in this message.
     */
    public InviteMessage(final String name, final String difficulty,
            final List<String> customValues,
            final Map<String, String> customProperties) {
        super(customValues, customProperties);
        this.name = name;
        this.difficulty = difficulty;
    }

    /**
     * Get the name associated with the invitation.
     * 
     * @return The name associated with the invitation.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the difficulty associated with this invitation.
     * 
     * @return The difficulty associated with the invitation.
     */
    public String getDifficulty() {
        return difficulty;
    }
}
