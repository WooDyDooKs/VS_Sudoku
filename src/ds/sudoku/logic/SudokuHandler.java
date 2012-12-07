package ds.sudoku.logic;

import ds.sudoku.logic.android_workaround.Handler;  // TODO: Change to android Handler
import ds.sudoku.logic.android_workaround.Message; // TODO: Change to android Message
import ds.sudoku.logic.android_workaround.Server;  // TODO: Change to communication.Server

import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;

/**
 * I am currently testing things on my computer and not on android, so I had to add a
 * few workaround classes to make it compatible.
 * But if you change the imports maked with a TODO_ it should (or lets say might) compile
 * to android.
 *
 * I only started to add the comunication with the server, so that doesn't work right yet.
 * But that doesn't make a difference for the gui team because the things a user wants to
 * do already happen, just without the involvement of the server.
 */
public class SudokuHandler extends Handler implements SudokuInfo, SudokuChangePublisher {
    private List<SudokuChangeListener> listeners = new LinkedList<SudokuChangeListener>();
    private final SudokuGrid sudoku;
    public final String username;
    private Server server;

    public final static int GUIRequestSetDigit = 1;
    public final static int GUIRequestRemoveDigit = 2; //TODO:: was heisst das gnau im multiplayer kontext
    public final static int GUIRequestAddCandidate = 3;
    public final static int GUIRequestRemoveCandidate = 4;
    public final static int GUIRequestToggleCandidate = 5; // for your convenience..
    public final static int GUIRequestUndo = 6;

    public final static int ServerRequestSetDigit = 7;

    public SudokuHandler(SudokuTemplate template, String username, Server server) {
        sudoku = new SudokuGrid(template, this);
        this.username = username;
        this.server = server;
    }


    @Override
    public void addSudokuChangeListener(SudokuChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public int getValue(int row, int column) {
        return sudoku.getValue(row - 1,column -1);
    }

    @Override
    public BitSet getCandidates(int row, int column) {
        return sudoku.getCandidates(row - 1,column - 1);
    }

    @Override
    public String[] getCandidatesString(int row, int column) {
        //return sudoku.getCandidates(row - 1,column - 1);
        String[] candidates = new String[9];
        for(int i = 1; i <= 9; i++){
           if(sudoku.getCandidates(row -1, column -1).get(i)) candidates[i] = Integer.toString(i);
           else candidates[i] = "";
        }
        return candidates;
    }

    @Override
    public boolean showCandidates(int row, int column) {
        return sudoku.getValue(row - 1,column - 1) == 0;
    }

    @Override
    public boolean isDigitCompleted(int digit) {
        return sudoku.isDigitCompleted(digit);
    }

    @Override
    public void handleMessage(Message msg) {
        switch(msg.what) {
            case GUIRequestSetDigit: {
                CellInfo cell = (CellInfo) msg.obj;
                cell.row--;cell.column--;
                //System.out.println("sudoku: set digit " + cell.digit + " at " + cell.row + "/" + cell.column);
                sudoku.setDigit(cell, SudokuGrid.Trigger.user);
                //System.out.println("\t digit at " + cell.row + "/" + cell.column + " is now " + sudoku.getValue(cell.row,cell.column));
                server.setField(cell.row, cell.column, cell.digit);
                break;
            }
            case GUIRequestRemoveDigit: {
                CellInfo cell = (CellInfo) msg.obj;
                cell.row--;cell.column--;
                //System.out.println("sudoku: removed digit at " + cell.row + "/" + cell.column) ;
                sudoku.removeDigit(cell, SudokuGrid.Trigger.user);
                //System.out.println("\t digit at " + cell.row + "/" + cell.column + " is now " + sudoku.getValue(cell.row,cell.column));
                break;
            }
            case GUIRequestRemoveCandidate: {
                CellInfo cell = (CellInfo) msg.obj;
                cell.row--;cell.column--;
                //System.out.println("sudoku: removed candidate " + cell.digit + " at " + cell.row + "/" + cell.column) ;
                sudoku.removeCandidate(cell, SudokuGrid.Trigger.user);
                //System.out.println("\t candidate at " + cell.row + "/" + cell.column + " is now " + sudoku.getCandidates(cell.row,cell.column).get(cell.digit));
                break;
            }
            case GUIRequestAddCandidate: {
                CellInfo cell = (CellInfo) msg.obj;
                cell.row--;cell.column--;
                //System.out.println("sudoku: added candidate " + cell.digit + " at " + cell.row + "/" + cell.column) ;
                sudoku.addCandidate(cell, SudokuGrid.Trigger.user);
                //System.out.println("\t candidate at " + cell.row + "/" + cell.column + " is now " + sudoku.getCandidates(cell.row,cell.column).get(cell.digit));
                break;
            }
            case GUIRequestToggleCandidate: {
                CellInfo cell = (CellInfo) msg.obj;
                cell.row--;cell.column--;
                //System.out.println("sudoku: toggled candidate " + cell.digit + " at " + cell.row + "/" + cell.column) ;
                sudoku.toggleCandidate(cell, SudokuGrid.Trigger.user);
                //System.out.println("\t candidate at " + cell.row + "/" + cell.column + " is now " + sudoku.getCandidates(cell.row,cell.column).get(cell.digit));
                break;
            }

            case GUIRequestUndo: {
                sudoku.undo();
                break;
            }

            case ServerRequestSetDigit: {
                ServerSetFieldInfo info = (ServerSetFieldInfo) msg.obj;
                //System.out.println("received Instruction from Server to set " + info.value + " at " + info.row + "/" + info.column);
                break;
            }

            default : throw new RuntimeException("Forgot to add cases to switch or received faulty message");

        }
    }

    protected void publishChange(SudokuChangedEvent e) {
        e.row++; e.column++;
        for(SudokuChangeListener listener : listeners) {
            listener.onSudokuChanged(e);
        }
    }
}
