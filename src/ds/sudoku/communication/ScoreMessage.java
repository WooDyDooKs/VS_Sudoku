package ds.sudoku.communication;

/**
 * This message is sent whenever there is a score update available
 * for a client. The score message contains information about the
 * client only. There is not an actual score inside of this message but
 * instead, the message contains information about the player having
 * the advantage in the game or not.
 * 
 * @author dalhai
 *
 */
public class ScoreMessage extends Message {
}