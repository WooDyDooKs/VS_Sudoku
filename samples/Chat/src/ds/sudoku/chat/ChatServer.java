package ds.sudoku.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ds.sudoku.communication.ACKMessage;
import ds.sudoku.communication.Client;
import ds.sudoku.communication.ClientListener;
import ds.sudoku.communication.ClientMessageHandler;
import ds.sudoku.communication.ConnectionHandler;
import ds.sudoku.communication.ConnectionManager;
import ds.sudoku.communication.DeathHandler;
import ds.sudoku.communication.DeregisterMessage;
import ds.sudoku.communication.ErrorMessage;
import ds.sudoku.communication.InviteMessage;
import ds.sudoku.communication.LeaveMessage;
import ds.sudoku.communication.Message;
import ds.sudoku.communication.NACKMessage;
import ds.sudoku.communication.NamedSetFieldMessage;
import ds.sudoku.communication.RegisterMessage;
import ds.sudoku.communication.SetFieldMessage;

/**
 * A simple test chat server which supports channels.
 * 
 * @author dalhai
 * 
 */
public class ChatServer implements ConnectionHandler, ClientMessageHandler,
        DeathHandler<Client> {
    private LinkedList<Client> clients;
    private List<Channel> channels;
    private Map<Client, String> registeredClients;
    private ConnectionManager manager;
    private Channel def;

    public ChatServer(int port) {
        this.clients = new LinkedList<Client>();
        this.channels = new ArrayList<Channel>();
        this.registeredClients = new HashMap<Client, String>();
        this.manager = new ClientListener(port);
        
        def = new ServerChannel("default", registeredClients);
        channels.add(def);
    }

    /**
     * Start accepting new users.
     */
    public void acceptUsers() {
        manager.setConnectionHandler(this);
        manager.acceptConnections();
        System.out.println(OutputChannels.DEBUG + "Waiting for clients!");
    }

    /**
     * Stop accepting new users.
     */
    public void stopAcceptingUsers() {
        manager.rejectConnections();
    }

    /**
     * Drop all users from the server and shut it down.
     */
    public void shutdown() {
        if (manager.isAccepting())
            manager.rejectConnections();

        for (Client client : clients) {
            client.deregister();
            client.stop();
        }

        clients.clear();
        registeredClients.clear();
    }

    @Override
    public void onNewConnectionAccepted(Client newClient) {
        // Add the client to the clients list, set the handler
        // and start it.
        newClient.setMessageHandler(this);
        newClient.setDeathHandler(this);
        newClient.start();
        clients.add(newClient);
        
        System.out.println(OutputChannels.DEBUG + "Client connected!");
    }

    @Override
    public void onDeath(Client instance, String message) {
        final String outputMsgP1 = OutputChannels.WARNING + "Client died. ";
        final String outputMsgP2 = OutputChannels.WARNING + "Reason: " + message;
        
        System.out.println(outputMsgP1);
        System.out.println(outputMsgP2);
        
        final boolean registered = registeredClients.containsKey(instance);
        
        if(registered) {
            final String name = registeredClients.get(instance);
            final String outputMsgP3 = OutputChannels.WARNING + "Client was registered as: " + name;
            System.out.println(outputMsgP3);
            
            registeredClients.remove(instance);
        }
        
        clients.remove(instance);    
        instance.stop();
    }

    @Override
    public void onRawMessageReceived(Client client, Message message) {
        final String messageType = message.getCustomProperty(Constants.MESSAGE_TYPE);
        
        if(messageType.equals(MessageTypes.TEXT_MESSAGE)) {
            final String channel = message.getCustomProperty(Constants.CHANNEL);
            final String sender = message.getCustomProperty(Constants.SENDER);
            final String content = message.getCustomProperty(Constants.MESSAGE);
            if(message.hasCustomProperty(Constants.RECEIVER)) {
                final String receiver = message.getCustomProperty(Constants.RECEIVER);
                
                for(Channel chan : channels) {
                    chan.accept(channel, sender, receiver, content);
                }
                
                final String output = String.format("%sC: %s\n%sF: %s\n%sT: %s\n%sM: %s",
                        OutputChannels.DEBUG, channel,
                        OutputChannels.DEBUG, sender,
                        OutputChannels.DEBUG, receiver,
                        OutputChannels.DEBUG, content
                        );
                System.out.println(output);
            } else {
                for(Channel chan : channels) {
                    if(chan.getName().equals(channel)) {
                        chan.broadcast(sender, content);
                        
                        final String output = String.format("%sC: %s\n%sF: %s\n%sM: %s",
                                OutputChannels.DEBUG, channel,
                                OutputChannels.DEBUG, sender,
                                OutputChannels.DEBUG, content
                                );
                        System.out.println(output);
                        
                        return;
                    }
                }
            }
        } else if(messageType.equals(MessageTypes.CHANNEL_COMMAND)) {
            final String command = message.getCustomProperty(Constants.COMMAND);
            if(command.equals(Constants.LIST_USERS)) {
                final String channel = message.getCustomProperty(Constants.CHANNEL);
                List<String> users = null;
                for(Channel chan : channels) {
                    if(chan.getName().equals(channel)) {
                        users = chan.users();
                        break;
                    }
                }
                
                Message msg = new Message();
                String usersp = "";
                for(String user : users)
                    usersp = usersp  + "," + user;
                msg.addCustomProperty(Constants.MESSAGE_TYPE, MessageTypes.CHANNEL_COMMAND);
                msg.addCustomProperty(Constants.COMMAND, Constants.LIST_USERS);
                msg.addCustomProperty(Constants.LIST_USERS, usersp);
                client.sendMessage(msg);
            }
        }
    }

    @Override
    public void onRegisterMessageReceived(Client client, RegisterMessage message) {  
        final String name = message.getName();
        
        // register
        registeredClients.put(client, name);
        
        // put into default channel
        def.join(name);
        
        // Send channel
        Message msg = new Message();
        msg.addCustomProperty(Constants.MESSAGE_TYPE, MessageTypes.CHANNEL_JOINED);
        msg.addCustomProperty(Constants.CHANNEL, def.getName());
        client.sendMessage(msg);
    }

    @Override
    public void onDeregisterMessageReceived(Client client,
            DeregisterMessage message) {
        clients.remove(client);
        registeredClients.remove(client);
        client.stop();
    }

    @Override
    public void onInviteMessageReceived(Client client, InviteMessage message) {
    }

    @Override
    public void onLeaveMessageReceived(Client client, LeaveMessage message) {
        final String name = registeredClients.get(client);
        
        for(Channel chan : channels) {
            chan.leave(name);
        }
        
        //  Aaaand put into default channel
        def.join(name);
        
        // Send channel
        Message msg = new Message();
        msg.addCustomProperty(Constants.MESSAGE_TYPE, MessageTypes.CHANNEL_JOINED);
        msg.addCustomProperty(Constants.CHANNEL, def.getName());
        client.sendMessage(msg);
    }

    @Override
    public void onSetFieldMessageReceived(Client client, SetFieldMessage message) {
    }

    @Override
    public void onNamedSetFieldMessageReceived(Client client,
            NamedSetFieldMessage message) {
    }

    @Override
    public void onErrorMessageReceived(Client client, ErrorMessage message) {
    }

    @Override
    public void onACKMessageReceived(Client client, ACKMessage message) {
    }

    @Override
    public void onNACKMessageReceived(Client client, NACKMessage message) {
    }
}
