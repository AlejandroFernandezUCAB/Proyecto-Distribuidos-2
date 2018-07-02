import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Anillo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // serverAddress = direccion del siguiente nodo del anillo
        //String serverAddress = "192.168.1.250";
        String serverAddress = JOptionPane.showInputDialog(
            "Ip del siguiente nodo:");
        int reply = JOptionPane.showConfirmDialog(null, "Es usted el que genera los transportes?\nSolo Puede haber uno", "Pregunta", JOptionPane.YES_NO_OPTION);
        Boolean servidorPrincipal = (reply == JOptionPane.YES_OPTION);
        int tiempoDeSalida = 5;
        // identificador de este nodo en el anillo
        int numeroNodo = Integer.parseInt(JOptionPane.showInputDialog(
            "Ip del siguiente nodo:"));
        
        System.out.println("Iniciado el nodo " + numeroNodo);
        
        ArrayList<Transporte> cargaUtil = cargaUtil(servidorPrincipal);
        envioDePaquetes( servidorPrincipal, serverAddress, tiempoDeSalida, cargaUtil);

        //Aqui es donde se reciben los paquetes y se tiene que hacer concurrente
        try{
            
            ServerSocket socketServidor = new ServerSocket(9001);
            int i = 0;
            int transportesRecibidos = 0;
            System.out.println("Info ---> Escuchando por el puerto " + 9001);
            //Se aceptan las conexiones
            while (true) {
               
                // el cliente se bloquea y espera a que le llegue el mensaje
                // osea se convierte en servidor
                Socket socket = socketServidor.accept();
                // aca el serverAddress seria la ip del siguiente nodo
                Cliente cliente = new Cliente(socket, numeroNodo, serverAddress);
                cliente.start();
                
                transportesRecibidos++;
                
                // hay 3 transportes
                // cada vez que me llegen 3 imprimo esto
                i = (i+1)%3;
                if(i == 2){
                
                System.out.println("\nInfo ---> Numero de Transportes Recibidos " + transportesRecibidos);
                }           
                
            }
        }catch (Exception e){
            
            System.out.println(e.getMessage());
            
        }
        
    }
 
    public static ArrayList<Transporte> cargaUtil(Boolean servidorPrincipal){
        // si no eres un servidor principal no tienes que generar la carga util
        if(servidorPrincipal == true){
            System.out.println("Info ---> Inicializando Carga util...");
            ArrayList<Transporte> transportes = new ArrayList<>();
            ArrayList<Paquete> paquetes = new ArrayList<>();
            Paquete paquete;
            Transporte transporte;

            //For de afuera para llenar el transporte
            for (int i = 0; i < 3; i++) {
                
                paquetes = new ArrayList<>();
                //For de adentro para llenar los paquetes
                for (int j = 0; j < 5; j++) {
                    paquete = new Paquete();
                    paquetes.add(paquete);
                }

                transporte = new Transporte(i, paquetes);
                transportes.add(transporte);
            }
            return transportes;
        }
        return null;
    }
    
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
                    Socket socket = new Socket(serverAddress, 9001);
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
