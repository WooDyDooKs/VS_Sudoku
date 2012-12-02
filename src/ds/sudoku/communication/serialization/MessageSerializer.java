package ds.sudoku.communication.serialization;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ds.sudoku.communication.Message;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;


/**
 * The message serializer used to transform messages into 
 * {@link JsonElement}s. 
 * 
 * <p>
 * The serializer takes {@link Message#getCustomProperties()} and 
 * stores the values in the returned map in a {@link JsonObject} <i>(not a
 * JsonArray!)</i>. It also takes {@link Message#getCustomValues()} and
 * stores the values in the returned list in a {@link JsonArray}. This 
 * results in the custom values being stored <i>nameless</i>.
 * </p>
 * 
 * <p>
 * Additionally, this serializer provides subclasses with the functionality
 * needed to deserialize the message parts out of a JsonObject.
 * </p>
 * @author dalhai
 *
 */
public class MessageSerializer {

	/**
	 * Serialize the given message into a {@link JsonElement} using
	 * GSON. 
	 * @param message The message to be serialized.
	 * @param type The type of the message.
	 * @param context The context in which the serialization takes place.
	 * @return The serialized message.
	 */
	public JsonElement serialize(Message message, Type type,
		JsonSerializationContext context) {
		final Map<String, String> customProperties = message.getCustomProperties();
		final Set<Entry<String, String>> entries = customProperties.entrySet();
		final List<String> customValues = message.getCustomValues();
		
		//	Map the custom properties into a json object
		JsonObject customJsonProperties = new JsonObject();
		for(Entry<String, String> entry : entries) {
			customJsonProperties.addProperty(entry.getKey(), entry.getValue());
		}
		
		//	Map the custom values into a json array
		JsonArray customJsonValues = new JsonArray();
		for(String value : customValues) {
			JsonPrimitive jsonValue = new JsonPrimitive(value);
			customJsonValues.add(jsonValue);
		}
		
		//	Put everything together
		JsonObject jsonMessage = new JsonObject();
		//	Add message type
		jsonMessage.addProperty(SerializationKeys.MESSAGE_TYPE_KEY, message.getMessageType());
		//	If there are properties, add them.
		if(!customProperties.isEmpty())
			jsonMessage.add(SerializationKeys.CUSTOM_PROPERTIES_KEY, customJsonProperties);
		//	If there are values, add them.
		if(!customValues.isEmpty())
			jsonMessage.add(SerializationKeys.CUSTOM_VALUES_KEY, customJsonValues);
		
		return jsonMessage;
	}

	/**
	 * Extracts a list of custom values from the given {@link JsonObject}.
	 * @param jsonMessage The message to be examined.
	 * @return A list of custom value stored in the given message.
	 * 			The list will be empty, if no custom values were stored in the message.
	 * @throws JsonParseException If the given message does not contain a 
	 * 			{@link JsonArray} with key {@link SerializationKeys#CUSTOM_VALUES_KEY}.
	 */
	protected List<String> extractCustomValues(JsonObject jsonMessage)
		throws JsonParseException {
		final List<String> values = new ArrayList<String>();
		
		//	Check if the given message has custom values.
		if(jsonMessage.has(SerializationKeys.CUSTOM_VALUES_KEY)) {
			//	Handle custom values
			JsonArray customJsonValues =
					jsonMessage.getAsJsonArray(SerializationKeys.CUSTOM_VALUES_KEY);
			
			for(JsonElement entry : customJsonValues) {
				values.add(entry.getAsString());
			}
		}
		
		return values;
	}
	
	/**
	 * Extracts a map of custom properties from the given {@link JsonObject}.
	 * @param jsonMessage The message to be examined.
	 * @return A map of custom properties stored in the given message.
	 * 			The map will be empty, if no custom properties were stored in the message.
	 * @throws JsonParseException If the given message does not contain a
	 * 			{@link JsonObject} with key {@link SerializationKeys#CUSTOM_PROPERTIES_KEY}.
	 */
	protected Map<String, String> extractCustomProperties(JsonObject jsonMessage)
		throws JsonParseException {	
		final Map<String, String> properties = new HashMap<String, String>();
	
		//	Check if the given message has custom properties.
		if(jsonMessage.has(SerializationKeys.CUSTOM_PROPERTIES_KEY)) {
			//	Handle custom Properties
			JsonObject customJsonProperties = 
					jsonMessage.getAsJsonObject(SerializationKeys.CUSTOM_PROPERTIES_KEY);
			
			final Set<Entry<String, JsonElement>> propertySet =
					customJsonProperties.entrySet();
			
			//	Convert each entry into a String pair and add it
			for(Entry<String, JsonElement> entry : propertySet) {
				String key = entry.getKey();
				String value = entry.getValue().getAsString();
				properties.put(key, value);
			}
		}
	
		return properties;
	}
}
