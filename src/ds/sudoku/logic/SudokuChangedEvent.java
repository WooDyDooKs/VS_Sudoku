// $Id: $
package ds.sudoku.logic;

public class SudokuChangedEvent {
    public int row;
    public int column;
    public Change change;
    public SudokuChangedEvent(int row, int column, Change change){
        this.row = row;
        this.column = column;
        this.change = change;
    }
    public SudokuChangedEvent(CellInfo cell, Change change) {
        this(cell.row, cell.column, change);
    }

    // wird eventuell no erwiteret...
    public static enum Change{digitSet, digitRemoved, candidateAdded,candidateRemoved, candidateToggled}
}
