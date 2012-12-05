package ds.sudoku.logic.android_workaround;

import ds.sudoku.logic.CellInfo;

/**
 * nur für kompatibilitäts zweck, damit ich ohni es android gui teste chan...
 */
public class Message {
    public Object 	obj;// 	An arbitrary object to send to the recipient.
    public int 	what; // 	User-defined message code so that the recipient can identify what this message is about.

    public Message(int what){
       this.what = what;
    }

    public Message(int what, int row, int column, int digit){
        this.what = what;
        obj = new CellInfo(row,column,digit);
    }

    public Message(int what, Object obj) {
        this.what = what;
        this.obj = obj;
    }

}



