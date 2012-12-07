package ds.sudoku.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ds.sudoku.communication.Client;
import ds.sudoku.communication.Message;

public class ServerChannel implements Channel {

    private final String name;
    private final Map<Client, String> clients;
    private final List<String> users;
    
    public ServerChannel(String name, Map<Client, String> clients) {
        this.name = name;
        this.clients = clients;
        this.users = new ArrayList<String>();
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> users() {
        return users;
    }

    @Override
    public void join(String name) {
       if(users.contains(name)) return;
       users.add(name);
    }

    @Override
    public void leave(String name) {
        users.remove(name);
    }

    @Override
    public void broadcast(String sender, String message) {
        final List<Client> targetClients = new ArrayList<Client>();
        final Set<Entry<Client, String>> clientEntries = clients.entrySet();
        for(Entry<Client, String> entry : clientEntries) {
            if(!entry.getValue().equals(sender))
                targetClients.add(entry.getKey());
        }
        
        for(Client target : targetClients) {
            Message msg = new Message();
            msg.addCustomProperty(Constants.MESSAGE_TYPE, MessageTypes.TEXT_MESSAGE);
            msg.addCustomProperty(Constants.SENDER, sender);
            msg.addCustomProperty(Constants.MESSAGE, message);
            target.sendMessage(msg);
        }
    }

    @Override
    public void accept(String channelName, String sender, String receiver,
            String message) {
        if(!channelName.equals(name)) return;
        if(!users.contains(receiver)) return;
        if(!users.contains(sender)) return;
        
        Client targetClient = null;
        final Set<Entry<Client, String>> clientEntries = clients.entrySet();
        for(Entry<Client, String> entry : clientEntries) {
            if(receiver.equals(entry.getValue())) {
                targetClient = entry.getKey();
                break;
            }
        }
        
        if(targetClient == null) return;
        
        Message msg = new Message();
        msg.addCustomProperty(Constants.MESSAGE_TYPE, MessageTypes.TEXT_MESSAGE);
        msg.addCustomProperty(Constants.SENDER, sender);
        msg.addCustomProperty(Constants.MESSAGE, message);
        targetClient.sendMessage(msg);
    }

}
