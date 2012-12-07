package ds.sudoku.chat;

import java.util.Scanner;

public class TestClient {
    
    public static void main(String[] args) {
        try {
            final Scanner input = new Scanner(System.in);
            
            //  Get server ip and port
            System.out.print("IP: ");
            final String ip = input.nextLine();
            System.out.print("PORT: ");
            final int port = Integer.parseInt(input.nextLine());
            
            // Connect the client
            System.out.println("Starting client!");
            ChatClient client = new ChatClient(ip, port);
            client.start();
            System.out.println("Client started!");
            
            // Registration
            System.out.print("NAME: ");
            final String name = input.nextLine();
            client.register(name);
            
            // lets send stuff
            String line = null;
            while((line = input.nextLine()) != "") {
                if(line.startsWith("->")) {
                    //   Directed message
                    final String recvt = line.split(":")[0];
                    final String msg = line.split(":")[1];
                    final String recv = recvt.split("->")[1];
                    
                    client.send(msg, recv);
                    continue;
                } 
                
                if(line.endsWith("lsu")) {
                    client.listUsers();
                }
                
                client.send(line);
            }
                        
            client.stop();
            input.close();
        
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
