package ds.sudoku.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import ds.sudoku.communication.serialization.Serializable;
import ds.sudoku.communication.serialization.SerializationKeys;

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

	// Data coming from outside
	private final Socket socket;
	private final Gson json;

	// Threads for specific tasks
	private final Thread receiver;
	private final Thread sender;

	// Internal data
	private ServerMessageHandler handler;
	private volatile boolean stop;

	private final LinkedList<Message> outgoingMessageQueue;

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

		this.handler = null;
		this.outgoingMessageQueue = new LinkedList<Message>();

		this.stop = false;

		// Start the receiver core
		this.receiver = new Thread(new ReceiverCore());
		this.receiver.start();

		// Start the sendercore
		this.sender = null;
	}

	/**
	 * The threaded receiving core of this server.
	 * 
	 * <p>
	 * This client constantly listens for incoming messages and dispatches them
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
					final String line = input.readLine();
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
						
						// Deregister
						if(messageType.equals(DeregisterMessage.class.getName())) {
							DeregisterMessage message = json.fromJson(parsedLine, DeregisterMessage.class);
							handler.onDeregisterMessageReceived(SudokuServer.this, message);
						}
						// Left
						else if(messageType.equals(LeftMessage.class.getName())) {
							LeftMessage message = json.fromJson(parsedLine, LeftMessage.class);
							handler.onLeftMessageReceived(SudokuServer.this, message);
						}
						// SetField
						else if(messageType.equals(SetFieldMessage.class.getName())) {
							SetFieldMessage message = json.fromJson(parsedLine, SetFieldMessage.class);
							handler.onSetFieldMessageReceived(SudokuServer.this, message);
						}
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void sendError(String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendMessage(Message message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ACK() {
		// TODO Auto-generated method stub

	}

	@Override
	public void NACK() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setField(int row, int column, int value) {;
		// TODO Auto-generated method stub

	}

	@Override
	public void setField(int index, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setField(int row, int column, int value, String sender) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setField(int index, int value, String sender) {
		// TODO Auto-generated method stub

	}

	@Override
	public void register(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void register(String name, Serializable token) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deregister(Serializable token) {
		// TODO Auto-generated method stub

	}

	@Override
	public void invite(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestRandomMatch() {
		// TODO Auto-generated method stub

	}

	@Override
	public void leave() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMessageHandler(ServerMessageHandler handler) {
		// TODO Auto-generated method stub

	}

}
