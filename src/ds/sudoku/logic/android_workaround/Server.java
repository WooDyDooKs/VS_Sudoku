package ds.sudoku.logic.android_workaround;

import ds.sudoku.logic.ServerSetFieldInfo;

public class Server{
    private String username;
    public Server(String username) {
        this.username = username;
    }

    public void setField(int x, int y, int value){
        GameServer.takeMessage(new ServerSetFieldInfo(x,y,value,username));
    }
}
