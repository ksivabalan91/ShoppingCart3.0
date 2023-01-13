package cart3;

import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ShoppingCartServer {
    // remember to run with database directory and some port arguments as follows: 
    // java -cp classes cart3.ShoppingCartServer directoryFolder 1056
    public static void main(String[] args) throws IOException, EOFException{
        //default port if no args given
        String dataDirectory = args[0];
        int port = Integer.parseInt(args[1]);
        
        System.out.println("\nStarting shopping cart server on port "+port+"\n");
        System.out.println("Connected to "+dataDirectory+" directory for persistence\n");

        ServerSocket serverSocket = new ServerSocket(port);
        Socket conn = serverSocket.accept();
        System.out.println("Connection received...\n");

        ClientHandler client1 = new ClientHandler(conn,dataDirectory);

        client1.run();

        System.out.println("Closing connection...\n");
        conn.close();
        serverSocket.close();
        System.out.println("Done\n");
    }
}
