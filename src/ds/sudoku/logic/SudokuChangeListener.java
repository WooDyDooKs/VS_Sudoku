// $Id: $
package ds.sudoku.logic;

import java.util.Map;

public interface SudokuChangeListener {
    public void onSudokuChanged(SudokuChangedEvent e);
    public void onLeaderChanged(boolean youAreWinnig);
    public void onGameFinished(String winner, Map<String, Integer> score);
    public void onPlayerLeft(String username);
}
