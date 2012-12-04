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

import ds.sudoku.communication.ErrorMessage;
import ds.sudoku.exceptions.SudokuError;

public class ErrorMessageAdapter extends MessageSerializer implements
        JsonSerializer<ErrorMessage>, JsonDeserializer<ErrorMessage> {

    /**
     * Deserialize the given {@link JsonElement} to a {@link ErrorMessage}.
     * 
     * @param jsonMessage
     *            The input for de-serialization.
     * @param type
     *            The type used for de-serialization.
     * @param context
     *            The context of the de-serialization.
     * @return A new ErrorMessage built from the input.
     * @throws JsonParseException
     *             Thrown, if the given input does not provide the structure
     *             needed by a {@link ErrorMessage}
     */
    @Override
    public ErrorMessage deserialize(JsonElement jsonMessage, Type type,
            JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonMessageObject = jsonMessage.getAsJsonObject();

        // First extract the message parts
        List<String> customValues = extractCustomValues(jsonMessageObject);
        Map<String, String> customProperties = extractCustomProperties(jsonMessageObject);

        // Extract error type
        JsonElement errorElement = jsonMessageObject
                .get(SerializationKeys.ERROR_KEY);
        final String errorString = errorElement.getAsString();
        final SudokuError error = SudokuError.valueOf(errorString);

        // Extract error messager
        JsonElement messageElement = jsonMessageObject
                .get(SerializationKeys.REASON_KEY);
        final String message = messageElement.getAsString();

        return new ErrorMessage(error, message, customValues, customProperties);
    }

    /**
     * Serialize the given {@link ErrorMessage} into a {@link JsonElement}.
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
    public JsonElement serialize(ErrorMessage message, Type type,
            JsonSerializationContext context) {
        JsonElement jsonMessageElement = super
                .serialize(message, type, context);
        JsonObject jsonMessageObject = jsonMessageElement.getAsJsonObject();

        // Add the error type.
        final String errorString = message.getError().toString();
        jsonMessageObject.addProperty(SerializationKeys.ERROR_KEY, errorString);

        // Add the error message.
        jsonMessageObject.addProperty(SerializationKeys.REASON_KEY,
                message.getMessage());

        return jsonMessageObject;
    }

}
