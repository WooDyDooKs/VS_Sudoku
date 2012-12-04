package ds.sudoku.communication.serialization;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import ds.sudoku.communication.NewGameMessage;
import ds.sudoku.logic.SudokuTemplate;

/**
 * Json adapter used to serialize and deserialize {@link NewGameMessage}.
 * 
 * @author dalhai
 * 
 */
public class NewGameMessageAdapter extends MessageSerializer implements
		JsonSerializer<NewGameMessage>, JsonDeserializer<NewGameMessage> {

	/**
	 * Deserialize the given {@link JsonElement} to a {@link NewGameMessage}.
	 * 
	 * @param jsonMessage
	 *            The input for de-serialization.
	 * @param type
	 *            The type used for de-serialization.
	 * @param context
	 *            The context of the de-serialization.
	 * @return A new game message built from the input.
	 * @throws JsonParseException
	 *             Thrown, if the given input does not provide the structure
	 *             needed by a {@link NewGameMessage}.
	 */
	@Override
	public NewGameMessage deserialize(JsonElement jsonMessage, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonMessageObject = jsonMessage.getAsJsonObject();

		// First extract the message parts
		List<String> customValues = extractCustomValues(jsonMessageObject);
		Map<String, String> customProperties = extractCustomProperties(jsonMessageObject);

		// Then, extract the Sudoku field sent with this message.
		JsonElement jsonFieldElement = jsonMessageObject
				.get(SerializationKeys.SUDOKU_FIELD);
		JsonArray jsonField = jsonFieldElement.getAsJsonArray();
		final int nRows = jsonField.size();
		final int nColumns = nRows == 0 ? 0 : jsonField.get(0).getAsJsonArray()
				.size();

		// The field to be filled
		final int[][] field = new int[nColumns][nRows];

		// Fill the field
		for (int i = 0; i < nRows; i++) {
			JsonArray row = jsonField.get(i).getAsJsonArray();
			for (int j = 0; j < nColumns; j++) {
				JsonElement jsonValue = row.get(j);
				final int value = jsonValue.getAsInt();
				field[i][j] = value;
			}
		}
		
		//	The sudoku template which is to be filled with the field
		final SudokuTemplate sudoku = new SudokuTemplate(field);
		
		return new NewGameMessage(sudoku, customValues, customProperties);
	}

	/**
	 * Serialize the given {@link NewGameMessage} into a {@link JsonElement}.
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
	public JsonElement serialize(NewGameMessage message, Type type,
			JsonSerializationContext context) {
		JsonElement jsonMessageElement = super
				.serialize(message, type, context);
		JsonObject jsonMessageObject = jsonMessageElement.getAsJsonObject();

		// We serialize each array as is...
		final SudokuTemplate sudoku = message.getSudokuField();
		final int[][] field = sudoku.getTemplate();
		final int nRows = field.length;
		final int nColumns = nRows == 0 ? 0 : field[0].length;

		final JsonArray jsonField = new JsonArray();
		// Each row is serialized into the message
		for (int i = 0; i < nRows; i++) {
			JsonArray jsonRow = new JsonArray();
			for (int j = 0; j < nColumns; j++) {
				JsonElement value = new JsonPrimitive(field[i][j]);
				jsonRow.add(value);
			}
			jsonField.add(jsonRow);
		}

		// Add the field to the message
		jsonMessageObject.add(SerializationKeys.SUDOKU_FIELD, jsonField);

		return jsonMessageObject;
	}

}
