// $Id: $
package ds.sudoku.logic;
import java.util.BitSet;

public interface SudokuInfo{
    /**
     *
     * @param row
     * @param column
     * @return  0 if no value is set in this cell, the value that is set otherwise
     */
    public int getValue(int row, int column);

    /**
     *
     * @param row
     * @param column
     * @return a bitset representing the candidates. To get the value for the candidate
     * for digit d just use get(d). (s nullte bit wird verschwändet.. :))
     */
    public BitSet getCandidates(int row, int column);
    //TODO: String[] mit no verfüegbare kandidate oder "" falls nöd verfüegbar
    public String[] getCandidatesString(int row, int column);

    /**
     *
     * @param row
     * @param column
     * @return  if the gui should show candidates for this cell
     */
    public boolean showCandidates(int row, int column);

    /**
     *
     * @param digit
     * @return  if the grid already contains 9 cells with this digit
     */
    public boolean isDigitCompleted(int digit);

    //public boolean wasSetByYou(int row, int column);

    //public boolean isClue(int row, int column);
}

