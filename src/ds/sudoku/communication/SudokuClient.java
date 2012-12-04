package ds.sudoku.communication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import ds.sudoku.communication.serialization.SerializationKeys;
import ds.sudoku.logic.SudokuTemplate;

/**
 * Implementation of the Client interface.
 * 
 * <p>
 * This implementiation is internally threaded. It stores a socket and
 * handles incoming and outgoing data. This implementation does not check
 * for the socket being dead. 
 * </p>
 * @author dalhai
 *
 */
public class SudokuClient implements Client {
    
    //  Data coming from outside
    private final Socket socket;
    private final Gson json;
    
    //  Threads for specific tasks
    private final Thread receiver;
    private final Thread sender;
    
    //  Internal data
    private ClientMessageHandler handler;
    private volatile boolean stop;
    
    private final LinkedList<Message> outgoingMessageQueue;
    
    
    
    /**
     * Create a new sudoku client using the given socket and the given
     * json converter.
     * 
     * @param socket The socket used in this client.
     * @param json The json converter used.
     */
    public SudokuClient(final Socket socket, final Gson json) {
        this.socket = socket;
        this.json = json;
        
        this.handler = null;
        this.outgoingMessageQueue = new LinkedList<Message>();
        
        this.stop = false;
        
        //  Start the receivercore
        this.receiver = new Thread(new ReceiverCore());
        this.receiver.start();
        
        //  Start the sendercore
        this.sender = new Thread(new SenderCore());
        this.sender.start();
    }
    
    /**
     * The threaded receiving core of this client.
     * 
     * <p>
     * This client constantly listens for incomming messages and dispatches
     * them into the registered handler.
     * </p>
     * 
     * @author dalhai
     *
     */
    private class ReceiverCore implements Runnable {
        
        @Override
        public void run() {        
            
            try {
                final LinkedList<String> incomingMessageQueue = new LinkedList<String>();
                final JsonParser parser = new JsonParser();
                final InputStreamReader isr = new InputStreamReader(socket.getInputStream());
                final BufferedReader input = new BufferedReader(isr);
                
                while(!stop) {
                    //  Get the next input line
                    final String line = input.readLine();
                    if(line == null) {
                        continue;
                    }
                    
                    //  Push the message into the message queue
                    incomingMessageQueue.addLast(line);

                    /*
                     * If we have a registered handler, go through all the messages in the
                     * queue and dispatch them to the handler. 
                     */
                    
                    if(handler == null) continue;
                    while(!incomingMessageQueue.isEmpty()) {
                        
                        final String nextMessageLine = incomingMessageQueue.pop();
                        
                        //  Fields used for parsing
                        JsonElement parsedLine = null;
                        JsonObject parsedObject = null;
                        try {
                            //  Parse the line
                            parsedLine = parser.parse(nextMessageLine);
                            parsedObject = parsedLine.getAsJsonObject();
                        } catch(JsonParseException e) {
                            //  We did not receive valid JSON,
                            //  drop the message.
                            continue;
                        }
                        
                        if(!parsedObject.has(SerializationKeys.MESSAGE_TYPE_KEY))
                            continue;
                        
                        final JsonElement messageTypeElement = 
                                parsedObject.get(SerializationKeys.MESSAGE_TYPE_KEY);
                        
                        final String messageType = messageTypeElement.getAsString(); 
                        
                        //  Dispatch the right message type. switch(...) not used to maintain
                        //  compatibility with older java versions
                        
                        //  RegisterMessage
                        if(messageType.equals(RegisterMessage.class.getName())) {
                            RegisterMessage message = json.fromJson(parsedLine, RegisterMessage.class);
                            handler.onRegisterMessageReceived(SudokuClient.this, message);
                        }
                        //  DeregisterMessage
                        else if(messageType.equals(DeregisterMessage.class.getName())) {
                            DeregisterMessage message = json.fromJson(parsedLine, DeregisterMessage.class);
                            handler.onDeregisterMessageReceived(SudokuClient.this, message);
                        }
                        //  ACKMessage
                        else if(messageType.equals(ACKMessage.class.getName())) {
                            ACKMessage message = json.fromJson(parsedLine, ACKMessage.class);
                            handler.onACKMessageReceived(SudokuClient.this, message);
                        }
                        //  NACKMessage
                        else if(messageType.equals(NACKMessage.class.getName())) {
                            NACKMessage message = json.fromJson(parsedLine, NACKMessage.class);
                            handler.onNACKMessageReceived(SudokuClient.this, message);
                        }
                        //  ErrorMessage
                        else if(messageType.equals(ErrorMessage.class.getName())) {
                            ErrorMessage message = json.fromJson(parsedLine, ErrorMessage.class);
                            handler.onErrorMessageReceived(SudokuClient.this, message);
                        }
                        //  InviteMessage
                        else if(messageType.equals(InviteMessage.class.getName())) {
                            InviteMessage message = json.fromJson(parsedLine, InviteMessage.class);
                            handler.onInviteMessageReceived(SudokuClient.this, message);
                        }
                        //  LeaveMessage
                        else if(messageType.equals(LeaveMessage.class.getName())) {
                            LeaveMessage message = json.fromJson(parsedLine, LeaveMessage.class);
                            handler.onLeaveMessageReceived(SudokuClient.this, message);
                        }
                        //  SetFieldMessage
                        else if(messageType.equals(SetFieldMessage.class.getName())) {
                            SetFieldMessage message = json.fromJson(parsedLine, SetFieldMessage.class);
                            handler.onSetFieldMessageReceived(SudokuClient.this, message);
                        }
                    }
                }
                
                //  Free resources
                if(input != null) input.close();
                if(isr != null) isr.close();
                
            } catch(IOException e) {
                e.printStackTrace();
            }
        }        
    }
    
