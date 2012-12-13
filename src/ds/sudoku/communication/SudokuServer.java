package ds.sudoku.communication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.LinkedList;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import ds.sudoku.communication.serialization.Serializable;
import ds.sudoku.communication.serialization.SerializationKeys;
import ds.sudoku.exceptions.SudokuError;

/**
 * Implementation of the Server interface.
 * 
 * <p>
 * This implementation is internally threaded. It stores a socket and handles
 * incoming and outgoing data. This implementation does not check for the socket
 * begin dead.
 * </p>
 * 
 * @author dalhai
 * 
 */
public class SudokuServer implements Server {

    /**
     * Constant timeout used in when reading from the connection.
     */
    public static final int TIMEOUT = 500;

    // Data coming from outside
    private final Socket socket;
    private final Gson json;

    // Threads for specific tasks
    private Thread receiver;
    private Thread sender;

    // Internal data
    private DeathHandler<Server> deathHandler;
    private ServerMessageHandler messageHandler;
    private volatile boolean stop;

    private LinkedList<Message> outgoingMessageQueue;

    /**
     * Create a new sudoku server using the given socket and the given json
     * converter.
     * 
     * @param socket
     *            The socket used in this server.
     * @param json
     *            The json converter used.
     */
    public SudokuServer(final Socket socket, final Gson json) {
        this.socket = socket;
        this.json = json;

        this.deathHandler = null;
        this.messageHandler = null;

        this.stop = true;

        try {
            socket.setSoTimeout(TIMEOUT);
        } catch (SocketException e) {
            // We cant really do anything here.
            // Just let the exception fade away,
            // it is caught in the listener and sender
            // threads and will cause onDeath to be called.
        }
    }

    /**
     * The threaded receiving core of this server.
     * 
     * <p>
     * This server constantly listens for incoming messages and dispatches them
     * into the registered handler.
     * </p>
     */
    private class ReceiverCore implements Runnable {

        @Override
        public void run() {

            try {
                final LinkedList<String> incomingMessageQueue = new LinkedList<String>();
                final JsonParser parser = new JsonParser();
                final InputStreamReader isr = new InputStreamReader(
                        socket.getInputStream());
                final BufferedReader input = new BufferedReader(isr);

                while (!stop) {
                    // Get the next input line
                    String line = null;
                    try {
                        line = input.readLine();
                    } catch (SocketTimeoutException e) {
                        // if we get a timeout, continue and try again.
                        continue;
                    }
                    if (line == null) {
                        continue;
                    }

                    // Push the message into the message queue
                    incomingMessageQueue.addLast(line);

                    /*
                     * If we have a registered handler, go through all the
                     * messages in the queue and dispatch them to the handler.
                     */

                    if (messageHandler == null)
                        continue;
                    while (!incomingMessageQueue.isEmpty()) {

                        final String nextMessageLine = incomingMessageQueue
                                .pop();

                        // Fields used for parsing
                        JsonElement parsedLine = null;
                        JsonObject parsedObject = null;
                        try {
                            // Parse the line
                            parsedLine = parser.parse(nextMessageLine);
                            parsedObject = parsedLine.getAsJsonObject();
                        } catch (JsonParseException e) {
                            // We did not receive valid JSON,
                            // drop the message.
                            continue;
                        }

                        if (!parsedObject
                                .has(SerializationKeys.MESSAGE_TYPE_KEY))
                            continue;

                        final JsonElement messageTypeElement = parsedObject
                                .get(SerializationKeys.MESSAGE_TYPE_KEY);

                        final String messageType = messageTypeElement
                                .getAsString();

                        // Dispatch the right message type. switch(...) not used
                        // to maintain
                        // compatibility with older java versions

                        // Raw Message
                        if (messageType.equals(Message.class.getName())) {
                            Message message = json.fromJson(parsedLine,
                                    Message.class);
                            messageHandler.onRawMessageReceived(
                                    SudokuServer.this, message);
                        }
                        // Deregister
                        else if (messageType.equals(DeregisterMessage.class
                                .getName())) {
                            DeregisterMessage message = json.fromJson(
                                    parsedLine, DeregisterMessage.class);
                            messageHandler.onDeregisterMessageReceived(
                                    SudokuServer.this, message);
                        }
                        // Left
                        else if (messageType
                                .equals(LeftMessage.class.getName())) {
                            LeftMessage message = json.fromJson(parsedLine,
                                    LeftMessage.class);
                            messageHandler.onLeftMessageReceived(
                                    SudokuServer.this, message);
                        }
                        // SetField
                        else if (messageType.equals(SetFieldMessage.class
                                .getName())) {
                            SetFieldMessage message = json.fromJson(parsedLine,
                                    SetFieldMessage.class);
                            messageHandler.onSetFieldMessageReceived(
                                    SudokuServer.this, message);
                        }
                        // ACK
                        else if (messageType.equals(ACKMessage.class.getName())) {
                            ACKMessage message = json.fromJson(parsedLine,
                                    ACKMessage.class);
                            messageHandler.onACKReceived(SudokuServer.this,
                                    message);
                        }
                        // NACK
                        else if (messageType
                                .equals(NACKMessage.class.getName())) {
                            NACKMessage message = json.fromJson(parsedLine,
                                    NACKMessage.class);
                            messageHandler.onNACKReceived(SudokuServer.this,
                                    message);
                        }
                        // Error
                        else if (messageType.equals(ErrorMessage.class
                                .getName())) {
                            ErrorMessage message = json.fromJson(parsedLine,
                                    ErrorMessage.class);
                            messageHandler.onErrorMesssageReceived(
                                    SudokuServer.this, message);
                        }
                        // InviteDirect
                        else if (messageType.equals(InviteMessage.class
                                .getName())) {
                            InviteMessage message = json.fromJson(
                                    parsedLine, InviteMessage.class);
                            messageHandler.onInviteMessageReceived(
                                    SudokuServer.this, message);
                        }
                        // GameOver
                        else if (messageType.equals(GameOverMessage.class
                                .getName())) {
                            GameOverMessage message = json.fromJson(parsedLine,
                                    GameOverMessage.class);
                            messageHandler.onGameOverMessageReceived(
                                    SudokuServer.this, message);
                        }
                        // NamedSetField
                        else if (messageType.equals(NamedSetFieldMessage.class
                                .getName())) {
                            NamedSetFieldMessage message = json.fromJson(
                                    parsedLine, NamedSetFieldMessage.class);
                            messageHandler.onNamedSetFieldMessageReceived(
                                    SudokuServer.this, message);
                        }
                        // NewGame
                        else if (messageType.equals(NewGameMessage.class
                                .getName())) {
                            NewGameMessage message = json.fromJson(parsedLine,
                                    NewGameMessage.class);
                            messageHandler.onNewGameMessageReceived(
                                    SudokuServer.this, message);
                        }
                        // Score
                        else if (messageType.equals(ScoreMessage.class
                                .getName())) {
                            ScoreMessage message = json.fromJson(parsedLine,
                                    ScoreMessage.class);
                            messageHandler.onScoreMessageReceived(
                                    SudokuServer.this, message);
                        }
                    }
                }
            } catch (IOException e) {
                if (!stop)
                    onDeath(e.getMessage());
            }
        }
    }

