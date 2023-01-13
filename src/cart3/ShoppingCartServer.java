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
        String dataDirectory = "morecarts";
        int port = 1025;       
        
        if (args.length>1){
            dataDirectory = args[0];
            port = Integer.parseInt(args[1]);
        }
        
        System.out.println("\nStarting shopping cart server on port "+port+"\n");
        System.out.println("Connected to "+dataDirectory+" directory for persistence\n\n"+Cart.cartList(dataDirectory));

        // start seversocket
        ServerSocket serverSocket = new ServerSocket(port);
        /// waiting for connection from client to accept
        Socket conn = serverSocket.accept();
        System.out.println("Connection received...\n");

        ClientHandler client1 = new ClientHandler(conn,dataDirectory);
        // calling client handler method to run
        client1.run();

        //close connections
        System.out.println("Closing connection...\n");
        conn.close();
        serverSocket.close();
        System.out.println("Done\n");
    }
}
