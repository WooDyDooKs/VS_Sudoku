// $Id: $
package ds.sudoku.logic;

public interface SudokuChangeListener {
    public void onSudokuChanged(SudokuChangedEvent e);
    public void onLeaderChanged(boolean youAreWinning);
    public void onGameFinished(String winner, int score);
    public void onPlayerLeft(String username);
}
