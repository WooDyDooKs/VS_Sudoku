package ds.sudoku.communication;

import java.util.List;
import java.util.Map;

public class InviteDirectMessage extends Message {

    private final String inviter;
    private final String invited;

    /**
     * Create a new {@link InviteDirectMessage} which represents an invitation
     * from {@code inviter} to {@code invited}.
     * 
     * @param inviter
     *            The participant sending the invite.
     * @param invited
     *            The participant being invited.
     */
    public InviteDirectMessage(final String inviter, final String invited) {
        this.inviter = inviter;
        this.invited = invited;
    }

    /**
     * Create a new {@link InviteDirectMessage} which represents an invitation
     * from {@code inviter} to {@code invited}. Additionally, store custom
     * values and properties and send them with this message.
     * @param inviter The participant sending the invite.
     * @param invited The participant being invited.
     * @param customValues The custom values stored in this message.
     * @param customProperties The custom properties stored in this message.
     */
    public InviteDirectMessage(final String inviter, final String invited,
            final List<String> customValues,
            final Map<String, String> customProperties) {
        super(customValues, customProperties);
        this.inviter = inviter;
        this.invited = invited;
    }
    
    /**
     * Get the participant sending the invite.
     * @return The inviter.
     */
    public String getInviter() {
        return inviter;
    }
    
    /**
     * Get the participant being invited.
     * @return The invited participant.
     */
    public String getInvited() {
        return invited;
    }

}
