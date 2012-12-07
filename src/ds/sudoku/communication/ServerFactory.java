package ds.sudoku.communication;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.gson.Gson;

/**
 * This factory is used to create new server connections and setup their
 * sockets. Since networking on android must occur on a background thread, this
 * factory does take care of that by creating the client in a background thread
 * and then returning it to the frontend.
 * 
 * @author dalhai
 * 
 */
public enum ServerFactory {
    ;
    
    public static final int TIMEOUT = 2000;

    /**
     * The core of the server factory, used to create a new sudoku server in the
     * background using an execution service.
     * 
     * @author dalhai
     * 
     */
    private static class CreationCore implements Callable<Server> {

        private final String address;
        private final int port;

        /**
         * Create a new Creation Core which will create a new sudoku server
         * connected to the given address and the given port.
         * 
         * @param address
         * @param port
         */
        public CreationCore(String address, int port) {
            this.address = address;
            this.port = port;
        }

        @Override
        public Server call() throws Exception {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(address, port), TIMEOUT);
            Gson json = GsonFactory.create();

            Server result = new SudokuServer(socket, json);
            return result;
        }
    }

    /**
     * Create a new sudoku server connected to the given address on the given
     * port.
     * 
     * @param address
     *            The address to be connected to.
     * @param port
     *            The port on which to connect.
     * @return A sudoku server ready to use but not yet started.
     * @throws ExecutionException
     *              Thrown if something went wrong while creating the server.
     * @throws InterruptedException
     *              Thrown when the creation of the server took too much time.
     */
    public static Server create(String address, int port)
            throws InterruptedException, ExecutionException {
        ExecutorService service = Executors.newSingleThreadExecutor();

        // The core to be executed in parallel
        CreationCore serverCreator = new CreationCore(address, port);

        // Submit a new task
        Future<Server> asyncResult = service.submit(serverCreator);

        // Wait for the result
        return asyncResult.get();
    }
}
