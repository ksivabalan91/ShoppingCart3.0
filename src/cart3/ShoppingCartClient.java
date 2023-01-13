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
        String customer = "tim";
        String host = "localhost";
        int port = 1025;
    
        if (args.length>1){    
            customer = args[0].split("@", 0)[0].trim().toLowerCase();
            host = args[0].split("@", 0)[1].split(":", 0)[0].trim().toLowerCase();
            port = Integer.parseInt(args[0].split("@", 0)[1].split(":", 0)[1].trim());
        }
            
        boolean exit = false;

        Console cons = System.console();

        try(Socket conn = new Socket(host,port)){
            System.out.println("\nConnected to shopping cart server at "+host+" on "+customer+" port "+port +"\n");
            
            //initiallize input and output streams
            OutputStream os = conn.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            InputStream is = conn.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            dos.writeUTF(customer);
            System.out.println(dis.readUTF());
            
            while(!exit){
                String input = cons.readLine("> ");
                String[] input_arr = input.toLowerCase().trim().split(" ", 2);
                
                switch(input_arr[0].trim()){
                    //shopping cart methods
                    case Constants.ADD: 
                        dos.writeUTF(Constants.ADD); 
                        dos.writeUTF(input_arr[1]);
                        System.out.println(dis.readUTF());
                        break;                
                    case Constants.DELETE ,Constants.REMOVE: 
                        dos.writeUTF(Constants.DELETE);
                        dos.writeUTF(input_arr[1]);
                        System.out.println(dis.readUTF());
                        break;
                    case Constants.CLEAR: 
                        dos.writeUTF(Constants.CLEAR);
                        System.out.println(dis.readUTF());
                        break;
                    case Constants.LIST: 
                        dos.writeUTF(Constants.LIST);
                        System.out.println(dis.readUTF());
                        break;
                    
                    case Constants.SAVE: 
                        dos.writeUTF(Constants.SAVE);
                        System.out.println(dis.readUTF());
                        break;                
                    
                    case Constants.EXIT:
                        dos.writeUTF(Constants.EXIT);
                        exit = true;
                        break;
                    default:
                        System.out.println("\nYou may input the following commands:\n"+
                        "1. add\t\t - add items to your cart, add multiple items to your cart with ','\n"+
                        "2. delete/remove - remove items from your cart\n"+
                        "3. list\t\t - list all items in your cart\n"+
                        "4. clear\t - remvoes all items from your cart\n"+
                        "5. save\t\t - save your shopping cart for future use\n"+
                        "6. exit\t\t - exits the shopping cart app\n");
                }
            }

            System.out.println("Closing connection...\n");
            
            try { 
                conn.close();
                System.out.println("Done\n");
            } catch (IOException ex) {}
            
        } catch(IOException e){System.out.println("Enter valid port\n"+e.getMessage());}
    }
}
