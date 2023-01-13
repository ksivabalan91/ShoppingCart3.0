package cart3;
import java.util.LinkedList;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Cart {

    //Members
    private String customerName;
    public static List<String> users = new LinkedList<>();
    private List<String> cart = new LinkedList<>();
    private boolean loggedIn = false;

    //constructor
    public Cart(){}
    public Cart(String customerName){this.customerName = customerName;users.add(customerName);}

    // Getters and setters
    public String getCustomerName() {return customerName;}
    public void setCustomerName(String customerName) {this.customerName = customerName;users.add(0, customerName);}

    public List<String> getCart() {return cart;}
    public void setCart(List<String> cart) {this.cart = cart;}
        
    public boolean isLoggedIn() {return loggedIn;}
    public void setLoggedIn(boolean loggedIn) {this.loggedIn = loggedIn;}

    //methods
    //add items 
    public String add(String input_arr){
        String temp = "";
        String[] items = input_arr.split(",", 0);
        for (int i = 0; i<items.length;i++){
            if(cart.contains(items[i].toLowerCase().trim())){
                temp = temp + "you have " + items[i].toLowerCase().trim()+" in your cart\n";
                continue;
            } else{
                cart.add(items[i].toLowerCase().trim());
                temp = temp + items[i].toLowerCase().trim() + " added to cart\n";                
              }
        }
        return temp;
    }

    //delete items
    public String delete(String input_arr){
        String temp = "";
        String[] junk = input_arr.split(",", 0);

        for (int i = 0,j=0; i<junk.length;i++,j++){
            if(Integer.parseInt(junk[i])-j  > cart.size()){
                temp = temp+ " Incorrect item index\n";                                
            } else{
                temp = temp + cart.get(Integer.parseInt(junk[i])-1-j)+ " removed from cart\n";
                cart.remove(Integer.parseInt(junk[i])-1-j);
            }
        }
        return temp;     
    }
    
    // clears shopping cart
    public String clear(){
        this.cart.removeAll(cart);
        return "your cart has been cleared\n";
    }

    // list items
    public String list(){
        String temp = "";
        if (cart.isEmpty()){
            temp = temp + "You have nothing in your cart\n";
        } else{        
            temp = temp + "You cart contains the following items\n";
            for (int i = 0; i < cart.size();i++){
                temp = temp + (i+1)+". "+cart.get(i)+"\n";
            }
        }
        return temp;
    }

    // NEW METHODS FOR OPENING/CLOSING/CREATING FILES
    public String userLogin(String user, String folder) throws FileNotFoundException, IOException{        
        // create a path to the .txt file location
        user = user.toLowerCase().trim();
        Path fileLocation = Paths.get(folder+"\\"+user+".txt");
        File customerFile = fileLocation.toFile();
        
        if(customerFile.exists()){
            FileReader fr = new FileReader(customerFile);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();

            br.close();
            fr.close();
            
            String[] nameItemSplit = line.trim().split(" ", 2);
            
            // incase an empty cart was saved
            if(nameItemSplit.length<2){
                this.customerName = nameItemSplit[0];
                this.loggedIn = true;           
                return customerName+" shopping cart loaded\n";
            } else{

                String[] itemSplit = nameItemSplit[1].split(" ", 0);

                List<String> fileItemList = new LinkedList<>();
                for (String item: itemSplit){fileItemList.add(item);}

                // initiallize variables with info from txt file
                this.customerName = nameItemSplit[0];
                this.cart=fileItemList;
                this.loggedIn = true;           
                return customerName+" shopping cart loaded\n";
            }
            
        } else{
            this.customerName = user;
            this.loggedIn = true;
            return "New cart loaded, welcome "+this.customerName+"\n";            
        }
    }

    public String saveCart(String folder) throws IOException{

        if(this.loggedIn){
            FileWriter writer = new FileWriter(folder+"\\"+this.customerName+".txt");
        
            String itemString = "";
            for (String item:this.cart){
                itemString = itemString + item +" ";
            }
            
            writer.write(this.customerName+" ");
            writer.write(itemString);

            writer.flush();
            writer.close();

            return "Cart contents saved to "+customerName+"\n";

        } else{
            return "Please login in first before saving\n";
            }           
    }    

    // not in use for this sgoppingcart iteration
    public static void userList(String folder){
        File folderPath = new File(folder+"\\");
        File[] listFiles = folderPath.listFiles();
        
        for(int i =0; i<listFiles.length;i++){
            String fileName = listFiles[i].getName();
            System.out.printf("%d. %s\n",i+1,fileName.substring(0,fileName.length()-4));
        }
    }   
}
