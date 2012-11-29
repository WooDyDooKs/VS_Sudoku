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

import ds.sudoku.communication.RegisterMessage;

/**
 * Json adapter used to serialize and de-serialize {@link RegisterMessage}s.
 * @author dalhai
 *
 */
public class RegisterMessageAdapter extends MessageSerializer 
implements JsonDeserializer<RegisterMessage>, JsonSerializer<RegisterMessage> {

	/**
	 * Deserialize the given {@link JsonElement} to a {@link RegisterMessage}.
	 * @param jsonMessage The input for de-serialization.
	 * @param type The type used for de-serialization.
	 * @param context The context of the de-serialization.
	 * @return A new register message built from the input.
	 * @throws JsonParseException 
	 * 				Thrown, if the given input does not provide the structure
	 * 				needed by a {@link RegisterMessage}.
	 */
	@Override
	public RegisterMessage deserialize(JsonElement jsonMessage, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonMessageObject = jsonMessage.getAsJsonObject();
	
		//	First extract the message parts
		List<String> customValues = extractCustomValues(jsonMessageObject);
		Map<String, String> customProperties = extractCustomProperties(jsonMessageObject);
		
		//	Extract name (name is mandatory)
		JsonElement nameElement = jsonMessageObject.get(SerializationKeys.NAME_KEY);		
		final String name = nameElement.getAsString();
		//	Extract token (token is optional)
		JsonElement tokenElement = null;
		if(jsonMessageObject.has(SerializationKeys.TOKEN_KEY))
			tokenElement = jsonMessageObject.get(SerializationKeys.TOKEN_KEY);
		//	If there is no token, set it to null.
		final String token = tokenElement != null ? tokenElement.getAsString() : null;
		
		return new RegisterMessage(name, token, customValues, customProperties);
	}

	/**
	 * Serialize the given {@link RegisterMessage} into a {@link JsonElement}.
	 * @param message The message to be serialized.
	 * @param type The type used for serialization.
	 * @param context The context of the serialization.
	 * @return A new JsonElement representing the message.
	 */
	@Override
	public JsonElement serialize(RegisterMessage message, Type type,
			JsonSerializationContext context) {
		JsonElement jsonMessageElement = super.serialize(message, type, context);
		JsonObject jsonMessage = jsonMessageElement.getAsJsonObject();
		
		//	jsonMessage has the custom properties, custom values and
		//	the message type already stored. We just need to add new
		//	properties for our fields.
		jsonMessage.addProperty(SerializationKeys.NAME_KEY, message.getName());
		//	Add the token, if a token is available.
		if(message.hasToken())
			jsonMessage.addProperty(SerializationKeys.TOKEN_KEY, message.getToken());
		
		return jsonMessage;
	}

}
