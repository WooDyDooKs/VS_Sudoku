package ds.sudoku.communication;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import ds.sudoku.exceptions.communication.NoSuchTypeException;
import ds.sudoku.exceptions.communication.NoSuchPropertyException;;

/**
 * The basic message class provides functionality to store
 * arbitrary data. Upon sending, the stored data will be transformed
 * to JSON and deserialized when received. 
 * 
 * <p>
 * @author dalhai
 *
 */
public abstract class Message {
	//	A queue of lists of JsonElement representing custom objects
	//	serialized into the message.
	Queue<List<JsonElement>> customValues;
	//	A list of JsonObjects representing custom key - value pairs
	//	stored in the message.
	JsonObject customFields;
	
	/**
	 * Adds a custom property - value pair to the message.
	 * @param property The name of the property.
	 * @param value The value of the property.
	 */
	public void addCustomField(String property, String value) {
		if(customFields == null)
			customFields = new JsonObject();
		
		customFields.addProperty(property, value);
	}
	
	/**
	 * Get the value of the custom field.
	 * @param property The name of the requested property.
	 * @return The value of the requested property.
	 */
	public String getCustomField(String property) 
		throws NoSuchPropertyException {
		if(customFields == null) 
			throw new NoSuchPropertyException();
		
		String valueFound = null;
		try {
			JsonElement elem = customFields.get(property);
			valueFound = elem.getAsString();
		} catch(JsonSyntaxException e) {
			throw new NoSuchPropertyException();
		}
		
		return valueFound;
	}
	
	/**
	 * Adds an arbitrary number of objects to the message.
	 * The objects will be deserialized in the same order as they
	 * are provided here.
	 * @param type The type of the information to be added.
	 * @param input The information to be added.
	 */
	public <T extends Object> void addCustomValues(T... input) {
		if(input.length == 0) return;
		
		Gson json = new Gson();
		
		//	Convert to a json element and store it
		List<JsonElement> values = new ArrayList<JsonElement>();
		for(T item : input) 
			values.add(json.toJsonTree(item));
		
		//	if custom values are null, initialize them
		if(customValues == null) customValues = new LinkedList<List<JsonElement>>();
		
		//	Add all values
		customValues.add(values);
	}
	
	/**
	 * Get the next list of values from the message.
	 * If the next list of values does not match the requested type, 
	 * throws an exception.
	 * 
	 * <p>
	 * Removes the requested elements from the message.
	 * 
	 * @param type The requested type.
	 * @return A List of objects of the requested type, if available. Else,
	 * 		   {@code null}.
	 * @throws NoSuchTypeException Thrown if the requested type is not available.
	 */
	public <T extends Object> List<T> getNextCustomValues(Class<T> type) 
			throws NoSuchTypeException  {
		
		if(customValues == null || customValues.isEmpty()) return null;
		
		Gson json = new Gson();
		
		//	Convert back from json element to an actual object
		List<T> results = new ArrayList<T>();
		List<JsonElement> values = customValues.peek();
		
		if(values == null)
			throw new NoSuchTypeException();
		
		//	Deserialize values
		try {
			for(JsonElement value : values) {
				T deserialized = json.fromJson(value, type);
				results.add(deserialized);
			}
		} catch (JsonSyntaxException e) {
			throw new NoSuchTypeException();
		}
		
		customValues.remove();
				
		return results;
	}
	
	/**
	 * Check if the message has custom values available.
	 * @return 
	 * 			{@code true}, if not all custom values have been processed,
	 * 			{@code false}, else.
	 */
	public boolean hasCustomValues() {
		return customValues != null && !customValues.isEmpty();
	}
	
	/**
	 * Check if the message has custom field available.
	 * @return
	 * 			{@code true}, if not all custom fields have been processed,
	 * 			{@code false}, else.
	 */
	public boolean hasCustomFields() {
		return customFields != null;
	}
	
	/**
	 * Convert the message to a JSON representation.
	 * @return The message as JSON string.
	 */
	public String toJson() {
		JsonObject message = new JsonObject();
		
		//	Add all custom fields
		message.add("CustomFields", customFields);
		
		//	Create JsonElements for custom values.
		JsonArray customValuesElement = new JsonArray();
		for(List<JsonElement> elements : customValues) {
			JsonArray list = new JsonArray();
			//	Add every element of the list
			for(JsonElement element : elements) {
				list.add(element);
			}
			customValuesElement.add(list);
		}
		
		//	Add the custom values
		message.add("CustomValues", customValuesElement);
		
		//	Conver to json
		Gson json = new Gson();
		return json.toJson(message);
	}
}