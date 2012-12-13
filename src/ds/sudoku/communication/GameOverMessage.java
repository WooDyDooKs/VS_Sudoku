package ds.sudoku.communication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This message is sent whenever a sudoku game has ended. It contains
 * information about the outcome of the game and supports adding statistics
 * about the game.
 * 
 * @author dalhai
 * 
 */
public class GameOverMessage extends Message {

    private final String name;
    private final Map<String, Integer> scores;

    /**
     * Creates a new GameOverMessage which signs, that the Sudoku is solved
     * correctly. It also carries the username of the player which won that
     * game.
     * 
     * @param name
     *            The username of the winning player
     */
    public GameOverMessage(String name) {
        this.name = name;
        this.scores = new HashMap<String, Integer>();
    }

    /**
     * Creates a new GameOverMessage which signs, that the Sudoku is solved
     * correctly. It also carries the username of the player which won the game
     * and a list of points for each participating player.
     * 
     * @param name
     *            The username of the winning player.
     * @param scores
     *            A map of scores containing an entry for each participating
     *            player.
     */
    public GameOverMessage(String name, Map<String, Integer> scores) {
        this.name = name;
        this.scores = scores;
    }

    /**
     * Creates a new GameOverMessage which signs, that the Sudoku is solved
     * correctly. It also carries the username of the player that won that game.
     * Additionally stores custom values ander properties.
     * 
     * @param name
     *            The Name of the player that won.
     * @param scores
     *            A map of scores containing an entry for each participating
     *            player.
     * @param The
     *            custom values stored in this message.
     * @param The
     *            custom properties in stored in this message.F
     */
    public GameOverMessage(String name, Map<String, Integer> scores,
            List<String> customValues, Map<String, String> customProperties) {
        super(customValues, customProperties);
        this.name = name;
        this.scores = scores;
    }

    /**
     * Get the name of the player that won.
     * 
     * @return the name of the player that won
     */
    public String getName() {
        return name;
    }

    /**
     * Get the scores for this game.
     * 
     * @return The scores.
     */
    public Map<String, Integer> getScores() {
        return scores;
    }
}