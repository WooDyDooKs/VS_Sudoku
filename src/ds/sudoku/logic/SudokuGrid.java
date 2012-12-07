package ds.sudoku.logic;

import java.util.BitSet;
import java.util.Iterator;
import java.util.Stack;

public class SudokuGrid {
    private SudokuHandler handler ;
    private Cell[][] cells = new Cell[9][9];
    private MoveTracker moves = new MoveTracker();
    private BitSet completedDigits = new BitSet(10);

    public enum Trigger {user,autoRemove,undo}//,clueAutoRemove,removePencilmarks}
    public enum MoveKind {setDigit,removeDigit,addCandidate,removeCandidate}

    public SudokuGrid(SudokuTemplate template, SudokuHandler handler) {
        this.handler = handler;
        for(int row = 0; row < 9; row++){
            for(int column = 0; column < 9; column++) {
                int value = template.getValue(row + 1,column + 1);
                cells[row][column] = new Cell(row, column, value, value != 0);
            }
        }
        init();
    }

    private void init(){
        updateCompletedDigits();
        updatePencilmarks();
    }
                                            /*Access/*
    /**************************************************************************************************************/

    public int getValue(int row, int column) {
        return cells[row][column].value;
    }

    public BitSet getCandidates(int row, int column) {
        return cells[row][column].candidates;
    }

    public boolean isDigitCompleted(int digit) {
        return completedDigits.get(digit);
    }

    // returns true if it actually does something
    protected boolean setDigit(CellInfo cell, Trigger trigger){
        return setDigit(cell.row, cell.column, cell.digit, trigger);
    }
    protected boolean setDigit(int row, int column, int digit, Trigger trigger) {
        if(cells[row][column].value == digit) return false;
        moves.add(new Move(MoveKind.setDigit, row, column, digit, trigger));
        cells[row][column].value = digit;
        updateCompletedDigits();
        handler.publishChange(new SudokuChangedEvent(row, column, SudokuChangedEvent.Change.digitSet));
        autoRemove(row,column);
        return true;
    }

    // returns true if it actually does something
    protected boolean removeDigit(CellInfo cell, Trigger trigger){
        return removeDigit(cell.row, cell.column, trigger);
    }
    protected boolean removeDigit(int row, int column, Trigger trigger){
        if(cells[row][column].value == 0 || cells[row][column].isClue) return false;
        moves.add(new Move(MoveKind.removeDigit, row, column, cells[row][column].value, trigger));
        cells[row][column].value = 0;
        updateCompletedDigits();
        handler.publishChange(new SudokuChangedEvent(row, column, SudokuChangedEvent.Change.digitRemoved));
        return true;
    }

    // returns true if it actually does something
    protected boolean addCandidate(CellInfo cell, Trigger trigger)  {
        return addCandidate(cell.row, cell.column, cell.digit, trigger);
    }
    protected boolean addCandidate(int row, int column, int candidate, Trigger trigger) {
        if(cells[row][column].candidates.get(candidate)) return false;
        moves.add(new Move(MoveKind.addCandidate, row, column, candidate, trigger));
        cells[row][column].candidates.set(candidate, true);
        handler.publishChange(new SudokuChangedEvent(row, column, SudokuChangedEvent.Change.candidateAdded));
        return true;
    }

    // returns true if it actually does something
    protected boolean removeCandidate(CellInfo cell, Trigger trigger) {
        return removeCandidate(cell.row, cell.column, cell.digit, trigger);
    }
    protected boolean removeCandidate(int row, int column, int candidate, Trigger trigger) {
        if(!cells[row][column].candidates.get(candidate)) return false;
        moves.add(new Move(MoveKind.removeCandidate, row, column, candidate, trigger));
        cells[row][column].candidates.set(candidate, false);
        handler.publishChange(new SudokuChangedEvent(row, column, SudokuChangedEvent.Change.candidateRemoved));
        return true;
    }

    // returns true if it actually does something
    protected boolean toggleCandidate(CellInfo cell, Trigger trigger) {
        return toggleCandidate(cell.row, cell.column, cell.digit, trigger);
    }
    protected boolean toggleCandidate(int row, int column, int candidate, Trigger trigger) {
        if(getCandidates(row,column).get(candidate)) return removeCandidate(row,column,candidate,trigger);
        else return(addCandidate(row,column,candidate,trigger));
    }

    protected void undo(){
        moves.undo();
    }

    private void updateCompletedDigits(){
        for(int digit = 1; digit <= 9 ; digit++) {
            int digitCounter = 0;
            for( int row = 0; row < 9; row++){
                Iterator<Cell> rowIt = row(row);
                while(rowIt.hasNext()){
                    if(rowIt.next().value == digit) digitCounter++;

                }
            }
            completedDigits.set(digit,digitCounter == 9);
        }
    }

    private void updatePencilmarks() {
        for(int row = 0; row < 9; row ++) {
            Iterator<Cell> cells = row(row);
            while(cells.hasNext()) {
                Cell cell = cells.next();
                if(cell.value != 0) {
                    autoRemove(cell.row, cell.column);
                }
            }
        }
    }

