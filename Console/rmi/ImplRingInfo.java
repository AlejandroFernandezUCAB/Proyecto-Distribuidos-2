import java.util.List;
import java.util.ArrayList;

// Implementing the remote interface 
public class ImplRingInfo implements RingInfo {  
   
    List<String> addresses ;

    public ImplRingInfo(){
        this.addresses = new ArrayList<String>();
    }

    // Implementing the interface method 
    public List<String> ringAddresses() {  
       System.out.println("Returning Ring addresses!...");
       return addresses;
    }  

    public void addAddress(String address) {
        if (!this.addresses.contains(address)) {
            this.addresses.add(address);
            System.out.println("Address " + address + " Registered!");
        }
    }
 } 