// $Id: $
package ds.sudoku.logic;

import javax.xml.bind.SchemaOutputResolver;

public interface SudokuChangeListener {
    public void onSudokuChanged(SudokuChangedEvent e);
    public void onLeaderChanged(boolean youAreWinnig);
    public void onGameFinished(String winner, int score);
}
