package ds.sudoku.chat;
import java.util.concurrent.ExecutionException;

import ds.sudoku.communication.ACKMessage;
import ds.sudoku.communication.DeathHandler;
import ds.sudoku.communication.DeregisterMessage;
import ds.sudoku.communication.ErrorMessage;
import ds.sudoku.communication.GameOverMessage;
import ds.sudoku.communication.InviteMessage;
import ds.sudoku.communication.LeftMessage;
import ds.sudoku.communication.Message;
import ds.sudoku.communication.NACKMessage;
import ds.sudoku.communication.NamedSetFieldMessage;
import ds.sudoku.communication.NewGameMessage;
import ds.sudoku.communication.ScoreMessage;
import ds.sudoku.communication.Server;
import ds.sudoku.communication.ServerFactory;
import ds.sudoku.communication.ServerMessageHandler;
import ds.sudoku.communication.SetFieldMessage;

/**
 * A simple test chat client which supports channels.
 * @author dalhai
 *
 */
public class ChatClient implements ServerMessageHandler, DeathHandler<Server> {
    private final String address;
    private final int port;
    
    private String name;
    
    private Server server;
    private String channel;
    
    public ChatClient(String address, int port) {
        this.address = address;
        this.port = port;
        this.server = null;
        this.channel = null;
    }
    
    public void start() throws InterruptedException, ExecutionException {
        server = ServerFactory.create(address, port);
        server.setMessageHandler(this);
        server.setDeathHandler(this);
        server.start();
    }
    
    public void stop() {
        server.deregister(null);
        server.stop();
    }

    public void register(String name) {
        this.name = name;
        server.register(name);
    }
    
    public void send(String message) {
        Message msg = new Message();
        msg.addCustomProperty(Constants.MESSAGE_TYPE, MessageTypes.TEXT_MESSAGE);
        msg.addCustomProperty(Constants.MESSAGE, message);
        msg.addCustomProperty(Constants.SENDER, name);
        msg.addCustomProperty(Constants.CHANNEL, channel); 
        server.sendMessage(msg);
    }
    
    public void send(String message, String user) {
        Message msg = new Message();
        msg.addCustomProperty(Constants.MESSAGE_TYPE, MessageTypes.TEXT_MESSAGE);
        msg.addCustomProperty(Constants.MESSAGE, message);
        msg.addCustomProperty(Constants.SENDER, name);
        msg.addCustomProperty(Constants.RECEIVER, user);
        msg.addCustomProperty(Constants.CHANNEL, channel);
        server.sendMessage(msg);
    }
    
    public void listUsers() {
        Message msg = new Message();
        msg.addCustomProperty(Constants.MESSAGE_TYPE, MessageTypes.CHANNEL_COMMAND);
        msg.addCustomProperty(Constants.COMMAND, Constants.LIST_USERS);
        msg.addCustomProperty(Constants.CHANNEL, channel);
        server.sendMessage(msg);
    }
    
    @Override
    public void onDeath(Server instance, String message) {
        instance.stop();
    }

    @Override
    public void onRawMessageReceived(Server server, Message message) {
        final String messageType = message.getCustomProperty(Constants.MESSAGE_TYPE);       
        
        /*
         * The server is telling us, that we successfully joined a channel.
         * Give the user feedback and set the channel to the channel sent by the server.
         */
        if(messageType.equals(MessageTypes.CHANNEL_JOINED)) {
            final String channel = message.getCustomProperty(Constants.CHANNEL);
            this.channel = channel;
            System.out.println("JOIN: " + channel);
            return;
        }
        
        /*
         * This is the answer to an earlier request for all the user names.
         * We just print out all the usernames directly.
         */
        if(messageType.equals(MessageTypes.CHANNEL_COMMAND)) {
            final String command = message.getCustomProperty(Constants.COMMAND);
            if(command.equals(Constants.LIST_USERS)) {
                final String users = message.getCustomProperty(Constants.LIST_USERS);
                String[] usersa = users.split(",");
                System.out.print("\n******************************************************************\n");
                for(String user : usersa) {
                    if(user.equals("")) continue;
                    System.out.println("USER: " + user);
                }
                System.out.print("******************************************************************\n");
                return;
            }
        }
        
        /*
         * Text messages are just displayed directly. 
         */
        if(messageType.equals(MessageTypes.TEXT_MESSAGE)) {
            final String content = message.getCustomProperty(Constants.MESSAGE);
            final String sender = message.getCustomProperty(Constants.SENDER);
            
            System.out.println(sender + ": " + content);
        }
        
    }

    @Override
    public void onDeregisterMessageReceived(Server server,
            DeregisterMessage message) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onLeftMessageReceived(Server server, LeftMessage message) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onSetFieldMessageReceived(Server server, SetFieldMessage message) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onNamedSetFieldMessageReceived(Server server,
            NamedSetFieldMessage message) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onErrorMesssageReceived(Server server, ErrorMessage message) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onInviteMessageReceived(Server server, InviteMessage message) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onACKReceived(Server server, ACKMessage message) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onNACKReceived(Server server, NACKMessage message) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onGameOverMessageReceived(Server server, GameOverMessage message) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onScoreMessageReceived(Server server, ScoreMessage message) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onNewGameMessageReceived(Server client, NewGameMessage message) {
        // TODO Auto-generated method stub
        
    }
}
