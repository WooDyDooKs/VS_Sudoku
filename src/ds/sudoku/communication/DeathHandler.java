package ds.sudoku.communication;

/**
 * This handler allows implementors to react on an object
 * instance dieing. 
 * 
 * @author dalhai
 *
 */
public interface DeathHandler<T> {
    
    /**
     * Invoked <b>after</b> the given instance died.
     * 
     * @param instance The instance which died.
     * @param message A message giving the reason.
     */
    public void onDeath(T instance, String message);
}
