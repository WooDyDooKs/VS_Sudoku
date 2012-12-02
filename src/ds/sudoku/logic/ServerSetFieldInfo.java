package ds.sudoku.logic;

public class ServerSetFieldInfo {
    int row;
    int column;
    int value;
    String setBy;

    public ServerSetFieldInfo(int row, int column, int value, String setBy ){
        this.row = row;
        this.column = column;
        this.value = value;
        this.setBy = setBy;
    }
}
