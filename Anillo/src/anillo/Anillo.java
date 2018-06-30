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
        String serverAddress = "127.0.0.1";
        int tiempoDeSalida = 5;
        ArrayList<Transporte> cargaUtil = cargaUtil();
        envioDePaquetes( servidorPrincipal, serverAddress, tiempoDeSalida, cargaUtil);
        //Aqui deberia haber un if que diga si es el almacen principal
        if (servidorPrincipal == true){
            try {
                
                Socket socket = new Socket(serverAddress, 9093);
                PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true);
                    //Se manda a traves del socket
                    out.println();

            } catch (IOException ex) {
                Logger.getLogger(Anillo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        try{
            
            ServerSocket socketServidor = new ServerSocket(9000);
            //Se aceptan las conexiones
            while (true) {
                
                Socket socket = socketServidor.accept();
                try {
                    //Se setea por donde se envia
                    Gson parseGson = new Gson();
                    BufferedReader input =
                            new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    String answer = input.readLine();
                    Transporte hola = new Transporte();
                    hola = parseGson.fromJson( answer , Transporte.class);
                } finally {
                    socket.close();
                }
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
                    Gson gson = new Gson();
                    String gsonAEnviar = gson.toJson( transporte, Transporte.class);
                    Socket socket = new Socket(serverAddress, 9093);
                    PrintWriter out =
                            new PrintWriter(socket.getOutputStream(), true);
                        //Se manda a traves del socket
                        out.println( gsonAEnviar );
                }
            } catch (IOException ex) {
                Logger.getLogger(Anillo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
