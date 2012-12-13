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

import ds.sudoku.communication.InviteMessage;

/**
 * A json adapter used to serialize and deserialize {@link InviteMessage}.
 * 
 * @author dalhai
 * 
 */
public class InviteMessageAdapter extends MessageSerializer implements
        JsonDeserializer<InviteMessage>, JsonSerializer<InviteMessage> {

    /**
     * Deserialize the given {@link JsonElement} to a {@link InviteMessage}.
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
     *             needed by a {@link InviteMessage}
     */
    @Override
    public InviteMessage deserialize(JsonElement jsonMessage, Type type,
            JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonMessageObject = jsonMessage.getAsJsonObject();

        // First extract the message parts
        List<String> customValues = extractCustomValues(jsonMessageObject);
        Map<String, String> customProperties = extractCustomProperties(jsonMessageObject);

        // Extract the name
        String name = null;
        if (jsonMessageObject.has(SerializationKeys.NAME_KEY)) {
            JsonElement nameElement = jsonMessageObject
                    .get(SerializationKeys.NAME_KEY);
            name = nameElement.getAsString();
        }

        // Extract the difficulty
        String difficulty = null;
        if (jsonMessageObject.has(SerializationKeys.DIFFICULTY_KEY)) {
            JsonElement difficultyElement = jsonMessageObject
                    .get(SerializationKeys.DIFFICULTY_KEY);
            difficulty = difficultyElement.getAsString();
        }

        return new InviteMessage(name, difficulty, customValues, customProperties);
    }

    /**
     * Serialize the given {@link InviteMessage} into a {@link JsonElement}.
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
    public JsonElement serialize(InviteMessage message, Type type,
            JsonSerializationContext context) {
        JsonElement jsonMessageElement = super
                .serialize(message, type, context);
        JsonObject jsonMessageObject = jsonMessageElement.getAsJsonObject();

        // Add the name
        final String name = message.getName();
        if (name != null) {
            jsonMessageObject.addProperty(SerializationKeys.NAME_KEY, name);
        }
        
        // Add the difficulty
        final String difficulty = message.getDifficulty();
        if(difficulty != null) {
            jsonMessageObject.addProperty(SerializationKeys.DIFFICULTY_KEY, difficulty);
        }

        return jsonMessageObject;
    }

}
