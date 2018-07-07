import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.util.Scanner;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.NotBoundException;
import java.util.concurrent.ThreadLocalRandom;
import java.net.*;
// import java.util.Enumeration;

/**
 *Clase para la iniciacion de la simulacion
 */
public class Anillo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Registry registry = null;
        // usa este objeto para obtener la info del anillo del servidor
        RingInfo _ringInfo = null;
        // usa este objeto para proveer data al servidor de estadisticas
        Estadisticas _estadisticas = null;
        try {
            // Getting the registry (Servidor de estadisticas: 192.168.1.249)
            registry = LocateRegistry.getRegistry("192.168.1.106",Registry.REGISTRY_PORT);
            // Looking up the registry for the remote objects 
            _ringInfo = (RingInfo) registry.lookup("RingInfo"); 
            _estadisticas = (Estadisticas) registry.lookup("Estadisticas"); 

        // OBTENIENDO MI IP
        InetAddress IP = Cliente.getLocalHostLANAddress();
        System.out.println("IP of my system is := "+IP.getHostAddress());
        System.out.println("Registering ip on server!...");
        _ringInfo.addAddress(IP.getHostAddress());

        System.out.println("Obtaining ring Addresses!...");
         // Calling the remote method using the obtained object 
         List<String> addresses = (List<String>)_ringInfo.ringAddresses(); 
         
         // Esta rutina seria para esperar que el anillo se complete
         // un tamano especifico
         if(addresses.size() < 4){
            System.out.print("The ring must have at least 4 nodes");
            System.out.print("Waiting for the nodes to join...");
            while (addresses.size() < 4) {
                Thread.sleep(3000);
                addresses = (List<String>)_ringInfo.ringAddresses();    
            }
            
         }
        

        Packets colaDePaquetes = Packets.getInstance();
        System.out.println("Se crearon:" + colaDePaquetes.tamano() + " Paquetes");

        int numeroNodo = addresses.indexOf(IP.getHostAddress());
        // el siguiente, hay 4 nodos
        String serverAddress = addresses.get((numeroNodo + 1)%4);
        int tiempoDeSalida = 5;
        Boolean servidorPrincipal = (numeroNodo == 0);

        System.out.println("Iniciado el nodo " + numeroNodo);
        
        ArrayList<Transporte> cargaUtil = cargaUtil(servidorPrincipal);
        envioDePaquetes( servidorPrincipal, serverAddress, tiempoDeSalida, cargaUtil);

        //Aqui es donde se reciben los paquetes y se tiene que hacer concurrente

            ServerSocket socketServidor = new ServerSocket(9002);
            int i = 0;
            int transportesRecibidos = 0;
            System.out.println("Info ---> Escuchando por el puerto " + 9002);
            //Se aceptan las conexiones
            while (true) {
               
                // el cliente se bloquea y espera a que le llegue el mensaje
                // osea se convierte en servidor
                Socket socket = socketServidor.accept();
                // aca el serverAddress seria la ip del siguiente nodo
                Cliente cliente = new Cliente(socket, numeroNodo, serverAddress, addresses);
                cliente.start();
                
                transportesRecibidos++;
                
                // hay 3 transportes
                // cada vez que me llegen 3 imprimo esto
                i = (i+1)%3;
                if(i == 2){
                
                System.out.println("\nInfo ---> Numero de Transportes Recibidos " + transportesRecibidos);
                }           
                
            }

        
        
        } catch (RemoteException e) {
            //TODO: handle exception
            System.out.println("Ocurrio un RemoteException: " + e.getMessage());
        }
        catch (NotBoundException e) {
            //TODO: handle exception
            System.out.println("Ocurrio un NotBoundException: " + e.getMessage());
        }
        catch (InterruptedException  e) {
            //TODO: handle exception
            System.out.println("Ocurrio un InterruptedException: " + e.getMessage() + "\nProbablemente un hilo se cerro inesperadamente");
        }
        catch (Exception e){
            
            System.out.println("Ocurrio una excepcion: " + e.getMessage());
            
        }
        
    }
    
    /**
    * Metodo para iniciar la carga util del primer nodo.
     */
    public static ArrayList<Transporte> cargaUtil(Boolean servidorPrincipal){
        // si no eres un servidor principal no tienes que generar la carga util
        if(servidorPrincipal == true){
            System.out.println("Info ---> Inicializando Carga util...");
            ArrayList<Transporte> transportes = new ArrayList<>();
            ArrayList<Paquete> paquetes = new ArrayList<>();
            Paquete paquete;
            Transporte transporte;

            //For de afuera para llenar el transporte
            for (int i = 1; i <= 3; i++) {
                
                paquetes = new ArrayList<>();
                //For de adentro para llenar los paquetes
                for (int j = 0; j < ThreadLocalRandom.current().nextInt(0,4); j++) {
                    paquete = new Paquete();
                    paquetes.add(paquete);
                }

                
                transporte = new Transporte(i, paquetes);
                transporte.imprimirPaquetes();
                transportes.add(transporte);
                
            }

            
            return transportes;
        }
        return null;
    }

    
    /*
    * Metodo para enviar los paquetes a traves del anillo
    */
    public static void envioDePaquetes(Boolean servidorPrincipal, String serverAddress, int tiempoDeSalida, ArrayList<Transporte> cargaUtil){
        
        if (servidorPrincipal == true){
            System.out.println("Enviar ---> Enviando Transportes");
            try {
                // enviamos cada uno de los 3 transportes
                for ( Transporte transporte : cargaUtil){
                    
                    Thread.sleep(5000);
                    // serializamos
                    Gson gson = new Gson();
                    String gsonAEnviar = gson.toJson( transporte, Transporte.class);
                    // enviamos por el socket del servidor
                    // (el siguiente nodo)
                    Socket socket = new Socket(serverAddress, 9002);
                    PrintWriter out =
                            new PrintWriter(socket.getOutputStream(), true);
                        //Se manda a traves del socket
                        out.println( gsonAEnviar );
                }
            } catch (IOException | InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
}
