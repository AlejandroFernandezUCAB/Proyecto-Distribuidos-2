import java.rmi.registry.LocateRegistry; 
import java.rmi.registry.Registry;  
import java.util.List;
import java.net.*;
import java.util.Enumeration;

public class Client {  
   private Client() {}  
   public static void main(String[] args) {  
      try {  
         // Getting the registry 
         Registry registry = LocateRegistry.getRegistry("192.168.1.249",Registry.REGISTRY_PORT);
    
         // Looking up the registry for the remote object 
         RingInfo stub = (RingInfo) registry.lookup("RingInfo"); 

         // getting host IP
         InetAddress IP=getLocalHostLANAddress();
         System.out.println("IP of my system is := "+IP.getHostAddress());

         System.out.println("Registering ip on server!...");
         stub.addAddress(IP.getHostAddress());


         System.out.println("Obtaining ring Addresses!...");
         // Calling the remote method using the obtained object 
         List<String> addresses = (List<String>)stub.ringAddresses(); 
        
         System.out.println("Returned info:\n");
         for (String address : addresses) {
             System.out.printf("->%s",address);
         }


         // System.out.println("Remote method invoked"); 
      } catch (Exception e) {
         System.err.println("Client exception: " + e.toString()); 
         e.printStackTrace(); 
      } 
   } 

   // source: https://stackoverflow.com/a/20418809
   private static InetAddress getLocalHostLANAddress() throws UnknownHostException {
        try {
            InetAddress candidateAddress = null;
            // Iterate all NICs (network interface cards)...
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // Iterate all IP addresses assigned to each card...
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {

                        if (inetAddr.isSiteLocalAddress()) {
                            // Found non-loopback site-local address. Return it immediately...
                            return inetAddr;
                        }
                        else if (candidateAddress == null) {
                            // Found non-loopback address, but not necessarily site-local.
                            // Store it as a candidate to be returned if site-local address is not subsequently found...
                            candidateAddress = inetAddr;
                            // Note that we don't repeatedly assign non-loopback non-site-local addresses as candidates,
                            // only the first. For subsequent iterations, candidate will be non-null.
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                // We did not find a site-local address, but we found some other non-loopback address.
                // Server might have a non-site-local address assigned to its NIC (or it might be running
                // IPv6 which deprecates the "site-local" concept).
                // Return this non-loopback candidate address...
                return candidateAddress;
            }
            // At this point, we did not find a non-loopback address.
            // Fall back to returning whatever InetAddress.getLocalHost() returns...
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        }
        catch (Exception e) {
            UnknownHostException unknownHostException = new UnknownHostException("Failed to determine LAN address: " + e);
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }



}

// docs: https://stackoverflow.com/questions/6810691/instantiating-a-list-in-java
// https://stackoverflow.com/questions/36400390/rmi-connection-refused-to-host-editing-etc-hosts-and-setproperty-not-worki