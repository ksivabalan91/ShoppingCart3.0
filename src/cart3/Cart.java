package cart3;
import java.util.Arrays;
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

    // _MEMBERS_
    private String customerName;                                                                // customer name
    public static List<String> users = new LinkedList<>();                                      // static variable shared across all instances to collate users
    private List<String> cart = new LinkedList<>();                                             // list of cart items
    private boolean loggedIn = false;                                                           // to check login status

    // _CONSTRUCTORS_
    public Cart(){}                                                                             // empty constructor
    public Cart(String customerName){this.customerName = customerName;users.add(customerName);} // construct with string input for setting customername

    // _GETTERS AND SETTERS_
    public String getCustomerName() {return customerName;}
    public void setCustomerName(String customerName) {this.customerName = customerName;users.add(0, customerName);}

    public List<String> getCart() {return cart;}
    public void setCart(List<String> cart) {this.cart = cart;}
        
    public boolean isLoggedIn() {return loggedIn;}
    public void setLoggedIn(boolean loggedIn) {this.loggedIn = loggedIn;}

    // _METHODS_

    // add item method
    public String add(String input_arr){                                                        // takes items as string and returns string
        String temp = "";                                                                       // temporary temp variable to store output as string
        String[] items = input_arr.split(",", 0);                                  // split items list by "," delimiter to store items in an string array items[]
        for (int i = 0; i<items.length;i++){
            if(cart.contains(items[i].toLowerCase().trim())){                                   // for loop to check if item already exist in cart
                temp = temp + "you have " + items[i].toLowerCase().trim()+" in your cart\n";    // adding response to temp string
                continue;                                                                       // continue to next item if item already in cart
            } else{
                cart.add(items[i].toLowerCase().trim());                                        // otherwise, add item to cart list
                temp = temp + items[i].toLowerCase().trim() + " added to cart\n";               // add response to temp string
              }
        }
        return temp;                                                                            // return compiled responses in the string temp
    }

    // delete item method - delete 
    public String delete(String input_arr){                                                     // takes items as string and returns string
        String temp = "";                                                                       // temporary temp variable to store output as string
        String[] junk = input_arr.split(",", 0);                                   // split items list by "," delimiter to store items in an string array junk[]

        for (int i = 0,j=0; i<junk.length;i++,j++){                                             // for loop to pass indexes for deletion
            if(Integer.parseInt(junk[i])-j  > cart.size()){                                     // i for listing all indexes to be deleted, j is to compensate for the reduction in junk[] array size 
                temp = temp+ " Incorrect item index\n";                                         // i.e if you were deleting indexes 2 & 4 in a array that has indexes 0 - 6
            } else{                                                                             // after deleting item in index 2, item that was in index 4 will now be in index 3
                temp = temp + cart.get(Integer.parseInt(junk[i])-1-j)+ " removed from cart\n";  // only works if indexes to be deleted are given in ascending order 
                cart.remove(Integer.parseInt(junk[i])-1-j);                                     // using .remove method to delete the item and pass response to temp string
            }
        }
        return temp;                                                                            // return compiled responses in the string temp
    }

    // new delete method - using name of items to delete instead of index
    public String newDelete(String input_arr){                                                  // takes items as string and returns string
        String temp = "";                                                                       // temporary temp variable to store output as string
        String[] junk = input_arr.trim().split(",", 0);                            // split items list by "," delimiter to store items in an string array junk[]
        List<String> junkName = new LinkedList<>();                                             // create new string array 
        boolean isEmpty = true;
        for (int i = 0;i< junk.length;i++ ){
            if ((Integer.parseInt(junk[i].trim()))>cart.size()){
                temp = temp+ "\nIncorrect item index\n";
                continue;
            } else{
                junkName.add(cart.get(Integer.parseInt(junk[i].trim())-1));                     // store item name in junkName list
                isEmpty = false;
            }            
        }
                
        if(!isEmpty){
            for (String item:junkName){                                                             // for loop to remove all items
                int removeThisItem = cart.indexOf(item);                                            // locate item index via name of item
                temp = temp + cart.get(removeThisItem)+ " removed from cart\n";                      
                cart.remove(removeThisItem);                                                        // using .remove method to delete the item and pass response to temp string
            }
        }

        return temp;                                                                            // return compiled responses in the string temp
    }
    
    // clear shopping cart method - removes all items in cart list
    public String clear(){                                                                      // takes no arguments, returns string
        this.cart.removeAll(cart);                                                              // removes all items from cart list
        return "your cart has been cleared\n";                                                  // returns string response
    }

    // list items method
    public String list(){                                                                       // takes no arguments, returns string
        String temp = "";                                                                       // temporary temp variable to store output as string
        if (cart.isEmpty()){
            temp = temp + "You have nothing in your cart\n";                                    // if cart list is empty, temp set to relevant response
        } else{        
            temp = temp + "You cart contains the following items\n";                            // otherwise, set temp first line to "cart contains.." string
            for (int i = 0; i < cart.size();i++){                                               // for loop created to go through all items in Cart lists 
                temp = temp + (i+1)+". "+cart.get(i)+"\n";                                      // and add items as string temp
            }
        }
        return temp;                                                                            // return compiled responses in the string temp
    }

    
    // login method
    // takes in 2 strings, user and directory to check if user already exists in directory
    // if user exists, load all items and set customerName string and set cart list with items from cart
    // if user does not exists it creates a new instance of Cart
    public String userLogin(String user, String folder) throws FileNotFoundException, IOException{        
        user = user.toLowerCase().trim();
        Path fileLocation = Paths.get(folder+"\\"+user+".txt");                                 // create a path to the .txt file location
        File customerFile = fileLocation.toFile();                                              // convert path to file
        
        if(customerFile.exists()){                                                              // check if file exists, returns boolean
            FileReader fr = new FileReader(customerFile);                                       // if true, read file
            BufferedReader br = new BufferedReader(fr);                                         // create buffered reader
            String line = br.readLine();                                                        // read first line where all information is available

            br.close();                                                                         // close readers
            fr.close();
            
            String[] nameItemSplit = line.trim().split(" ", 2);                    // split by " " delimiter to get the user name eg. fred apple pear orange
                                                                                                // limit set to 2 to split only once after the first space is detected
            if(nameItemSplit.length<2){                                                         // incase an empty cart was saved, there wont be a index 1
                this.customerName = nameItemSplit[0];                                           // set customer name
                this.loggedIn = true;                                                           // set login to true
                return customerName+" shopping cart loaded\n";                                  // returns string reponse
            } else{ 
                String[] itemSplit = nameItemSplit[1].split(" ", 0);               // split by " " delimiter to get all items and store in itemSplit String array

                List<String> fileItemList = new LinkedList<>();                                 // create new list to store all its items
                for (String item: itemSplit){fileItemList.add(item);}                           // for loop to add all items in itemSplit array to fileItemList list

                // initiallize variables with info from txt file
                this.customerName = nameItemSplit[0];                                           // set customername
                this.cart=fileItemList;                                                         // cart list equals to compiled fileItemList
                this.loggedIn = true;                                                           // set login to true
                return customerName+" shopping cart loaded\n";                                  // return string response
            }
            
        } else{                                                                                 // if file cannot be found
            this.customerName = user;                                                           // set customername
            this.loggedIn = true;                                                               // set login to true
            return "New cart loaded, welcome "+this.customerName+"\n";                          // return string response to indicate new user cart
        }
    }

    // savecart method -  to write items in shopping cart to a file and output to specified directory
    public String saveCart(String folder) throws IOException{                                   // takes in directory to be saved in as string "folder", returns string

        if(this.loggedIn){                                                                      // checked if logged in
            FileWriter writer = new FileWriter(folder+"\\"+this.customerName+".txt");           // create new filewrite to output file
        
            String itemString = "";                                                             // create new string to store all items
            for (String item:this.cart){                                                        // for loop to add items in cart list to itemString
                itemString = itemString + item +" ";
            }
            
            writer.write(this.customerName+" ");                                                // write customer name
            writer.write(itemString);                                                           // write compiled string

            writer.flush();
            writer.close();

            return "Cart contents saved to "+customerName+"\n";                                 // return string response

        } else{
            return "Please login in first before saving\n";                                     // if not logged in, returns following string as response
            }           
    }    

    // cartList method - to collate all available carts
    public static String cartList(String folder){                                               // static method, does not require Cart instance to run. Takes in directory of folder with all cart txt files returns string. 
        File folderPath = new File(folder+"\\");                                                // set folder path
        File[] listFiles = folderPath.listFiles();                                              // create array of files
        String temp = "There are "+ listFiles.length+" carts in "+folder+" directory\n";        // add number of files as string to temp

        for(int i =0; i<listFiles.length;i++){                                                  // for loop with all files to get name
            String fileName = listFiles[i].getName();                                           // extract name of file
            temp = temp + (i+1)+". "+fileName.substring(0,fileName.length()-4)+"\n"; // store all names to temp string
        }
        return temp;                                                                            // retrun compiled string
    }
    // userList method not in use for this shoppingcart iteration
    public static void userList(String folder){
        File folderPath = new File(folder+"\\");
        File[] listFiles = folderPath.listFiles();
        
        for(int i =0; i<listFiles.length;i++){
            String fileName = listFiles[i].getName();
            System.out.printf("%d. %s\n",i+1,fileName.substring(0,fileName.length()-4));
        }
    }   
}
