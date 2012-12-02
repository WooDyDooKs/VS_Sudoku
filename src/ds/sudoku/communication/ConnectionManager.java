package ds.sudoku.communication;

/**
 * A connection manager listens for incomming connections, accepts them and
 * returns them as a new {@link Client}.
 * 
 * <p>
 * The clients generated by the connection manager are internally threaded.
 */
public interface ConnectionManager {

    /**
     * Start waiting and forwarding connections to the handler.
     */
    public void acceptConnections();

    /**
     * Start waiting and forwarding connections to the handler. Accept at most
     * {@code maximumConnections}.
     * 
     * @param maximumConnections
     *            The maximum number of connections accepted at the same time.
     */
    public void acceptConnections(int maximumConnections);

    /**
     * Stop listening for connections.
     */
    public void rejectConnections();

    /**
     * Set the connection handler for this connection manager.
     * 
     * @param handler
     */
    public void setConnectionHandler(ConnectionHandler handler);
}
