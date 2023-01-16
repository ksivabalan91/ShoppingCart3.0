package cart3;

import java.io.IOException;
import java.net.Socket;

public class Threader extends Thread{
    private Socket socket;
    private String dataDirectory;

    public Threader(Socket socket, String dataDirectory){
        this.socket = socket;
        this.dataDirectory = dataDirectory;
    }

    @Override
    public void run(){
        try{
            System.out.println("Connection received...\n");
            ClientHandler client1 = new ClientHandler(socket,dataDirectory);              // instantiate new clienthandler with socket and directory input
            client1.run();              
            
        } catch (IOException e){
         System.out.println("Oops "+e.getMessage());   
        }
    }    
}