    private void autoRemove(int triggerRow, int triggerColumn) {
       int digit = cells[triggerRow][triggerColumn].value;
       int triggerBox = (triggerRow / 3) * 3 + (triggerColumn / 3);
       Iterator<Cell> row = row(triggerRow);
       Iterator<Cell> column = column(triggerColumn);
       Iterator<Cell> box = box(triggerBox);

       while(row.hasNext()){
           Cell cell = row.next();
           removeCandidate(cell.row, cell.column, digit, Trigger.autoRemove);
       }

        while(column.hasNext()){
            Cell cell = column.next();
            removeCandidate(cell.row, cell.column, digit, Trigger.autoRemove);
        }

        while(box.hasNext()){
            Cell cell = box.next();
            removeCandidate(cell.row, cell.column, digit, Trigger.autoRemove);
        }

    }

    /*MoveManagement*/
    /*****************************************************************************************************************/

    private class MoveTracker {
        private Stack<Move> moves = new Stack<Move>();
        public void add(Move move){
            if(move.trigger != Trigger.undo) {
                moves.push(move);
            }
        }

        public void undo() {
            while(!moves.empty()) {
                Move move = moves.pop();
                //TODO: das sind nonig all fäll
                if(true) undoMove(move);
                //TODO: das sind au nonig all fäll
                if(move.trigger != Trigger.autoRemove) break;
            }
        }
        private void undoMove(Move move) {
            switch(move.moveKind) {
                case setDigit: removeDigit(move.row, move.column, Trigger.undo); break;
                case removeDigit: setDigit(move.row, move.column, move.digit, Trigger.undo); break;
                case addCandidate: removeCandidate(move.row, move.column, move.digit, Trigger.undo); break;
                case removeCandidate: addCandidate(move.row, move.column, move.digit, Trigger.undo); break;
                default :throw new RuntimeException("SudokuGrid.undoMove : forgot to implement a kind of move");
            }
        }
    }

    private class Move{
        MoveKind moveKind;
        int row;
        int column;
        int digit;
        Trigger trigger;
        public Move(MoveKind move, int row, int column, int digit, Trigger trigger){
            this.moveKind = move;
            this.row = row;
            this.column = column;
            this.digit = digit;
            this.trigger = trigger;
        }
        public String toString(){
            return moveKind + " " + digit + " at " + row + "/" + column + " triggerd by " + trigger;
        }
    }

                                            /*Iterators*/
    /****************************************************************************************************************/

    protected Iterator<Cell> row(int row){
        if(0 <= row && row < 9)  return new RowIterator(row);
        else throw new RuntimeException("there is no row " + row);
    }

    protected Iterator<Cell> column(int column){
        if(0 <= column && column < 9)  return new ColumnIterator(column);
        else throw new RuntimeException("there is no column " + column);
    }

    protected Iterator<Cell> box(int box){
        if(0 <= box&& box < 9)  return new BoxIterator(box);
        else throw new RuntimeException("there is no box " + box);
    }

    abstract class CellIterator implements Iterator<Cell> {
        public abstract boolean hasNext();
        public abstract Cell next();
        public void remove() {throw new UnsupportedOperationException("you can't remove Cells from a SudokuLogic.Sudoku!!!");}
    }

    class RowIterator extends CellIterator {
        private final int row;
        private int column = -1;
        public RowIterator(int row){
            this.row = row;
        }
        public boolean hasNext(){
            return column < 8;
        }
        public Cell next() {
            column++;
            return cells[row][column];
        }
    }

    class ColumnIterator extends CellIterator {
        private final int column;
        private int row = -1;
        public ColumnIterator(int column){
            this.column = column;
        }
        public boolean hasNext(){
            return row < 8;
        }
        public Cell next() {
            row++;
            return cells[row][column];
        }
    }

    class BoxIterator extends CellIterator {
        private int cell = -1;
        private final int box;
        public BoxIterator(int box){
            this.box = box;
        }
        public boolean hasNext(){
            return cell < 8;
        }
        public Cell next() {
            cell ++;
            int row = (box /3) * 3  + cell/3;
            int column = (box%3) * 3 + cell%3;
            return cells[row][column];
        }
    }

    class RowsIterator implements Iterator<Iterator<Cell>>{
        int row = -1;
        public boolean hasNext(){
           return row < 8;
        }
        public Iterator<Cell> next() {
            row++;
            return row(row);
        }
        public void remove(){
        throw new UnsupportedOperationException();
        }
    }

    class ColumnsIterator implements Iterator<Iterator<Cell>>{
        int column = -1;
        public boolean hasNext(){
            return column < 8;
        }
        public Iterator<Cell> next() {
            column++;
            return column(column);
        }
        public void remove(){
            throw new UnsupportedOperationException();
        }
    }

    class BoxesIterator implements Iterator<Iterator<Cell>>{
        int box = -1;
        public boolean hasNext(){
            return box < 8;
        }

        public Iterator<Cell> next() {
            box++;
            return box(box);
        }
        public void remove(){
            throw new UnsupportedOperationException();
        }
    }
}


