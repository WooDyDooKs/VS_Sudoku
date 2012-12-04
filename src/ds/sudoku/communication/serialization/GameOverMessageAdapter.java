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

import ds.sudoku.communication.GameOverMessage;

public class GameOverMessageAdapter extends MessageSerializer implements
        JsonSerializer<GameOverMessage>, JsonDeserializer<GameOverMessage> {

    /**
     * Deserialize the given {@link JsonElement} to a {@link GameOverMessage}.
     * 
     * @param jsonMessage
     *            The input for de-serialization.
     * @param type
     *            The type used for de-serialization.
     * @param context
     *            The context of the de-serialization.
     * @return A new GameOverMessage built from the input.
     * @throws JsonParseException
     *             Thrown, if the given input does not provide the structure
     *             needed by a {@link GameOverMessage}
     */
    @Override
    public GameOverMessage deserialize(JsonElement jsonMessage, Type type,
            JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonMessageObject = jsonMessage.getAsJsonObject();
        
        // First extract the message parts
        List<String> customValues = extractCustomValues(jsonMessageObject);
        Map<String, String> customProperties = extractCustomProperties(jsonMessageObject);
        
        // Extract string name
        JsonElement nameElement = jsonMessageObject.get(SerializationKeys.NAME_KEY);
        final String name = nameElement.getAsString();
        
        return new GameOverMessage(name, customValues, customProperties);
    }

    /**
     * Serialize the given {@link GameOverMessage} into a {@link JsonElement}.
     * @param message The message to be serialized.
     * @param type The type used for serialization.
     * @param context The context of the serialization.
     * @return A new JsonElement representing the message.
     */
    @Override
    public JsonElement serialize(GameOverMessage message, Type type,
            JsonSerializationContext context) {
        JsonElement jsonMessageElement = super.serialize(message, type, context);
        JsonObject jsonMessageObject = jsonMessageElement.getAsJsonObject();
        
        // Add the string name
        jsonMessageObject.addProperty(SerializationKeys.NAME_KEY, message.getName());
        return jsonMessageObject;
    }
    
}
