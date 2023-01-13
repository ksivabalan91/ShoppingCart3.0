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
    
    public ClientHandler(Socket conn, String dataDirectory){
        this.conn = conn;
        this.dataDirectory = dataDirectory;
    }

    public void run() throws IOException,EOFException{

        InputStream in = conn.getInputStream();
        DataInputStream dis = new DataInputStream(in);
        OutputStream os = conn.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);

        String userCart = dis.readUTF();
        dos.writeUTF(customer.userLogin(userCart, dataDirectory));

        while(!exit){

            String command = dis.readUTF().trim();
            switch(command){
                //shopping cart methods
                case Constants.ADD: 
                    dos.writeUTF(customer.add(dis.readUTF()));
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
                case Constants.EXIT:
                    this.exit = true;
                    break;
            }
        }
    }    
}
