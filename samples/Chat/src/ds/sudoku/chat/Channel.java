package ds.sudoku.chat;

import java.util.List;

/**
 * Represents a channel in a chat.
 * @author dalhai
 *
 */
public interface Channel {

    /**
     * Get the name of the channel.
     * @return The name of the channel.
     */
    public String getName();
    
    /**
     * Get all users currently in this channel.
     * @return A list of all users in this channel.
     */
    public List<String> users();
    
    /**
     * Adds the user with the given name to the channel.
     * @param name The name of the user to join.
     */
    public void join(String name);
    
    /**
     * Remove the user with the given name from the channel.
     * @param name The name of the user to kick.
     */
    public void leave(String name);
    
    /**
     * Broadcast a message to all users in this channel.
     * @param message The message to broadcast.
     */
    public void broadcast(String sender, String message);
    
    /**
     * Put a message into this channel. The message will be processed
     * if it belongs to this channel. Else, the channel will drop it.
     * @param channelName The name of the target channel.
     * @param sender The origin of this message.
     * @param message The actual message.
     */
    public void accept(String channelName, String sender, String receiver, String message);
    
}
