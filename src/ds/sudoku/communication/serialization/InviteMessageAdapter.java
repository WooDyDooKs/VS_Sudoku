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
 * Json adapter used to serialize and deserializes {@link InviteMessage}.
 * 
 * @author dalhai
 * 
 */
public class InviteMessageAdapter extends MessageSerializer implements
		JsonSerializer<InviteMessage>, JsonDeserializer<InviteMessage> {

	/**
	 * Deserialize the given {@link JsonElement} to a {@link InviteMessage}.
	 * 
	 * @param jsonMessage
	 *            The input for de-serialization.
	 * @param type
	 *            The type used for de-serialization.
	 * @param context
	 *            The context of the de-serialization.
	 * @return A new invite message built from the input.
	 * @throws JsonParseException
	 *             Thrown, if the given input does not provide the structure
	 *             needed by a {@link InviteMessage}.
	 */
	@Override
	public InviteMessage deserialize(JsonElement jsonMessage, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonMessageObject = jsonMessage.getAsJsonObject();

		// First extract the message parts
		List<String> customValues = extractCustomValues(jsonMessageObject);
		Map<String, String> customProperties = extractCustomProperties(jsonMessageObject);

		// Extract the sender
		JsonElement senderElement = jsonMessageObject
				.get(SerializationKeys.SENDER_KEY);
		final String sender = (senderElement != null) ? senderElement.getAsString() : null;

		// Extract the receiver
		JsonElement receiverElement = jsonMessageObject
				.get(SerializationKeys.RECEIVER_KEY);
		final String receiver = (receiverElement != null) ? receiverElement.getAsString() : null;
		
		return new InviteMessage(sender, receiver, customValues, customProperties);
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

		// Custom properties and values, aswell as the message type are
		// already stored.
		jsonMessageObject.addProperty(SerializationKeys.SENDER_KEY,
				message.getSender());
		jsonMessageObject.addProperty(SerializationKeys.RECEIVER_KEY,
				message.getReciever());

		return jsonMessageObject;
	}

}
