package ds.sudoku.communication.serialization;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import ds.sudoku.communication.InviteDirectMessage;

/**
 * A json adapter used to serialize and deserialize {@link InviteDirectMessage}.
 * 
 * @author dalhai
 * 
 */
public class InviteDirectMessageAdapter extends MessageSerializer implements
        JsonDeserializer<InviteDirectMessage>,
        JsonSerializer<InviteDirectMessage> {

    /**
     * Deserialize the given {@link JsonElement} to a
     * {@link InviteDirectMessage}.
     * 
     * @param jsonMessage
     *            The input for de-serialization.
     * @param type
     *            The type used for de-serialization.
     * @param context
     *            The context of the de-serialization.
     * @return A new LeaveMessage built from the input.
     * @throws JsonParseException
     *             Thrown, if the given input does not provide the structure
     *             needed by a {@link InviteDirectMessage}
     */
    @Override
    public InviteDirectMessage deserialize(JsonElement jsonMessage, Type type,
            JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonMessageObject = jsonMessage.getAsJsonObject();

        // First extract the message parts
        List<String> customValues = extractCustomValues(jsonMessageObject);
        Map<String, String> customProperties = extractCustomProperties(jsonMessageObject);

        // Extract the inviter
        String inviter = null;
        if (jsonMessageObject.has(SerializationKeys.INVITER)) {
            JsonElement inviterElement = jsonMessageObject
                    .get(SerializationKeys.INVITER);
            inviter = inviterElement.getAsString();
        }

        // Extract the invited
        String invited = null;
        if (jsonMessageObject.has(SerializationKeys.INVITED)) {
            JsonElement invitedElement = jsonMessageObject
                    .get(SerializationKeys.INVITED);
            invited = invitedElement.getAsString();
        }
        
        return new InviteDirectMessage(inviter, invited, customValues, customProperties);
    }

    /**
     * Serialize the given {@link InviteDirectMessage} into a
     * {@link JsonElement}.
     * 
     * @param message
     *            The message to be serialized.
     * @param type
     *            The type used for serialization.
     * @param context
     *            The context of the serialization.
     * @return A new JsonElement representing the message.
     */
    @Override
    public JsonElement serialize(InviteDirectMessage message, Type type,
            JsonSerializationContext context) {
        JsonElement jsonMessageElement = super
                .serialize(message, type, context);
        JsonObject jsonMessageObject = jsonMessageElement.getAsJsonObject();

        // Add the inviting participant
        final String inviter = message.getInviter();
        if (inviter != null) {
            jsonMessageObject.addProperty(SerializationKeys.INVITER, inviter);
        }

        // Add the invited participant
        final String invited = message.getInvited();
        if (invited != null) {
            jsonMessageObject.addProperty(SerializationKeys.INVITED, invited);
        }

        return jsonMessageObject;
    }

}