    /**
     * The threaded sending core of this client.
     * 
     * <p>
     * This client looks at the outgoing message queue in a loop.
     * Whenever there is a message to be sent, the message is converted
     * to JSON and then sent over the connection. The core then starts
     *  looking at the message queue again.
     * </p>
     * 
     * @author dalhai
     *
     */
    private class SenderCore implements Runnable {

        @Override
        public void run() {
            try {
                //  Streams for easy sending
                final OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
                final BufferedWriter output = new BufferedWriter(osw);
                
                while(!stop) {
                    //  The next message to be sent.
                    Message nextMessage = null;     
                    
                    //  Aquire the lock
                    synchronized(outgoingMessageQueue) {
                        //  Check if there is something on the queue.
                        if(!outgoingMessageQueue.isEmpty()) {
                            nextMessage = outgoingMessageQueue.pop();
                        }                    
                    }
                    
                    //  No message? Continue
                    if(nextMessage == null) continue;
                    
                    //  Lock is released, send the message
                    final String jsonMessage = json.toJson(nextMessage);
                    output.write(jsonMessage);
                    output.newLine();
                    output.flush();
                }
                
                //   Free Resources
                if(output != null) output.close();
                if(osw != null) osw.close();
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void sendError(String message) {
        
    }

    @Override
    public void sendMessage(Message message) {
        
    }

    @Override
    public void ACK() {
        
    }

    @Override
    public void NACK() {
        
    }

    @Override
    public void setField(int row, int column, int value, String sender) {
        
    }

    @Override
    public void setField(int index, int value, String sender) {        
    }

    @Override
    public void deregister() {        
        //  Create the message
        DeregisterMessage message = new DeregisterMessage("Kicked from server!");
        //  Add the message to the queue
        synchronized(outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    @Override
    public void invite(String otherPlayer) {
        //  Create the message
        InviteMessage message = new InviteMessage(otherPlayer);
        //  Add the message to the queue
        synchronized(outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    @Override
    public void playerLeft(String otherPlayer) {        
    }
    
    @Override
    public void gameOver(String winner) {
        //  Create the message
        GameOverMessage message = new GameOverMessage(winner);
        //  Add the message to the queue
        synchronized(outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    @Override
    public void score(boolean winning) {        
    }

    @Override
    public void newGame(SudokuTemplate sudoku) {        
    }

    @Override
    public void setField(int row, int column, int value) {
    }

    @Override
    public void setField(int index, int value) {
        
    }

    @Override
    public void setMessageHandler(ClientMessageHandler handler) {
        this.handler = handler;
    }
}
