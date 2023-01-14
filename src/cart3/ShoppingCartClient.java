package cart3;

import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ShoppingCartClient {

    // remember to run withe arguments as follows: 
    // java -cp classes cart3.ShoppingCartClient customer@localhost:1056
    public static void main(String[] args) throws IOException{
        String customer = "tim";                                                                                    // default customer
        String host = "localhost";                                                                                  // default host
        int port = 1025;                                                                                            // default port
        boolean exit = false;
        Console cons = System.console();

        if (args.length>=1){                                                                                        // if arguments are passed in
            customer = args[0].split("@", 0)[0].trim().toLowerCase();                                  // set customer
            host = args[0].split("@", 0)[1].split(":", 0)[0].trim().toLowerCase();        // set host
            port = Integer.parseInt(args[0].split("@", 0)[1].split(":", 0)[1].trim());    // set port
        }
        
        try(Socket conn = new Socket(host,port)){                                                                   // connect to server with host and port number
            System.out.println("\nConnected to shopping cart server at "+host+" on "+customer+" port "+port +"\n");
            
            //initiallize input and output streams
            OutputStream os = conn.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            InputStream is = conn.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            dos.writeUTF(customer);                                                                                 // send customer name information to ClientHandler file
            System.out.println(dis.readUTF());                                                                      // recieve string from ClientHandler and print
            
            while(!exit){
                String input = cons.readLine("> ");                                                             // get input from console
                String[] input_arr = input.toLowerCase().trim().split(" ", 2);                          // split input into "command" and "items", eg. add apple, pear, add-> command; apple,pear-> items
                                                                                             
                
                switch(input_arr[0].trim()){                                                                        // check command, first word entered in console
                    case Constants.ADD: 
                        dos.writeUTF(Constants.ADD);                                                                // send command to ClientHandler
                        dos.writeUTF(input_arr[1]);                                                                 // send items string to ClientHandler
                        System.out.println(dis.readUTF());                                                          // print string recieved from ClientHandler
                        break;                
                    case Constants.DELETE ,Constants.REMOVE: 
                        dos.writeUTF(Constants.DELETE);                                                             // send command to ClientHandler
                        dos.writeUTF(input_arr[1]);                                                                 // send items string to ClientHandler
                        System.out.println(dis.readUTF());                                                          // print string recieved from ClientHandler
                        break;
                    case Constants.CLEAR: 
                        dos.writeUTF(Constants.CLEAR);                                                              // send command to ClientHandler
                        System.out.println(dis.readUTF());                                                          // print string recieved from ClientHandler
                        break;
                    case Constants.LIST: 
                        dos.writeUTF(Constants.LIST);                                                               // send command to ClientHandler
                        System.out.println(dis.readUTF());                                                          // print string recieved from ClientHandler
                        break;
                    
                    case Constants.SAVE:
                        dos.writeUTF(Constants.SAVE);                                                               // send command to ClientHandler
                        System.out.println(dis.readUTF());                                                          // print string recieved from ClientHandler
                        break;                
                    
                    case Constants.EXIT:                                                                            
                        dos.writeUTF(Constants.EXIT);                                                               // send command to ClientHandler
                        exit = true;                                                                                // exit keywword sets exit to true and breaks out the while loop
                        break;
                    default:
                        System.out.println("\nYou may input the following commands:\n"+                             // default case listing possible commands
                        "1. add\t\t - add items to your cart, add multiple items to your cart with ','\n"+
                        "2. delete\\remove - remove items from your cart, remove multiple items with ','\n"+
                        "3. list\t\t - list all items in your cart\n"+
                        "4. clear\t - remvoes all items from your cart\n"+
                        "5. save\t\t - save your shopping cart for future use\n"+
                        "6. exit\t\t - exits the shopping cart app\n");
                }
            }

            System.out.println("Closing connection...\n");
            
            try { 
                conn.close();                                                                                       // close connections
                System.out.println("Done\n");
            } catch (IOException ex) {}
            
        } catch(IOException e){System.out.println(e.getMessage());}                                                 // catch any error messages
    }
}
