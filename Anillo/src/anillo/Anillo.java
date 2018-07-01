/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anillo;

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

/**
 *
 * @author pedro
 */
public class Anillo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Boolean servidorPrincipal = true;
        String serverAddress = "192.168.1.1";
        int tiempoDeSalida = 5;
        int numeroNodo = 0;
        ArrayList<Transporte> cargaUtil = cargaUtil();
        envioDePaquetes( servidorPrincipal, serverAddress, tiempoDeSalida, cargaUtil);

        //Aqui es donde se reciben los paquetes y se tiene que hacer concurrente
        try{
            
            ServerSocket socketServidor = new ServerSocket(9001);
            int i = 0;
            //Se aceptan las conexiones
            while (true) {
                i++;
                System.out.print("Nodo escuchando por el puerto " + 9001);
                System.out.println("Esperando por el transporte numero " + i);
                Socket socket = socketServidor.accept();
                new Cliente(socket, numeroNodo).start();
            }
        }catch (Exception e){
            
            System.out.println(e.getMessage());
            
        }
        
    }
 
    public static ArrayList<Transporte> cargaUtil(){
        ArrayList<Transporte> transportes = new ArrayList<>();
        ArrayList<Paquete> paquetes = new ArrayList<>();
        Paquete paquete;
        Transporte transporte;
        
        //For de afuera para llenar el transporte
        for (int i = 0; i < 3; i++) {
            paquetes = new ArrayList<>();
            //For de adentro para llenar los paquetes
            for (int j = 0; j < 4; j++) {
                paquete = new Paquete();
                paquetes.add(paquete);
            }
            
            transporte = new Transporte(i, paquetes);
            transportes.add(transporte);
        }
        return transportes;
    }
    
    public static void envioDePaquetes(Boolean servidorPrincipal, String serverAddress, int tiempoDeSalida, ArrayList<Transporte> cargaUtil){
       
        if (servidorPrincipal == true){
            try {
                for ( Transporte transporte : cargaUtil){
                    
                    Thread.sleep(5000);
                    Gson gson = new Gson();
                    String gsonAEnviar = gson.toJson( transporte, Transporte.class);
                    Socket socket = new Socket(serverAddress, 9000);
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
