package ds.sudoku.communication.serialization;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import ds.sudoku.communication.DeregisterMessage;

public class DeregisterMessageAdapter extends MessageSerializer 
implements JsonSerializer<DeregisterMessage>, JsonDeserializer<DeregisterMessage> {

	@Override
	public DeregisterMessage deserialize(JsonElement arg0, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonElement serialize(DeregisterMessage arg0, Type arg1,
			JsonSerializationContext arg2) {
		// TODO Auto-generated method stub
		return null;
	}

}
