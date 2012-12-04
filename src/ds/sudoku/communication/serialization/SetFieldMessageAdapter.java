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

import ds.sudoku.communication.SetFieldMessage;

/**
 * Json adapter used to serialize and deserialize {@link SetFieldMessage}.
 * 
 * @author dalhai
 * 
 */
public class SetFieldMessageAdapter extends MessageSerializer implements
		JsonSerializer<SetFieldMessage>, JsonDeserializer<SetFieldMessage> {

	/**
	 * Deserialize the given {@link JsonElement} to a {@link SetFieldMessage}.
	 * 
	 * @param jsonMessage
	 *            The input for de-serialization.
	 * @param type
	 *            The type used for de-serialization.
	 * @param context
	 *            The context of the de-serialization.
	 * @return A new set field message built from the input.
	 * @throws JsonParseException
	 *             Throw, if the given input does not provide the structure
	 *             needed by a {@link SetFieldMessage}.
	 */
	@Override
	public SetFieldMessage deserialize(JsonElement jsonMessage, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonMessageObject = jsonMessage.getAsJsonObject();

		// First extract the message parts
		List<String> customValues = extractCustomValues(jsonMessageObject);
		Map<String, String> customProperties = extractCustomProperties(jsonMessageObject);

		// Extract index
		JsonElement indexElement = jsonMessageObject
				.get(SerializationKeys.INDEX_KEY);
		final int index = indexElement.getAsInt();

		// Extract sudoku width and height
		JsonElement widthElement = jsonMessageObject
				.get(SerializationKeys.SUDOKU_WIDTH_KEY);
		JsonElement heightElement = jsonMessageObject
				.get(SerializationKeys.SUDOKU_HEIGHT_KEY);
		final int sudokuWidth = widthElement.getAsInt();
		final int sudokuHeight = heightElement.getAsInt();

		// Extract zero based - ness
		JsonElement zeroBasedElement = jsonMessageObject
				.get(SerializationKeys.ZERO_BASED_KEY);
		final boolean zeroBased = zeroBasedElement.getAsBoolean();

		// Extract value
		JsonElement valueElement = jsonMessageObject
				.get(SerializationKeys.VALUE_KEY);
		final int value = valueElement.getAsInt();

		return new SetFieldMessage(index, value, zeroBased, sudokuWidth,
				sudokuHeight, customValues, customProperties);
	}

	/**
	 * Serialize the given {@link SetFieldMessage} into a {@link JsonElement}.
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
	public JsonElement serialize(SetFieldMessage message, Type type,
			JsonSerializationContext context) {
		JsonElement jsonMessageElement = super
				.serialize(message, type, context);
		JsonObject jsonMessageObject = jsonMessageElement.getAsJsonObject();

		// Custom properties and values, aswell as the message type are
		// already stored. Add the field of this message.
		jsonMessageObject.addProperty(SerializationKeys.INDEX_KEY,
				message.getIndex());
		jsonMessageObject.addProperty(SerializationKeys.SUDOKU_WIDTH_KEY,
				message.getSudokuWidth());
		jsonMessageObject.addProperty(SerializationKeys.SUDOKU_HEIGHT_KEY,
				message.getSudokuHeight());
		jsonMessageObject.addProperty(SerializationKeys.ZERO_BASED_KEY,
				message.isZeroBased());
		jsonMessageObject.addProperty(SerializationKeys.VALUE_KEY,
				message.getValue());

		return jsonMessageObject;
	}
}
