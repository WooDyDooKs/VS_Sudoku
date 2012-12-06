package ds.sudoku.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.gson.Gson;

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
        this.json = GsonFactory.create();
        
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
            serverSocket.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
     * connection is accepted. If no handler is specified, the listener will wait
     * {@link ClientListener#NO_HANDLER_SLEEP_TIME} ms until it continues.
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
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

    }
}
