package ds.sudoku.communication.serialization;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import ds.sudoku.communication.InviteDirectMessage;

/**
 * 
 * @author dalhai
 *
 */
public class InviteDirectMessageAdapter extends MessageSerializer implements
        JsonDeserializer<InviteDirectMessage>,
        JsonSerializer<InviteDirectMessage> {

    @Override
    public InviteDirectMessage deserialize(JsonElement arg0, Type arg1,
            JsonDeserializationContext arg2) throws JsonParseException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public JsonElement serialize(InviteDirectMessage arg0, Type arg1,
            JsonSerializationContext arg2) {
        // TODO Auto-generated method stub
        return null;
    }

}
