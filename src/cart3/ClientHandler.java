package cart3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientHandler {

    private Socket conn;
    private String dataDirectory;
    private Cart customer = new Cart();
    private boolean exit = false;
    
    // constructor
    public ClientHandler(Socket conn, String dataDirectory){
        this.conn = conn;
        this.dataDirectory = dataDirectory;
    }

    // main method
    public void run() throws IOException,EOFException{

        // initiallize input and output streams from connection
        InputStream in = conn.getInputStream();
        DataInputStream dis = new DataInputStream(in);
        OutputStream os = conn.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);

        String userCart = dis.readUTF();                            // reciever user name, corresponding client write in Client file, line 40
        dos.writeUTF(customer.userLogin(userCart, dataDirectory));  // returns string to client, refer to Cart.java userLogin method for return info

        while(!exit){
            // reading datainputstream from client
            String command = dis.readUTF().trim();                  // recieve command
            switch(command){                                        // check command type
                //shopping cart methods
                case Constants.ADD:                                 
                    dos.writeUTF(customer.add(dis.readUTF()));      // pass items list from client into Cart.java method for execution and return string to client
                    dos.flush();
                    break;
                case Constants.DELETE ,Constants.REMOVE: 
                    dos.writeUTF(customer.delete(dis.readUTF()));
                    dos.flush();
                    break;
                case Constants.CLEAR: 
                    dos.writeUTF(customer.clear());
                    dos.flush();
                    break;
                case Constants.LIST: 
                    dos.writeUTF(customer.list());
                    dos.flush();
                    break;                    
                case Constants.SAVE: 
                    dos.writeUTF(customer.saveCart(dataDirectory));
                    dos.flush();
                    break;
                case Constants.EXIT:                                //exit keywword sets exit to true and breaks out the while loop
                    this.exit = true;
                    break;
            }
        }
    }    
}
