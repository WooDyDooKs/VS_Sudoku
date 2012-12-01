// $Id: $
package logic;

public class SudokuChangedEvent {
    int row;
    int column;
    Change change;
    public SudokuChangedEvent(int row, int column, Change change){
        this.row = row;
        this.column = column;
        this.change = change;
    }

    // wird eventuell no erwiteret...
    static enum Change{digitSet, digitRemoved, candidateAdded,candidateRemoved}
}
