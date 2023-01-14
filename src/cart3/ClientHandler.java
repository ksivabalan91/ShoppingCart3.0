package cart3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientHandler {

    private Socket conn;                                            // socket
    private String dataDirectory;                                   // directory of folder of all cart txt files
    private Cart customer = new Cart();                             // create instance of Cart class
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

        String userCart = dis.readUTF();                            // recieves user name string from client via readUTF
        dos.writeUTF(customer.userLogin(userCart, dataDirectory));  // run Cart.userLogin method with customer name and cart txt file folder directory, returns string to client via write UTF, 

        while(!exit){
            // reading datainputstream from client
            String command = dis.readUTF().trim();                  // recieve command from client via readUTF
            switch(command){                                        // check command type with swtich statement
                //shopping cart methods
                case Constants.ADD:                                 
                    dos.writeUTF(customer.add(dis.readUTF()));      // read items list from client via readUTF into Cart.add methods for execution
                    dos.flush();                                    // return string to client via writeUTF and flush writer
                    break;
                case Constants.DELETE ,Constants.REMOVE: 
                    dos.writeUTF(customer.newDelete(dis.readUTF()));   // read items list from client via readUTF into Cart.delete methods for execution
                    dos.flush();                                    // return string to client via writeUTF and flush writer
                    break;
                case Constants.CLEAR: 
                    dos.writeUTF(customer.clear());                 // run Cart.clear method
                    dos.flush();                                    // return string to client via writeUTF and flush writer    
                    break;
                case Constants.LIST: 
                    dos.writeUTF(customer.list());                  // run Cart.list method
                    dos.flush();                                    // return string to client via writeUTF and flush writer
                    break;                    
                case Constants.SAVE: 
                    dos.writeUTF(customer.saveCart(dataDirectory)); // run Cart.save method with cart txt folder as directory
                    dos.flush();                                    // return string to client via writeUTF and flush writer
                    break;
                case Constants.EXIT:                                
                    this.exit = true;                               // exit keywword sets exit to true and breaks out the while loop
                    break;
            }
        }
    }    
}
