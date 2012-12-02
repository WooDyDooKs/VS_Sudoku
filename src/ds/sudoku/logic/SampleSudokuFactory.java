package ds.sudoku.logic;

public class SampleSudokuFactory {
    private static int[][] sudoku =
            {       {0,8,0,0,5,7,0,0,1},
                    {0,1,0,3,0,0,5,0,7},
                    {0,6,5,9,0,0,0,0,2},
                    {5,0,0,0,3,0,9,2,0},
                    {1,0,6,0,0,2,4,0,0},
                    {3,0,0,4,6,0,0,7,0},
                    {0,0,0,0,0,3,2,6,8},
                    {6,3,2,0,9,0,0,0,0},
                    {0,0,4,6,0,1,0,5,0}
            } ;
    private static int[][] autoRemoveTest =
            {       {0,0,0,0,0,0,0,0,0},
                    {0,1,0,0,0,0,0,0,4},
                    {0,0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0,0},
                    {0,0,3,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0,2}
            }  ;

    public static int[][] sampleSudoku2 = {
            {0,0,8,0,0,0,3,0,0},
            {0,4,0,3,6,8,0,0,9},
            {0,9,0,0,4,0,0,0,7},
            {0,0,0,0,9,0,0,0,0},
            {4,0,0,0,0,0,0,2,0},
            {0,0,0,2,0,3,0,0,0},
            {5,0,0,9,3,1,0,4,0},
            {0,0,0,0,8,0,0,0,0},
            {3,0,9,0,5,0,8,6,0}
    }        ;

    public static int[][] hardSudoku = {
            {7,0,0,9,0,2,1,0,0},
            {8,0,6,0,0,0,0,4,9},
            {1,0,0,0,0,0,0,0,0},
            {0,0,0,3,0,6,0,0,0},
            {0,0,0,0,8,0,4,9,3},
            {3,9,8,0,2,0,0,1,0},
            {4,0,0,8,0,0,0,0,5},
            {6,0,0,0,1,0,0,0,4},
            {9,0,0,0,7,0,8,0,0}
    }       ;

    public static SudokuTemplate getHardSudoku(){
        return new SudokuTemplate(hardSudoku);
    }

    public static SudokuTemplate getSampleSudoku(){
        return new SudokuTemplate(sudoku);
    }

    public static SudokuTemplate getSampleSudoku2(){
        return new SudokuTemplate(sampleSudoku2);
    }

    public static SudokuTemplate getAutoRemoveTest(){
        return new SudokuTemplate(autoRemoveTest);
    }
}
