package ds.sudoku.communication;

import java.util.List;
import java.util.Map;

import ds.sudoku.communication.serialization.Serializable;

/**
 * Represents a positive confirmation to another message.
 * 
 * <p>
 * Should be used in a clearly defined protocol only.
 * </p>
 * 
 * @author dalhai
 * 
 */
public class ACKMessage extends TokenizedMessage {
    
    private final Message confirmedMessage;
    
    /**
     * Create a new ACKMessage which confirms the given
     * message. The given message will not be available as
     * subtype. 
     * @param confirmedMessage The message to be confirmed.
     */
    public ACKMessage(final Message confirmedMessage) {
        super(null);
        this.confirmedMessage = confirmedMessage;
    }
    
    /**
     * Create a new ACKMessage which confirms the given message.
     * The given message will not be available as subtype. 
     * The message allows to attach a token.
     * @param confirmedMessage The message to be confirmed.
     * @param token The token to be sent with this message.
     */
    public ACKMessage(final Message confirmedMessage,
            Serializable token) {
        super(token);
        this.confirmedMessage = confirmedMessage;
    }
    
    /**
     * Create a new ACKMessage which confirms the given
     * message. The given message will not be available as
     * subtype. The message allows to attach a token.
     * Additionally, store the given custom values and 
     * properties in the message.
     * @param confirmedMessage The message to be confirmed.
     * @param token The token to be sent with this message.
     * @param customValues The custom values stored in this message.
     * @param customProperties The custom properties stored in this message.
     */
    public ACKMessage(final Message confirmedMessage, Serializable token,
            List<String> customValues, Map<String, String> customProperties) {
        super(token, customValues, customProperties);
        this.confirmedMessage = confirmedMessage;
    }
    
    /**
     * Create a new ACKMessage which confirms the given
     * message. The given message will not be available as
     * subtype. The message allows to attach a token.
     * Additionally, store the given custom values and 
     * properties in the message.
     * @param confirmedMessage The message to be confirmed.
     * @param token The token to be sent with this message.
     * @param customValues The custom values stored in this message.
     * @param customProperties The custom properties stored in this message.
     */
    public ACKMessage(final Message confirmedMessage, String token,
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
