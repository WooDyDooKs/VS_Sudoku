package ds.sudoku.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ds.sudoku.communication.serialization.ACKMessageAdapter;
import ds.sudoku.communication.serialization.DeregisterMessageAdapter;
import ds.sudoku.communication.serialization.ErrorMessageAdapter;
import ds.sudoku.communication.serialization.GameOverMessageAdapter;
import ds.sudoku.communication.serialization.InviteMessageAdapter;
import ds.sudoku.communication.serialization.LeaveMessageAdapter;
import ds.sudoku.communication.serialization.LeftMessageAdapter;
import ds.sudoku.communication.serialization.NACKMessageAdapter;
import ds.sudoku.communication.serialization.NamedSetFieldMessageAdapter;
import ds.sudoku.communication.serialization.NewGameMessageAdapter;
import ds.sudoku.communication.serialization.RegisterMessageAdapter;
import ds.sudoku.communication.serialization.ScoreMessageAdapter;
import ds.sudoku.communication.serialization.SetFieldMessageAdapter;

public class ClientListener implements ConnectionManager {

    /**
     * The timeout used when waiting for a new connection.
     */
    public final static int TIMEOUT = 5000;

    /**
     * The time in millisecons the reciever thread sleeps if no handler is
     * specified.
     */
    public final static int NO_HANDLER_SLEEP_TIME = 1000;

    private ConnectionHandler handler;
    private volatile boolean stop;
    private ServerSocket serverSocket;
    private  Thread listener;
    
    private final Gson json;

    /**
     * Create a new client listener which will listen for new connections on the
     * given port. Whenever the client registers a new connection and has a
     * handler registered, it will dispatch the new connection into a
     * SudokuClient.
     */
    public ClientListener(int port) {
        this.handler = null;
        this.stop = true;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ACKMessage.class,
                new ACKMessageAdapter());
        gsonBuilder.registerTypeAdapter(NACKMessage.class,
                new NACKMessageAdapter());
        gsonBuilder.registerTypeAdapter(RegisterMessage.class,
                new RegisterMessageAdapter());
        gsonBuilder.registerTypeAdapter(DeregisterMessage.class,
                new DeregisterMessageAdapter());
        gsonBuilder.registerTypeAdapter(ErrorMessage.class,
                new ErrorMessageAdapter());
        gsonBuilder.registerTypeAdapter(GameOverMessage.class,
                new GameOverMessageAdapter());
        gsonBuilder.registerTypeAdapter(InviteMessage.class,
                new InviteMessageAdapter());
        gsonBuilder.registerTypeAdapter(LeftMessage.class,
                new LeftMessageAdapter());
        gsonBuilder.registerTypeAdapter(SetFieldMessage.class,
                new SetFieldMessageAdapter());
        gsonBuilder.registerTypeAdapter(NamedSetFieldMessage.class,
                new NamedSetFieldMessageAdapter());
        gsonBuilder.registerTypeAdapter(ScoreMessage.class,
                new ScoreMessageAdapter());
        gsonBuilder.registerTypeAdapter(NewGameMessage.class,
                new NewGameMessageAdapter());
        gsonBuilder.registerTypeAdapter(LeaveMessage.class,
                new LeaveMessageAdapter());

        json = gsonBuilder.create();
        
        serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        listener = null;
    }

    /**
     * {@inheritDoc ConnectionManager#acceptConnections()}
     */
    @Override
    public void acceptConnections() {
        if(serverSocket == null) return;
        
        stop = false;
        listener = new Thread(new ListenerCore());
        listener.start();
    }

    /**
     * {@inheritDoc ConnectionManager#rejectConnections()}
     */
    @Override
    public void rejectConnections() {
        stop = true;
        
        try {
            listener.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc ConnectionManager#setConnectionHandler(ConnectionHandler)}
     */
    @Override
    public void setConnectionHandler(ConnectionHandler handler) {
        this.handler = handler;
    }

    /**
     * The threaded core waiting for new connections. Calls the handler if a new
     * connection is accepted.
     * 
     * @author dalhai
     * 
     */
    private class ListenerCore implements Runnable {

        @Override
        public void run() {
            try {
                while (!stop) {
                    // If we do not have a handler, there is no sense in
                    // accepting a socket.
                    if (handler == null) {
                        Thread.sleep(NO_HANDLER_SLEEP_TIME);
                        continue;
                    }

                    // Accept a socket
                    final Socket clientSocket = serverSocket.accept();

                    // Wrap the socket into a sudoku client
                    final SudokuClient sudokuClient = new SudokuClient(
                            clientSocket, json);

                    // Pass the client to the handler
                    handler.onNewConnectionAccepted(sudokuClient);
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }

    }
}
