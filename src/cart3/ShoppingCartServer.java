package cart3;

import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;

public class ShoppingCartServer {
    // remember to run with database directory and some port arguments as follows: 
    // java -cp classes cart3.ShoppingCartServer directoryFolder 1056
    public static void main(String[] args) throws IOException, EOFException{
        
        String dataDirectory = "morecarts";                                         // default directory
        int port = 1025;                                                            // default port
        
        if (args.length>1){
            dataDirectory = args[0];                                                // set directory
            port = Integer.parseInt(args[1]);                                       // set port number string parsed to integer
        }
        
        System.out.println("\nStarting shopping cart server on port "+port+"\n");
        System.out.println("Connected to "+dataDirectory+" directory for persistence\n\n"+Cart.cartList(dataDirectory));

        
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while(true){
                new Threader(serverSocket.accept(),dataDirectory).start();
            }
        }        
    }
}
