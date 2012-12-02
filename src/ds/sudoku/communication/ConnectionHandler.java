package ds.sudoku.communication;

/**
 * This handler interface allows implementors to react on new connections being
 * accepted.
 * 
 */
public interface ConnectionHandler {

    /**
     * Will be invoked right after a new connection has been accepted. The
     * connection will automatically be wrapped inside a client which can from
     * now on be used to send messages over the connection.
     * 
     * @param newClient
     *            Thew new {@link Client} generated from the connection.
     */
    public void onNewConnectionAccepted(Client newClient);
}
