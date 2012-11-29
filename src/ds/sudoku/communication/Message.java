package ds.sudoku.communication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ds.sudoku.communication.serialization.Serializable;
import ds.sudoku.exceptions.communication.DeserializationException;
import ds.sudoku.exceptions.communication.NoSuchPropertyException;

/**
 * The basic message class provides functionality to store
 * arbitrary data. Upon sending, the stored data will be transformed
 * to JSON and deserialized when received. 
 * 
 * 
 * @author dalhai
 *
 */
public abstract class Message {
	
	private int currentCustomValue;
	private List<String> customValues;
	private Map<String, String> customProperties;
	
	/**
	 * Create a new, empty message.
	 */
	public Message() {
		this.currentCustomValue = -1;
		this.customValues = new ArrayList<String>();
		this.customProperties = new HashMap<String, String>();
	}
	
	/**
	 * Creates a new message with the given custom values and properties.
	 * @param customValues The custom values stored in this message.
	 * @param customProperties The custom properties stored in this message.
	 */
	public Message(List<String> customValues,
		Map<String, String> customProperties) {
		this.currentCustomValue = -1;
		this.customValues = customValues;
		this.customProperties = customProperties;
	}
	
	/**
	 * Add a custom property to the message.
	 * If a property with the given name already exists, its value will
	 * be replaced by the new value.
	 * @param property The name of the property.
	 * @param value The value of the property.
	 */
	public void addCustomProperty(String property, String value) {
		customProperties.put(property, value);
	}
	
	/**
	 * Get the requested property out of the message, if possible.
	 * @param property Name of the requested property.
	 * @return The value of the requested property.
	 * @throws NoSuchPropertyException
	 * 				Thrown when the requested property does
	 * 				not exists in this message.
	 */
	public String getCustomProperty(String property) 
	throws NoSuchPropertyException {
		if(!customProperties.containsKey(property))
			throw new NoSuchPropertyException();
		
		return customProperties.get(property);
	}
	
	/**
	 * Checks if there is another custom value available.
	 * If true, the next custom value can be accessed with 
	 * {@link #getNextCustomValue}.
	 * @return {@code true}, if there is another custom value,
	 * 			{@code false}, else.
	 */
	public boolean hasCustomValue() {
		return currentCustomValue < customValues.size();
	}
	
	/**
	 * Add a custom value. The custom value to be added must implement
	 * the {@link Serializable} interface. The serialized input will be
	 * stored and sent with the message.
	 * 
	 * <p>
	 * The receiver can then provide the type of the object to be
	 * deserialized to the {@link #getNextCustomValue} method.
	 * @param input
	 */
	public void addCustomValue(Serializable input) {
		customValues.add(input.serialize());
	}
	
	/**
	 * Deserialize the next custom value if possible. 
	 * @param target The target serialization object.
	 * @return The deserialized object.
	 * @throws DeserializationException
	 * 				Thrown, if the target object could not be deserialized
	 * 				from the current string.
	 */
	public <T extends Serializable> T getNextCustomValue(T target)	
	throws DeserializationException {		
		//	Check if there is another string.
		if(++currentCustomValue >= customValues.size())
			throw new DeserializationException();
		
		//	We are in bounds, get the target string		
		String source = customValues.get(currentCustomValue);
		
		//	Deserialize the string.
		target.deserialize(source);
		return target;
	}
	
	/**
	 * Get a map of all custom properties stored in this {@link Message}.
	 * The returned map is unmodifiable.
	 * @return
	 */
	public Map<String, String> getCustomProperties() {
		return Collections.unmodifiableMap(customProperties);
	}
	
	/**
	 * Get a list of all custom values stored in this {@link Message}.
	 * The returned list is unmodifiable.
	 * @return An unmodifiable list of all custom values.
	 */
	public List<String> getCustomValues() {
		return Collections.unmodifiableList(customValues);
	}
	
	/**
	 * Every successor of {@link Message} must provide its type as a
	 * String. This String will be added to the JSON output and will
	 * determine the type of message generated on the receiver side.
	 * 
	 * @return The type of the message as string.
	 */
	public abstract String getMessageType();
}