    /**
     * The threaded sending core of this server.
     * 
     * <p>
     * This server looks at the outgoing message queue in a loop. Whenever thee
     * is a message to be sent, the message is converted to JSON and then sent
     * over the connection. The core then starts looking at the message queue
     * again.
     * 
     * @author dalhai
     * 
     */
    private class SenderCore implements Runnable {

        @Override
        public void run() {
            try {
                // Streams for easy sending
                final OutputStreamWriter osw = new OutputStreamWriter(
                        socket.getOutputStream());
                final BufferedWriter output = new BufferedWriter(osw);

                while (true) {
                    // The next message to be sent
                    Message nextMessage = null;

                    // Used to check if we still need to send messages
                    boolean canStop = false;

                    // Aquire the lock
                    synchronized (outgoingMessageQueue) {
                        // Check if there is something on the queue.
                        if (!outgoingMessageQueue.isEmpty()) {
                            nextMessage = outgoingMessageQueue.pop();
                        } else {
                            canStop = true;
                        }
                    }

                    // Check if we need to stop
                    if (stop && canStop)
                        break;

                    // no message? continue
                    if (nextMessage == null)
                        continue;

                    // Lock is released, send the message
                    final String jsonMessage = json.toJson(nextMessage);
                    output.write(jsonMessage);
                    output.newLine();
                    output.flush();
                }

                // Free resources
                if (output != null)
                    output.close();
                if (osw != null)
                    osw.close();

            } catch (IOException e) {
                if (!stop)
                    onDeath(e.getMessage());
            }
        }

    }

