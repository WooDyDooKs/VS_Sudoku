package ds.sudoku.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ds.sudoku.communication.serialization.ACKMessageAdapter;
import ds.sudoku.communication.serialization.DeregisterMessageAdapter;
import ds.sudoku.communication.serialization.ErrorMessageAdapter;
import ds.sudoku.communication.serialization.GameOverMessageAdapter;
import ds.sudoku.communication.serialization.InviteDirectMessageAdapter;
import ds.sudoku.communication.serialization.InviteRandomMessageAdapter;
import ds.sudoku.communication.serialization.LeaveMessageAdapter;
import ds.sudoku.communication.serialization.LeftMessageAdapter;
import ds.sudoku.communication.serialization.NACKMessageAdapter;
import ds.sudoku.communication.serialization.NamedSetFieldMessageAdapter;
import ds.sudoku.communication.serialization.NewGameMessageAdapter;
import ds.sudoku.communication.serialization.RawMessageAdapter;
import ds.sudoku.communication.serialization.RegisterMessageAdapter;
import ds.sudoku.communication.serialization.ScoreMessageAdapter;
import ds.sudoku.communication.serialization.SetFieldMessageAdapter;

/**
 * This factory is used to get a new Gson - Json parser with all the type
 * adapters already registered.
 * 
 * @author dalhai
 * 
 */
public enum GsonFactory {
    ;

    private static Gson gson = null;

    /**
     * Creates a new Gson Json parser with all message type adapters already
     * registered. Works both on Android and on normal java platforms.
     * 
     * @return
     */
    public static Gson create() {
        if (gson != null)
            return gson;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ACKMessage.class,
                new ACKMessageAdapter());
        gsonBuilder.registerTypeAdapter(NACKMessage.class,
                new NACKMessageAdapter());
        gsonBuilder.registerTypeAdapter(RegisterMessage.class,
                new RegisterMessageAdapter());
        gsonBuilder.registerTypeAdapter(DeregisterMessage.class,
                new DeregisterMessageAdapter());
        gsonBuilder.registerTypeAdapter(ErrorMessage.class,
                new ErrorMessageAdapter());
        gsonBuilder.registerTypeAdapter(GameOverMessage.class,
                new GameOverMessageAdapter());
        gsonBuilder.registerTypeAdapter(LeftMessage.class,
                new LeftMessageAdapter());
        gsonBuilder.registerTypeAdapter(SetFieldMessage.class,
                new SetFieldMessageAdapter());
        gsonBuilder.registerTypeAdapter(NamedSetFieldMessage.class,
                new NamedSetFieldMessageAdapter());
        gsonBuilder.registerTypeAdapter(ScoreMessage.class,
                new ScoreMessageAdapter());
        gsonBuilder.registerTypeAdapter(NewGameMessage.class,
                new NewGameMessageAdapter());
        gsonBuilder.registerTypeAdapter(LeaveMessage.class,
                new LeaveMessageAdapter());
        gsonBuilder.registerTypeAdapter(InviteDirectMessage.class,
                new InviteDirectMessageAdapter());
        gsonBuilder.registerTypeAdapter(InviteRandomMessage.class,
                new InviteRandomMessageAdapter());
        gsonBuilder.registerTypeAdapter(Message.class, new RawMessageAdapter());

        gson = gsonBuilder.create();

        return gson;
    }
}
