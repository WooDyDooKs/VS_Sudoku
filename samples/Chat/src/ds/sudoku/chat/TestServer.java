package ds.sudoku.chat;

public class TestServer {
    
    public static void main(String[] args) {
        ChatServer s = new ChatServer(1234);
        s.acceptUsers();
        
        while(true) {
            try {
                System.out.println("MAIN -> SLEEP");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
