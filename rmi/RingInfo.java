import java.rmi.Remote; 
import java.rmi.RemoteException;  
import java.util.List;

// Creating Remote interface for our application 
public interface RingInfo extends Remote {  
   List<String> ringAddresses() throws RemoteException;  
   void addAddress(String address) throws RemoteException;
} 