package ds.sudoku.communication;

public class NewGameMessage extends Message {

    @Override
    public String getMessageType() {
        return "NewGameMessage";
    }

}
