import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class Cliente extends Thread{
    
    public Socket _socket;
    public int _numeroNodo;
    String nextNodeAddress;

    public void run(){
        try {
            //Se setea por donde se envia
            Gson parseGson = new Gson();
            BufferedReader input =
            new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            String answer = input.readLine();
            Transporte transporte = new Transporte();
            transporte = parseGson.fromJson( answer , Transporte.class);            
            _socket.close();
            transporte = procesamientoDePaquetes(transporte, _numeroNodo);
            enviarToken(transporte);
            
        }catch( Exception e){
            System.out.println();
        } 
    }

    public Cliente(Socket _socket, int _numeroNodo, String nextNode) {
        this._socket = _socket;
        this._numeroNodo = _numeroNodo;
        this.nextNodeAddress = nextNode;
    }
    
    
    public static Transporte procesamientoDePaquetes( Transporte transporte, int numeroNodo){
        ActiveWorkers instancia = ActiveWorkers.getInstance();
        
        System.out.println("Recibir ---> Recibido Transporte id: " + transporte._id);
        for (int i = 0; i < transporte._paquetes.size(); i++) {
            try {

                Paquete paquete = transporte._paquetes.get(i);
                

                //Si esto sucede es porque es para mi y lo saco de donde esta
                if( paquete._nodoDestino == numeroNodo && instancia.getCount() < 3 ){
                   
                    //Ejecutando el hilo del cliente
                    Escritorio _escritorioHilo = new Escritorio( transporte._paquetes.get(i));
                    instancia.addWorker();
                    _escritorioHilo.start();
                    System.out.println("Hay" + instancia.getCount() + " Trabajadores activos");
                    System.out.println("Procesar ---> Recibi un Paquete! lo quito del Transporte (id:" +transporte._id+")");
                    transporte._paquetes.remove(i);
                    
                }else{
                    
                    transporte._paquetes.get(i)._tiempo += 10;  
                    
                }
                
            } catch (Exception e) {
            }
        }
        return transporte;
    }

    public void enviarToken( Transporte transporte ){
        try {
                if(transporte._paquetes.size() == 0){
                    System.out.println("Enviar ---> Envio Transporte (id:" +transporte._id+") VACIO al siguiente nodo");
                    //return;
                }
                else{
                    System.out.println("Enviar ---> Envio Transporte (id:" +transporte._id+") al siguiente nodo");
                }
                // envialo al siguiente nodo
                
                    Thread.sleep(5000);
                    // serializamos
                    Gson gson = new Gson();
                    String gsonAEnviar = gson.toJson( transporte, Transporte.class);
                    // enviamos por el socket del servidor
                    // (el siguiente nodo)
                    Socket socket = new Socket(this.nextNodeAddress, 9002);
                    PrintWriter out =
                            new PrintWriter(socket.getOutputStream(), true);
                        //Se manda a traves del socket
                        out.println( gsonAEnviar );


            } catch (IOException | InterruptedException ex) {
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
