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

import ds.sudoku.communication.DeregisterMessage;

/**
 * Json adapter used to serialize and deserialize {@link DeregisterMessage}.
 * @author dalhai
 *
 */
public class DeregisterMessageAdapter extends MessageSerializer 
implements JsonSerializer<DeregisterMessage>, JsonDeserializer<DeregisterMessage> {

	/**
	 * Deserialize the given {@link JsonElement} to a {@link DeregisterMessage}.
	 * @param jsonMessage The input for de-serialization.
	 * @param type The type used for de-serialization.
	 * @param context The context of the de-serialization.
	 * @return A new deregister message built from the input.
	 * @throws JsonParseException
	 * 				Thrown, if the given input does not provide the structure
	 * 				needed by a {@link DeregisterMessage}.
	 */
	@Override
	public DeregisterMessage deserialize(JsonElement jsonMessage, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonMessageObject = jsonMessage.getAsJsonObject();
		
		//	First extract the message parts
		List<String> customValues = extractCustomValues(jsonMessageObject);
		Map<String, String> customProperties = extractCustomProperties(jsonMessageObject);
		
		//	Extract reason
		JsonElement reasonElement = jsonMessageObject.get(SerializationKeys.REASON_KEY);
		final String reason = reasonElement.getAsString();
		//	Extract token (token is optional)
		JsonElement tokenElement = null;
		if(jsonMessageObject.has(SerializationKeys.TOKEN_KEY))
			tokenElement = jsonMessageObject.get(SerializationKeys.TOKEN_KEY);
		//	If there is no token, set it to null
		final String token = tokenElement != null ? tokenElement.getAsString() : null;
		
		return new DeregisterMessage(reason, token, customValues, customProperties);
	}

	/**
	 * Serialize the given {@link DeregisterMessage} into a {@link JsonElement}.
	 * @param message The message to be serialized.
	 * @param type The type used for serialization.
	 * @param context The context of the serialization.
	 * @return A new JsonElement representing the message.
	 */
	@Override
	public JsonElement serialize(DeregisterMessage message, Type type,
			JsonSerializationContext context) {
		JsonElement jsonMessageElement = super.serialize(message, type, context);
		JsonObject jsonMessageObject = jsonMessageElement.getAsJsonObject();
		
		//	jsonMessageObject already has the custom properties, values and
		//	the message type stored. We just need to add new properties for our
		//	fields.
		jsonMessageObject.addProperty(SerializationKeys.REASON_KEY, message.getReason());
		//	Add the token, if a token is available.
		if(message.hasToken())
			jsonMessageObject.addProperty(SerializationKeys.TOKEN_KEY, message.getToken());
		
		return jsonMessageObject;
	}
}
