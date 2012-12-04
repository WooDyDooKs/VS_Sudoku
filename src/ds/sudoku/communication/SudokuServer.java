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
		this.sender = new Thread(new SenderCore());
		this.sender.start();
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
						if (messageType.equals(DeregisterMessage.class
								.getName())) {
							DeregisterMessage message = json.fromJson(
									parsedLine, DeregisterMessage.class);
							handler.onDeregisterMessageReceived(
									SudokuServer.this, message);
						}
						// Left
						else if (messageType
								.equals(LeftMessage.class.getName())) {
							LeftMessage message = json.fromJson(parsedLine,
									LeftMessage.class);
							handler.onLeftMessageReceived(SudokuServer.this,
									message);
						}
						// SetField
						else if (messageType.equals(SetFieldMessage.class
								.getName())) {
							SetFieldMessage message = json.fromJson(parsedLine,
									SetFieldMessage.class);
							handler.onSetFieldMessageReceived(
									SudokuServer.this, message);
						}
						// ACK
						else if (messageType.equals(ACKMessage.class.getName())) {
							ACKMessage message = json.fromJson(parsedLine,
									ACKMessage.class);
							handler.onACKReceived(SudokuServer.this, message);
						}
						// NACK
						else if (messageType
								.equals(NACKMessage.class.getName())) {
							NACKMessage message = json.fromJson(parsedLine,
									NACKMessage.class);
							handler.onNACKReceived(SudokuServer.this, message);
						}
						// Error
						else if (messageType.equals(ErrorMessage.class
								.getName())) {
							ErrorMessage message = json.fromJson(parsedLine,
									ErrorMessage.class);
							handler.onErrorMesssageReceived(SudokuServer.this,
									message);
						}
						// GameOver
						else if (messageType.equals(GameOverMessage.class
								.getName())) {
							GameOverMessage message = json.fromJson(parsedLine,
									GameOverMessage.class);
							handler.onGameOverMessageReceived(
									SudokuServer.this, message);
						}
						// Invite
						else if (messageType.equals(InviteMessage.class
								.getName())) {
							InviteMessage message = json.fromJson(parsedLine,
									InviteMessage.class);
							handler.onInviteMessageReceived(SudokuServer.this,
									message);
						}
						// NamedSetField
						else if (messageType.equals(NamedSetFieldMessage.class
								.getName())) {
							NamedSetFieldMessage message = json.fromJson(
									parsedLine, NamedSetFieldMessage.class);
							handler.onNamedSetFieldMessageReceived(
									SudokuServer.this, message);
						}
						// NewGame
						else if (messageType.equals(NewGameMessage.class
								.getName())) {
							NewGameMessage message = json.fromJson(parsedLine,
									NewGameMessage.class);
							handler.onNewGameMessageReceived(SudokuServer.this,
									message);
						}
						// Score
						else if (messageType.equals(ScoreMessage.class
								.getName())) {
							ScoreMessage message = json.fromJson(parsedLine,
									ScoreMessage.class);
							handler.onScoreMessageReceived(SudokuServer.this,
									message);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
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

				while (!stop) {
					// The next message to be sent
					Message nextMessage = null;

					// Aquire the lock
					synchronized (outgoingMessageQueue) {
						// Check if there is something on the queue.
						if (!outgoingMessageQueue.isEmpty()) {
							nextMessage = outgoingMessageQueue.pop();
						}
					}

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
				e.printStackTrace();
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
		synchronized(outgoingMessageQueue) {
			outgoingMessageQueue.addLast(message);
		}
	}

	/**
	 * {@inheritDoc Server#setField(int, int, int, String)}
	 */
	@Override
	public void setField(int row, int column, int value, String sender) {
		// Generate the message
		NamedSetFieldMessage message = new NamedSetFieldMessage(sender, row, column, value);
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
		NamedSetFieldMessage message = new NamedSetFieldMessage(sender, index, value);
		// Lock the queue and add the message
		synchronized (outgoingMessageQueue) {
			outgoingMessageQueue.addLast(message);
		}
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
	public void sendError(SudokuError error, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ACK(Message confirmedMessage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void NACK(Message confirmedMessage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMessageHandler(ServerMessageHandler handler) {
		// TODO Auto-generated method stub

	}

}
