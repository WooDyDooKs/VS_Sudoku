package ds.sudoku.logic;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;

import android.os.Handler;
import android.os.Message;
import ds.sudoku.communication.GameOverMessage;
import ds.sudoku.communication.LeftMessage;
import ds.sudoku.communication.NamedSetFieldMessage;
import ds.sudoku.communication.ScoreMessage;
import ds.sudoku.communication.Server;

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
    public final static int ServerInfoLeaderChanged = 8;
    public final static int ServerInfoGameFinished = 9;
    public final static int ServerInfoPlayerLeft = 11;

    public SudokuHandler(SudokuTemplate template, String username, Server server) {
        sudoku = new SudokuGrid(template, this);
        this.username = username;
        this.server = server;
    }

    public void addSudokuChangeListener(SudokuChangeListener listener) {
        listeners.add(listener);
    }

    public int getValue(int row, int column) {
        return sudoku.getValue(row - 1, column - 1);
    }

    @Override
    public boolean isClue(int row, int column) {
        return sudoku.isClue(row-1,column-1);
    }

    public BitSet getCandidates(int row, int column) {
        return sudoku.getCandidates(row - 1,column - 1);
    }

    public String[] getCandidatesString(int row, int column) {
        String[] candidates = new String[10];
        for(int i = 1; i <= 9; i++){
           if(sudoku.getCandidates(row -1, column -1).get(i)) candidates[i] = Integer.toString(i);
           else candidates[i] = " ";
        }
        return candidates;
    }

    public boolean showCandidates(int row, int column) {
        return sudoku.getValue(row - 1,column - 1) == 0;
    }

    public boolean setByUser(int row,int column){
        return sudoku.setByUser(row -1, column -1);
    }

    public boolean isDigitCompleted(int digit) {
        return sudoku.isDigitCompleted(digit);
    }

    @Override
    public void handleMessage(Message msg) {
        switch(msg.what) {
            case GUIRequestSetDigit: {
                CellInfo cell = (CellInfo) msg.obj;
                //if(getValue(cell.row, cell.column)==0 || setByUser(cell.row, cell.column) )
                //cell.row--;cell.column--;
                //System.out.println("sudoku: set digit " + cell.digit + " at " + cell.row + "/" + cell.column);
                //sudoku.setDigit(cell, SudokuGrid.Trigger.user);
                //System.out.println("\t digit at " + cell.row + "/" + cell.column + " is now " + sudoku.getValue(cell.row,cell.column));
                server.setField(cell.row, cell.column, cell.digit);
                break;
            }
            case GUIRequestRemoveDigit: {
                //TODO:
                CellInfo cell = (CellInfo) msg.obj;
                if(!setByUser(cell.row, cell.column)) break;
                server.setField(cell.row,cell.column,0);
                //System.out.println("sudoku: removed digit at " + cell.row + "/" + cell.column) ;
                //sudoku.removeDigit(cell, SudokuGrid.Trigger.user);
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

            case ServerRequestSetDigit: {
                NamedSetFieldMessage info = (NamedSetFieldMessage) msg.obj;
                //System.out.println("acho im richtige case und cast isch ok");
                if(info.getValue() == 0) {
                    sudoku.removeDigit(info.getRow()-1, info.getColumn() -1,SudokuGrid.Trigger.user);
                    System.out.println("removing");
                    break;
                }
                if(getValue(info.getRow(), info.getColumn()) != 0){
                    sudoku.removeDigit(info.getRow()-1, info.getColumn()-1, SudokuGrid.Trigger.autoComplete);
                }
                SudokuGrid.Trigger trigger = null;
                if(info.getSender().equals(username))  trigger = SudokuGrid.Trigger.user;
                else trigger = SudokuGrid.Trigger.otherUser;
                sudoku.setDigit(info.getRow() - 1,info.getColumn() - 1, info.getValue(), trigger);
                //System.out.println("received Instruction from Server to set " + info.getValue() + " at " + info.getRow() + "/" + info.getColumn());
                break;
            }

            case ServerInfoLeaderChanged: {
                ScoreMessage info = (ScoreMessage) msg.obj;
                for(SudokuChangeListener listener : listeners) {
                    listener.onLeaderChanged(info.isWinning());
                }
                break;
            }

            case ServerInfoGameFinished: {
                GameOverMessage info = (GameOverMessage) msg.obj;
                for(SudokuChangeListener listener : listeners) {
                    listener.onGameFinished(info.getName(), info.getScores());
                }
                break;
            }

            case ServerInfoPlayerLeft: {
                LeftMessage info = (LeftMessage) msg.obj;
                for(SudokuChangeListener listener : listeners) {
                    listener.onPlayerLeft(info.getOtherPlayer());
                }
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
