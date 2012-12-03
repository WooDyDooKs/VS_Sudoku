package ds.sudoku.communication;

import java.util.List;
import java.util.Map;

import ds.sudoku.communication.serialization.Serializable;

/**
 * Represents a negative confirmation to another message.
 * 
 * <p>
 * Should be usined in a clearly defined protocol only.
 * </p>
 * 
 * @author dalhai
 *
 */
public class NACKMessage extends TokenizedMessage {
    
    private final Message confirmedMessage;
    
    /**
     * Create a new NACKMessage which confirms the given message.
     * The given message will not be available as subtype.
     * @param confirmedMessage The message to be confirmed.
     */
    public NACKMessage(final Message confirmedMessage) {
        super(null);
        this.confirmedMessage = confirmedMessage;
    }
    
    /**
     * Create a new NACKMessage which confirms the given message.
     * The given message will not be available as subtype.
     * The message allows to attach a token.
     * @param confirmedMessage The message to be confirmed.
     * @param token The token to be sent with this message.
     */
    public NACKMessage(final Message confirmedMessage,
            Serializable token) {
        super(token);
        this.confirmedMessage = confirmedMessage;
    }
    
    /**
     * Create a new NACKMessage which confirms the given message.
     * The given message will not be available as subtype.
     * The message allows to attach a token and additionally,
     * custom values and properties can be stored.
     * @param confirmedMessage The message to be confirmed.
     * @param token The token to be sent with this message.
     * @param customValues The custom values stored in this message.
     * @param customProperties The custom properties stored in this message.
     */
    public NACKMessage(final Message confirmedMessage, Serializable token,
            List<String> customValues, Map<String, String> customProperties) {
        super(token, customValues, customProperties);
        this.confirmedMessage = confirmedMessage;
    }
    
    /**
     * Get the message that is confirmed with this message.
     * @return
     */
    public Message getConfirmedMessage() {
        return confirmedMessage;
    }
}