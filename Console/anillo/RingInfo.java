import java.rmi.Remote; 
import java.rmi.RemoteException;  
import java.util.List;

// Creating Remote interface for our application 
public interface RingInfo extends Remote {  
   // Retorna las ip de los nodos activos
   List<String> ringAddresses() throws RemoteException;
   // Registers a node into the ring
   void addAddress(String address) throws RemoteException;
   // Removes a node from the ring
   void removeAddress(String address) throws RemoteException;
   
} 