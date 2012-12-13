package ds.sudoku.communication;

import java.util.List;
import java.util.Map;

public class InviteRandomMessage extends Message {

    private final String inviter;

    /**
     * Creates a new {@link InviteRandomMessage} which represents a request for
     * a random match from the {@code inviter} to the server.
     * 
     * @param inviter
     *            The participant requesting a random match.
     */
    public InviteRandomMessage(final String inviter) {
        this.inviter = inviter;
    }

    /**
     * Creates a new {@link InviteRandomMessage} which represents a request for
     * a random match from the {@code inviter} to the server. Additionally, store
     * custom values and properties and send them with this message.
     * 
     * @param inviter
     *            The participant requesting a random match.
     * @param customValues
     *            The custom values stored in this message.
     * @param customProperties
     *            The custom properties stored in this message.
     */
    public InviteRandomMessage(final String inviter,
            final List<String> customValues,
            final Map<String, String> customProperties) {
        super(customValues, customProperties);
        this.inviter = inviter;
    }

    /**
     * Get the participant sending the request.
     * @return The inviter.
     */
    public String getInviter() {
        return inviter;
    }    
}
