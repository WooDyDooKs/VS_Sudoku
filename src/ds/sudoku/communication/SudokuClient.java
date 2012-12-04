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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import ds.sudoku.communication.serialization.SerializationKeys;
import ds.sudoku.exceptions.SudokuError;
import ds.sudoku.logic.SudokuTemplate;

/**
 * Implementation of the Client interface.
 * 
 * <p>
 * This implementiation is internally threaded. It stores a socket and handles
 * incoming and outgoing data. This implementation does not check for the socket
 * being dead.
 * </p>
 * 
 * @author dalhai
 * 
 */
public class SudokuClient implements Client {

    // Data coming from outside
    private final Socket socket;
    private final Gson json;

    // Threads for specific tasks
    private Thread receiver;
    private Thread sender;

    // Internal data
    private ClientMessageHandler handler;
    private volatile boolean stop;

    private LinkedList<Message> outgoingMessageQueue;

    /**
     * Create a new sudoku client using the given socket and the given json
     * converter.
     * 
     * @param socket
     *            The socket used in this client.
     * @param json
     *            The json converter used.
     */
    public SudokuClient(final Socket socket, final Gson json) {
        this.socket = socket;
        this.json = json;

        this.handler = null;

        this.stop = true;
    }

    /**
     * The threaded receiving core of this client.
     * 
     * <p>
     * This client constantly listens for incomming messages and dispatches them
     * into the registered handler.
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
                final InputStreamReader isr = new InputStreamReader(
                        socket.getInputStream());
                final BufferedReader input = new BufferedReader(isr);

                while (!stop) {
                    // Get the next input line
                    String line = null;
                    try {
                        line = input.readLine();
                    } catch (SocketTimeoutException e) {
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

                    if (handler == null)
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

                        // RegisterMessage
                        if (messageType.equals(RegisterMessage.class.getName())) {
                            RegisterMessage message = json.fromJson(parsedLine,
                                    RegisterMessage.class);
                            handler.onRegisterMessageReceived(
                                    SudokuClient.this, message);
                        }
                        // DeregisterMessage
                        else if (messageType.equals(DeregisterMessage.class
                                .getName())) {
                            DeregisterMessage message = json.fromJson(
                                    parsedLine, DeregisterMessage.class);
                            handler.onDeregisterMessageReceived(
                                    SudokuClient.this, message);
                        }
                        // ACKMessage
                        else if (messageType.equals(ACKMessage.class.getName())) {
                            ACKMessage message = json.fromJson(parsedLine,
                                    ACKMessage.class);
                            handler.onACKMessageReceived(SudokuClient.this,
                                    message);
                        }
                        // NACKMessage
                        else if (messageType
                                .equals(NACKMessage.class.getName())) {
                            NACKMessage message = json.fromJson(parsedLine,
                                    NACKMessage.class);
                            handler.onNACKMessageReceived(SudokuClient.this,
                                    message);
                        }
                        // ErrorMessage
                        else if (messageType.equals(ErrorMessage.class
                                .getName())) {
                            ErrorMessage message = json.fromJson(parsedLine,
                                    ErrorMessage.class);
                            handler.onErrorMessageReceived(SudokuClient.this,
                                    message);
                        }
                        // InviteMessage
                        else if (messageType.equals(InviteMessage.class
                                .getName())) {
                            InviteMessage message = json.fromJson(parsedLine,
                                    InviteMessage.class);
                            handler.onInviteMessageReceived(SudokuClient.this,
                                    message);
                        }
                        // LeaveMessage
                        else if (messageType.equals(LeaveMessage.class
                                .getName())) {
                            LeaveMessage message = json.fromJson(parsedLine,
                                    LeaveMessage.class);
                            handler.onLeaveMessageReceived(SudokuClient.this,
                                    message);
                        }
                        // SetFieldMessage
                        else if (messageType.equals(SetFieldMessage.class
                                .getName())) {
                            SetFieldMessage message = json.fromJson(parsedLine,
                                    SetFieldMessage.class);
                            handler.onSetFieldMessageReceived(
                                    SudokuClient.this, message);
                        }
                        // NamedSetFieldMessage
                        else if (messageType.equals(NamedSetFieldMessage.class
                                .getName())) {
                            NamedSetFieldMessage message = json.fromJson(
                                    parsedLine, NamedSetFieldMessage.class);
                            handler.onSetFieldMessageReceived(
                                    SudokuClient.this, message);
                        }
                    }
                }

                // Free resources
                if (input != null)
                    input.close();
                if (isr != null)
                    isr.close();

            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The threaded sending core of this client.
     * 
     * <p>
     * This client looks at the outgoing message queue in a loop. Whenever there
     * is a message to be sent, the message is converted to JSON and then sent
     * over the connection. The core then starts looking at the message queue
     * again.
     * </p>
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
                    // The next message to be sent.
                    Message nextMessage = null;

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

                    // No message? Continue
                    if (nextMessage == null)
                        continue;

                    // Lock is released, send the message
                    final String jsonMessage = json.toJson(nextMessage);
                    output.write(jsonMessage);
                    output.newLine();
                    output.flush();
                }

                // Free Resources
                if (output != null)
                    output.close();
                if (osw != null)
                    osw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * {@inheritDoc Client#sendError(SudokuError, String)}
     */
    @Override
    public void sendError(SudokuError error, String textMessage) {
        // Create the message
        ErrorMessage message = new ErrorMessage(error, textMessage);
        // Add the message to the queue
        synchronized (outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    /**
     * {@inheritDoc Client#sendMessage(Message)}
     */
    @Override
    public void sendMessage(Message message) {
        // Just add the message to the queue
        synchronized (outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    /**
     * {@inheritDoc Client#ACK(Message)}
     */
    @Override
    public void ACK(Message confirmedMessage) {
        // Create the message
        ACKMessage message = new ACKMessage(confirmedMessage);
        // Add the message to the queue
        synchronized (outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    /**
     * {@inheritDoc Client#NACK(Message)}
     */
    @Override
    public void NACK(Message confirmedMessage) {
        // Create the message
        NACKMessage message = new NACKMessage(confirmedMessage);
        // Add the message to the queue
        synchronized (outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    /**
     * {@inheritDoc Client#setField(int, int, int, String)}
     */
    @Override
    public void setField(int row, int column, int value, String sender) {
        // Create the message
        NamedSetFieldMessage message = new NamedSetFieldMessage(sender, row,
                column, value);
        // Add the message to the queue
        synchronized (outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    /**
     * {@inheritDoc Client#setField(int, int, String)}
     */
    @Override
    public void setField(int index, int value, String sender) {
        // Create the message
        NamedSetFieldMessage message = new NamedSetFieldMessage(sender, index,
                value);
        // Add the message to the queue
        synchronized (outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    /**
     * {@inheritDoc Client#setField(int, int, int)}
     */
    @Override
    public void setField(int row, int column, int value) {
        // Create the message
        SetFieldMessage message = new SetFieldMessage(row, column, value);
        // Add the message to the queue
        synchronized (outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    /**
     * {@inheritDoc Client#setField(int, int)}
     */
    @Override
    public void setField(int index, int value) {
        // Create the message
        SetFieldMessage message = new SetFieldMessage(index, value);
        // Add the message to the queue
        synchronized (outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    /**
     * {@inheritDoc Client#deregister()}
     */
    @Override
    public void deregister() {
        // Create the message
        DeregisterMessage message = new DeregisterMessage("Kicked from server!");
        // Add the message to the queue
        synchronized (outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    /**
     * {@inheritDoc Client#invite(String)}
     */
    @Override
    public void invite(String otherPlayer) {
        // Create the message
        InviteMessage message = new InviteMessage(otherPlayer);
        // Add the message to the queue
        synchronized (outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    /**
     * {@inheritDoc Client#playerLeft(String)}
     */
    @Override
    public void playerLeft(String otherPlayer) {
        // Create the message
        LeftMessage message = new LeftMessage(otherPlayer);
        // Add the message to the queue
        synchronized (outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    /**
     * {@inheritDoc Client#gameOver(String)}
     */
    @Override
    public void gameOver(String winner) {
        // Create the message
        GameOverMessage message = new GameOverMessage(winner);
        // Add the message to the queue
        synchronized (outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    /**
     * {@inheritDoc Client#score(boolean)}
     */
    @Override
    public void score(boolean winning) {
        // Create the message
        ScoreMessage message = new ScoreMessage(winning);
        // Add the message to the queue
        synchronized (outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    /**
     * {@inheritDoc Client#newGame(SudokuTemplate)}
     */
    @Override
    public void newGame(SudokuTemplate sudoku) {
        // Create the message
        NewGameMessage message = new NewGameMessage(sudoku);
        // Add the message to the queue
        synchronized (outgoingMessageQueue) {
            outgoingMessageQueue.addLast(message);
        }
    }

    /**
     * {@inheritDoc Client#setMessageHandler(ClientMessageHandler)}
     */
    @Override
    public void setMessageHandler(ClientMessageHandler handler) {
        this.handler = handler;
    }

    /**
     * {@inheritDoc Client#start()}
     */
    @Override
    public void start() {
        // If the threads already are running, do nothing.
        if (receiver != null || sender != null)
            return;

        this.outgoingMessageQueue = new LinkedList<Message>();

        this.stop = false;

        // Start the receivercore
        this.receiver = new Thread(new ReceiverCore());
        this.receiver.start();

        // Start the sendercore
        this.sender = new Thread(new SenderCore());
        this.sender.start();
    }

    /**
     * {@inheritDoc Client#stop()}
     */
    @Override
    public void stop() {
        // If the client is already stopped, do nothing.
        if (stop)
            return;

        stop = true;

        try {
        	sender.join();
            socket.close();
            
            receiver = null;
            sender = null;
        } catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
            e.printStackTrace();
        } 
    }
}
