import java.util.List;
import java.util.ArrayList;

// Implementing the remote interface 
public class ImplRingInfo implements RingInfo {  
   
    List<String> _addresses ;

    public ImplRingInfo(){
        this._addresses = new ArrayList<String>();
    }

    // Implementing the interface method 
    public List<String> ringAddresses() {  
       System.out.println("Returning Ring addresses!...");
       return this._addresses;
    }  

    public void addAddress(String address) {
        if (!this._addresses.contains(address)) {
            this._addresses.add(address);
            System.out.println("Address " + address + " Registered!");
        }
    }

    public void removeAddress(String address){
        this._addresses.remove(address);
    }
 } 