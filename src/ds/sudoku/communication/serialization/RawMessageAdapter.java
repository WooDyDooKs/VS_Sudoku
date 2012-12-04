package ds.sudoku.communication.serialization;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializer;

import ds.sudoku.communication.Message;

/**
 * A json adapter for raw messages.
 * 
 * @author dalhai
 * 
 */
public class RawMessageAdapter extends MessageSerializer implements
		JsonSerializer<Message>, JsonDeserializer<Message> {

	/**
	 * Deserialize the given {@link JsonElement} into a {@link Message}.
	 * 
	 * @param jsonMeesage
	 *            The input used for de-serialization.
	 * @param type
	 *            The type used for de-serialization.
	 * @param context
	 *            The context of the de-serialization.
	 * @return A new Message generated from the input data.
	 */
	@Override
	public Message deserialize(JsonElement jsonMessage, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonMessageObject = jsonMessage.getAsJsonObject();
		
		List<String> customValues = extractCustomValues(jsonMessageObject);
		Map<String, String> customProperties = extractCustomProperties(jsonMessageObject);
		
		return new Message(customValues, customProperties);
	}

}
