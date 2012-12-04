package ds.sudoku.communication;

import java.util.List;
import java.util.Map;

/**
 * This message is sent from the server to all other players of a game to inform
 * them, that a player left.
 * 
 * @author dalhai
 * 
 */
public class LeftMessage extends Message {

    private final String otherPlayer;

    /**
     * Creates a new LeftMessage. The message carries the name of the player
     * that left the game.
     * 
     * @param otherPlayer
     *            The player that left the game.
     */
    public LeftMessage(String otherPlayer) {
        this.otherPlayer = otherPlayer;
    }

    /**
     * Creates a new LeftMessage. The message carries the name of the player
     * that left the game. Additionally the message can store custom values and
     * properties.
     * 
     * @param otherPlayer
     *            The player that left the game.
     * @param customValues
     *            The custom values stored in this message.
     * @param customProperties
     *            The custom properties stored in this message.
     */
    public LeftMessage(String otherPlayer, List<String> customValues,
            Map<String, String> customProperties) {
        super(customValues, customProperties);
        this.otherPlayer = otherPlayer;
    }

    /**
     * The name of the player that left the game.
     * 
     * @return The name as a {@code String}.
     */
    public String getOtherPlayer() {
        return otherPlayer;
    }
}