    /**
     * {@inheritDoc Server#sendMessage(Message)}
     */
    @Override
    public void sendMessage(Message message) {
        // Directly lock the queue and add the message
        synchronized (outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    /**
     * {@inheritDoc Server#setField(int, int, int)}
     */
    @Override
    public void setField(int row, int column, int value) {
        // Generate the message
        SetFieldMessage message = new SetFieldMessage(row, column, value);
        // Lock the queue and add the message
        synchronized (outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    /**
     * {@inheritDoc Server#setField(int, int)}
     */
    @Override
    public void setField(int index, int value) {
        // Generate the message
        SetFieldMessage message = new SetFieldMessage(index, value);
        // Lock the queue and add the message
        synchronized (outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    /**
     * {@inheritDoc Server#setField(int, int, int, String)}
     */
    @Override
    public void setField(int row, int column, int value, String sender) {
        // Generate the message
        NamedSetFieldMessage message = new NamedSetFieldMessage(sender, row,
                column, value);
        // Add the message to the message queue
        synchronized (outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    /**
     * {@inheritDoc Server#setField(int, int, String)}
     */
    @Override
    public void setField(int index, int value, String sender) {
        // Generate the message
        NamedSetFieldMessage message = new NamedSetFieldMessage(sender, index,
                value);
        // Lock the queue and add the message
        synchronized (outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    /**
     * {@inheritDoc Server#register(String)}
     */
    @Override
    public void register(String name) {
        // Generate the message
        RegisterMessage message = new RegisterMessage(name);
        // Lock the queue and add the message
        synchronized (outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    /**
     * {@inheritDoc Server#register(String, Serializable)}
     */
    @Override
    public void register(String name, Serializable token) {
        // Generate the message
        RegisterMessage message = new RegisterMessage(name, token);
        // Lock the queue and add the message
        synchronized (outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    /**
     * {@inheritDoc Server#deregister(Serializable)}
     */
    @Override
    public void deregister(Serializable token) {
        // Generate the message
        DeregisterMessage message = new DeregisterMessage("", token);
        // Lock the queue and add the message
        synchronized (outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    /**
     * {@inheritDoc Server#invite(String)}
     */
    @Override
    public void invite(String name, String difficulty) {
        // Generate the message
        InviteMessage message = new InviteMessage(name, difficulty);
        // Lock the queue and add the message
        synchronized (outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    /**
     * {@inheritDoc Server#requestRandomMatch()}
     */
    @Override
    public void requestRandomMatch(String difficulty) {
        // Generate the message
        InviteRandomMessage message = new InviteRandomMessage(difficulty);
        // Lock the queue and add the message
        synchronized (outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    /**
     * {@inheritDoc Server#leave()}
     */
    @Override
    public void leave() {
        // Generate the message
        LeaveMessage message = new LeaveMessage();
        // Lock the queue and add the message
        synchronized (outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    /**
     * {@inheritDoc Server#sendError(SudokuError, String)}
     */
    @Override
    public void sendError(SudokuError error, String emessage) {
        // Generate the message
        ErrorMessage message = new ErrorMessage(error, emessage);
        // Lock the queue and add the message
        synchronized (outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    /**
     * {@inheritDoc Server#ACK(Message)}
     */
    @Override
    public void ACK(Message confirmedMessage) {
        // Generate the message
        ACKMessage message = new ACKMessage(confirmedMessage);
        // Lock the queue and add the message
        synchronized (outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    /**
     * {@inheritDoc Server#NACK(Message)}
     */
    @Override
    public void NACK(Message confirmedMessage) {
        // Generate the message
        NACKMessage message = new NACKMessage(confirmedMessage);
        // Lock the queue and add the message
        synchronized (outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    /**
     * {@inheritDoc Server#setMessageHandler(ServerMessageHandler)}
     */
    @Override
    public void setMessageHandler(ServerMessageHandler handler) {
        this.messageHandler = handler;
    }

    /**
     * {@inheritDoc Server#start()}
     */
    @Override
    public void start() {
        if (sender != null || receiver != null)
            return;

        this.outgoingMessageQueue = new LinkedList<Message>();

        this.stop = false;

        // Start the receiver core
        this.receiver = new Thread(new ReceiverCore());
        this.receiver.start();

        // Start the sendercore
        this.sender = new Thread(new SenderCore());
        this.sender.start();
    }

    /**
     * {@inheritDoc Server#stop()}
     */
    @Override
    public void stop() {
        if (stop)
            return;

        stop = true;
        try {
            sender.join();
            socket.close();

            sender = null;
            receiver = null;
        } catch (InterruptedException e) {
            onDeath(e.getMessage());
        } catch (IOException e) {
            onDeath(e.getMessage());
        }
    }

    /**
     * {@inheritDoc Server#setDeathHandler(DeathHandler)}
     */
    @Override
    public void setDeathHandler(DeathHandler<Server> handler) {
        this.deathHandler = handler;
    }

    /**
     * Helper method to react to the client dieing.
     * 
     * @param cause
     *            The cause of the death.
     */
    private void onDeath(String cause) {
        String finalCause = cause;

        stop = true;

        // do the cleanup
        try {
            socket.close();
        } catch (IOException e) {
            finalCause = finalCause + ", " + e.getMessage();
        }

        // call the death handler if possible
        if (deathHandler != null)
            deathHandler.onDeath(this, finalCause);
    }

}
