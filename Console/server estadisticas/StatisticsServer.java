import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry;
import java.rmi.Remote;
import java.rmi.RemoteException; 
import java.rmi.server.UnicastRemoteObject; 

// public class StatisticsServer extends ImplRingInfo { 
  public class StatisticsServer { 

   public StatisticsServer() {} 
   
   public static void main(String args[]) { 
      try { 
        // the following line is magic, took me aroud 1 hour to git it to work...Gian.
        System.setProperty("java.rmi.server.hostname","192.168.1.2");
         // Instantiating the implementation class 
         ImplRingInfo obj1 = new ImplRingInfo(); 
         ImplEstadisticas obj2 = new ImplEstadisticas();
    
         // Exporting the object of implementation class  
         // (here we are exporting the remote object to the stub) 
         RingInfo ringInfoStub = (RingInfo) UnicastRemoteObject.exportObject((Remote) obj1, Registry.REGISTRY_PORT);  
         Estadisticas estadisticasStub = (Estadisticas) UnicastRemoteObject.exportObject((Remote) obj2, Registry.REGISTRY_PORT);  

         // Binding the remote object (stub) in the registry 
         Registry registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        //  System.setProperty("java.rmi.server.hostname","192.168.1.249");

         registry.rebind("RingInfo", ringInfoStub);
         registry.rebind("Estadisticas", estadisticasStub);
         
         System.err.println("Server ready"); 
      } catch (Exception e) { 
         System.err.println("Server exception: " + e.toString()); 
         e.printStackTrace(); 
      } 
   } 
} 


// Source: https://recalll.co/ask/v/topic/exception-java.rmi.ConnectException%3A-Connection-refused-to-host%3A-127.0.1.1%3B/5a43c8d31126f4f71f8b5922
/*exception java.rmi.ConnectException: Connection refused to host: 127.0.1.1;?

System.setProperty("java.rmi.server.hostname","192.168.1.2");

I had exactly the same error. When the remote object got binded to the rmiregistry it was attached with the loopback IP Address which will obviously fail if you try to invoke a method from a remote address. In order to fix this we need to set the java.rmi.server.hostname property to the IP address where other devices can reach your rmiregistry over the network. It doesn't work when you try to set the parameter through the JVM. It worked for me just by adding the following line to my code just before binding the object to the rmiregistry:

In this case the IP address on the local network of the PC binding the remote object on the RMI Registry is 192.168.1.2.

It does work when you 'try to set the parameter through the JVM', by which you presumably mean on the command line. If you use System.setProperty(), you have to set it before exporting the remote object, not just before binding.
*/