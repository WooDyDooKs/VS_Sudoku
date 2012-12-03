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

import ds.sudoku.communication.ScoreMessage;

public class ScoreMessageAdapter extends MessageSerializer implements
        JsonSerializer<ScoreMessage>, JsonDeserializer<ScoreMessage> {

    /**
     * Deserialize the given {@link JsonElement} to a {@link ScoreMessage}.
     * 
     * @param jsonMessage
     *            The input fpr de-serialization.
     * @param type
     *            The type used for de-serialization.
     * @param context
     *            The context of the de-serialization.
     * @return A new ScoreMessage built from the input.
     * @throws JsonParseException
     *             Thrown, if the given input does not provide the structure
     *             needed by a {@link ScoreMessage}
     */
    @Override
    public ScoreMessage deserialize(JsonElement jsonMessage, Type type,
            JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonMessageObject = jsonMessage.getAsJsonObject();
        
        // First extract the message parts
        List<String> customValues = extractCustomValues(jsonMessageObject);
        Map<String, String> customProperties = extractCustomProperties(jsonMessageObject);

        // Extract boolean winning
        JsonElement winningElement = jsonMessageObject.get(SerializationKeys.IS_WINNING_KEY);
        final boolean winning = winningElement.getAsBoolean();
        
        return new ScoreMessage(winning, customValues, customProperties);
    }

    /**
     * Serialize the given {@link ScoreMessage} into a {@link JsonElement}.
     * @param message The message to be serialized.
     * @param type The type used for serialization.
     * @param context The context of the serialization.
     * @return A new JsonElement representing the message.
     */
    @Override
    public JsonElement serialize(ScoreMessage message, Type type,
            JsonSerializationContext context) {
        JsonElement jsonMessageElement = super.serialize(message, type, context);
        JsonObject jsonMessageObject = jsonMessageElement.getAsJsonObject();
        
        // add the boolean winning.
        jsonMessageObject.addProperty(SerializationKeys.IS_WINNING_KEY, message.isWinning());
        
        return jsonMessageObject;
    }

}
