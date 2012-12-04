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

import ds.sudoku.communication.ACKMessage;
import ds.sudoku.communication.Message;

/**
 * Message adapter used for serialization of {@link ACKMessage}.
 * 
 * @author dalhai
 * 
 */
public class ACKMessageAdapter extends MessageSerializer implements
        JsonSerializer<ACKMessage>, JsonDeserializer<ACKMessage> {

    /**
     * Deserialize the given {@link JsonElement} to a {@link ACKMessage}.
     * 
     * @param jsonMessage
     *            The input for de-serialization.
     * @param type
     *            The type used for de-serialization.
     * @param context
     *            The context of the de-serialization.
     * @return A new ACKMessage built from the input.
     * @throws JsonParseException
     *             Thrown, if the given input does not provide the structure
     *             needed by a {@link ACKMessage}
     */
    @Override
    public ACKMessage deserialize(JsonElement jsonMessage, Type type,
            JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonMessageObject = jsonMessage.getAsJsonObject();

        // Extract the message parts
        List<String> customValues = extractCustomValues(jsonMessageObject);
        Map<String, String> customProperties = extractCustomProperties(jsonMessageObject);

        // Get the inner message
        JsonElement jsonInnerMessageElement = jsonMessageObject
                .get(SerializationKeys.CONFIRMED_MESSAGE_KEY);
        JsonObject jsonInnerMessageObject = jsonInnerMessageElement
                .getAsJsonObject();

        // Get the message type of the inner message
        JsonElement messageTypeElement = jsonInnerMessageObject
                .get(SerializationKeys.MESSAGE_TYPE_KEY);
        final String messageType = messageTypeElement.getAsString();

        Message ACKedMessage = null;

        // Try to get the class of the message type
        try {
            final Class<?> messageTypeClass = Class.forName(messageType);
            // Deserialize the message
            ACKedMessage = context.deserialize(jsonInnerMessageElement,
                    messageTypeClass);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(messageType + " not found!");
        }
        
        //  Extract the token if possible
        String token = null;
        if(jsonMessageObject.has(SerializationKeys.TOKEN_KEY)) {
            JsonElement tokenElement = jsonMessageObject.get(SerializationKeys.TOKEN_KEY);
            token = tokenElement.getAsString();
        }

        return new ACKMessage(ACKedMessage, token, customValues,
                customProperties);
    }

    /**
     * Serialize the given {@link ACKMessage} into a {@link JsonElement}.
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
    public JsonElement serialize(ACKMessage message, Type type,
            JsonSerializationContext context) {
        JsonElement jsonMessageElement = super
                .serialize(message, type, context);
        JsonObject jsonMessageObject = jsonMessageElement.getAsJsonObject();

        // Serialize the confirmed message
        final Message ACKedMessage = message.getConfirmedMessage();
        JsonElement ACKedMessageElement = context.serialize(ACKedMessage);
        jsonMessageObject.add(SerializationKeys.CONFIRMED_MESSAGE_KEY,
                ACKedMessageElement);

        // Serialize the token if set
        if (message.hasToken())
            jsonMessageObject.addProperty(SerializationKeys.TOKEN_KEY,
                    message.getToken());

        return jsonMessageObject;
    }

}
