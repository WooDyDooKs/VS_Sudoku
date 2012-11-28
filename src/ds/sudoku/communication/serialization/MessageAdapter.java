package ds.sudoku.communication.serialization;

import java.lang.reflect.Type;

import ds.sudoku.communication.Message;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;


public class MessageAdapter 
implements JsonSerializer<Message>, JsonDeserializer<Message> {

	@Override
	public JsonElement serialize(Message message, Type type,
			JsonSerializationContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message deserialize(JsonElement arg0, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		// TODO Auto-generated method stub
		return null;
	}

}
