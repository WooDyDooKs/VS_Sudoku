package ds.sudoku.logic;

import java.util.BitSet;

public class Cell {
    final int row;
    final int column;
    int value = 0;
    BitSet candidates ;
    BitSet candidatesRemovedByUser;
    final boolean isClue;
    boolean setByUser = false;

    public Cell (int row, int column, int value, boolean isClue){
        this.row = row;
        this.column = column;
        this.value = value;
        this.isClue = isClue;
        candidates = new BitSet(10);
        candidates.set(0,10,true);
        candidatesRemovedByUser = new BitSet(10);
        candidatesRemovedByUser.set(0, 10, false);
    }
}